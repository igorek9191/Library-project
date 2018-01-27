package com.bean.form.model;

import com.bean.form.view.BookView;

import javax.persistence.*;

@Entity
@Table(name = "Books")
public class BookModel {

    @Id
    @Column(name = "BookID")
    private String bookID;

    @Column(name = "Name")
    private String bookName;

    @ManyToOne(fetch = FetchType.LAZY)//или @OneToOne...?
    @JoinColumn(name = "Person_Id")
    private PersonModel person;

    /*@Override
    public String toString() {
        if(this.person == null){
            return "BookModel is:\n"+ "bookID: "+ this.bookID + "\nbookName: " + this.bookName + "\npersonID: NULL";
        } else {
            return "BookModel is:\n" + "bookID: " + this.bookID + "\nbookName: " + this.bookName + "\npersonID: " + this.person.getId();
        }
    }*/
    
    public BookModel() {
    }

    public BookModel(String bookName) {
        this.bookName = bookName;
    }

    public BookModel(String id, String bookName) {
        this.bookID = id;
        this.bookName = bookName;
    }

    public BookModel(String bookID, String bookName, PersonModel person) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.person = person;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String id) {
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