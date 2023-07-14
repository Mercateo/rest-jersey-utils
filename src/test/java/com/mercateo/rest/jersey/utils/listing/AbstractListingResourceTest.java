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
package com.mercateo.rest.jersey.utils.listing;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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