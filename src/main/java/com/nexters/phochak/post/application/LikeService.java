package com.nexters.phochak.post.application;

import com.nexters.phochak.post.application.port.in.LikesFetchDto;
import com.nexters.phochak.post.application.port.in.LikesUseCase;
import com.nexters.phochak.post.application.port.out.CancelLikesPort;
import com.nexters.phochak.post.application.port.out.CheckLikedPort;
import com.nexters.phochak.post.application.port.out.LoadLikesPort;
import com.nexters.phochak.post.application.port.out.LoadPostPort;
import com.nexters.phochak.post.application.port.out.LoadUserPort;
import com.nexters.phochak.post.application.port.out.SaveLikesPort;
import com.nexters.phochak.post.domain.Likes;
import com.nexters.phochak.post.domain.Post;
import com.nexters.phochak.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService implements LikesUseCase {
    private final LoadUserPort loadUserPort;
    private final LoadPostPort loadPostPort;
    private final SaveLikesPort saveLikesPort;
    private final CancelLikesPort cancelLikesPort;
    private final LoadLikesPort loadLikesPort;
    private final CheckLikedPort checkLikedPort;

    @Override
    public void addPhochak(final Long userId, final Long postId) {
        final User user = loadUserPort.load(userId);
        final Post post = loadPostPort.load(postId);
        final Likes likes = new Likes(user, post);
        saveLikesPort.save(likes);
    }

    @Override
    public void cancelPhochak(final Long userId, final Long postId) {
        final User user = loadUserPort.load(userId);
        final Post post = loadPostPort.load(postId);
        final Likes likes = loadLikesPort.load(user, post);
        cancelLikesPort.cancel(likes);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, LikesFetchDto> checkIsLikedPost(List<Long> postIds, Long userId) {
        return checkLikedPort.checkIsLikedPostList(postIds, userId);
    }

}
