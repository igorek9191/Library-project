package com.bean.form.exceptions;


public class BookAlreadyIssuedException extends CustomException {
    public BookAlreadyIssuedException(String bookName, String personName) {
        super("Книга "+bookName+" уже выдана читателю " +personName);
    }
}