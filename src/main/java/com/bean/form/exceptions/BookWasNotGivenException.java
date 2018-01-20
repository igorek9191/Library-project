package com.bean.form.exceptions;


public class BookWasNotGivenException extends CustomException {
    public BookWasNotGivenException(String bookName, String personName){
        super("Книга "+bookName+" не была выдана читателю "+personName);
    }
}
