package com.nexters.phochak.exception;

/**
 * 서비스 최상위 Exception.
 * 해당 클래스 상속 후 에러 코드 및 메시지 정의
 */
public class PhochakException extends Exception {

    private ResCode resCode;
    private String detail;

    public PhochakException(ResCode resCode) {
        super(resCode.getMessage());
        this.resCode = resCode;
    }

    public PhochakException(ResCode resCode, String detail) {
        super(resCode.getMessage());
        this.resCode = resCode;
        this.detail = detail;
    }

    public ResCode getResCode() {
        return resCode;
    }

    public String getDetail() {
        return detail;
    }
}
