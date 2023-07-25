/*
 * Copyright © 2017 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mercateo.rest.jersey.utils.exception;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class JsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {

    @Override
    public Response toResponse(JsonMappingException exception) {

        if (exception instanceof InvalidFormatException) {

            String path = constructJsonPath(exception.getPath());
            InvalidFormatException e = (InvalidFormatException) exception;
            Class<?> targetType = e.getTargetType();

            if (targetType != null && targetType == UUID.class) {
                return createUuidResponse(path);
            } else if (targetType != null && targetType.isEnum()) {
                return createResponseForWrongEnumValue(path);
            } else {
                if (targetType != null && isWrongTypeMappableField(targetType)) {
                    return createResponseForWrongType(path);
                }
            }
        }

        return createDefaultResponse(exception);

    }

    private Response createUuidResponse(String path) {

        List<ValidationError> errors = Arrays.asList(new ValidationError(
                ValidationErrorCode.PATTERN.name(), path,
                "^[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}$"));

        return createCustomResponse(errors);
    }

    private Response createResponseForWrongEnumValue(String path) {

        List<ValidationError> errors = Arrays.asList(new ValidationError(
                ValidationErrorCode.ENUM.name(), path));

        return createCustomResponse(errors);
    }

    private Response createResponseForWrongType(String path) {

        List<ValidationError> errors = Arrays.asList(new ValidationError(
                ValidationErrorCode.TYPE.name(), path));

        return createCustomResponse(errors);
    }

    private Response createCustomResponse(List<ValidationError> errors) {
        ValidationExceptionJson entity = new ValidationExceptionJson(
                "https://developers.unite.eu/errors/invalid",
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
    
    private static String constructJsonPath(List<Reference> path) {
        StringBuilder jsonPath = new StringBuilder("#");
        path.forEach(pathComponent -> {
            jsonPath.append("/").append(pathComponent.getFieldName());
        });
        return jsonPath.toString();
    }
}
