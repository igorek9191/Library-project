package com.bean.form.dao.impl;

import com.bean.form.dao.HistoryDAO;
import com.bean.form.model.HistoryModel;
import com.bean.form.view.BookView;
import com.bean.form.view.PersonView;
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
    public void saveGivenEntry(BookView bookView, PersonView personView) {

        Date date = new Date();
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());

        String stringSQLdate = sqlDate.toString();
        String bookID = bookView.getBookID();
        String bookName = bookView.getBookName();
        Long personID = personView.getId();
        String personName = personView.getFullName();
        String phoneNumber = personView.getPhoneNumber();
        String givenDate = dateFormat.format( date );
        String returnDate = null;

        HistoryModel historyModel = new HistoryModel(stringSQLdate, bookID, bookName, personID, personName, phoneNumber, givenDate, returnDate);

        entityManager.persist(historyModel);

    }

    @Override
    @Transactional
    public void saveReturnEntry(BookView bookView, PersonView personView) {

        Date date = new Date();
        java.sql.Timestamp sqlDate = new java.sql.Timestamp(date.getTime());
        String stringSQLdate = sqlDate.toString();

        TypedQuery<HistoryModel> query = entityManager.createQuery("SELECT h FROM HistoryModel h WHERE bookID =:bookID AND personID =:personID AND returnDate IS NULL ORDER BY sysCreationDate DESC", HistoryModel.class);
        query.setParameter("bookID", bookView.getBookID());
        query.setParameter("personID", personView.getId());
        HistoryModel historyModel = query.getSingleResult();
        historyModel.setSysCreationDate(stringSQLdate);
        historyModel.setReturnDate( dateFormat.format(date) );

        entityManager.merge(historyModel);
    }

    @Override
    public List<String> showEntriesThroughPerson(PersonView personView) {
        TypedQuery<HistoryModel> query = entityManager.createQuery("SELECT h FROM HistoryModel h WHERE personName =:name", HistoryModel.class).setParameter("name", personView.getFullName());
        List<HistoryModel> result = query.getResultList();
        List<String> stringList = result.stream().map(o -> o.toString()).collect(Collectors.toList());
        return stringList;
    }

    @Override
    public List<String> showEntriesThroughBook(BookView bookView) {
        TypedQuery<HistoryModel> query = entityManager.createQuery("SELECT h FROM HistoryModel h WHERE bookID =:bookID", HistoryModel.class).setParameter("bookID", bookView.getBookID());
        List<HistoryModel> result = query.getResultList();
        List<String> stringList = result.stream().map(o -> o.toString()).collect(Collectors.toList());
        return stringList;
    }


}
