package com.bean.form.controller;

import com.bean.form.exceptions.BookExceptions.*;
import com.bean.form.model.BookModel;
import com.bean.form.service.BookService;
import com.bean.form.view.BookView;
import com.bean.form.view.ErrorResponse;
import com.bean.form.view.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
@Api
@CrossOrigin
@RestController
@RequestMapping(value = "/bookController", produces = APPLICATION_JSON_VALUE)
public class BookController {

    private final BookService bookService;

    private final TaskExecutor executor;

    @Autowired
    public BookController(BookService bookService, TaskExecutor executor) {
        this.bookService = bookService;
        this.executor = executor;
    }

    @ApiOperation(value = "add")
    @CrossOrigin
    @RequestMapping(value = "/book/add", method = {POST})
    public Response<BookView, String> addBook(@RequestBody BookView bookView) {

        BookView data = null;
        String error = null;
        BookModel book = null;

        try {
            book = bookService.findById(bookView);
            data = new BookView(book.getBookID(), book.getBookName());
        }
        catch (Exception e) {
            if (book == null) {
                data = bookService.addBook(bookView);
                return new Response<>(data, error);
            }
            error = e.getMessage();
            return new Response<>(data, error);
        }
        throw new BookAlreadyPresentException(data.getBookName(), data.getBookID());
    }

    @ApiOperation(value = "edit")
    @CrossOrigin
    @RequestMapping(value = "/book/edit", method = {PUT})
    public Response<BookView, String> editBook(@RequestBody BookView bookView){

        BookView data = null;
        String error = null;
        BookModel book = null;

        try{
            book = bookService.findById(bookView);
            data = new BookView(book.getBookID(), book.getBookName());
        }
        catch (Exception e){
            if (book == null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
            error = e.getMessage();
            return new Response<>(data, error);
        }
        data = bookService.editBook(bookView);
        return new Response<>(data, error);
    }

    @ApiOperation(value = "delete")
    @CrossOrigin
    @RequestMapping(value = "/book/delete", method = {DELETE})
    public Response<BookView, String> deleteBook(@RequestBody  BookView bookView) {

        BookView data = null;
        String error = null;
        BookModel book = null;

        try{
            book = bookService.findById(bookView);
        }
        catch (Exception e) {
            if (book == null) {
                throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
            }
            error = e.getMessage();
            return new Response<>(data, error);
        }
        if(!book.equals(bookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        data = new BookView(book.getBookID(), book.getBookName());
        bookService.deleteBook(bookView.getBookID());
        return new Response<>(data, error);
    }

    @ApiOperation(value = "get allBooks", nickname = "get allBooks", httpMethod = "GET")
    @CrossOrigin
    @RequestMapping(value = "/book/all", method = {GET})
    public Response<List<BookView>, String> bookCatalog() {
        List<BookView> data = null;
        String errors = null;
        try {
            data = bookService.getBookList();
        } catch (Exception e) {
            return new Response<>(data, e.getMessage());
        }
        if (data.size() == 0) return new Response<>(data, "Нет книг в БД");
        return new Response<>(data, errors);
    }

    @RequestMapping(value = "/book/addbyexcel", method = {POST})
    public Response<String, String> addBooksFromEXCEL() {
        InputStream in = null;
        try {
            in = new FileInputStream("files\\bookCatalog.xlsx");
        } catch (FileNotFoundException e) {
            return new Response<>(null, "Не найден файл для загрузки книг\n"+e.getMessage());
        }
        // Внимание InputStream будет закрыт
        // Если нужно не закрывающий см. JavaDoc по POIFSFileSystem :  http://goo.gl/1Auu7
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(in);
        } catch (IOException e) {
            return new Response<>(null, "Не удалось загрузить книги из файла\n" + e.getMessage());
        }
        List<String> list = new ArrayList<>();

        Sheet sheet = wb.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            Iterator<Cell> cells = row.iterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                DataFormatter formatter = new DataFormatter();
                String val = formatter.formatCellValue(cell);
                //String str = cell.getRichStringCellValue().getString();
                list.add(val);
            }
        }
        for (int i = 0; i < list.size(); i += 2) {
            bookService.addBook(new BookView(list.get(i), list.get(i + 1)));
        }
        return new Response<>("Книги успешно загружены из файла", null);
    }

    @ExceptionHandler({BookAlreadyPresentException.class, BookNotFoundException.class, IncorrectInputBookDataException.class})
    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    public ErrorResponse<List<String>> handleException(RuntimeException ex){
        ErrorResponse<List<String>> listOfResponse = new ErrorResponse<>();
        List<String> list = new ArrayList<>();
        list.add(ex.getMessage());
        listOfResponse.setErrors(list);
        return listOfResponse;
    }
}