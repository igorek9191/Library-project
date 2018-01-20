package com.bean.form.exceptions.PersonExceptions;

import com.bean.form.exceptions.CustomException;

public class PersonAlreadyExistsException extends CustomException {

    public PersonAlreadyExistsException(String personName, String phoneNumber) {
        super("Читатель " + personName + ", с телефоном " + phoneNumber +" уже занесён в БД");
    }
}
