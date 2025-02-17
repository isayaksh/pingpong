package org.prography.pingpong.global.exception;

import org.prography.pingpong.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiResponse handleException(Exception e) {
        return ApiResponse.error();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiResponse.error(e.getMessage());
    }

}
