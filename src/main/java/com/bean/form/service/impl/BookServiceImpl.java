package com.bean.form.service.impl;

import com.bean.form.dao.BookDAO;
import com.bean.form.dao.HistoryGiveAndReturnBookDAO;
import com.bean.form.dao.PersonDAO;
import com.bean.form.exceptions.BookExceptions.IncorrectInputBookDataException;
import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;
import com.bean.form.service.BookService;
import com.bean.form.view.BookView;
import com.bean.form.view.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final HistoryGiveAndReturnBookDAO historyGiveAndReturnBookDAO;

    @Autowired
    public BookServiceImpl(BookDAO bookDAO, PersonDAO personDAO, HistoryGiveAndReturnBookDAO historyGiveAndReturnBookDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.historyGiveAndReturnBookDAO = historyGiveAndReturnBookDAO;
    }

    @Override
    @Transactional
    public BookView addBook(BookView bookView) {
        validateInputData(bookView);
        BookModel bookModel = new BookModel(bookView.bookID, bookView.bookName);
        BookModel savedBook = bookDAO.addBook(bookModel);
        return new BookView(savedBook.getBookID(), savedBook.getBookName());
    }

    @Override
    @Transactional
    public BookView editBook(BookView bookView) {
        validateInputData(bookView);
        BookModel bookModel = new BookModel(bookView.getBookID(), bookView.getBookName());
        BookModel newBookModel = bookDAO.editBook(bookModel);
        return new BookView(newBookModel.getBookID(), newBookModel.getBookName());
    }

    @Override
    @Transactional
    public void deleteBook(String bookID) {
        bookDAO.deleteBook(bookID);
    }

    @Override
    public BookModel findById(BookView bookView) {

        BookModel bookModel = bookDAO.findById(bookView.getBookID());
        //BookView foundedBookView = new BookView(bookModel.getBookID(), bookModel.getBookName());
        return bookModel;
    }

    @Override
    public BookModel findByIdAndName(BookView bookView) {

        return null;
    }

    @Override
    public PersonModel checkPersonOfBook(BookView bookView) {
        //validateInputData(bookView);
        return bookDAO.checkPersonOfBook(bookView.getBookID());
    }

    @Override
    public void addPersonToBook(BookView bookView, PersonView personView) {
        PersonModel personModel = personDAO.findByFullNameAndTelNomber(personView.getFullName(), personView.getPhoneNumber());
        BookModel bookModel = new BookModel(bookView.getBookID(), bookView.getBookName(), personModel);
        bookDAO.saveBookWithPerson(bookModel);

        historyGiveAndReturnBookDAO.saveGivenEntry(bookView, personView);
    }

    @Override
    public void detachPersonFromBook(BookView bookView, PersonView personView) {
        PersonModel personModel = personDAO.findByFullNameAndTelNomber(personView.getFullName(), personView.getPhoneNumber());
        BookModel bookModel = new BookModel(bookView.getBookID(), bookView.getBookName(), personModel);
        bookDAO.detachBookFromPerson(bookModel);

        historyGiveAndReturnBookDAO.saveReturnEntry(bookView, personView);
    }

    @Override
    public List<String> findPersonBooks(Long personId) {
        return bookDAO.findPersonBooks(personId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookView> getBookList() {
        List<BookModel> bookModelList = bookDAO.allBooks();
        Function<BookModel, BookView> bookModelBookViewFunction = p -> {
            BookView view = new BookView();

            view.bookID = p.getBookID();
            view.bookName = p.getBookName();

            return view;
        };
        return bookModelList.stream().map(bookModelBookViewFunction).collect(Collectors.toList());
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