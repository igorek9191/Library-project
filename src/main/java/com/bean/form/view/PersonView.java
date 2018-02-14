package com.bean.form.view;

import com.bean.form.model.PersonModel;

public class PersonView {

    public Long id;

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

    public PersonView(Long id, String fullName, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public PersonView(PersonModel personModel){
        this.id = personModel.getId();
        this.fullName = personModel.getFullName();
        this.phoneNumber = personModel.getPhoneNumber();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonView view = (PersonView) o;

        if (id != null ? !id.equals(view.id) : view.id != null) return false;
        if (fullName != null ? !fullName.equals(view.fullName) : view.fullName != null) return false;
        return phoneNumber != null ? phoneNumber.equals(view.phoneNumber) : view.phoneNumber == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }
}
