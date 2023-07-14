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
