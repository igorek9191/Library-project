package com.bean.form.view;

import com.bean.form.model.BookModel;

public class BookView {

    public String bookID;

    public String bookName;

    public BookView() {
    }

    public BookView(String bookID, String bookName) {
        this.bookID = bookID;
        this.bookName = bookName;
    }

    public BookView(BookModel book) {
        this.bookID = book.getBookID();
        this.bookName = book.getBookName();
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

    /*@Override
    public String toString() {
        return "BookModel{" +
                " id= " + bookID +
                " fullName= " + bookName +
                " }";
    }*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookView bookView = (BookView) o;

        if (!bookID.equals(bookView.bookID)) return false;
        return bookName.equals(bookView.bookName);
    }

    @Override
    public int hashCode() {
        int result = bookID.hashCode();
        result = 31 * result + bookName.hashCode();
        return result;
    }
}