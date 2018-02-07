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
        return entityManager.merge(bookModel);
    }

    @Override
    public void deleteBook(BookModel bookModel) {
        entityManager.remove(bookModel);
    }

    @Override
    public List<BookModel> allBooks() {
        TypedQuery<BookModel> query = entityManager.createQuery("SELECT p FROM BookModel p", BookModel.class);
        return query.getResultList();
    }

    @Override
    public BookModel findById(String bookId) {
        return entityManager.find(BookModel.class, bookId);
    }

    @Override
    public PersonModel checkPersonOfBook(String bookId) {
        TypedQuery<PersonModel> query = entityManager.createQuery("SELECT person FROM BookModel WHERE bookID =:bookId", PersonModel.class).setParameter("bookId", bookId);
        return query.getSingleResult();
    }

    @Override
    public void saveBookWithPerson(BookModel bookModel){
        Query query = entityManager.createQuery("UPDATE BookModel SET person =:person WHERE bookID =:bookID");
        query.setParameter("person", bookModel.getPerson());
        query.setParameter("bookID", bookModel.getBookID()).executeUpdate();
    }

    @Override
    public void detachBookFromPerson(BookModel bookModel) {
        String id = bookModel.getBookID();
        Query query = entityManager.createQuery("UPDATE BookModel SET person = null WHERE bookID =:id");
        query.setParameter("id", id).executeUpdate();
    }

    @Override
    public List<String> findPersonBooks(Long personId) {
        PersonModel person = entityManager.find(PersonModel.class, personId);
        TypedQuery<String> query = entityManager.createQuery("SELECT bookName FROM BookModel WHERE person =:person", String.class).setParameter("person", person);
        return query.getResultList();
    }

    @Override
    public List<String> listOfBusyBooks() {
        TypedQuery<String> query = entityManager.createQuery("SELECT bookName FROM BookModel WHERE person is not null", String.class);
        return query.getResultList();
    }

    @Override
    public List<String> listOfFreeBooks() {
        TypedQuery<String> query = entityManager.createQuery("SELECT bookName FROM BookModel WHERE person is null", String.class);
        return query.getResultList();
    }
}