package com.mercateo.rest.jersey.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {

    Class<?> targetEnum();

    Class<?>[] groups() default {};

    String message() default "ENUM";

    Class<? extends Payload>[] payload() default {};


}

