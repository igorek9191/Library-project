package com.bean.form.service;

import com.bean.form.view.BookView;
import com.bean.form.view.PersonView;

import java.util.List;

public interface HistoryService {

    List<String> showEntriesThroughPerson (PersonView personView);

    List<String> showEntriesThroughBook(BookView bookView);
}
