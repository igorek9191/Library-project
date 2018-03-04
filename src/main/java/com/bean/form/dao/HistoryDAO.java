package com.bean.form.dao;

import com.bean.form.model.BookModel;
import com.bean.form.model.PersonModel;

import java.util.List;

public interface HistoryDAO {

    void saveEntryOfGiven(BookModel bookModel, PersonModel personModel);

    void saveEntryOfReturn(BookModel bookModel, PersonModel personModel);

    List<String> showEntriesThroughPerson (PersonModel personModel);

    List<String> showEntriesThroughBook (BookModel bookModel);
}
