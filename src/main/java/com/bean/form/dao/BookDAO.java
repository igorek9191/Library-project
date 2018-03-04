package com.bean.form.dao;

import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookDAO extends JpaRepository<BookModel, Integer> {

    BookModel save(BookModel bookModel);

    void delete(BookModel bookModel);

    List<BookModel> findAll();

    BookModel findByBookID (Integer bookId);

    @Query(value = "SELECT person FROM BookModel WHERE book_ID =?1")
    PersonModel checkPersonOfBook (Integer bookId);

    @Modifying
    @Query(value = "UPDATE BookModel SET person =:person WHERE bookID =:bookId")
    void saveBookWithPerson(@Param("person") PersonModel person,
                            @Param("bookId") Integer bookId);

    @Modifying
    @Query(value = "UPDATE BookModel SET person =null WHERE bookID =?1")
    void detachBookFromPerson(Integer bookId);

    @Query(value = "SELECT bookName FROM BookModel WHERE person =?1")
    List<String> findPersonBooks (PersonModel person);

    @Query(value = "SELECT bookName FROM BookModel WHERE person IS NOT NULL")
    List<String> listOfBusyBooks();

    @Query(value = "SELECT bookName FROM BookModel WHERE person IS NULL")
    List<String> listOfFreeBooks();
}