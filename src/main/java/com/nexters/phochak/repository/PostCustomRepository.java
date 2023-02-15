package com.nexters.phochak.repository;

import com.nexters.phochak.dto.PostFetchCommand;
import com.nexters.phochak.dto.response.PostPageResponseDto;

import java.util.List;

public interface PostCustomRepository {
    List<PostPageResponseDto> findNextPageByCursor(PostFetchCommand command);
}
