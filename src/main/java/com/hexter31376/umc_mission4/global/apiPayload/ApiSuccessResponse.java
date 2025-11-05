package com.hexter31376.umc_mission4.global.apiPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiSuccessResponse<T> {
    private final String code;
    private final String message;
    private final T data;
}

