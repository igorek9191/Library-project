package com.bean.form.exceptions.BookExceptions;


import com.bean.form.exceptions.CustomException;

public class BookNotFoundException extends CustomException {
    public BookNotFoundException(Integer bookID, String bookName){
        super("Книга с ID = " + bookID + " и названием " + bookName+ " не найдена в БД");
    }

}
