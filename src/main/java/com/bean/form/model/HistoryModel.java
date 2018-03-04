package com.bean.form.model;


import javax.persistence.*;


@Entity
@Table(name = "History")
public class HistoryModel {

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Sys_Creation_Date")
    private String sysCreationDate;

    @Column(name = "Book_ID")
    private Integer bookID;

    @Column(name = "Book_Name")
    private String bookName;

    @Column(name = "Person_ID")
    private Integer personID;

    @Column(name = "Peson_Name")
    private String personName;

    @Column(name = "Phone_Number")
    private String phoneNumber;

    @Column(name = "Given_Date")
    private String givenDate;

    @Column(name = "Return_Date")
    private String returnDate;

    public HistoryModel(String sysCreationDate, Integer bookID, String bookName, Integer personID, String personName, String phoneNumber, String givenDate, String returnDate) {
        this.sysCreationDate = sysCreationDate;
        this.bookID = bookID;
        this.bookName = bookName;
        this.personID = personID;
        this.personName = personName;
        this.phoneNumber = phoneNumber;
        this.givenDate = givenDate;
        this.returnDate = returnDate;
    }

    public HistoryModel() {
    }

    @Override
    public String toString (){
        StringBuffer buffer = new StringBuffer();
        if(this.returnDate == null) {
            buffer
                    .append("\n" + "Библиотечный № книги: " + this.bookID + "\n")
                    .append("Название книги: " + this.bookName + "\n")
                    .append("Имя читателя: " + this.personName + "\n")
                    .append("Телефон читателя: " + this.phoneNumber + "\n")
                    .append("Дата выдачи книги: " + this.givenDate);
            return buffer.toString();
        } else {
            buffer
                    .append("\n" + "Библиотечный № книги: " + this.bookID + "\n")
                    .append("Название книги: " + this.bookName + "\n")
                    .append("Имя читателя: " + this.personName + "\n")
                    .append("Телефон читателя: " + this.phoneNumber + "\n")
                    .append("Дата выдачи книги: " + this.givenDate + "\n")
                    .append("Дата возврата книги: "+this.returnDate);
            return buffer.toString();
        }
    }

    public String getSysCreationDate() {
        return sysCreationDate;
    }

    public void setSysCreationDate(String sysCreationDate) {
        this.sysCreationDate = sysCreationDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
