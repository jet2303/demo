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
public enum UserErrorCode implements ErrorCode {

    // login error code enum 으로 뺄것
    INACTIVE_USER(HttpStatus.FORBIDDEN, "650", "user is inactive"),
    NOT_FOUND_USER(HttpStatus.FORBIDDEN, "651", "not found user"),
    NOT_MATCHED_PASSWORD(HttpStatus.FORBIDDEN, "652", "not matched password"),
    ANONYMOUS_USER(HttpStatus.FORBIDDEN, "653", "Anonymous don't make a board"),
    CUSTOM_ERROR(HttpStatus.FORBIDDEN, "654", "Custom Error"),
    USER_ALREADY_EXISTS(HttpStatus.FORBIDDEN, "655", "user is already exists");

    private final HttpStatus httpStatus;
    private final String statusCode;
    private final String message;

    private static final Map<String, UserErrorCode> BY_STATUS_CODE = Stream.of(values())
            .collect(Collectors.toMap(UserErrorCode::getStatusCode, Function.identity()));

    private static final Map<String, UserErrorCode> BY_MESSAGE = Stream.of(values())
            .collect(Collectors.toMap(UserErrorCode::getStatusCode, Function.identity()));

    public static UserErrorCode valueOfStatusCode(String statusCode) {
        return BY_STATUS_CODE.get(statusCode);
    }

    public static UserErrorCode valueOfMessage(String message) {
        return BY_MESSAGE.get(message);
    }

}
