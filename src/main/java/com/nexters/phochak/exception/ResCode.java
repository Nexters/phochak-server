package com.nexters.phochak.exception;

public enum ResCode {

    // P0xx: 정상 처리
    OK("P000", "정상 처리"),

    //P1xx: 서버 예외
    INTERNAL_SERVER_ERROR("P100", "서버 에러 발생"),

    //P2xx: 인증 예외
    INVALID_INPUT("P200", "요청 값의 형식이 올바르지 않습니다"),
    TOKEN_NOT_FOUND("P201", "토큰을 찾을 수 없습니다(로그인 되지 않은 사용자입니다)"),
    INVALID_TOKEN("P202", "올바르지 않은 토큰입니다"),
    EXPIRED_TOKEN("P203", "만료된 토큰입니다"),
    INVALID_APPLE_TOKEN("P204", "올바르지 않은 apple identifyToken입니다"),
    NOT_SUPPORTED_PROVIDER("P205", "지원하지 않는 provider입니다"),
    NOT_FOUND_REQUIRED_FIELD("P206", "요청 필수 값이 존재하지 않습니다"),
    //P3xx: 유저 예외
    NOT_FOUND_USER("P300", "존재하지 않는 유저입니다"),
    DUPLICATED_NICKNAME("P301", "닉네임이 중복되었습니다"),
    ALREADY_IGNORED_USER("P302", "이미 무시하기한 유저입니다"),
    NOT_IGNORED_USER("P303", "무시하기 하지 않았던 유저입니다"),

    //P4xx: 게시글 예외
    NOT_FOUND_POST("P400", "존재하지 않는 게시글입니다"),
    ALREADY_PHOCHAKED("P410", "이미 포착된 게시글입니다"),
    NOT_PHOCHAKED("P411", "포착 하지 않은 게시글입니다"),
    NOT_FOUND_SORT_VALUE("P412", "최신순이 아닌 경우 정렬기준의 값은 필수입니다"),
    NOT_SUPPORTED_SORT_OPTION("P413", "지원하지 않는 정렬기준입니다"),
    NOT_FOUND_SORT_OPTION("P414", "정렬 기준이 존재하지 않습니다"),
    ALREADY_REPORTED("P415", "이미 신고된 게시글입니다"),
    NOT_POST_OWNER("P430", "해당 게시글의 소유자가 아닙니다"),

    INVALID_VIDEO_FORMAT("P450", "지원하지 않는 비디오 확장자입니다.");

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
