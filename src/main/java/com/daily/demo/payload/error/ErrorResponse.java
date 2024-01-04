package com.daily.demo.payload.error;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {
    // private final String name;
    private final LocalDateTime timeStamp;
    private final String message;

    private final List<FieldError> errorList;
    private final String code;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<ValidationError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class ValidationError {
        private final String field;
        private final String message;

        public static ValidationError of(final FieldError fieldError) {
            return ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }

    // public static Optional<ErrorResponse> of(ErrorCode errorCode) {
    // return Optional.of(ErrorResponse.builder()
    // .message(errorCode.getMessage())
    // // .name(errorCode.getName())
    // .timeStamp(LocalDateTime.now())

    // .build());

    // }

}
