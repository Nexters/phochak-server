package com.nexters.phochak.shorts.adapter.out.persistence;

import com.nexters.phochak.shorts.domain.Shorts;
import org.springframework.stereotype.Component;

@Component
public class ShortsMapper {

    public Shorts toDomain(final ShortsEntity shortsEntity) {
        return Shorts.forMapper(
                shortsEntity.getId(),
                shortsEntity.getShortsStateEnum(),
                shortsEntity.getUploadKey(),
                shortsEntity.getShortsUrl(),
                shortsEntity.getThumbnailUrl());
    }

    public ShortsEntity toEntity(final Shorts shorts) {
        return new ShortsEntity(
                shorts.getId(),
                shorts.getShortsStateEnum(),
                shorts.getUploadKey(),
                shorts.getShortsUrl(),
                shorts.getThumbnailUrl());
    }
}
