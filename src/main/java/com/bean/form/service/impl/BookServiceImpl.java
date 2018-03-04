package com.bean.form.service.impl;

import com.bean.form.dao.BookDAO;
import com.bean.form.dao.HistoryDAO;
import com.bean.form.dao.PersonDAO;
import com.bean.form.exceptions.BookAlreadyIssuedException;
import com.bean.form.exceptions.BookExceptions.BookAlreadyPresentException;
import com.bean.form.exceptions.BookExceptions.BookNotFoundException;
import com.bean.form.exceptions.BookIssuedAnotherPersonException;
import com.bean.form.exceptions.BookWasNotGivenException;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;
import com.bean.form.service.BookService;
import com.bean.form.service.PersonService;
import com.bean.form.view.BookView;
import com.bean.form.view.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final HistoryDAO historyDAO;

    @Autowired
    public BookServiceImpl(BookDAO bookDAO, PersonDAO personDAO, HistoryDAO historyDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.historyDAO = historyDAO;
    }

    @Override
    @Transactional
    public BookView addBook(BookView bookView) {
        BookModel savedBook;
        BookModel existBook = bookDAO.findByBookID(bookView.getBookID());
        if (existBook == null) {
            savedBook = bookDAO.save(new BookModel(bookView));
        } else {
            throw new BookAlreadyPresentException(bookView.getBookID(), existBook.getBookName());
        }
        return new BookView(savedBook);
    }

    @Override
    @Transactional
    public BookView editBook(BookView bookView) {
        BookModel savedBook;
        BookModel existBook = bookDAO.findByBookID(bookView.getBookID());
        if (existBook == null) {
            throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        } else {
            existBook.setBookName(bookView.getBookName());
            savedBook = bookDAO.save(existBook);
        }
        return new BookView(savedBook);
    }

    @Override
    @Transactional
    public void deleteBook(BookView bookView) {
        BookModel existBook = bookDAO.findByBookID(bookView.getBookID());
        if(existBook == null){
            throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        }
        //на случай если ID одинаковые но названия разные
        BookView existBookView = new BookView(existBook);
        if(!bookView.equals(existBookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());

        bookDAO.delete(existBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookView> getBookList() {
        List<BookModel> bookModelList = bookDAO.findAll();
        Function<BookModel, BookView> bookModelBookViewFunction = BookView::new;
        return bookModelList.stream().map(bookModelBookViewFunction).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addPersonToBook(BookView bookView, PersonView personView) {
        BookModel findBook = null;
        PersonModel findPerson = null;
        PersonModel checkPerson = null;

        findBook = bookDAO.findByBookID(bookView.getBookID());
        if(findBook==null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());

        //на случай если ID одинаковые но названия разные
        BookView existBookView = new BookView(findBook);
        if(!bookView.equals(existBookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());

        findPerson = personDAO.findByFullNameAndPhoneNumber(personView.getFullName(), personView.getPhoneNumber());
        if(findPerson == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());

        checkPerson = bookDAO.checkPersonOfBook(bookView.getBookID());
        if(checkPerson == null){
            bookDAO.saveBookWithPerson(findPerson, bookView.getBookID());
            historyDAO.saveEntryOfGiven(findBook, findPerson);
        } else {
            throw new BookAlreadyIssuedException(bookView.getBookName(), checkPerson.getFullName());
        }
    }

    @Override
    @Transactional
    public void detachPersonFromBook(BookView bookView, PersonView personView) {
        BookModel findBook = null;
        PersonModel findPerson = null;
        PersonModel checkPerson = null;

        findBook = bookDAO.findByBookID(bookView.getBookID());
        if(findBook==null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());

        //на случай если ID одинаковые но названия разные
        BookView existBookView = new BookView(findBook);
        if(!bookView.equals(existBookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());

        findPerson = personDAO.findByFullNameAndPhoneNumber(personView.getFullName(), personView.getPhoneNumber());
        if(findPerson == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());

        checkPerson = bookDAO.checkPersonOfBook(bookView.getBookID());
        if(checkPerson == null) throw new BookWasNotGivenException(bookView.getBookName(), personView.getFullName());

        if(checkPerson.equals(findPerson)){
            bookDAO.detachBookFromPerson(bookView.getBookID());
            historyDAO.saveEntryOfReturn(findBook, findPerson);
        } else {
            throw new BookIssuedAnotherPersonException(bookView.getBookName(), checkPerson.getFullName(), checkPerson.getPhoneNumber(), findPerson.getFullName(), findPerson.getPhoneNumber());
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findPersonBooks(PersonView personView) {
        PersonModel person = personDAO.findByFullNameAndPhoneNumber(personView.getFullName(), personView.getPhoneNumber());
        if(person == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());

        return bookDAO.findPersonBooks(person);
    }
}