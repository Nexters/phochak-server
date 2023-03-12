package com.nexters.phochak.integration;

import com.nexters.phochak.client.SlackPostReportFeignClient;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.ReportPost;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.request.ReportPostRequestDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.ReportPostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.ReportPostService;
import com.nexters.phochak.specification.OAuthProviderEnum;
import com.nexters.phochak.specification.PostCategoryEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ReportPostServiceIntegrationTest {

    @Autowired
    ReportPostService reportPostService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReportPostRepository reportPostRepository;
    @MockBean
    SlackPostReportFeignClient slackPostReportFeignClient;


    ReportPostRequestDto request;
    User user;
    Post post;

    @BeforeEach
    void setUp() {
        request = new ReportPostRequestDto("test");
        user = new User(1234L, OAuthProviderEnum.KAKAO, "testId", "report", "testImage");
        Shorts shorts = new Shorts(1L, "upload key", "shorts", "thumbnail");
        post = new Post(user, shorts, PostCategoryEnum.CAFE);

        userRepository.save(user);
        postRepository.save(post);
    }

    @Test
    @DisplayName("신고에 성공하면 신고 카운트가 1 올라간다")
    void processReport() {
        // given
        Long userId = user.getId();
        Long postId = post.getId();

        // when
        reportPostService.processReport(userId, postId, request);

        // then
        assertThat(reportPostRepository.countByPost_Id(post.getId())).isEqualTo(1);
    }

    @Test
    @DisplayName("신고 시 중복된 신고가 존재하면 예외가 발생하여 저장에 실패한다")
    void processReport_fail() {
        // given
        Long userId = user.getId();
        Long postId = post.getId();
        reportPostService.processReport(userId, postId, request);

        // when & then
        assertThat(reportPostRepository.countByPost_Id(post.getId())).isEqualTo(1);
        assertThatThrownBy(() -> reportPostService.processReport(userId, postId, request))
                .isInstanceOf(PhochakException.class);
    }

    @Test
    @DisplayName("신고 카운트가 20개 이상 쌓이면 포스트가 노출되지 않는다")
    void processReport_overCriteria() throws InterruptedException {
        // given
        Long userId = user.getId();
        Long postId = post.getId();
        for (int i = 0; i < 19; i++) {
            User reporter = userRepository.save(User.builder()
                    .nickname("nickname" + i)
                    .providerId("providerId" + i)
                    .provider(OAuthProviderEnum.KAKAO)
                    .build());
            reportPostRepository.save(ReportPost.builder()
                    .post(post)
                    .reporter(reporter)
                    .reason("reason")
                    .build());
        }

        // when
        reportPostService.processReport(userId, postId, request);

        // then
        Optional<Post> post = postRepository.findById(postId);
        assertThat(post).isPresent();
        assertThat(post.get().isBlind()).isTrue();
    }
}
