package com.daily.demo.payload.error.errorCodes;

import org.springframework.http.HttpStatus;

import com.daily.demo.payload.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DailyErrorCode implements ErrorCode {

    NOT_FOUND_DAILY(HttpStatus.INTERNAL_SERVER_ERROR, "Not Found Daily");

    private final HttpStatus httpStatus;
    private final String Message;
}
