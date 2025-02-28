package com.example.dws.Controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatusException(ResponseStatusException exception) {
        ModelAndView modelAndView = new ModelAndView("message"); // Redirige a message.mustache
        modelAndView.addObject("error", true);
        modelAndView.addObject("message", exception.getReason()); // Usa getReason() en lugar de getMessage()
        return modelAndView;
    }
}

