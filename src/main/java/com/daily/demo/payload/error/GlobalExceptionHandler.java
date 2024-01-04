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

import com.daily.demo.payload.error.errorCodes.CommonErrorCode;
import com.daily.demo.payload.error.errorCodes.UserErrorCode;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@RestControllerAdvice
// public abstract class GlobalExceptionHandler extends
// ResponseEntityExceptionHandler {
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // @ExceptionHandler(RestApiException.class)
    // public ResponseEntity<Object> handleCustomException(RestApiException e) {
    // ErrorCode errorCode = e.getErrorCode();
    // return handleExceptionInternal(errorCode);
    // }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalargument(IllegalArgumentException e) {
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    // @ExceptionHandler(value = { CustomException.class })
    // public ResponseEntity<Object> noSuchElementException(NoSuchElementException
    // e) {
    // ErrorCode errorCode = UserErrorCode.CUSTOM_ERROR;
    // return handleExceptionInternal(errorCode);
    // }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> customException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(ex, errorCode);
    }

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

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                // .code(errorCode.getName())
                .code(errorCode.getHttpStatus().toString())
                .message(errorCode.getMessage())
                .timeStamp(null)
                .build();
    }

    private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                // .code(errorCode.getName())
                .code(errorCode.getHttpStatus().toString())
                .message(message)
                .build();
    }

    private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {

        List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream().map(ErrorResponse.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                // .code(errorCode.getName())
                .code(errorCode.getHttpStatus().toString())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();

        // List<ErrorResponse> errorList =
        // e.getBindingResult().getFieldErrors().stream().map(ErrorResponse.of(errorCode))
        // .collect(Collectors.toList());
        // return
        // ErrorResponse.builder().name(errorCode.getHttpStatus().toString()).message(errorCode.getMessage()).timeStamp(LocalDateTime.now()).build();
    }

    // @Override
    // protected ResponseEntity<Object>
    // handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
    // HttpHeaders headers, HttpStatus status, WebRequest request) {

    // ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
    // return handleExceptionInternal(ex, errorCode);
    // }
}
