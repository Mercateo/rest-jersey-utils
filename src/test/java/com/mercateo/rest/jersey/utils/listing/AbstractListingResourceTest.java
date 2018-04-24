package com.mercateo.rest.jersey.utils.listing;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import lombok.NonNull;

public class AbstractListingResourceTest {

    public static class TestJson implements IdProvider<String> {
        @Override
        public String getId() {
            return null;
        }
    }

    public static class TestResource extends
            AbstractListingResource<TestJson, TestJson, SearchQueryParameterBean, TestResource> {

        public TestResource() {
            super(TestResource.class);
        }

        @Override
        protected ListingResult<TestJson> getSummaryListing(
                SearchQueryParameterBean searchQueryParameterBean) {
            return null;
        }

        @Override
        protected TestJson getSummaryForId(@NonNull String id) {
            return null;
        }

        @Override
        protected TestJson getForId(@NonNull String id) {
            return null;
        }
    }

    @Test
    public void testConstructor_passesCorrectConverter() {
        // given
        TestResource testResource = new TestResource();

        // when
        String convertedId = testResource.tryToConvertId("something");

        // then
        Assertions.assertThat(convertedId).isEqualTo("something");

    }
}