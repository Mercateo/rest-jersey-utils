package com.mercateo.rest.jersey.utils.validation;

import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class NullOrNotBlankValidatorTest {

    private NullOrNotBlankValidator uut = new NullOrNotBlankValidator();

    @Test
    public void testEmptyString(){
        val result = uut.isValid("", null);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void testNullValue(){
        val result = uut.isValid(null, null);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void testStringWithSpaces(){
        val result = uut.isValid("    ", null);
        Assertions.assertThat(result).isFalse();
    }

}
