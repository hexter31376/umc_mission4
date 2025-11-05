package com.hexter31376.umc_mission4.global.apiPayload;

import com.hexter31376.umc_mission4.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private final String code;
    private final String message;
}

