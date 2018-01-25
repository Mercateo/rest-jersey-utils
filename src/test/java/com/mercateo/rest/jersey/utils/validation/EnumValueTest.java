package com.mercateo.rest.jersey.utils.validation;

import lombok.val;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.*;


public class EnumValueTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testInvalidEnumType(){
        ClassUsesInvalidTestEnum classToBeValidated = new ClassUsesInvalidTestEnum();
        val violations = validator.validate(classToBeValidated);

        assertThat(violations.size()).isGreaterThan(0);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("ENUM");
    }


    @Test
    public void testValidEnumType(){
        ClassUsesValidTestEnum classToBeValidated = new ClassUsesValidTestEnum();
        val violations = validator.validate(classToBeValidated);

        assertThat(violations).size().isEqualTo(0);
    }

    private static class ClassUsesInvalidTestEnum {
        @EnumValue(targetEnum = TestEnum.class)
        String invalidStringOfTestEnum = "INVALID_TYPE";
    }

    private static class ClassUsesValidTestEnum {
        @EnumValue(targetEnum = TestEnum.class)
        String invalidStringOfTestEnum = "VALID_TYPE_1";
    }

}

enum TestEnum {
    VALID_TYPE_1
}

