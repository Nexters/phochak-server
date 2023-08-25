package com.nexters.phochak.common.exception;

import com.nexters.phochak.post.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(PhochakException.class)
    protected CommonResponse<Void> handlePhochakCustomException(PhochakException e, HttpServletRequest request) {
        log.warn("[PhochakException 발생] request url: {}", request.getRequestURI(), e);
        return new CommonResponse<>(e.getResCode().getCode(), e.getCustomResMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse<Void>> handleInternalErrorException(Exception e, HttpServletRequest request) {
        log.error("[Internal Error Message] request url: {}", request.getRequestURI(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse<>(ResCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(BindException.class)
    protected CommonResponse<Void> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("[BindException 발생] request url: {}", request.getRequestURI(), e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        StringBuilder stringBuilder = getResponseStringBuilder(allErrors);

        return new CommonResponse<>(ResCode.INVALID_INPUT.getCode(), stringBuilder.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected CommonResponse<Void> pathParamException(MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("[MissingServletRequestParameterException 발생] request url: {}", request.getRequestURI(), e);

        return new CommonResponse<>(ResCode.INVALID_INPUT.getCode(), "check this param: " + e.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected CommonResponse<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("[MethodArgumentTypeMismatchException 발생] request url: {}", request.getRequestURI(), e);

        return new CommonResponse<>(ResCode.INVALID_INPUT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected CommonResponse<Void> handleRequestBodyException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("[HttpMessageNotReadableException 발생] request url: {}", request.getRequestURI(), e);

        return new CommonResponse<>(ResCode.NOT_FOUND_REQUIRED_FIELD.getCode(), "request body에 필요한 값이 존재하지 않습니다");
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
