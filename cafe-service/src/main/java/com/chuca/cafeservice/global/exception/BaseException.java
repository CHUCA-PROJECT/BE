package com.chuca.cafeservice.global.exception;


import com.chuca.cafeservice.global.response.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;
    private final HttpStatus httpStatus;

    protected BaseException(ErrorCode errorCode, HttpStatus httpStatus) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.httpStatus = httpStatus;
    }

    public BaseException(ErrorCode errorCode, String message, HttpStatus httpstatus) {
        super();
        this.errorCode = errorCode;
        this.message = message;
        this.httpStatus = httpstatus;
    }
}
