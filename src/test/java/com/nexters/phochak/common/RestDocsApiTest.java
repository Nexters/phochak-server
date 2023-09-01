package com.nexters.phochak.common;

import com.nexters.phochak.common.docs.RestDocs;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
import com.nexters.phochak.user.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.user.domain.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
public class RestDocsApiTest extends RestDocs {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired UserRepository userRepository;
    @Autowired
    JwtTokenUseCase jwtTokenUseCase;

    @BeforeEach
    void setUp() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
        TestUtil.setAccessToken(generateTestUserAndToken());
        TestUtil.setUserRepository(userRepository);
        TestUtil.setJwtTokenUseCase(jwtTokenUseCase);
    }

    private String generateTestUserAndToken() {
        UserEntity userEntity = UserFixture.anUser().userId(1L).build();
        userRepository.save(userEntity);
        return generateTestToken(userEntity.getId());
    }

    private String generateTestToken(final Long userId) {
        JwtTokenUseCase.TokenVo tokenDto = jwtTokenUseCase.generateToken(userId, 999999999L);
        return JwtTokenUseCase.TokenVo.TOKEN_TYPE + " " + tokenDto.getTokenString();
    }

}
