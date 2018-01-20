package com.bean.form.dao;

import com.bean.form.view.BookView;
import com.bean.form.view.PersonView;

import java.util.List;

public interface HistoryGiveAndReturnBookDAO {

    void saveGivenEntry(BookView bookView, PersonView personView);

    void saveReturnEntry(BookView bookView, PersonView personView);

    List<String> showEntriesThroughPerson (PersonView personView);

    List<String> showEntriesThroughBook (BookView bookView);
}
