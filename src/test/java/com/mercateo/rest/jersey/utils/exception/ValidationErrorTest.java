package com.mercateo.rest.jersey.utils.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;

import org.hibernate.validator.constraints.NotBlank;
import org.junit.Before;
import org.junit.Test;

public class ValidationErrorTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testInvalidPattern() {

        // given
        ClassInvalidIban classInvalidIban = new ClassInvalidIban();

        // when
        Set<ConstraintViolation<Object>> violations = validator.validate(classInvalidIban);
        ValidationError validationError = ValidationError.of(violations.iterator().next());

        // then
        assertThat(validationError.getCode()).isEqualTo(ValidationErrorCode.PATTERN.name());
        assertThat(validationError.getPath()).isEqualTo("#/iban");
        assertThat(validationError.getPattern()).isEqualTo(
                "^[A-Z]{2}[0-9]{2}(?:[ ]?[0-9]{4}){4}(?!(?:[ ]?[0-9]){3})(?:[ ]?[0-9]{1,2})?$");

    }

    @Test
    public void testInvalidMaxLength() {

        // given
        ClassInvalidMaxLength classInvalidMaxLength = new ClassInvalidMaxLength();

        // when
        Set<ConstraintViolation<Object>> violations = validator.validate(classInvalidMaxLength);
        ValidationError validationError = ValidationError.of(violations.iterator().next());

        // then
        assertThat(validationError.getCode()).isEqualTo(ValidationErrorCode.MAXLENGTH.name());
        assertThat(validationError.getPath()).isEqualTo("#/longString");
        assertThat(validationError.getLimit()).isEqualTo(9);

    }

    @Test
    public void testInvalidMinLength() {

        // given
        ClassInvalidMinLength classInvalidMinLength = new ClassInvalidMinLength();

        // when
        Set<ConstraintViolation<Object>> violations = validator.validate(classInvalidMinLength);
        ValidationError validationError = ValidationError.of(violations.iterator().next());

        // then
        assertThat(validationError.getCode()).isEqualTo(ValidationErrorCode.MINLENGTH.name());
        assertThat(validationError.getPath()).isEqualTo("#/shortString");
        assertThat(validationError.getLimit()).isEqualTo(4);

    }

    @Test
    public void test_violationInList_path() {

        // given
        ClassInvalidElementInList invalidListClass = new ClassInvalidElementInList();

        // when
        Set<ConstraintViolation<Object>> violations = validator.validate(invalidListClass);
        ValidationError validationError = ValidationError.of(violations.iterator().next());

        // then
        assertThat(validationError.getPath()).isEqualTo("#/list[1]/wrapped");

    }

    private static class ClassInvalidMaxLength {

        @Max(9)
        private final String longString = "longString";

    }

    private static class ClassInvalidMinLength {

        @Min(4)
        private final String shortString = "short";

    }

    private static class ClassInvalidIban {

        @Pattern(regexp = "^[A-Z]{2}[0-9]{2}(?:[ ]?[0-9]{4}){4}(?!(?:[ ]?[0-9]){3})(?:[ ]?[0-9]{1,2})?$")
        private final String iban = "DEX0111122223333444455";

    }

    private static class ClassInvalidElementInList {

        @Valid
        List<ValidationWrapper> list = Arrays.asList(new ValidationWrapper("valid"),
                new ValidationWrapper(" "));

        @AllArgsConstructor
        private class ValidationWrapper {
            @NotBlank
            private String wrapped;
        }

    }

}
