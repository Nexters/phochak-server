package com.nexters.phochak.ignore.application.port.in;

import com.nexters.phochak.ignore.domain.IgnoredUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IgnoredUserResponseDto {

    private Long id;
    private String nickname;
    private String profileImgUrl;

    public static IgnoredUserResponseDto of(IgnoredUser ignoredUser) {
        return new IgnoredUserResponseDto(
                ignoredUser.getIgnoredUser().getId(),
                ignoredUser.getIgnoredUser().getNickname(),
                ignoredUser.getIgnoredUser().getProfileImgUrl()
            );
    }
}
