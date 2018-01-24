package com.mercateo.rest.jersey.utils.exception;

import com.mercateo.rest.jersey.utils.validation.EnumValue;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

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
    public void testSizeMaxValidationToReponse(){
        val invalidVal = new ClassWithSizeAnnotation("12345678911");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                    .get("code").toString()
        ).isEqualTo("MAXLENGTH");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()
        ).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("limit")
        ).isEqualTo(MAX_VAL);
    }

    @Test
    public void testMaxAnnotationValidationToReponse(){
        val invalidVal = new ClassWithMinMaxAnnotation("12345678911");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()
        ).isEqualTo("MAXLENGTH");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()
        ).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("limit")
        ).isEqualTo(MAX_VAL);
    }

    @Test
    public void testMinAnnotationValidationToReponse(){
        val invalidVal = new ClassWithMinMaxAnnotation("1");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()
        ).isEqualTo("MINLENGTH");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()
        ).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("limit")
        ).isEqualTo(MIN_VAL);
    }

    @Test
    public void testNotBlankAnnotationValidationToReponse(){
        val invalidVal = new ClassWithNotBlankAnnotation("");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()
        ).isEqualTo("REQUIRED");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()
        ).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .has("limit")
        ).isFalse();
    }

    @Test
    public void testNotNullAnnotationValidationToReponse(){
        val invalidVal = new ClassWithNotNullAnnotation(null);
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()
        ).isEqualTo("REQUIRED");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()
        ).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .has("limit")
        ).isFalse();
    }

    @Test
    public void testEmailAnnotationValidationToReponse(){
        val invalidVal = new ClassWithEmailValueAnnotation("INVALID_EMAIL");
        Response response = uut.toResponse(new ConstraintViolationException(validator.validate(invalidVal)));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()
        ).isEqualTo("INVALID_EMAIL");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()
        ).isEqualTo("#/annotatedVar");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .has("limit")
        ).isFalse();
    }



    @RequiredArgsConstructor
    public static class ClassWithSizeAnnotation{
        @Size(min = MIN_VAL, max = MAX_VAL)
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithMinMaxAnnotation{
        @Min(MIN_VAL)
        @Max(MAX_VAL)
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithNotBlankAnnotation{
        @NotBlank
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithNotNullAnnotation{
        @NotNull
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithEnumValueAnnotation{
        @EnumValue(targetEnum = TEST_ENUM.class)
        final String annotatedVar;
    }

    @RequiredArgsConstructor
    public static class ClassWithEmailValueAnnotation{
        @Email
        final String annotatedVar;
    }

    enum TEST_ENUM{
        VAL
    }
}
