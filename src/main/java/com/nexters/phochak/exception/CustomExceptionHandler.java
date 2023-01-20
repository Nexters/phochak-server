package com.nexters.phochak.exception;

import com.nexters.phochak.dto.ExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PhochakException.class)
    protected ResponseEntity<ExceptionResponseDto> handlePhochakCustomException(PhochakException e) {
        log.info(e.getMessage());
        return ResponseEntity.ok().body(ExceptionResponseDto.builder()
                .resCode(e.getResCode())
                .customResMessage(e.getCustomResMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponseDto> handleInternalErrorException(Exception e) {
        log.error("Internal Error Message: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponseDto.builder()
                .resCode(ResCode.INTERNAL_SERVER_ERROR)
                .build());
    }

}
