package com.nexters.phochak.deprecated.integration;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.post.domain.PostRepository;
import com.nexters.phochak.report.application.ReportPostService;
import com.nexters.phochak.report.domain.ReportPost;
import com.nexters.phochak.report.domain.ReportPostRepository;
import com.nexters.phochak.report.presentation.ReportNotificationFeignClient;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nexters.phochak.user.domain.UserEntity;
import com.nexters.phochak.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@Disabled
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
    ReportNotificationFeignClient slackPostReportFeignClient;


    UserEntity userEntity;
    Post post;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity(1234L, OAuthProviderEnum.KAKAO, "testId", "report", "testImage");
        Shorts shorts = new Shorts(1L, "upload key", "shorts", "thumbnail");
        post = new Post(userEntity, shorts, PostCategoryEnum.CAFE);

        userRepository.save(userEntity);
        postRepository.save(post);
    }

    @Test
    @DisplayName("신고에 성공하면 신고 카운트가 1 올라간다")
    void processReport() {
        // given
        Long userId = userEntity.getId();
        Long postId = post.getId();

        // when
        reportPostService.processReport(userId, postId);

        // then
        assertThat(reportPostRepository.countByPost_Id(post.getId())).isEqualTo(1);
    }

    @Test
    @DisplayName("신고 시 중복된 신고가 존재하면 예외가 발생하여 저장에 실패한다")
    void processReport_fail() {
        // given
        Long userId = userEntity.getId();
        Long postId = post.getId();
        reportPostService.processReport(userId, postId);

        // when & then
        assertThat(reportPostRepository.countByPost_Id(post.getId())).isEqualTo(1);
        assertThatThrownBy(() -> reportPostService.processReport(userId, postId))
                .isInstanceOf(PhochakException.class);
    }

    @Test
    @DisplayName("신고 카운트가 20개 이상 쌓이면 포스트가 노출되지 않는다")
    void processReport_overCriteria() throws InterruptedException {
        // given
        Long userId = userEntity.getId();
        Long postId = post.getId();
        for (int i = 0; i < 19; i++) {
            UserEntity reporter = userRepository.save(UserEntity.builder()
                    .nickname("nickname" + i)
                    .providerId("providerId" + i)
                    .provider(OAuthProviderEnum.KAKAO)
                    .build());
            reportPostRepository.save(ReportPost.builder()
                    .post(post)
                    .reporter(reporter)
                    .build());
        }

        // when
        reportPostService.processReport(userId, postId);

        // then
        Optional<Post> post = postRepository.findById(postId);
        assertThat(post).isPresent();
        assertThat(post.get().isBlind()).isTrue();
    }
}
