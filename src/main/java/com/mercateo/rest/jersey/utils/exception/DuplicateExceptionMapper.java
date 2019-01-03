/**
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

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class DuplicateExceptionMapper implements ExceptionMapper<DuplicateValidationException> {

    @Override
    public Response toResponse(DuplicateValidationException duplicateValidationException) {
        List<ValidationError> errors = Arrays.asList(new ValidationError(
                ValidationErrorCode.DUPLICATE.name(),
                duplicateValidationException.getPath()));

        log.error("Sending error response to client {}", errors);

        ValidationExceptionJson entity = new ValidationExceptionJson(
                "https://portal.unite.eu/de_DE/developers/errors/invalid",
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

}
