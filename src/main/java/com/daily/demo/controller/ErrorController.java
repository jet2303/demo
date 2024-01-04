package com.daily.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.daily.demo.dto.response.ErrorResponse;
import com.daily.demo.payload.error.CustomException;
import com.daily.demo.payload.error.errorCodes.CommonErrorCode;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ErrorController {

    @GetMapping("/error/exception01")
    public void getMethodName() {
        throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);

    }

}
