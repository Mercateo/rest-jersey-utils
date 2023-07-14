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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tngtech.junit.dataprovider.DataProvider;
import com.tngtech.junit.dataprovider.UseDataProvider;

import jakarta.ws.rs.core.Response;

@SuppressWarnings("boxing")
//@RunWith(DataProviderRunner.class)
public class JsonMappingExceptionMapperTest {

    private JsonMappingExceptionMapper uut = new JsonMappingExceptionMapper();

    @Test
    public void test_ContentTypeHeader_defaultResponse() throws Exception {

        // given

        // when
        Response response = uut.toResponse(mock(JsonMappingException.class));

        // then
        assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                "text/plain");
    }

    @Test
    public void test_ContentTypeHeader_noTargetType() throws Exception {

        // given
        JsonMappingException ex = mock(InvalidFormatException.class);
        Reference reference = new Reference(Object.class, "certainField");
        List<Reference> references = Arrays.asList(reference);
        when(ex.getPath()).thenReturn(references);

        // when
        Response response = uut.toResponse(ex);

        // then
        Assertions.assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                "text/plain");
    }

    @Test
    public void test_Error_Uuid() throws Exception {

        // given
        InvalidFormatException ex = createExceptionWithTargetType(UUID.class);

        // when
        Response response = uut.toResponse(ex);

        // then
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        Assertions.assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                "application/problem+json");
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson.getString("title").equals("Invalid"));

        JSONObject errorJson = responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0);

        assertThat(errorJson.get("code").toString()).isEqualTo("PATTERN");
        assertThat(errorJson.get("pattern").toString()).isEqualTo(
                "^[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89ab][a-f0-9]{3}-[a-f0-9]{12}$");

    }

    @Test
    public void test_Error_complexJsonPath() throws Exception {

        // given
        InvalidFormatException ex = createExceptionWithNestedFields();

        // when
        Response response = uut.toResponse(ex);

        // then

        JSONObject errorJson = new JSONObject(response.getEntity())
                .getJSONArray("errors")
                .getJSONObject(0);

        assertThat(errorJson.get("path").toString()).isEqualTo(
                "#/fieldLayer1/fieldLayer2/fieldLayer3");

    }

    private InvalidFormatException createExceptionWithNestedFields() {
        InvalidFormatException ex = new InvalidFormatException(null, "msg", new Object(),
                UUID.class);
        Reference refLayer1 = new Reference(Object.class, "fieldLayer1");
        Reference refLayer2 = new Reference(Object.class, "fieldLayer2");
        Reference refLayer3 = new Reference(Object.class, "fieldLayer3");

        ex.prependPath(refLayer3);
        ex.prependPath(refLayer2);
        ex.prependPath(refLayer1);
        return ex;
    }

    @Test
    public void test_Error_Enum() throws Exception {

        // given
        InvalidFormatException ex = createExceptionWithTargetType(CountryCode.class);

        // when
        Response response = uut.toResponse(ex);

        // then
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        Assertions.assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                "application/problem+json");
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson.getString("title").equals("Invalid"));

        JSONObject errorJson = responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0);

        assertThat(errorJson.get("code").toString()).isEqualTo(ValidationErrorCode.ENUM.name());
        assertThat(errorJson.get("path").toString()).isEqualTo("#/certainField");

    }

    private InvalidFormatException createExceptionWithTargetType(Class<?> targetType) {
        InvalidFormatException ex = new InvalidFormatException(null, "msg", new Object(),
                targetType);
        Reference reference = new Reference(Object.class, "certainField");
        ex.prependPath(reference);
        return ex;
    }

    @Test
    public void test_Error_MappableJsonField_LongAsExample() throws Exception {

        // given
        InvalidFormatException ex = createExceptionWithTargetType(Long.class);

        // when
        Response response = uut.toResponse(ex);

        // then
        JSONObject responseEntityJson = new JSONObject(response.getEntity());
        Assertions.assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                "application/problem+json");
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson.getString("title").equals("Invalid"));

        JSONObject errorJson = responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0);

        assertThat(errorJson.get("code").toString()).isEqualTo("TYPE");

    }

    @DataProvider
    public static Object[][] fieldsForMappingTest() {
        return new Object[][] {
        // @formatter:off
        { int.class,true  },
        { Integer.class,true },
        { long.class,true },
        { Long.class,true },
        { Boolean.class,true },
        { boolean.class,true },
        { OffsetDateTime.class,true },
        { LocalDateTime.class ,true },
        { Date.class,true },
        { Object.class,false },
        { String.class,false },
        // @formatter:on
        };
    }

    @ParameterizedTest
    @UseDataProvider("fieldsForMappingTest")
    public void test_mappableFields(Class<?> targetType, boolean customError) throws Throwable {

        // given
        InvalidFormatException mock = createExceptionWithTargetType(targetType);
        Reference reference = new Reference(Object.class, "certainId");
        mock.prependPath(reference);

        // when
        Response response = uut.toResponse(mock);

        // then
        if (customError) {
            assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                    "application/problem+json");
        } else {
            assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                    "text/plain");
        }

    }

    private enum CountryCode {
        DE, BE, CH, IT, FR, HU, ES, AT, CZ, SK, GB, IE, PL, NL
    }

}
