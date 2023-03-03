package com.nexters.phochak.service;

import com.nexters.phochak.client.StorageBucketClient;
import com.nexters.phochak.domain.Post;
import com.nexters.phochak.domain.User;
import com.nexters.phochak.dto.PostUploadKeyResponseDto;
import com.nexters.phochak.exception.PhochakException;
import com.nexters.phochak.exception.ResCode;
import com.nexters.phochak.repository.PostRepository;
import com.nexters.phochak.repository.UserRepository;
import com.nexters.phochak.service.impl.PostServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks PostServiceImpl postService;

    @Mock
    StorageBucketClient storageBucketClient;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;

    @Test
    @DisplayName("Presigned URL과 UploadKey 생성에 성공한다")
    void generateUploadKey() throws MalformedURLException {
        //given
        given(storageBucketClient.generatePresignedUrl(any())).willReturn(new URL("http://test.com"));

        //when
        PostUploadKeyResponseDto result = postService.generateUploadKey("mov");

        //then
        assertThat(result.getUploadKey()).isNotNull();
    }

    @Test
    @DisplayName("포스트 주인이 아닌 유저가 삭제를 시도하면 NOT_POST_OWNER 예외가 발생한다")
    void deletePostByNotPostOwner() {
        //given
        User user = new User();
        User owner = new User();
        Post post = Post.builder()
                        .user(owner)
                        .build();
        given(userRepository.getReferenceById(any())).willReturn(user);
        given(postRepository.findPostFetchJoin(any())).willReturn(Optional.of(post));

        //when, then
        assertThatThrownBy(() -> postService.delete(0L, 0L))
                .isInstanceOf(PhochakException.class)
                .hasMessage(ResCode.NOT_POST_OWNER.getMessage());
    }
}
