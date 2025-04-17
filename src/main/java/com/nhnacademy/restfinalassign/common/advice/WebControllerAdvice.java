package com.nhnacademy.restfinalassign.common.advice;

import com.nhnacademy.restfinalassign.exception.DuplicateResourceException;
import com.nhnacademy.restfinalassign.exception.NotFoundMemberException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> duplicateResourceException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundMemberException.class)
    public ResponseEntity<String> notFoundMemberException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
