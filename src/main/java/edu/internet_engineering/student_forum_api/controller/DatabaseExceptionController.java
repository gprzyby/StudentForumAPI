package edu.internet_engineering.student_forum_api.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DatabaseExceptionController {
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Integrity database error")
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void databaseIntegrityError() {
    }
}
