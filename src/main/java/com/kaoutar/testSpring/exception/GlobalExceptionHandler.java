package com.kaoutar.testSpring.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.swing.text.html.parser.Entity;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> hanleNotFound(EntityNotFoundException ex){
        Map<String, Object> errorDetails = Map.of(
                "error", "Entity Not Found",
                "message", ex.getMessage(),
                "status", 404
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public  ResponseEntity<String> handleGeneralError(Exception ex){
        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
