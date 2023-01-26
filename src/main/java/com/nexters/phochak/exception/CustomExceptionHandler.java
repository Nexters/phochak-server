package com.nexters.phochak.exception;

import com.nexters.phochak.dto.response.ExceptionResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PhochakException.class)
    protected ResponseEntity<ExceptionResponseDto> handlePhochakCustomException(PhochakException e, HttpServletRequest request) {
        log.warn("[PhochakException 발생] request url: {}", request.getRequestURI(), e);
        return ResponseEntity.ok().body(ExceptionResponseDto.builder()
                .resCode(e.getResCode())
                .customResMessage(e.getCustomResMessage())
                .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponseDto> handleInternalErrorException(Exception e, HttpServletRequest request) {
        log.error("[Internal Error Message] request url: {}", request.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ExceptionResponseDto.builder()
                .resCode(ResCode.INTERNAL_SERVER_ERROR)
                .build());
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ExceptionResponseDto> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("[BindException 발생] request url: {}", request.getRequestURI(), e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        StringBuilder stringBuilder = getResponseStringBuilder(allErrors);

        return ResponseEntity.ok(ExceptionResponseDto.builder()
                .resCode(ResCode.INVALID_INPUT)
                .customResMessage(stringBuilder.toString())
                .build());
    }

    private static StringBuilder getResponseStringBuilder(Iterable<ObjectError> allErrors) {
        StringBuilder stringBuilder = new StringBuilder();
        allErrors.forEach(error -> stringBuilder
                            .append("['")
                            .append(((FieldError) error).getField())
                            .append("' is '")
                            .append(((FieldError) error).getRejectedValue())
                            .append("' :: ")
                            .append(error.getDefaultMessage())
                            .append("] ")
                );
        return stringBuilder;
    }
}
