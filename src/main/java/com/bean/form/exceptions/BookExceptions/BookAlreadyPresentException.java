package com.bean.form.exceptions.BookExceptions;

import com.bean.form.exceptions.CustomException;

public class BookAlreadyPresentException extends CustomException {
    public BookAlreadyPresentException(String bookName, String bookId){
        super("Книга " + bookName + " с ID = "+ bookId+ " уже есть в БД");
    }
}
