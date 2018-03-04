package com.bean.form.exceptions;

import com.bean.form.exceptions.BookExceptions.BookAlreadyPresentException;
import com.bean.form.exceptions.BookExceptions.BookNotFoundException;
import com.bean.form.exceptions.BookExceptions.IncorrectInputBookDataException;
import com.bean.form.exceptions.PersonExceptions.IncorrectPersonInputDataException;
import com.bean.form.exceptions.PersonExceptions.PersonAlreadyExistsException;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            BookAlreadyPresentException.class,
            BookNotFoundException.class,
            IncorrectInputBookDataException.class,
            IncorrectPersonInputDataException.class,
            PersonAlreadyExistsException.class,
            PersonNotFoundException.class,
            BookAlreadyIssuedException.class,
            BookIssuedAnotherPersonException.class,
            BookWasNotGivenException.class})
    public ResponseEntity<String> handleLogicalExceptions(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException() {
        return new ResponseEntity<>("Некорректные входные данные\n", HttpStatus.BAD_REQUEST);
    }

}
