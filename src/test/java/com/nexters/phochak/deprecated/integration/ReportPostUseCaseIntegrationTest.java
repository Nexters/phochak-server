package com.nexters.phochak.deprecated.integration;

import com.nexters.phochak.common.exception.PhochakException;
import com.nexters.phochak.post.adapter.out.api.ReportNotificationFeignClient;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.post.adapter.out.persistence.ReportPostEntity;
import com.nexters.phochak.post.adapter.out.persistence.ReportPostRepository;
import com.nexters.phochak.post.application.port.in.ReportPostUseCase;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.adapter.out.persistence.Shorts;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
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
class ReportPostUseCaseIntegrationTest {

    @Autowired
    ReportPostUseCase reportPostUseCase;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReportPostRepository reportPostRepository;
    @MockBean
    ReportNotificationFeignClient slackPostReportFeignClient;


    UserEntity userEntity;
    PostEntity postEntity;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1234L)
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("testId")
                .nickname("report")
                .profileImgUrl("testImage")
                .build();
        Shorts shorts = new Shorts(1L, "upload key", "shorts", "thumbnail");
        postEntity = new PostEntity(userEntity, shorts, PostCategoryEnum.CAFE);

        userRepository.save(userEntity);
        postRepository.save(postEntity);
    }

    @Test
    @DisplayName("신고에 성공하면 신고 카운트가 1 올라간다")
    void processReport() {
        // given
        Long userId = userEntity.getId();
        Long postId = postEntity.getId();

        // when
        reportPostUseCase.processReport(userId, postId);

        // then
        assertThat(reportPostRepository.countByPostId(postEntity.getId())).isEqualTo(1);
    }

    @Test
    @DisplayName("신고 시 중복된 신고가 존재하면 예외가 발생하여 저장에 실패한다")
    void processReport_fail() {
        // given
        Long userId = userEntity.getId();
        Long postId = postEntity.getId();
        reportPostUseCase.processReport(userId, postId);

        // when & then
        assertThat(reportPostRepository.countByPostId(postEntity.getId())).isEqualTo(1);
        assertThatThrownBy(() -> reportPostUseCase.processReport(userId, postId))
                .isInstanceOf(PhochakException.class);
    }

    @Test
    @DisplayName("신고 카운트가 20개 이상 쌓이면 포스트가 노출되지 않는다")
    void processReport_overCriteria() throws InterruptedException {
        // given
        Long userId = userEntity.getId();
        Long postId = postEntity.getId();
        for (int i = 0; i < 19; i++) {
            UserEntity reporter = userRepository.save(UserEntity.builder()
                    .nickname("nickname" + i)
                    .providerId("providerId" + i)
                    .provider(OAuthProviderEnum.KAKAO)
                    .build());
            reportPostRepository.save(ReportPostEntity.builder()
                    .post(postEntity)
                    .reporter(reporter)
                    .build());
        }

        // when
        reportPostUseCase.processReport(userId, postId);

        // then
        Optional<PostEntity> post = postRepository.findById(postId);
        assertThat(post).isPresent();
        assertThat(post.get().isBlind()).isTrue();
    }
}
