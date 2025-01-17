package com.chuca.memberservice.global.exception;

import com.chuca.memberservice.global.response.ErrorResponse;
import com.chuca.memberservice.global.response.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.function.Consumer;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        return handleException(ex, ex.getErrorCode(), ex.getMessage(), ex.getHttpStatus(), log::warn);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> inputMethodArgumentInvalidExceptionHandler (MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return handleException(ex, ErrorCode.BAD_REQUEST, message, HttpStatus.BAD_REQUEST, log::warn);
    }

    @ExceptionHandler(PatternSyntaxException.class)
    public ResponseEntity<ErrorResponse> inputPatternSyntaxExceptionHandler(PatternSyntaxException ex) {
        return handleException(ex, ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getMessage(), HttpStatus.BAD_REQUEST, log::warn);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonParseExceptionHandler(HttpMessageNotReadableException ex) {
        return handleException(ex, ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getMessage(), HttpStatus.BAD_REQUEST, log::warn);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpRequestNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex) {
        return handleException(ex, ErrorCode.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, log::warn);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> internalServerErrorHandler(Exception ex) {
        return handleException(ex, ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, log::error);
    }

    private ResponseEntity<ErrorResponse> handleException(Exception ex, ErrorCode errorCode, String message, HttpStatus httpStatus, Consumer<String> logger) {
        log.error(LOG_FORMAT, ex.getClass().getSimpleName(), errorCode.getErrorCode(), ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return ResponseEntity.status(httpStatus.value()).body(errorResponse);
    }
}
