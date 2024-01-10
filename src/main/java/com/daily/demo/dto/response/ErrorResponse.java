package com.daily.demo.dto.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.daily.demo.payload.error.errorCodes.CommonErrorCode;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private String statusCode;

    private String message;

    private String timeStamp;

    @Builder
    ErrorResponse(String statusCode, String message, String timeStamp) {

        this.statusCode = statusCode;
        this.message = StringUtils.isNotBlank(message) ? message : CommonErrorCode.valueOfMessage(message).getMessage();
        this.timeStamp = StringUtils.isNotBlank(timeStamp) ? timeStamp
                : LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMDDHHMMSS"));
    }

}
