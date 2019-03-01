package com.mercateo.rest.jersey.utils.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import lombok.Getter;

@Getter
public enum ConstraintViolationExceptionType implements RFCExceptionDetailsProvider {

    INVALID("https://developers.unite.eu/errors/invalid",
            BAD_REQUEST.getStatusCode(),
            "Invalid",
            "The request body is syntactically correct, but is not accepted, because of its data."),
    INVALID_QUERY_PARAM(
            "https://developers.unite.eu/errors/invalid-query-param",
            BAD_REQUEST.getStatusCode(),
            "Invalid Query Parameter",
            "The filter query is not valid.");

    private final String type;

    private final int status;

    private final String title;

    private final String detail;

    ConstraintViolationExceptionType(String type, int status, String title, String detail) {
        this.type = type;
        this.status = status;
        this.title = title;
        this.detail = detail;
    }

}
