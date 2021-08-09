package org.app.controller;

import org.app.exception.BookShelfLoginException;
import org.app.exception.FilterOrRemoveByFieldException;
import org.app.exception.UploadFileException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@ControllerAdvice
public class ErrorController {

    @GetMapping("/404")
    public String notFoundError() {
        return "errors/404";
    }

    @ExceptionHandler(BookShelfLoginException.class)
    public String handleError(Model model, BookShelfLoginException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/404";
    }

    @ExceptionHandler(UploadFileException.class)
    public String handleUploadFileError(Model model, UploadFileException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/general_error_page_with_message";
    }

    @ExceptionHandler(FilterOrRemoveByFieldException.class)
    public String handleNoSuchFieldDueFilterOrDelete(Model model, NoSuchFieldException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/general_error_page_with_message";
    }
}
