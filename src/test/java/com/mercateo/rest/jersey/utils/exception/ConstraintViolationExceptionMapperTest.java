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

import java.lang.annotation.Annotation;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Sets;
import com.mercateo.rest.jersey.utils.listing.SearchQueryParameterBean;
import com.mercateo.rest.jersey.utils.validation.EnumValue;

import lombok.RequiredArgsConstructor;
import lombok.val;

@RunWith(MockitoJUnitRunner.class)
public class ConstraintViolationExceptionMapperTest {

    private final static int MIN_VAL = 2;

    private final static int MAX_VAL = 10;

    private ConstraintViolationExceptionMapper uut = new ConstraintViolationExceptionMapper();

    private static Validator validator;

    @BeforeClass
    public static void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testSizeMaxValidationToReponse() {
        val invalidVal = new ClassWithSizeAnnotation("12345678911");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(
                invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("MAXLENGTH");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("limit")).isEqualTo(MAX_VAL);
    }

    @Test
    public void testMaxAnnotationValidationToReponse() {
        val invalidVal = new ClassWithMinMaxAnnotation("12345678911");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(
                invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("MAXLENGTH");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("limit")).isEqualTo(MAX_VAL);
    }

    @Test
    public void testMinAnnotationValidationToReponse() {
        val invalidVal = new ClassWithMinMaxAnnotation("1");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(
                invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("MINLENGTH");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("limit")).isEqualTo(MIN_VAL);
    }

    @Test
    public void testNotBlankAnnotationValidationToReponse() {
        val invalidVal = new ClassWithNotBlankAnnotation("");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(
                invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("REQUIRED");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .has("limit")).isFalse();
    }

    @Test
    public void testNotNullAnnotationValidationToReponse() {
        val invalidVal = new ClassWithNotNullAnnotation(null);
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(
                invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("REQUIRED");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .has("limit")).isFalse();
    }

    @Test
    public void testEmailAnnotationValidationToReponse() {
        val invalidVal = new ClassWithEmailValueAnnotation("INVALID_EMAIL");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(
                invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("INVALID_EMAIL");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .has("limit")).isFalse();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearchQueryParameterViolationToReponse() {

        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        @SuppressWarnings("rawtypes")
        ConstraintDescriptor constraintDescriptor = mock(ConstraintDescriptor.class);
        ClassWhichImplementsAnnotation classWhichImplementsAnnotation = new ClassWhichImplementsAnnotation(
                "foobar");
        ConstraintViolationException searchQueryParameterException = new ConstraintViolationException(
                Sets.newHashSet(constraintViolation));

        when(constraintViolation.getConstraintDescriptor()).thenReturn(constraintDescriptor);
        when(constraintViolation.getLeafBean()).thenReturn(new SearchQueryParameterBean());
        when(constraintViolation.getPropertyPath()).thenReturn(mock(Path.class));
        when(constraintDescriptor.getAnnotation()).thenReturn(classWhichImplementsAnnotation);

        Response response = uut.toResponse(searchQueryParameterException);
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson.getString("title")).isEqualTo("Invalid Query Parameter");
        assertThat(responseEntityJson.getString("detail")).isEqualTo(
                "The filter query is not valid.");
        assertThat(responseEntityJson.getInt("status")).isEqualTo(400);
        assertThat(responseEntityJson.getString("type")).isEqualTo(
                "https://developers.unite.eu/errors/invalid-query-param");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo(ValidationErrorCode.REQUIRED.name());

    }

    @RequiredArgsConstructor
    public static class ClassWithSizeAnnotation {
        @Size(min = MIN_VAL, max = MAX_VAL)
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithMinMaxAnnotation {
        @Min(MIN_VAL)
        @Max(MAX_VAL)
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithNotBlankAnnotation {
        @NotBlank
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithNotNullAnnotation {
        @NotNull
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithEnumValueAnnotation {
        @EnumValue(targetEnum = TEST_ENUM.class)
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithEmailValueAnnotation {
        @Email
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public class ClassWhichImplementsAnnotation implements Annotation {
        @NotBlank
        final String annotatedVar;

        @Override
        public Class<? extends Annotation> annotationType() {
            return NotBlank.class;
        }

    }

    enum TEST_ENUM {
        VAL
    }
}
