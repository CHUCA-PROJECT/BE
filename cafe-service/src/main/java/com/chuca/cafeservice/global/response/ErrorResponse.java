package com.chuca.cafeservice.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse<T> {
    private Boolean isSuccess;
    private LocalDateTime timeStamp;
    private String errorCode;
    private String message;

    /* 요청에 실패한 경우 1 */
    public ErrorResponse(String errorCode, String message) {
        this.isSuccess=false;
        this.timeStamp = LocalDateTime.now().withNano(0);
        this.errorCode = errorCode;
        this.message = message;
    }

    /* 요청에 실패한 경우 2 */
    public ErrorResponse(ErrorCode errorCode, String message) {
        this.isSuccess=false;
        this.timeStamp = LocalDateTime.now().withNano(0);
        this.errorCode=errorCode.getErrorCode();
        this.message=message;
    }

    /* 요청에 실패한 경우 3 */
    public ErrorResponse(ErrorCode errorCode) {
        this.isSuccess=false;
        this.timeStamp = LocalDateTime.now().withNano(0);
        this.errorCode=errorCode.getErrorCode();
        this.message=errorCode.getMessage();
    }
}
