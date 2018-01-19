package com.mercateo.rest.jersey.utils.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationExceptionJson extends SimpleExceptionJson {

    List<ValidationError> errors;

    public ValidationExceptionJson(String title, int status, String detail,List<ValidationError> errors) {
        super(title, status, detail);
        this.errors = errors;
    }
}
