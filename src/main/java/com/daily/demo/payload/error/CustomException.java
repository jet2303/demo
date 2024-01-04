package com.daily.demo.payload.error;

import com.daily.demo.payload.error.errorCodes.CommonErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public CustomException(ErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    // public CustomException(String message){
    // super();
    // this.errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
    // this.message = message;
    // }

}
