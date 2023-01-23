package com.nexters.phochak.exception;

public enum ResCode {

    // P0xx: 정상 처리
    OK("P000", "정상 처리"),

    //P1xx: 서버 예외
    INTERNAL_SERVER_ERROR("P100", "서버 에러 발생"),

    //P2xx: 인증 예외
    INVALID_INPUT("P200", "올바르지 않은 입력값");

    //P3xx: 유저 예외
    NOT_FOUND_USER("P300", "존재하지 않는 유저입니다");

    //P4xx: 게시글 예외

    //P5xx: 파일 예외

    private final String code;
    private final String message;

    ResCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
