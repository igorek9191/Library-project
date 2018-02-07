package com.bean.form.exceptions.PersonExceptions;


import com.bean.form.exceptions.CustomException;

public class IncorrectPersonInputDataException extends CustomException {
    public IncorrectPersonInputDataException() {
        super("Некорректные входные данные для читателя. Имя должно быть из кириллицы (например Иванов ИИ), телефон из 6 или 11 цифр");
    }
}
