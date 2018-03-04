package com.bean.form.model;

import com.bean.form.view.BookView;

import javax.persistence.*;

@Entity
@Table(name = "Books")
public class BookModel {

    @Id
    @Column(name = "Book_ID")
    private Integer bookID;

    @Column(name = "Name")
    private String bookName;

    @ManyToOne(fetch = FetchType.LAZY)//или @OneToOne...?
    @JoinColumn(name = "Person_Id")
    private PersonModel person;
    
    public BookModel() {
    }

    public BookModel(Integer bookID, String bookName, PersonModel person) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.person = person;
    }

    public BookModel(BookView book) {
        this.bookID = book.getBookID();
        this.bookName = book.getBookName();
    }

    public Integer getBookID() {
        return bookID;
    }

    public void setBookID(Integer id) {
        this.bookID = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String fullName) {
        this.bookName = fullName;
    }

    public PersonModel getPerson() {
        return person;
    }

    public void setPerson(PersonModel person) {
        this.person = person;
    }

}