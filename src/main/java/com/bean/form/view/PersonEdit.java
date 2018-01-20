package com.bean.form.view;

public class PersonEdit {

    public String oldFullName;

    public String oldPhoneNumber;

    public String newFullName;

    public String newPhoneNumber;

    public PersonView oldPersonView() {
        return new PersonView(this.oldFullName, this.oldPhoneNumber);
    }

    public PersonView newPersonView() {
        return new PersonView(this.newFullName, this.newPhoneNumber);
    }

    public PersonEdit() {
    }

    public PersonEdit(String oldFullName, String oldPhoneNumber, String newFullName, String newPhoneNumber) {
        this.oldFullName = oldFullName;
        this.oldPhoneNumber = oldPhoneNumber;
        this.newFullName = newFullName;
        this.newPhoneNumber = newPhoneNumber;
    }

    public String getOldFullName() {
        return oldFullName;
    }

    public void setOldFullName(String oldFullName) {
        this.oldFullName = oldFullName;
    }

    public String getOldPhoneNumber() {
        return oldPhoneNumber;
    }

    public void setOldPhoneNumber(String oldPhoneNumber) {
        this.oldPhoneNumber = oldPhoneNumber;
    }

    public String getNewFullName() {
        return newFullName;
    }

    public void setNewFullName(String newFullName) {
        this.newFullName = newFullName;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }
}
