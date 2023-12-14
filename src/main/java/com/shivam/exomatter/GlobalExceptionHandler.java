package com.shivam.exomatter;

import com.shivam.exomatter.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationExceptions(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                .errorMessage(ex.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .errorMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage())
                        .build(), HttpStatus.BAD_REQUEST);

    }

}
