package com.bean.form.exceptions.BookExceptions;

import com.bean.form.exceptions.CustomException;

public class IncorrectInputBookDataException extends CustomException {
    public IncorrectInputBookDataException() {
        super("Некорректные входные данные. ID должен быть от 1 до 4 цифр. Название книги должно состоять из кириллицы.");
    }
}
