package com.shoping.commonservice.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.axonframework.commandhandling.CommandExecutionException;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.shoping.commonservice.model.ErrorMessage;
import com.shoping.commonservice.model.response.RestResponse;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return new ResponseEntity<>(new ErrorMessage("500", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        RestResponse<Object> response = new RestResponse<>();
        response.setData(null);
        response.setError("Validation failed");
        response.setMessage(errors);
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AggregateNotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleAggregateNotFound(
            AggregateNotFoundException ex) {

        RestResponse<Object> response = new RestResponse<>();

        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setError("NOT_FOUND");
        response.setMessage(ex.getMessage());
        response.setData(null);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(CommandExecutionException.class)
    public ResponseEntity<RestResponse<Object>> handleCommandExecutionException(
            CommandExecutionException ex) {

        RestResponse<Object> response = new RestResponse<>();

        if (ex.getMessage() != null
                && ex.getMessage().contains("aggregate was not found")) {

            response.setStatusCode(404);
            response.setError("NOT_FOUND");
            response.setMessage(ex.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(response);
        }

        response.setStatusCode(500);
        response.setError("INTERNAL_SERVER_ERROR");
        response.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

}
