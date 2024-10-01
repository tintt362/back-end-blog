package com.trongtin.blog.identity.exception;

import com.trongtin.blog.identity.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<String> sqlIntegrityConstraintViolationException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = NoSuchElementException.class)
    ResponseEntity<String> NoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);
        Map<String, Object> attributes;
        var constraintViolation = e .getBindingResult()
                .getAllErrors().getFirst().unwrap(ConstraintViolation.class);
        attributes = constraintViolation.getConstraintDescriptor().getAttributes();
        log.warn(attributes.toString());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());

        apiResponse.setMessage(Objects.nonNull(attributes)?
                mapAttribute(errorCode.getMessage(), attributes) :
                errorCode.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> appException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());


        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minvalue = attributes.get(MIN_ATTRIBUTE).toString();
        return message.replace("{min}", minvalue);
    }
}
