package com.bean.form.view;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BookToFromPerson {

    public String bookID;

    public String bookName;

    public String fullName;

    public String phoneNumber;

    public BookView bookView (){
        return new BookView(this.bookID, this.bookName);
    }

    public PersonView personView() {
        return new PersonView(this.fullName, this.phoneNumber);
    }

    public BookToFromPerson(String bookID, String bookName, String fullName, String phoneNumber) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public BookToFromPerson() {
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    /*public BookView bookView = new BookView();

    public PersonView personView = new PersonView();

    public BookToFromPerson(BookView bookView, PersonView personView) {
        this.bookView = bookView;
        this.personView = personView;
    }

    public BookToFromPerson() {
    }

    public  BookView getBookView() {
        return bookView;
    }

    public  void setBookView(BookView bookView) {
        this.bookView = bookView;
    }

    public PersonView getPersonView() {
        return personView;
    }

    public void setPersonView(PersonView personView) {
        this.personView = personView;
    }*/
}
