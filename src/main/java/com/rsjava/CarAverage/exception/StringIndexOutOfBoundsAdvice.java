package com.rsjava.CarAverage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StringIndexOutOfBoundsAdvice {

    @ResponseBody
    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String indexOutOfBoundsHandler (IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
