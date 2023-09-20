package com.nexters.phochak.shorts.adapter.out.persistence;

import com.nexters.phochak.shorts.application.port.out.LoadShortsPort;
import com.nexters.phochak.shorts.domain.Shorts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoadShortsAdapter implements LoadShortsPort {

    private final ShortsRepository shortsRepository;
    private final ShortsMapper shortsMapper;

    @Override
    public Shorts findByUploadKey(final String uploadKey) {
        final Optional<ShortsEntity> optionalShorts = shortsRepository.findByUploadKey(uploadKey);
        return optionalShorts.map(shortsMapper::toDomain).orElse(null);
    }
}
