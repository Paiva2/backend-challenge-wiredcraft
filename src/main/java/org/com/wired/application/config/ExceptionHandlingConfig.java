package org.com.wired.application.config;

import org.com.wired.domain.usecase.common.exception.BadRequestException;
import org.com.wired.domain.usecase.common.exception.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;

@ControllerAdvice
public class ExceptionHandlingConfig {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(mapErrors(e.getAllErrors().toString(), e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleBadRequestException(Exception e) {
        LinkedHashMap<String, Object> errors = mapErrors(e.getMessage(), e.getClass().getSimpleName(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleConflictException(Exception e) {
        LinkedHashMap<String, Object> errors = mapErrors(e.getMessage(), e.getClass().getSimpleName(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }
    
    private LinkedHashMap<String, Object> mapErrors(String message, String exceptionName, Integer status) {
        return new LinkedHashMap<>() {{
            put("timestamp", new Date());
            put("message", message);
            put("status", status);
            put("exception", exceptionName);
        }};
    }
}
