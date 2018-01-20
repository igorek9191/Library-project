package com.bean.form.exceptions;

public class BookIssuedAnotherPersonException extends CustomException {
    public BookIssuedAnotherPersonException (String bookName, String truePerson, String truePhoneNumber, String fakePerson, String fakePhonenumber){
        super("Книга "+bookName+" была выдана читателю "+truePerson+ " с телефоном "+truePhoneNumber+" а не читателю "+fakePerson+ " с телефоном " +fakePhonenumber);
    }
}
