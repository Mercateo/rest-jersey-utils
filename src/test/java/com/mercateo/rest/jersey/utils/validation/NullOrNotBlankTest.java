package com.mercateo.rest.jersey.utils.validation;

import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class NullOrNotBlankTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testEmptyString(){
        ClassWithString classToBeValidated = new ClassWithString("");
        val violations = validator.validate(classToBeValidated);

        assertThat(violations).size().isGreaterThan(0);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("INVALID_VALUE");
    }


    @Test
    public void testEmptyStringWithSpaces(){
        ClassWithString classToBeValidated = new ClassWithString("   ");
        val violations = validator.validate(classToBeValidated);

        assertThat(violations).size().isGreaterThan(0);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("INVALID_VALUE");
    }

    @Test
    public void testNullString(){
        ClassWithString classToBeValidated = new ClassWithString(null);
        val violations = validator.validate(classToBeValidated);

        assertThat(violations).size().isEqualTo(0);
    }

    @Test
    public void testNotBlankString(){
        ClassWithString classToBeValidated = new ClassWithString("valid String");
        val violations = validator.validate(classToBeValidated);

        assertThat(violations).size().isEqualTo(0);
    }

    @AllArgsConstructor
    private static class ClassWithString {
        @NullOrNotBlank
        String shouldBeNullOrNotBlank = "";
    }
}
