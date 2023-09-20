package com.nexters.phochak.shorts.application.port.in;

import java.net.URL;

public record PostUploadKeyResponseDto(URL uploadUrl, String uploadKey) {
}
