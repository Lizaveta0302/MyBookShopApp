package com.example.bookshop_app.exception;

import com.example.bookshop_app.aop.annotation.ExceptionHandlerLoggable;
import io.jsonwebtoken.JwtException;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandlerLoggable
    @ExceptionHandler(ParseException.class)
    public ResponseEntity<Response> handleParseException(DateTimeParseException e) {
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandlerLoggable
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandlerLoggable
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<Response> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandlerLoggable
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Response> handleSpringSecurityExceptions(AuthenticationException e) {
        return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.OK);
    }

    @ExceptionHandlerLoggable
    @ExceptionHandler(EmptySearchException.class)
    public String handleEmptySearchException(EmptySearchException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("searchError", e);
        return "redirect:/bookshop/main";
    }

    @ExceptionHandlerLoggable
    @ExceptionHandler(JwtException.class)
    public String handleJwtException() {
        return "redirect:/signin";
    }
}
