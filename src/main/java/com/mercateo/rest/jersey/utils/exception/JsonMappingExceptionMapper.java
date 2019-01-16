package com.mercateo.rest.jersey.utils.exception;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

    @Override
    public Response toResponse(JsonMappingException exception) {

        if (exception instanceof InvalidFormatException) {

            String fieldName = exception.getPath().iterator().next().getFieldName();
            InvalidFormatException e = (InvalidFormatException) exception;
            Class<?> targetType = e.getTargetType();

            if (targetType != null && targetType == UUID.class) {
                return createUuidResponse(fieldName);
            } else if (targetType != null && targetType.isEnum()) {
                return createResponseForWrongEnumValue(fieldName);
            } else {
                if (targetType != null && isWrongTypeMappableField(targetType)) {
                    return createResponseForWrongType(fieldName);
                }
            }
        }

        return createDefaultResponse(exception);

    }

    private Response createUuidResponse(String field) {

        List<ValidationError> errors = Arrays.asList(new ValidationError(
                ValidationErrorCode.PATTERN.name(), "#/" + field,
                "^[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}$"));

        return createCustomResponse(errors);
    }

    private Response createResponseForWrongEnumValue(String field) {

        List<ValidationError> errors = Arrays.asList(new ValidationError(
                ValidationErrorCode.ENUM.name(), "#/" + field));

        return createCustomResponse(errors);
    }

    private Response createResponseForWrongType(String field) {

        List<ValidationError> errors = Arrays.asList(new ValidationError(
                ValidationErrorCode.TYPE.name(), "#/" + field));

        return createCustomResponse(errors);
    }

    private Response createCustomResponse(List<ValidationError> errors) {
        ValidationExceptionJson entity = new ValidationExceptionJson(
                "http://developers.unite.eu/errors/invalid",
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

    private boolean isWrongTypeMappableField(Class<?> targetType) {

        boolean isMappable = targetType == Integer.class
                || targetType == int.class
                || targetType == Long.class
                || targetType == long.class
                || targetType == Boolean.class
                || targetType == boolean.class
                || targetType == OffsetDateTime.class
                || targetType == LocalDateTime.class
                || targetType == Date.class;

        return isMappable;
    }

    private Response createDefaultResponse(Exception exception) {

        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).type(
                "text/plain").build();
    }
}
