package com.nexters.phochak.dto.response;

import com.nexters.phochak.domain.IgnoredUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IgnoredUserResponseDto {

    private Long id;
    private String nickname;
    private String profileImageUrl;

    public static IgnoredUserResponseDto of(IgnoredUser ignoredUser) {
        return new IgnoredUserResponseDto(
                ignoredUser.getIgnoredUser().getId(),
                ignoredUser.getIgnoredUser().getNickname(),
                ignoredUser.getIgnoredUser().getProfileImgUrl()
            );
    }

    public static List<IgnoredUserResponseDto> of(List<IgnoredUser> ignoredUserList) {
        return ignoredUserList.stream()
                .map(IgnoredUserResponseDto::of)
                .collect(Collectors.toList());
    }
}
