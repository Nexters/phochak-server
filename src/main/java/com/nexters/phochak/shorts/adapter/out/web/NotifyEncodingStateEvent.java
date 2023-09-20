package com.nexters.phochak.shorts.adapter.out.web;

import com.nexters.phochak.shorts.domain.ShortsStateEnum;

public record NotifyEncodingStateEvent(String uploadKey, ShortsStateEnum shortsStateEnum) {
}
