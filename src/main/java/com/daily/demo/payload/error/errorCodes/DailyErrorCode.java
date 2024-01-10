package com.daily.demo.payload.error.errorCodes;

import org.springframework.http.HttpStatus;

import com.daily.demo.payload.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum DailyErrorCode implements ErrorCode {

    NOT_FOUND_DAILY(HttpStatus.INTERNAL_SERVER_ERROR, "656", "Not Found Daily");

    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String Message;

    private static final Map<String, DailyErrorCode> BY_STATUS_CODE = Stream.of(values())
            .collect(Collectors.toMap(DailyErrorCode::getStatusCode, Function.identity()));

    private static final Map<String, DailyErrorCode> BY_MESSAGE = Stream.of(values())
            .collect(Collectors.toMap(DailyErrorCode::getStatusCode, Function.identity()));

    public static DailyErrorCode valueOfStatusCode(String statusCode) {
        return BY_STATUS_CODE.get(statusCode);
    }

    public static DailyErrorCode valueOfMessage(String message) {
        return BY_MESSAGE.get(message);
    }
}
