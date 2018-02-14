package com.bean.form.service.impl;

import com.bean.form.dao.BookDAO;
import com.bean.form.dao.HistoryDAO;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final HistoryDAO historyDAO;
    private final PersonService personService;

    @Autowired
    public BookServiceImpl(BookDAO bookDAO, HistoryDAO historyDAO, PersonService personService) {
        this.bookDAO = bookDAO;
        this.historyDAO = historyDAO;
        this.personService = personService;
    }

    @Override
    @Transactional
    public BookView addBook(BookView bookView) {
        BookModel existBook = bookDAO.findById(bookView.getBookID());
        if (existBook != null) {
            throw new BookAlreadyPresentException(existBook.getBookName(), existBook.getBookID());
        }

        BookModel newBook = new BookModel(bookView);
        BookModel savedBook = bookDAO.addBook(newBook);
        return new BookView(savedBook);
    }

    @Override
    @Transactional
    public BookView editBook(BookView bookView) {
        BookModel existBook = bookDAO.findById(bookView.getBookID());
        if(existBook == null){
            throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        }

        BookModel editingBook = new BookModel(bookView);
        BookModel newBookModel = bookDAO.editBook(editingBook);
        return new BookView(newBookModel);
    }

    @Override
    @Transactional
    public void deleteBook(BookView bookView) {
        BookModel existBook = bookDAO.findById(bookView.getBookID());
        if(existBook == null){
            throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        }

        BookView existBookView = new BookView(existBook);
        //на случай если ID одинаковые но названия разные
        if(!bookView.equals(existBookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        bookDAO.deleteBook(existBook);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookView> getBookList() {
        List<BookModel> bookModelList = bookDAO.allBooks();
        Function<BookModel, BookView> bookModelBookViewFunction = BookView::new;
        return bookModelList.stream().map(bookModelBookViewFunction).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookView findById(BookView bookView) {
        BookModel bookModel = bookDAO.findById(bookView.getBookID());
        if(bookModel == null) return null;
        return new BookView(bookModel);
    }

    @Override
    @Transactional(readOnly = true)
    public PersonView checkPersonOfBook(BookView bookView) throws EmptyResultDataAccessException {
        PersonModel person = bookDAO.checkPersonOfBook(bookView.getBookID());
        return new PersonView(person.getId(), person.getFullName(), person.getPhoneNumber());
    }

    @Override
    @Transactional
    public void addPersonToBook(BookView bookView, PersonView personView) {
        BookView findBook = null;
        PersonView findPerson = null;
        PersonView checkPerson = null;

        findBook = findById(bookView);//найти книгу
        if(findBook==null || !findBook.equals(bookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());

        try {
            findPerson = personService.findPersonWithId(personView);//найти читателя
            checkPerson = checkPersonOfBook(bookView);//проверить читателя у книги
        } catch (EmptyResultDataAccessException e) {
            if(findPerson == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
            if(checkPerson==null){
                PersonModel personModel = new PersonModel(findPerson.getId(), findPerson.getFullName(), findPerson.getPhoneNumber());
                BookModel bookModel = new BookModel(bookView.getBookID(), bookView.getBookName(), personModel);
                bookDAO.saveBookWithPerson(bookModel);

                historyDAO.saveGivenEntry(findBook, findPerson);
                return;
            }
        }
        throw new BookAlreadyIssuedException(bookView.getBookName(), checkPerson.getFullName());
    }

    @Override
    @Transactional
    public void detachPersonFromBook(BookView bookView, PersonView personView) {
        BookView findBook = null;
        PersonView findPerson = null;
        PersonView checkPerson = null;

        findBook = findById(bookView);//найти книгу
        if(findBook==null || !findBook.equals(bookView)) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());

        try {
            findPerson = personService.findPersonWithId(personView);
            checkPerson = checkPersonOfBook(bookView);
        } catch (EmptyResultDataAccessException e) {
            if(findPerson == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
            if(checkPerson == null) throw new BookWasNotGivenException(bookView.getBookName(), personView.getFullName());
        }
        if (checkPerson.equals(findPerson)) {
            PersonModel personModel = new PersonModel(findPerson.getId(), findPerson.getFullName(), findPerson.getPhoneNumber());
            BookModel bookModel = new BookModel(findBook.getBookID(), findBook.getBookName(), personModel);
            bookDAO.detachBookFromPerson(bookModel);

            historyDAO.saveReturnEntry(findBook, findPerson);
        } else {
            throw new BookIssuedAnotherPersonException(bookView.getBookName(), checkPerson.getFullName(), checkPerson.getPhoneNumber(), findPerson.getFullName(), findPerson.getPhoneNumber());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findPersonBooks(PersonView personView) {
        PersonView person;
        try {
            person = personService.findPersonWithId(personView);
        } catch (EmptyResultDataAccessException e){
            throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        }
        return bookDAO.findPersonBooks(person.getId());
    }
}