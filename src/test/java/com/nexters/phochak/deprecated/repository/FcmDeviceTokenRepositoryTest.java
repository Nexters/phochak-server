package com.nexters.phochak.deprecated.repository;

import com.nexters.phochak.notification.domain.FcmDeviceToken;
import com.nexters.phochak.notification.domain.FcmDeviceTokenRepository;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.post.domain.PostCategoryEnum;
import com.nexters.phochak.post.domain.PostRepository;
import com.nexters.phochak.shorts.domain.Shorts;
import com.nexters.phochak.shorts.domain.ShortsRepository;
import com.nexters.phochak.user.domain.OAuthProviderEnum;
import com.nexters.phochak.user.domain.User;
import com.nexters.phochak.user.domain.UserRepository;
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
        User user = User.builder()
                .nickname("nickname")
                .profileImgUrl("url")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("id")
                .build();
        userRepository.save(user);

        Shorts shorts = Shorts.builder()
                .uploadKey("uploadKey")
                .shortsUrl("url")
                .thumbnailUrl("url")
                .build();
        shortsRepository.save(shorts);

        Post post = Post.builder()
                .shorts(shorts)
                .postCategory(PostCategoryEnum.TOUR)
                .user(user)
                .build();
        postRepository.save(post);
        Long postId = post.getId();

        FcmDeviceToken fcmDeviceToken = FcmDeviceToken.builder()
                .token("deviceToken")
                .user(user)
                .build();
        fcmDeviceTokenRepository.save(fcmDeviceToken);
        em.flush();
        em.clear();

        List<Object[]> resultList = fcmDeviceTokenRepository.findDeviceTokenAndPostIdByUploadKey("uploadKey");

        String resultDeviceToken = resultList.get(0)[0].toString();
        Long resultPostId = Long.valueOf(String.valueOf(resultList.get(0)[1]));

        Assertions.assertThat(resultDeviceToken).isEqualTo("deviceToken");
        Assertions.assertThat(resultPostId).isEqualTo(postId);
    }
}
