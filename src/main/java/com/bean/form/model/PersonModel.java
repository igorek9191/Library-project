package com.bean.form.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Persons")
public class PersonModel {
    @Id
    @GeneratedValue
    @Column(name = "Id")
    private Long id;

    //@Basic(optional = false)
    @Column(name = "Full_Name")
    private String fullName;

    //@Basic(optional = false)
    @Column(name = "Phone_Number")
    private String phoneNumber;

    @OneToMany(mappedBy = "person")
    List<BookModel> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PersonModel() {
    }

    public PersonModel(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public PersonModel(Long id, String fullName, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public boolean equals (PersonModel person){
        if(this.id==person.getId() && this.fullName.equals(person.getFullName()) && this.phoneNumber==person.getPhoneNumber() && this.books.equals(person.books)) return true;
        else return false;
    }
}
