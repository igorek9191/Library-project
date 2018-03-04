package com.bean.form.controller;

import com.bean.form.dao.BookDAO;
import com.bean.form.service.BookService;
import com.bean.form.service.HistoryService;
import com.bean.form.view.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@RestController
@RequestMapping(value = "/")
public class GiveTakeBackController {

    private final BookService bookService;

    private final BookDAO bookDAO;

    private final HistoryService historyService;

    @Autowired
    public GiveTakeBackController(BookService bookService, BookDAO bookDAO, HistoryService historyService) {
        this.bookService = bookService;
        this.bookDAO = bookDAO;
        this.historyService = historyService;
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

        List<String> books = bookService.findPersonBooks(personView);
        if(books.isEmpty()) {
            books.add("У читателя нет книг");
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @ApiOperation(value = "checkBusyBooks")
    @CrossOrigin
    @RequestMapping(value = "/checkBusyBooks", method = {GET})
    public ResponseEntity<List<String>> checkBusyBooks() {

        List<String> busyBooks = bookDAO.listOfBusyBooks();
        if(busyBooks.isEmpty()) {
            busyBooks.add("Нет выданных книг");
            return new ResponseEntity<>(busyBooks, HttpStatus.OK);
        }
        return new ResponseEntity<>(busyBooks, HttpStatus.OK);
    }

    @ApiOperation(value = "checkFreeBooks")
    @CrossOrigin
    @RequestMapping(value = "/checkFreeBooks", method = {GET})
    public ResponseEntity<List<String>> checkFreeBooks() {

        List<String> freeBooks = bookDAO.listOfFreeBooks();
        if(freeBooks.isEmpty()) {
            freeBooks.add("Нет выданных книг");
            return new ResponseEntity<>(freeBooks, HttpStatus.OK);
        }
        return new ResponseEntity<>(freeBooks, HttpStatus.OK);
    }

    @ApiOperation(value = "checkPersonHistory")
    @CrossOrigin
    @RequestMapping(value = "/checkPersonHistory", method = {POST})
    public ResponseEntity<List<String>> checkPersonHistory(@RequestBody PersonView personView) {

        PersonController.validateInputData(personView);
        List<String> history = historyService.showEntriesThroughPerson(personView);
        if(history.isEmpty()) {
            history.add("Читателю книги не выдавались");
            return new ResponseEntity<>(history, HttpStatus.OK);
        }
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @ApiOperation(value = "checkBookHistory")
    @CrossOrigin
    @RequestMapping(value = "/checkBookHistory", method = {POST})
    public ResponseEntity<List<String>> checkBookHistory(@RequestBody BookView bookView) {

        BookController.validateInputData(bookView);
        List<String> history = historyService.showEntriesThroughBook(bookView);
        if(history.isEmpty()) {
            history.add("Книга читателям не выдавалась");
            return new ResponseEntity<>(history, HttpStatus.OK);
        }
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
