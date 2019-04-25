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
package com.mercateo.rest.jersey.utils.validation;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

import lombok.AllArgsConstructor;
import lombok.val;

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
        assertThat(violations.iterator().next().getMessage()).isEqualTo("INVALID");
    }


    @Test
    public void testEmptyStringWithSpaces(){
        ClassWithString classToBeValidated = new ClassWithString("   ");
        val violations = validator.validate(classToBeValidated);

        assertThat(violations).size().isGreaterThan(0);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("INVALID");
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
