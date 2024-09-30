package com.chuca.memberservice.global.exception;

import com.chuca.memberservice.global.response.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailException extends BaseException {
  public EmailException(ErrorCode errorCode) {
    super(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
