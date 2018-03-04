package com.bean.form.dao.impl;

import com.bean.form.dao.HistoryDAO;
import com.bean.form.model.BookModel;
import com.bean.form.model.HistoryModel;
import com.bean.form.model.PersonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class HistoryDAOImpl implements HistoryDAO {

    private final EntityManager entityManager;

    @Autowired
    public HistoryDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");

    @Override
    @Transactional
    public void saveEntryOfGiven(BookModel bookModel, PersonModel personModel) {

        Date date = new Date();
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());

        String stringSQLdate = sqlDate.toString();
        Integer bookID = bookModel.getBookID();
        String bookName = bookModel.getBookName();
        Integer personID = personModel.getId();
        String personName = personModel.getFullName();
        String phoneNumber = personModel.getPhoneNumber();
        String givenDate = dateFormat.format( date );
        String returnDate = null;

        HistoryModel historyModel = new HistoryModel(stringSQLdate, bookID, bookName, personID, personName, phoneNumber, givenDate, returnDate);

        entityManager.persist(historyModel);
    }

    @Override
    @Transactional
    public void saveEntryOfReturn(BookModel bookModel, PersonModel personModel) {

        Date date = new Date();
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        String stringSQLdate = sqlDate.toString();

        TypedQuery<HistoryModel> query = entityManager.createQuery("SELECT h FROM HistoryModel h WHERE bookID =:bookID AND personID =:personID AND returnDate IS NULL ORDER BY sysCreationDate DESC", HistoryModel.class);
        query.setParameter("bookID", bookModel.getBookID());
        query.setParameter("personID", personModel.getId());
        HistoryModel historyModel = query.getSingleResult();
        historyModel.setSysCreationDate(stringSQLdate);
        historyModel.setReturnDate( dateFormat.format(date) );

        entityManager.merge(historyModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> showEntriesThroughPerson(PersonModel personModel) {
        TypedQuery<HistoryModel> query = entityManager.createQuery("SELECT h FROM HistoryModel h WHERE personName =:name", HistoryModel.class).setParameter("name", personModel.getFullName());
        List<HistoryModel> result = query.getResultList();
        List<String> stringList = result.stream().map(o -> o.toString()).collect(Collectors.toList());
        return stringList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> showEntriesThroughBook(BookModel bookModel) {
        TypedQuery<HistoryModel> query = entityManager.createQuery("SELECT h FROM HistoryModel h WHERE bookID =:bookID", HistoryModel.class).setParameter("bookID", bookModel.getBookID());
        List<HistoryModel> result = query.getResultList();
        List<String> stringList = result.stream().map(o -> o.toString()).collect(Collectors.toList());
        return stringList;
    }


}
