package com.mercateo.rest.jersey.utils.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ConstraintViolationExceptionTypeTest {

    @Test
    public void test_INVALID_QUERY_PARAMExceptionMembers() {

        assertThat(ConstraintViolationExceptionType.INVALID_QUERY_PARAM.getType()).isEqualTo(
                "https://developers.unite.eu/errors/invalid-query-param");
        assertThat(ConstraintViolationExceptionType.INVALID_QUERY_PARAM.getStatus()).isEqualTo(
                BAD_REQUEST.getStatusCode());
        assertThat(ConstraintViolationExceptionType.INVALID_QUERY_PARAM.getTitle()).isEqualTo(
                "Invalid Query Parameter");
        assertThat(ConstraintViolationExceptionType.INVALID_QUERY_PARAM.getDetail()).isEqualTo(
                "The filter query is not valid.");
    }

    @Test
    public void test_INVALIDExceptionMembers() {

        assertThat(ConstraintViolationExceptionType.INVALID.getType()).isEqualTo(
                "https://developers.unite.eu/errors/invalid");
        assertThat(ConstraintViolationExceptionType.INVALID.getStatus()).isEqualTo(BAD_REQUEST
                .getStatusCode());
        assertThat(ConstraintViolationExceptionType.INVALID.getTitle()).isEqualTo("Invalid");
        assertThat(ConstraintViolationExceptionType.INVALID.getDetail()).isEqualTo(
                "The request body is syntactically correct, but is not accepted, because of its data.");
    }

}
