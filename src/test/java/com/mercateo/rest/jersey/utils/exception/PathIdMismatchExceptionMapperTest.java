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

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.core.Response;

public class PathIdMismatchExceptionMapperTest {

    PathIdMismatchExceptionMapper uut = new PathIdMismatchExceptionMapper();

    @Test
    public void test_ContentTypeHeader() throws Exception {
        Response response = uut.toResponse(new PathIdMismatchException());
        Assertions.assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                "application/problem+json");
    }

    @Test
    public void test_ErrorResponseFields() throws Exception {
        Response response = uut.toResponse(new PathIdMismatchException());
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson.getString("title").equals("Invalid"));

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("PATH_ID_MISMATCH");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/id");
    }

}
