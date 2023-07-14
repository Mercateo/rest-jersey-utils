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
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

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
