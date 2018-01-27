package com.bean.form.service;

import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;
import com.bean.form.view.BookView;
import com.bean.form.view.PersonView;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

public interface BookService {

    BookView addBook(BookView bookView);

    BookView editBook(BookView bookView);

    void deleteBook(String bookID);

    List<BookView> getBookList();

    BookView findById (BookView bookView);

    PersonView checkPersonOfBook(BookView bookView) throws EmptyResultDataAccessException;

    void addPersonToBook(BookView bookView, PersonView personView);

    void detachPersonFromBook(BookView bookView, PersonView personView);

    List<String> findPersonBooks(Long persomId);
}