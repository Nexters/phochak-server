package com.nexters.phochak.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PhochakException.class)
    protected ResponseEntity<ExceptionResponseDto> handleBreakingCustomException(PhochakException e) {
        log.info(e.getMessage());
        return ResponseEntity.ok().body(ExceptionResponseDto.builder()
                .resCode(e.getResCode().getCode())
                .resMessage(e.getResCode().getMessage())
                .resDetail(e.getDetail())
                .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponseDto> handleCustomInternalErrorException(Exception e) {
        log.error("Internal Error Message: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponseDto.builder()
                .resCode(ResCode.INTERNAL_SERVER_ERROR.getCode())
                .resMessage(ResCode.INTERNAL_SERVER_ERROR.getMessage())
                .build());
    }

}
