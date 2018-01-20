package com.bean.form.exceptions.PersonExceptions;


import com.bean.form.exceptions.CustomException;

public class IncorrectPersonInputData extends CustomException {
    public IncorrectPersonInputData() {
        super("Некорректные входные данные для читателя. Имя должно быть из кириллицы (например - Иванов Иван), телефон из 11 или 6 цифр");
    }
}
