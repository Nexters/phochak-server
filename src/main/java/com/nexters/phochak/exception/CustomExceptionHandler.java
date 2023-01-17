package com.nexters.phochak.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PhochakException.class)
    protected ResponseEntity<ExceptionResponseDto> handleBreakingCustomException(PhochakException e) {
        log.info(e.getMessage());
        return ResponseEntity.ok().body(ExceptionResponseDto.builder()
                .resCode(e.getResCode().getCode())
                .resMessage(e.getResCode().getMessage())
                .detail(e.getDetail())
                .build());
    }

}
