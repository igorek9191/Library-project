package com.bean.form.controller;

import com.bean.form.dao.BookDAO;
import com.bean.form.dao.HistoryDAO;
import com.bean.form.exceptions.*;
import com.bean.form.exceptions.BookExceptions.BookNotFoundException;
import com.bean.form.exceptions.BookExceptions.IncorrectInputBookDataException;
import com.bean.form.exceptions.PersonExceptions.IncorrectPersonInputDataException;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
import com.bean.form.service.BookService;
import com.bean.form.service.PersonService;
import com.bean.form.view.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@RestController
@RequestMapping(value = "/", produces = APPLICATION_JSON_VALUE)
public class GiveTakeBackController {

    private final TaskExecutor executor;

    private final BookService bookService;

    private final PersonService personService;

    private final BookDAO bookDAO;

    private final HistoryDAO givenAndReturnBookDAO;

    @Autowired
    public GiveTakeBackController(TaskExecutor executor, BookService bookService, PersonService personService, BookDAO bookDAO, HistoryDAO givenAndReturnBookDAO) {
        this.executor = executor;
        this.bookService = bookService;
        this.personService = personService;
        this.bookDAO = bookDAO;
        this.givenAndReturnBookDAO = givenAndReturnBookDAO;
    }

    @ApiOperation(value = "giveBookToPerson")
    @CrossOrigin
    @RequestMapping(value = "/giveBookToPerson", method = {POST})
    public ResponseEntity<String> giveBookToPerson (@RequestBody BookToFromPerson bookToFromPerson) {

        BookView bookView = bookToFromPerson.bookView();
        PersonView personView = bookToFromPerson.personView();

        PersonController.validateInputData(personView);
        BookController.validateInputData(bookView);

        bookService.addPersonToBook(bookView, personView);
        return new ResponseEntity<>("Книга "+bookView.getBookName()+" успешно выдана читателю "+personView.getFullName(), HttpStatus.OK);
    }

    @ApiOperation(value = "takeBackBookFromPerson")
    @CrossOrigin
    @RequestMapping(value = "/takeBackBookFromPerson", method = {POST})
    public ResponseEntity<String> takeBackBookFromPerson (@RequestBody BookToFromPerson bookFromPersonView) {

        BookView bookView = bookFromPersonView.bookView();
        PersonView personView = bookFromPersonView.personView();

        PersonController.validateInputData(personView);
        BookController.validateInputData(bookView);

        bookService.detachPersonFromBook(bookView, personView);
        return new ResponseEntity<>("Книгу " + bookView.getBookName() + " забрали у читателя " + personView.getFullName(), HttpStatus.OK);
    }

    @ApiOperation(value = "checkBooksOfPerson")
    @CrossOrigin
    @RequestMapping(value = "/checkBooksOfPerson", method = {POST})
    public ResponseEntity<List<String>> checkBooksOfPerson(@RequestBody PersonView personView) {

        PersonController.validateInputData(personView);

        PersonView person;
        List<String> books;

        try {
            person = personService.findPersonWithId(personView);
        } catch (EmptyResultDataAccessException e){
            throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        }
        books = bookService.findPersonBooks(person.getId());
        if(books.isEmpty()) return new ResponseEntity<>(books, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @ApiOperation(value = "checkBusyBooks")
    @CrossOrigin
    @RequestMapping(value = "/checkBusyBooks", method = {GET})
    public ResponseEntity<List<String>> checkBusyBooks() {

        List<String> busyBooks = bookDAO.listOfBusyBooks();
        if(busyBooks.isEmpty()) return new ResponseEntity<>(busyBooks, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(busyBooks, HttpStatus.OK);
    }

    @ApiOperation(value = "checkFreeBooks")
    @CrossOrigin
    @RequestMapping(value = "/checkFreeBooks", method = {GET})
    public ResponseEntity<List<String>> checkFreeBooks() {

        List<String> freeBooks = bookDAO.listOfFreeBooks();
        if(freeBooks.isEmpty()) return new ResponseEntity<>(freeBooks, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(freeBooks, HttpStatus.OK);
    }

    @ApiOperation(value = "checkPersonHistory")
    @CrossOrigin
    @RequestMapping(value = "/checkPersonHistory", method = {POST})
    public ResponseEntity<List<String>> checkPersonHistory(@RequestBody PersonView personView) {

        PersonController.validateInputData(personView);

        List<String> history;

        try {
            personService.findByFullNameAndTelNomber(personView);
        } catch (EmptyResultDataAccessException e){
            throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        }
        history = givenAndReturnBookDAO.showEntriesThroughPerson(personView);
        if(history.isEmpty()) return new ResponseEntity<>(history, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @ApiOperation(value = "checkBookHistory")
    @CrossOrigin
    @RequestMapping(value = "/checkBookHistory", method = {POST})
    public ResponseEntity<List<String>> checkBookHistory(@RequestBody BookView bookView) {

        BookController.validateInputData(bookView);

        List<String> history;

        BookView book = bookService.findById(bookView);
        if(book == null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());

        history = givenAndReturnBookDAO.showEntriesThroughBook(bookView);
        if(history.isEmpty()) return new ResponseEntity<>(history, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @ExceptionHandler({BookAlreadyIssuedException.class, BookIssuedAnotherPersonException.class,BookWasNotGivenException.class, PersonNotFoundException.class, IncorrectPersonInputDataException.class, BookNotFoundException.class, IncorrectInputBookDataException.class})
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ErrorResponse<List<String>> handleException(RuntimeException ex){
        ErrorResponse<List<String>> listOfResponse = new ErrorResponse<>();
        List<String> list = new ArrayList<>();
        list.add(ex.getMessage());
        listOfResponse.setErrors(list);
        return listOfResponse;
    }

}
