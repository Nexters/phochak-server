package com.nexters.phochak.ignore.application.port.in;

import com.nexters.phochak.ignore.adapter.out.persistence.IgnoredUserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IgnoredUserResponseDto {

    private Long id;
    private String nickname;
    private String profileImgUrl;

    public static IgnoredUserResponseDto of(IgnoredUserEntity ignoredUser) {
        return new IgnoredUserResponseDto(
                ignoredUser.getIgnoredUsersRelation().getIgnoredUser().getId(),
                ignoredUser.getIgnoredUsersRelation().getIgnoredUser().getNickname(),
                ignoredUser.getIgnoredUsersRelation().getIgnoredUser().getProfileImgUrl()
            );
    }

    public static List<IgnoredUserResponseDto> of(List<IgnoredUserEntity> ignoredUserList) {
        return ignoredUserList.stream()
                .map(IgnoredUserResponseDto::of)
                .collect(Collectors.toList());
    }
}
