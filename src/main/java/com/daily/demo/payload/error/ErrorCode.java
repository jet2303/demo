package com.daily.demo.payload.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    // String getName();

    HttpStatus getHttpStatus();

    String getMessage();

    String getStatusCode();

}
