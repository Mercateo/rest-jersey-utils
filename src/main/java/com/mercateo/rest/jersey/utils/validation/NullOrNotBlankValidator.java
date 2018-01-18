package com.mercateo.rest.jersey.utils.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {

    public void initialize(NullOrNotBlank parameters) { }

    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return (value == null) ? true : (hasValidLength(value)) ? true : false;
    }

    private boolean hasValidLength(String value){
        boolean isAllWhitespace = value.matches("^\\s*$");
        return !isAllWhitespace && (value.length() > 0);
    }
}
