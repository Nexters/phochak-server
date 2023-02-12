package com.nexters.phochak.repository;

import com.nexters.phochak.domain.Hashtag;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.Shorts;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.specification.OAuthProviderEnum;
import com.nexters.phochak.specification.PostCategoryEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = "test")
class PostRepositoryTest {

    @Autowired PostRepository postRepository;
    @Autowired
    private ShortsRepository shortsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("orphanremove 적용 시 short, hashtag 자식 엔티티도 제거한다")
    void postDeleteOrphanremove() {
        //given
        User user = User.builder()
                .nickname("nickname")
                .provider(OAuthProviderEnum.KAKAO)
                .providerId("providerId")
                .profileImgUrl("profileImgUrl")
                .build();
        userRepository.save(user);
        Shorts shorts = Shorts.builder()
                .shortsUrl("shortsUrl")
                .uploadKey("uploadKey")
                .thumbnailUrl("thumbnailUrl")
                .build();
        shortsRepository.save(shorts);

        Post post = Post.builder()
                .user(user)
                .postCategory(PostCategoryEnum.TOUR)
                .shorts(shorts)
                .build();
        post.setShorts(shorts);
        postRepository.save(post);

        Hashtag hashtag1 = Hashtag.builder()
                .post(post)
                .tag("해시태그1")
                .build();
        Hashtag hashtag2 = Hashtag.builder()
                .post(post)
                .tag("해시태그2")
                .build();

        Long userId = user.getId();

        hashtagRepository.saveAll(List.of(hashtag1, hashtag2));

        em.flush();
        em.clear();
        Post savedPost = postRepository.findById(userId).get();
        postRepository.delete(savedPost);

        //then
        Assertions.assertThat(postRepository.count()).isZero();
        Assertions.assertThat(shortsRepository.count()).isZero();
        Assertions.assertThat(hashtagRepository.count()).isZero();
    }
}