package com.bean.form.view;

public class Response<T, E> {

    private T data;

    private E errors;

    //for Jackson
    public Response() {
    }

    public Response(T data, E errors) {
        this.data = data;
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public E getErrors() {
        return errors;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setErrors(E errors) {
        this.errors = errors;
    }
}