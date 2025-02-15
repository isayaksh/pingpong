package org.prography.pingpong.global.common;

public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T result;

    public static <T> ApiResponse<T> success(T result) {
        return new ApiResponse<>(200, "API 요청이 성공했습니다.", result);
    }

    public static ApiResponse success() {
        return success(null);
    }

    public static <T> ApiResponse<T> fail(T result) {
        return new ApiResponse<>(201, "불가능한 요청입니다.", result);
    }

    public static ApiResponse fail() {
        return fail(null);
    }

    public static <T> ApiResponse<T> error(T result) {
        return new ApiResponse<>(500, "에러가 발생했습니다.", result);
    }

    public static ApiResponse error() {
        return error(null);
    }

    private ApiResponse(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

}
