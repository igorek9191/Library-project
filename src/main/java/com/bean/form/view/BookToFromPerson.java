package com.bean.form.view;


public class BookToFromPerson {

    public Integer bookID;

    public String bookName;

    public String fullName;

    public String phoneNumber;

    public BookView bookView (){
        return new BookView(this.bookID, this.bookName);
    }

    public PersonView personView() {
        return new PersonView(this.fullName, this.phoneNumber);
    }

    public BookToFromPerson(Integer bookID, String bookName, String fullName, String phoneNumber) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public BookToFromPerson() {
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer bookID) {
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

    @Override
    public String toString() {
        return "BookToFromPerson{" +
                "bookID=" + bookID +
                ", bookName='" + bookName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
