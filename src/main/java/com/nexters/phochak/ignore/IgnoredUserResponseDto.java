package com.nexters.phochak.ignore;

import com.nexters.phochak.ignore.domain.IgnoredUsers;
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

    public static IgnoredUserResponseDto of(IgnoredUsers ignoredUser) {
        return new IgnoredUserResponseDto(
                ignoredUser.getIgnoredUsersRelation().getIgnoredUser().getId(),
                ignoredUser.getIgnoredUsersRelation().getIgnoredUser().getNickname(),
                ignoredUser.getIgnoredUsersRelation().getIgnoredUser().getProfileImgUrl()
            );
    }

    public static List<IgnoredUserResponseDto> of(List<IgnoredUsers> ignoredUserList) {
        return ignoredUserList.stream()
                .map(IgnoredUserResponseDto::of)
                .collect(Collectors.toList());
    }
}
