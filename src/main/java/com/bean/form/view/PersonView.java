package com.bean.form.view;


import javax.persistence.Column;

public class PersonView {

    //private Long id;

    public String fullName;

    public String phoneNumber;

    public PersonView() {
    }

    public PersonView(String fullName) {
        this.fullName = fullName;
    }

    public PersonView(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    /*public PersonView(Long id, String fullName, int phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

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


    public boolean equals (PersonView personView){
        if(this.fullName.equals(personView.fullName) && (this.phoneNumber == personView.phoneNumber)) return true;
        else return false;
    }
}
