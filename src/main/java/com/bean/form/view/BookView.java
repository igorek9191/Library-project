package com.bean.form.view;

public class BookView {

    public String bookID;

    public String bookName;

    public BookView() {
    }

    public BookView(String bookID) {
        this.bookID = bookID;
    }

    public BookView(String bookID, String bookName) {
        this.bookID = bookID;
        this.bookName = bookName;
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

    @Override
    public String toString() {
        return "BookModel{" +
                " id= " + bookID +
                " fullName= " + bookName +
                " }";
    }

    public boolean equals (BookView bookView){
        if(this.bookID.equals(bookView.getBookID()) && this.bookName.equals(bookView.getBookName())) return true;
        else return false;
    }
}