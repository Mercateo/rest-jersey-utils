package com.mercateo.rest.jersey.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValueValidator implements ConstraintValidator<EnumValue, CharSequence> {

    private Class targetEnum;

    @Override
    public void initialize(EnumValue enumValue) {
        this.targetEnum = enumValue.targetEnum();
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        List<Object> values =  Arrays.stream(targetEnum.getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.toList());

        return values.contains(value);
    }
}