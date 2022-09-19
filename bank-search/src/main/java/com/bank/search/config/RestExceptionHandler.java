package com.bank.search.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * BAD_REQUEST 처리
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handlerBadRequestException() {
        return ResponseEntity.badRequest().build();
    }

    /**
     * COMMON INTERNAL_SERVER_ERROR 처리
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handlerIllegalArgumentException() {
        return ResponseEntity.internalServerError().build();
    }
}
