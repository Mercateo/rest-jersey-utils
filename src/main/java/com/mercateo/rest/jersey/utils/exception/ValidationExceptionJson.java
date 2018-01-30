package com.mercateo.rest.jersey.utils.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.util.List;

@Getter
@JsonPropertyOrder({ "type", "status","title","detail", "errors" })
public class ValidationExceptionJson extends SimpleExceptionJson {

    final String type = "https://unite.eu/developers/errors/invalid";
    List<ValidationError> errors;

    public ValidationExceptionJson(String title, int status, String detail,List<ValidationError> errors) {
        super(title, status, detail);
        this.errors = errors;
    }
}
