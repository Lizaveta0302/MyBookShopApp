package com.example.bookshop_app.exception;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Response> handleParseException(DateTimeParseException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<Response> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
