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

