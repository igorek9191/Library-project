package com.bean.form.dao;

import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;

import java.util.List;

public interface BookDAO {

    BookModel addBook(BookModel person);

    BookModel editBook (BookModel bookModel);

    void deleteBook(BookModel bookModel);

    List<BookModel> allBooks();

    BookModel findById (String bookId);

    PersonModel checkPersonOfBook (String bookId);

    void saveBookWithPerson(BookModel bookModel);

    void detachBookFromPerson(BookModel bookModel);

    List<String> findPersonBooks (Long personId);

    List<String> listOfBusyBooks();

    List<String> listOfFreeBooks();
}