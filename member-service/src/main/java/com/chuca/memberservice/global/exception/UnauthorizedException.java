package com.chuca.memberservice.global.exception;

import com.chuca.memberservice.global.response.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends BaseException {
    private String message;

    protected UnauthorizedException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode, httpStatus);
    }

    public UnauthorizedException(String message, HttpStatus httpStatus) {
        super(ErrorCode.BAD_REQUEST, message, httpStatus);
        this.message = message;
    }
}
