package com.bean.form.exceptions.BookExceptions;

import com.bean.form.exceptions.CustomException;

public class BookAlreadyPresentException extends CustomException {
    public BookAlreadyPresentException(Integer bookId, String bookName){
        super("В БД с ID = "+ bookId+ " занесена книга " + bookName);
    }
}
