package com.nexters.phochak.deprecated.repository;

import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenEntity;
import com.nexters.phochak.notification.adapter.out.persistence.FcmDeviceTokenRepository;
import com.nexters.phochak.notification.domain.OperatingSystem;
import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.shorts.adapter.out.persistence.Shorts;
import com.nexters.phochak.shorts.adapter.out.persistence.ShortsRepository;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Disabled
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FcmDeviceTokenRepositoryTest {

    @Autowired
    private FcmDeviceTokenRepository fcmDeviceTokenRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ShortsRepository shortsRepository;
    @Autowired
    private EntityManager em;

    @DisplayName("uploadKey로 device 조회 쿼리가 정상적으로 조회된다.")
    @Test
    void findDeviceTokenAndPostIdByUploadKey() {
        UserEntity userEntity = UserEntity.builder()
                .nickname("nickname")
                .profileImgUrl("url")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("id")
                .isBlocked(false)
                .build();
        userRepository.save(userEntity);

        Shorts shorts = Shorts.builder()
                .uploadKey("uploadKey")
                .shortsUrl("url")
                .thumbnailUrl("url")
                .build();
        shortsRepository.save(shorts);

        PostEntity postEntity = PostEntity.builder()
                .shorts(shorts)
                .postCategory(PostCategoryEnum.TOUR)
                .userEntity(userEntity)
                .build();
        postRepository.save(postEntity);
        Long postId = postEntity.getId();

        FcmDeviceTokenEntity fcmDeviceTokenEntity = FcmDeviceTokenEntity.builder()
                .token("deviceToken")
                .operatingSystem(OperatingSystem.ANDROID)
                .user(userEntity)
                .build();
        fcmDeviceTokenRepository.save(fcmDeviceTokenEntity);
        em.flush();
        em.clear();

        List<Object[]> resultList = fcmDeviceTokenRepository.findDeviceTokenAndPostIdByUploadKey("uploadKey");

        String resultDeviceToken = resultList.get(0)[0].toString();
        Long resultPostId = Long.valueOf(String.valueOf(resultList.get(0)[1]));

        Assertions.assertThat(resultDeviceToken).isEqualTo("deviceToken");
        Assertions.assertThat(resultPostId).isEqualTo(postId);
    }
}
