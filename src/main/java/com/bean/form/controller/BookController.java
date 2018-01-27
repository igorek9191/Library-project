package com.bean.form.controller;

import com.bean.form.exceptions.BookExceptions.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        validateInputData(bookView);

        BookView data = null;

        try {
            data = bookService.findById(bookView);
        } catch (NullPointerException e) {
            if (data == null) {
                data = bookService.addBook(bookView);
                return new Response<>(data, null);
            }
        }
        throw new BookAlreadyPresentException(data.getBookName(), data.getBookID());
    }

    @ApiOperation(value = "edit")
    @CrossOrigin
    @RequestMapping(value = "/book/edit", method = {PUT})
    public Response<BookView, String> editBook(@RequestBody BookView bookView){

        validateInputData(bookView);

        BookView data = null;

        try{
            data = bookService.findById(bookView);
        } catch (NullPointerException e){
            if (data == null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        }
        data = bookService.editBook(bookView);
        return new Response<>(data, null);
    }

    @ApiOperation(value = "delete")
    @CrossOrigin
    @RequestMapping(value = "/book/delete", method = {DELETE})
    public Response<BookView, String> deleteBook(@RequestBody  BookView bookView) {

        validateInputData(bookView);

        BookView data = null;

        try{
            data = bookService.findById(bookView);
        } catch (NullPointerException e) {
            if (data == null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        }
        //на случай если ID одинаковые но названия разные
        if(!data.equals(bookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        bookService.deleteBook(bookView.getBookID());
        return new Response<>(data, null);
    }

    @ApiOperation(value = "get allBooks", nickname = "get allBooks", httpMethod = "GET")
    @CrossOrigin
    @RequestMapping(value = "/book/all", method = {GET})
    public Response<List<BookView>, String> bookCatalog() {

        List<BookView> data = null;

        data = bookService.getBookList();
        if (data.size() == 0) return new Response<>(data, "Нет книг в БД");
        return new Response<>(data, null);
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

    public static void validateInputData (BookView bookView){
        Pattern bookIdPt = Pattern.compile("[\\d]{1,4}");
        Pattern bookNamePt = Pattern.compile("[А-Яа-я]{0,}\\s{0,2}?[А-Яа-я]{0,}?\\s{0,2}?[А-Яа-я]{0,}?");//+(\s){1}[А-Яа-я]+

        Matcher bookIdMt = bookIdPt.matcher(bookView.bookID);
        Matcher fullNameMt = bookNamePt.matcher(bookView.bookName);

        boolean bookIdMatch = bookIdMt.matches();
        boolean bookNameMatch = fullNameMt.matches();

        if(!bookIdMatch || !bookNameMatch){
            throw new IncorrectInputBookDataException();
        }
    }
}