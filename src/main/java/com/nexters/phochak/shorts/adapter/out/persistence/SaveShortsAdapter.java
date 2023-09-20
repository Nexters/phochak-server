package com.nexters.phochak.shorts.adapter.out.persistence;

import com.nexters.phochak.shorts.application.port.out.SaveShortsPort;
import com.nexters.phochak.shorts.domain.Shorts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SaveShortsAdapter implements SaveShortsPort {

    private final ShortsMapper shortsMapper;
    private final ShortsRepository shortsRepository;

    @Override
    @Transactional
    public void save(final Shorts shorts) {
        final ShortsEntity entity = shortsMapper.toEntity(shorts);
        shortsRepository.save(entity);
    }
}
