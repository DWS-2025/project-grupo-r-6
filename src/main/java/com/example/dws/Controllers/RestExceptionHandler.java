package com.example.dws.Controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = org.springframework.web.bind.annotation.RestController.class)
public class RestExceptionHandler {

    @ExceptionHandler({ResponseStatusException.class, Exception.class})
    public ResponseEntity<Map<String, Object>> handleRestException(Exception exception, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        HttpStatus status;

        if (exception instanceof ResponseStatusException resExp) {
            status = (HttpStatus) resExp.getStatusCode();
            body.put("message", resExp.getReason());
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body.put("message", exception.getMessage());
        }

        body.put("status", status.value());
        return new ResponseEntity<>(body, status);
    }
}

