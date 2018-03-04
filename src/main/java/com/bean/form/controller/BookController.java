package com.bean.form.controller;

import com.bean.form.exceptions.BookExceptions.IncorrectInputBookDataException;
import com.bean.form.service.BookService;
import com.bean.form.view.BookView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api
@CrossOrigin
@RestController
@RequestMapping(value = "/bookController", produces = APPLICATION_JSON_VALUE)
public class BookController {

    private static final String ID_PATTERN  = "[\\d]{1,4}";
    private static final String NAME_PATTERN = "[А-Яа-я]*\\s*[А-Яа-я]*\\s*[А-Яа-я]*";

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ApiOperation(value = "add")
    @CrossOrigin
    @RequestMapping(value = "/book/add", method = {POST})
    public ResponseEntity<BookView> addBook(@RequestBody BookView bookView){
        validateInputData(bookView);

        BookView data = bookService.addBook(bookView);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @ApiOperation(value = "edit")
    @CrossOrigin
    @RequestMapping(value = "/book/edit", method = {PUT})
    public ResponseEntity<BookView> editBook(@RequestBody BookView bookView) {
        validateInputData(bookView);

        BookView data = bookService.editBook(bookView);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "delete")
    @CrossOrigin
    @RequestMapping(value = "/book/delete", method = {DELETE})
    public ResponseEntity<String> deleteBook(@RequestBody BookView bookView) {
        validateInputData(bookView);

        bookService.deleteBook(bookView);
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }

    @ApiOperation(value = "get allBooks", nickname = "get allBooks", httpMethod = "GET")
    @CrossOrigin
    @RequestMapping(value = "/book/all", method = {GET})
    public ResponseEntity<List<BookView>> bookCatalog() {

        List<BookView> data = bookService.getBookList();
        if (data.isEmpty()) return new ResponseEntity<>(data, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    protected static void validateInputData(BookView bookView) {
        Integer bookID = bookView.getBookID();
        String bookName = bookView.getBookName();
        if (bookID == null || !bookID.toString().matches(ID_PATTERN) ||
                bookName == null || !bookName.matches(NAME_PATTERN)) {
            throw new IncorrectInputBookDataException();
        }
    }
}