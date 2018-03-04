package com.bean.form.service.impl;

import com.bean.form.dao.BookDAO;
import com.bean.form.dao.HistoryDAO;
import com.bean.form.dao.PersonDAO;
import com.bean.form.exceptions.BookExceptions.BookNotFoundException;
import com.bean.form.exceptions.PersonExceptions.PersonNotFoundException;
import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;
import com.bean.form.service.HistoryService;
import com.bean.form.view.BookView;
import com.bean.form.view.PersonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final PersonDAO personDAO;
    private final BookDAO bookDAO;
    private final HistoryDAO historyDAO;

    @Autowired
    public HistoryServiceImpl(PersonDAO personDAO, BookDAO bookDAO, HistoryDAO historyDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
        this.historyDAO = historyDAO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> showEntriesThroughPerson(PersonView personView) {
        PersonModel person = personDAO.findByFullNameAndPhoneNumber(personView.getFullName(), personView.getPhoneNumber());
        if(person == null) throw new PersonNotFoundException(personView.getFullName(), personView.getPhoneNumber());
        return historyDAO.showEntriesThroughPerson(person);

    }

    @Override
    @Transactional(readOnly = true)
    public List<String> showEntriesThroughBook(BookView bookView) {
        BookModel book = bookDAO.findByBookID(bookView.getBookID());
        if(book == null) throw new BookNotFoundException(bookView.getBookID(), bookView.getBookName());
        return historyDAO.showEntriesThroughBook(book);
    }
}
