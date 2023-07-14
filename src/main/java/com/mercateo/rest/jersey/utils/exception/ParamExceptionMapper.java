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

import java.util.Arrays;
import java.util.List;

import org.glassfish.jersey.server.ParamException;
import org.glassfish.jersey.server.ParamException.QueryParamException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

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
