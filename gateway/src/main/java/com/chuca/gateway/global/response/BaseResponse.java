package com.chuca.gateway.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private Boolean isSuccess;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /* 요청이 성공한 경우 1 */
    public static <T> BaseResponse<T> create(T data) {
        return new BaseResponse<>(true, "요청에 성공하였습니다.", data);
    }

    /* 요청이 성공한 경우 2 */
    public static <T> BaseResponse<T> create(String message, T data) {
        return new BaseResponse<>(true, message, data);
    }
}
