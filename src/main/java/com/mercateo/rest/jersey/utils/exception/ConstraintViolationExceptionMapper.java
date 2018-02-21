package com.mercateo.rest.jersey.utils.exception;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Slf4j
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException>{

    @Override
    public Response toResponse(ConstraintViolationException violationException) {
        List<ValidationError> errors = toValidationErrors(violationException);

        log.error("Sending error response to client {}", errors);

        ValidationExceptionJson entity = new ValidationExceptionJson(
                "https://unite.eu/developers/errors/invalid",
                "Invalid",
                BAD_REQUEST.getStatusCode(),
                "The request body is syntactically correct, but is not accepted, because of its data.",
                errors);

        return Response
                .status(BAD_REQUEST)
                .entity(entity)
                .type("application/problem+json")
                .build();
    }

    private List<ValidationError> toValidationErrors(ConstraintViolationException violationException){
        return  violationException
                .getConstraintViolations()
                .stream() //
                .map(ValidationError::of)
                .collect(Collectors.toList());

    }

}
