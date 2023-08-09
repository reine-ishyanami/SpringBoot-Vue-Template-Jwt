package com.reine.backend.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author reine
 */
public record ApiResponse<T>(int code, T data, String message) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, data, "请求成功");
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }


    public static <T> ApiResponse<T> unauthorized(String message) {
        return failure(401, message);
    }
    public static <T> ApiResponse<T> forbidden(String message) {
        return failure(403, message);
    }

    public static <T> ApiResponse<T> failure(int code, String message) {
        return new ApiResponse<>(code, null, message);
    }

    public String asJsonString() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

}
