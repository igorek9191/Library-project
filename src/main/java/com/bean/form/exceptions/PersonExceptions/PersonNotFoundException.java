package com.bean.form.exceptions.PersonExceptions;

import com.bean.form.exceptions.CustomException;

public class PersonNotFoundException extends CustomException {
    public PersonNotFoundException(String personName, String telephone) {
        super("Читатель с именем " + personName+ " и телефоном "+telephone+" не найден в БД.");
    }
}
