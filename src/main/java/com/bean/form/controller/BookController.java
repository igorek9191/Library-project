package com.bean.form.controller;

import com.bean.form.exceptions.BookExceptions.BookAlreadyPresentException;
import com.bean.form.exceptions.BookExceptions.BookNotFoundException;
import com.bean.form.exceptions.BookExceptions.IncorrectInputBookDataException;
import com.bean.form.service.BookService;
import com.bean.form.view.BookView;
import com.bean.form.view.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api
@CrossOrigin
@RestController
@RequestMapping(value = "/bookController", produces = APPLICATION_JSON_VALUE)
public class BookController {

    private static final String ID_PATTERN = "[\\d]{1,4}";
    private static final String NAME_PATTERN = "[А-Яа-я]*\\s*[А-Яа-я]*\\s*[А-Яа-я]*";

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @ApiOperation(value = "add")
    @CrossOrigin
    @RequestMapping(value = "/book/add", method = {POST})
    public ResponseEntity<BookView> addBook(@RequestBody BookView bookView) {
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

    @RequestMapping(value = "/book/addbyexcel", method = {POST})
    public ResponseEntity<String> addBooksFromEXCEL() throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream("files\\bookCatalog.xlsx");
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>("Не найден файл для загрузки книг\n" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Внимание InputStream будет закрыт
        // Если нужно не закрывающий см. JavaDoc по POIFSFileSystem :  http://goo.gl/1Auu7
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(in);
        } catch (IOException e) {
            return new ResponseEntity<>("Не удалось загрузить книги из файла\n" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
        return new ResponseEntity<>("Книги успешно загружены из файла", HttpStatus.OK);
    }

    @ExceptionHandler({BookAlreadyPresentException.class, BookNotFoundException.class, IncorrectInputBookDataException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse<List<String>> handleException(RuntimeException ex) {
        ErrorResponse<List<String>> listOfResponse = new ErrorResponse<>();
        List<String> list = new ArrayList<>();
        list.add(ex.getMessage());
        listOfResponse.setErrors(list);
        return listOfResponse;
    }

    protected static void validateInputData(BookView bookView) {
        String bookID = bookView.getBookID();
        String bookName = bookView.getBookName();
        if (bookID == null || !bookID.matches(ID_PATTERN) ||
                bookName == null || !bookName.matches(NAME_PATTERN)) {
            throw new IncorrectInputBookDataException();
        }
    }
}