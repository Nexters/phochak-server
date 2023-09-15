package com.nexters.phochak.shorts;

import lombok.Getter;

import java.net.URL;

@Getter
public record PostUploadKeyResponseDto(URL uploadUrl, String uploadKey) {
}
