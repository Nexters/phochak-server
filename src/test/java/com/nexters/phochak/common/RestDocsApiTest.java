package com.nexters.phochak.common;

import com.nexters.phochak.auth.application.port.in.JwtTokenUseCase;
import com.nexters.phochak.common.docs.RestDocs;
import com.nexters.phochak.user.adapter.out.persistence.UserEntity;
import com.nexters.phochak.user.adapter.out.persistence.UserRepository;
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

    protected String testToken;

    @BeforeEach
    void setUp() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();

        generateTestUser();
    }

    private void generateTestUser() {
        UserEntity userEntity = UserFixture.anUser().build();
        userRepository.save(userEntity);
        generateTestToken(userEntity.getId());
    }

    private void generateTestToken(final Long userId) {
        JwtTokenUseCase.TokenVo tokenDto = jwtTokenUseCase.generateToken(userId, 999999999L);
        testToken = JwtTokenUseCase.TokenVo.TOKEN_TYPE + " " + tokenDto.getTokenString();
    }
}
