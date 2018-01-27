package com.bean.form.controller;

import com.bean.form.dao.BookDAO;
import com.bean.form.dao.HistoryDAO;
import com.bean.form.exceptions.*;
import com.bean.form.exceptions.BookExceptions.BookNotFoundException;
import com.bean.form.exceptions.BookExceptions.IncorrectInputBookDataException;
import com.bean.form.exceptions.PersonExceptions.IncorrectPersonInputData;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
import com.bean.form.service.BookService;
import com.bean.form.service.PersonService;
import com.bean.form.view.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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
    public Response<String, String> giveBookToPerson (@RequestBody BookToFromPerson bookToFromPerson) {

        BookView bookView = bookToFromPerson.bookView();
        PersonView personView = bookToFromPerson.personView();

        PersonController.validateInputData(personView);
        BookController.validateInputData(bookView);

        BookView findBook = null;
        PersonView findPerson = null;
        PersonView checkPerson = null;

        try {
            findBook = bookService.findById(bookView);
            findPerson = personService.findPersonWithId(personView);
            checkPerson = bookService.checkPersonOfBook(bookView);
        }
        catch(NullPointerException e) {
            if(findBook == null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        }
        catch (EmptyResultDataAccessException e) {
            if(findPerson == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
            if(!findBook.equals(bookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
            if(checkPerson==null){
                bookService.addPersonToBook(bookView, findPerson);
                return new Response<>("Книга "+bookView.getBookName()+" успешно выдана читателю "+personView.getFullName(),null);
            }
        }
        if(!findBook.equals(bookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        throw new BookAlreadyIssuedException(bookView.getBookName(), checkPerson.getFullName());
    }

    @ApiOperation(value = "takeBackBookFromPerson")
    @CrossOrigin
    @RequestMapping(value = "/takeBackBookFromPerson", method = {POST})
    public Response<String, String> takeBackBookFromPerson (@RequestBody BookToFromPerson bookFromPersonView) {

        BookView bookView = bookFromPersonView.bookView();
        PersonView personView = bookFromPersonView.personView();

        PersonController.validateInputData(personView);
        BookController.validateInputData(bookView);

        BookView findBook = null;
        PersonView findPerson = null;
        PersonView checkPerson = null;

        try {
            findBook = bookService.findById(bookView);
            findPerson = personService.findPersonWithId(personView);
            checkPerson = bookService.checkPersonOfBook(bookView);
        }
        catch(NullPointerException e) {
            if(findBook == null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        }
        catch (EmptyResultDataAccessException e) {
            if(findPerson == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
            if(checkPerson == null) throw new BookWasNotGivenException(bookView.getBookName(), personView.getFullName());
        }
        if(!findBook.equals(bookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        if (findPerson.equals(checkPerson)) {
            bookService.detachPersonFromBook(bookView, findPerson);
            return new Response<>("Книгу " + findBook.getBookName() + " забрали у читателя " + findPerson.getFullName(), null);
        } else {
            throw new BookIssuedAnotherPersonException(bookView.getBookName(), checkPerson.getFullName(), checkPerson.getPhoneNumber(), findPerson.getFullName(), findPerson.getPhoneNumber());
        }
    }

    @ApiOperation(value = "checkBooksOfPerson")
    @CrossOrigin
    @RequestMapping(value = "/checkBooksOfPerson", method = {POST})
    public Response<List<String>, String> checkBooksOfPerson(@RequestBody PersonView personView) {

        PersonController.validateInputData(personView);

        PersonView person = null;
        List<String> books;

        try {
            person = personService.findPersonWithId(personView);
        }
        catch (EmptyResultDataAccessException e){
            if(person == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        }
        books = bookService.findPersonBooks(person.getId());
        if(books.size() == 0) return new Response<>(null, "У читателя нет книг");
        return new Response<>(books, null);
    }

    @ApiOperation(value = "checkBusyBooks")
    @CrossOrigin
    @RequestMapping(value = "/checkBusyBooks", method = {GET})
    public Response<List<String>, String> checkBusyBooks() {

        List<String> busyBooks = bookDAO.listOfBusyBooks();
        if(busyBooks.size() == 0) return new Response<>(null, "Нет выданных книг читателям");
        return new Response<>(busyBooks, null);
    }

    @ApiOperation(value = "checkFreeBooks")
    @CrossOrigin
    @RequestMapping(value = "/checkFreeBooks", method = {GET})
    public Response<List<String>, String> checkFreeBooks() {

        List<String> freeBooks = bookDAO.listOfFreeBooks();
        if(freeBooks.size() == 0) return new Response<>(null, "Все книги выданы читателям");
        return new Response<>(freeBooks, null);
    }

    @ApiOperation(value = "checkPersonHistory")
    @CrossOrigin
    @RequestMapping(value = "/checkPersonHistory", method = {POST})
    public Response<List<String>, String> checkPersonHistory(@RequestBody PersonView personView) {

        PersonController.validateInputData(personView);

        PersonView person = null;
        List<String> history;

        try {
            person = personService.findByFullNameAndTelNomber(personView);
        }
        catch (EmptyResultDataAccessException e){
            if(person == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        }
        history = givenAndReturnBookDAO.showEntriesThroughPerson(personView);
        if(history.size() == 0) return new Response<>(null, "Читателю не выдавали книг");
        return new Response<>(history, null);
    }

    @ApiOperation(value = "checkBookHistory")
    @CrossOrigin
    @RequestMapping(value = "/checkBookHistory", method = {POST})
    public Response<List<String>, String> checkBookHistory(@RequestBody BookView bookView) {

        BookController.validateInputData(bookView);

        BookView book = null;
        List<String> history;

        try {
            book = bookService.findById(bookView);
        } catch (NullPointerException e){
            if(book == null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        }
        history = givenAndReturnBookDAO.showEntriesThroughBook(bookView);
        if(history.size() == 0) return new Response<>(null, "Книга никому не выдавалась");
        return new Response<>(history, null);
    }

    @ExceptionHandler({BookAlreadyIssuedException.class, BookIssuedAnotherPersonException.class,BookWasNotGivenException.class, PersonNotFoundException.class, IncorrectPersonInputData.class, BookNotFoundException.class, IncorrectInputBookDataException.class})
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ErrorResponse<List<String>> handleException(RuntimeException ex){
        ErrorResponse<List<String>> listOfResponse = new ErrorResponse<>();
        List<String> list = new ArrayList<>();
        list.add(ex.getMessage());
        listOfResponse.setErrors(list);
        return listOfResponse;
    }

}
