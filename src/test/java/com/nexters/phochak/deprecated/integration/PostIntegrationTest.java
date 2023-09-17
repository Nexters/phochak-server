//package com.nexters.phochak.deprecated.integration;
//
//import com.nexters.phochak.common.exception.PhochakException;
//import com.nexters.phochak.post.adapter.out.persistence.PostEntity;
//import com.nexters.phochak.post.adapter.out.persistence.PostRepository;
//import com.nexters.phochak.post.application.PostService;
//import com.nexters.phochak.post.domain.PostCategoryEnum;
//import jakarta.persistence.EntityManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//
//@Disabled
//@ActiveProfiles("test")
//@Transactional
//@SpringBootTest
//class PostIntegrationTest {
//
//    @Autowired
//    PostService postService;
//
//    @Autowired
//    PostRepository postRepository;
//
//    @Autowired
//    EntityManager entityManager;
//
//    PostEntity postEntity;
//
//    @BeforeEach
//    void setUp() {
//        postEntity = PostEntity.builder()
//                .postCategory(PostCategoryEnum.CAFE)
//                .build();
//    }
//
//    @Test
//    @DisplayName("조회수 UPDATE 서비스를 실행하면, 조회수가 1 올라간다")
//    void updateView() {
//        // given
//        postEntity = postRepository.save(postEntity);
//
//        // when
//        int i = postService.updateView(postEntity.getId());
//        assertThat(i).isEqualTo(1);
//        entityManager.clear();
//
//        // then
//        Optional<PostEntity> updatedPostOptional = postRepository.findById(postEntity.getId());
//        assertThat(updatedPostOptional).isPresent();
//        PostEntity updatedPostEntity = updatedPostOptional.get();
//        assertThat(updatedPostEntity.getView()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("조회수 UPDATE시 게시글이 존재하지 않으면 예외가 발생한다")
//    void updateView_exception() {
//        // given
//        // do nothing
//
//        // when & then
//        assertThatThrownBy(() -> postService.updateView(1L))
//                .isInstanceOf(PhochakException.class);
//    }
//}
