package com.example.taskFlow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.openmbean.KeyAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ArgumentNotValidException handleBadRequestException(MethodArgumentNotValidException ex){
        return new ArgumentNotValidException("Something doesn't look right, the arguments you've provided are invalid.", ex.getBindingResult()
                .getFieldErrors()
                .stream().map(FieldError::getDefaultMessage)
                .toList());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception handleIllegalArgumentException(IllegalArgumentException ex){
        return new Exception("You've provided an invalid argument.", ex.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception handleNullPointerException(NullPointerException ex){
        return new Exception("You've provided an invalid argument.", ex.getMessage());
    }

    @ExceptionHandler(KeyAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Exception handleConflictException(KeyAlreadyExistsException ex){
        return new Exception("You've provided an invalid argument.", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Exception handleConflictException(RuntimeException ex){
        return new Exception("Something went wrong :(", ex.getMessage());
    }
}
