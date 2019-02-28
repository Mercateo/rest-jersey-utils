package com.mercateo.rest.jersey.utils.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.server.ParamException;
import org.glassfish.jersey.server.ParamException.QueryParamException;

public class ParamExceptionMapper implements ExceptionMapper<ParamException> {

    @Override
    public Response toResponse(ParamException exception) {

        if (exception instanceof QueryParamException) {
            return createCustomResponse(exception.getParameterName());
        }

        return createDefaultResponse(exception);
    }

    private Response createCustomResponse(String parameterName) {

        List<ValidationError> errors = Arrays.asList(new ValidationError(
                ValidationErrorCode.INVALID.name(), "#/expression/" + parameterName));

        ValidationExceptionJson entity = new ValidationExceptionJson(
                "https://developers.unite.eu/errors/invalid-query-param",
                "Invalid Query Parameter",
                BAD_REQUEST.getStatusCode(),
                "The filter query is not valid.",
                errors);

        return Response
                .status(BAD_REQUEST)
                .entity(entity)
                .type("application/problem+json")
                .build();
    }

    private Response createDefaultResponse(Exception exception) {

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exception.getMessage())
                .type("text/plain")
                .build();

    }

}