package com.example.dws.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;



@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResponseStatusException.class, Exception.class})
    public ModelAndView handleException(Exception exception, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("message");
        modelAndView.addObject("error", true);
        modelAndView.addObject("previousPage", request.getHeader("Referer"));

        if (exception instanceof ResponseStatusException resExp) {
            modelAndView.addObject("message", resExp.getReason());
            modelAndView.addObject("status", resExp.getStatusCode().value());  // Pass HTTP status code
        } else {
            modelAndView.addObject("message", exception.getMessage());
            modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return modelAndView;
    }
}

