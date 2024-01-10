package com.daily.demo.payload.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.daily.demo.dto.response.ErrorResponse;
import com.daily.demo.payload.error.errorCodes.CommonErrorCode;
import com.daily.demo.payload.error.errorCodes.UserErrorCode;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
// public abstract class GlobalExceptionHandler extends
// ResponseEntityExceptionHandler {
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalargument(IllegalArgumentException e) {
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    // @Override
    // protected ResponseEntity<Object>
    // handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    // HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    // ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
    // return handleExceptionInternal(ex, errorCode);
    // }

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleAllException(Exception ex) {
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {

        return ErrorResponse.builder()
                // .statusCode(errorCode.getHttpStatus().toString())
                .statusCode(errorCode.getStatusCode())
                .message(errorCode.getMessage())
                .timeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")))
                .build();
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {

        return ErrorResponse.builder()
                .statusCode(errorCode.getHttpStatus().toString())
                .message(message)
                .timeStamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss")))
                .build();
    }

}
