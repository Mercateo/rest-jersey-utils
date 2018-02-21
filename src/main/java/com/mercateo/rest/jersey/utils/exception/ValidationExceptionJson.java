package com.mercateo.rest.jersey.utils.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({ "type", "status","title","detail", "errors" })
public class ValidationExceptionJson extends SimpleExceptionJson {

    final String type;
    final List<ValidationError> errors;

    public ValidationExceptionJson(final String type,
                                   final String title,
                                   final int status,
                                   final String detail,
                                   final List<ValidationError> errors) {

        super(title, status, detail);
        this.type = type;
        this.errors = errors;
    }
}
