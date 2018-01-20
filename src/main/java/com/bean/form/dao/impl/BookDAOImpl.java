package com.bean.form.dao.impl;

import com.bean.form.dao.BookDAO;
import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Repository
public class BookDAOImpl implements BookDAO {

    private final EntityManager entityManager;

    @Autowired
    public BookDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public BookModel addBook(BookModel bookModel) {
        entityManager.persist(bookModel);
        return entityManager.find(BookModel.class, bookModel.getBookID());
    }

    @Override
    public BookModel editBook(BookModel bookModel) {
        BookModel newBookModel = entityManager.merge(bookModel);
        return newBookModel;
    }

    @Override
    public void deleteBook(String bookId) {
        BookModel book = entityManager.find(BookModel.class, bookId);
        if(book!=null) entityManager.remove(book);
    }

    @Override
    public List<BookModel> allBooks() {
        TypedQuery<BookModel> query = entityManager.createQuery("SELECT p FROM BookModel p", BookModel.class);
        return query.getResultList();
    }

    @Override
    public BookModel findById(String bookId) {
        BookModel bookModel = entityManager.find(BookModel.class, bookId);
        return bookModel;
    }

    @Override
    public BookModel findByIdAndName(String Id, String name) {

        return null;
    }

    @Override
    public PersonModel checkPersonOfBook(String bookId) {
        Query query = entityManager.createQuery("SELECT person FROM BookModel WHERE bookID =:bookId").setParameter("bookId", bookId);
        PersonModel personModel = (PersonModel) query.getSingleResult();
        return personModel;
    }

    @Transactional
    @Override
    public void saveBookWithPerson(BookModel bookModel){
        //entityManager.merge(bookModel);
        Query query = entityManager.createQuery("UPDATE BookModel SET person =:person WHERE bookID =:bookID");
        query.setParameter("person", bookModel.getPerson());
        query.setParameter("bookID", bookModel.getBookID()).executeUpdate();
        BookModel model = entityManager.find(BookModel.class, bookModel.getBookID());
        System.out.println(model.toString());
    }

    @Transactional
    @Override
    public void detachBookFromPerson(BookModel bookModel) {
        String id = bookModel.getBookID();
        Query query = entityManager.createQuery("UPDATE BookModel SET person = null where bookID =:id");
        query.setParameter("id", id).executeUpdate();
        System.out.println(entityManager.find(BookModel.class, id).toString());
    }

    @Override
    public List<String> findPersonBooks(Long personId) {
        PersonModel person = entityManager.find(PersonModel.class, personId);
        TypedQuery<String> query = entityManager.createQuery("SELECT bookName FROM BookModel WHERE person =:person", String.class).setParameter("person", person);
        List<String> books = query.getResultList();
        return books;
    }

    @Override
    public List<String> listOfBusyBooks() {
        TypedQuery<String> query = entityManager.createQuery("SELECT bookName FROM BookModel WHERE person is not null", String.class);
        List<String> resultList = query.getResultList();
        //List<String> resultList = query.getResultList().stream().map(e -> e.getBookName()).collect(Collectors.toList());
        return resultList;
    }

    @Override
    public List<String> listOfFreeBooks() {
        TypedQuery<String> query = entityManager.createQuery("SELECT bookName FROM BookModel WHERE person is null", String.class);
        List<String> resultList = query.getResultList();
        //List<String> resultList = query.getResultList().stream().map(e -> e.getBookName()).collect(Collectors.toList());
        return resultList;
    }
}