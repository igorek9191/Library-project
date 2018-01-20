package com.bean.form.view;

public class ErrorResponse<T> {

    private T errors;

    public ErrorResponse() {
    }

    public ErrorResponse(T errors) {
        this.errors = errors;
    }

    public T getErrors() {
        return errors;
    }

    public void setErrors(T errors) {
        this.errors = errors;
    }
}
