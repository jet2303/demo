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
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Custom Internal server error");

    private final HttpStatus httpStatus;
    private final String message;

    private static final Map<HttpStatus, CommonErrorCode> BY_STATUS_CODE = Stream.of(values())
            .collect(Collectors.toMap(CommonErrorCode::getHttpStatus, Function.identity()));

    private static final Map<String, CommonErrorCode> BY_MESSAGE = Stream.of(values())
            .collect(Collectors.toMap(CommonErrorCode::getMessage, Function.identity()));

    public static CommonErrorCode valueOfStatusCode(String statusCode) {
        // return BY_STATUS_CODE.get(statusCode);
        return BY_STATUS_CODE.get(HttpStatus.valueOf(statusCode));
    }

    public static CommonErrorCode valueOfMessage(String message) {
        return BY_MESSAGE.get(message);
    }

}
