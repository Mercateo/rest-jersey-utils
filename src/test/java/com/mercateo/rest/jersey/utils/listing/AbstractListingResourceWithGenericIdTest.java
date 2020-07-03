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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.link.injection.LinkFactoryResourceConfig;

import lombok.Data;
import lombok.NonNull;

public class AbstractListingResourceWithGenericIdTest extends JerseyTest {

    @JsonInclude(Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class TestJson implements IdProvider<UUID> {
        public TestJson(@JsonProperty("id") UUID id) {
            super();
            this.id = id;
        }

        private UUID id;

    }

    private static final UUID UUID_1 = UUID.fromString("6948baff-bd52-4ec9-a2f2-5a229fc38bac");

    private static final UUID UUID_2 = UUID.fromString("ba9d4569-ebb9-4722-b3f9-ddd3716ea1f2");

    private static final UUID UUID_3 = UUID.fromString("b7dfdbe0-7c9a-4613-bb2e-ff21c996cc18");

    @Path("test")
    public static class TestResource
            extends
            AbstractListingResourceWithGenericId<TestJson, TestJson, SearchQueryParameterBean, TestResource, UUID> {

        public TestResource() {
            super(TestResource.class, UUID::fromString);
        }

        @Override
        protected ListingResult<TestJson> getSummaryListing(
                SearchQueryParameterBean searchQueryParameterBean) {
            return new ListingResult<>(Arrays.asList(new TestJson(UUID_1)), 1);

        }

        @Override
        protected TestJson getSummaryForId(@NonNull UUID id) {
            return new TestJson(UUID_2);
        }

        @Override
        protected TestJson getForId(@NonNull UUID id) {
            return new TestJson(UUID_3);
        }

    }

    @SuppressWarnings("null")
    @Mock
    private LinkMetaFactory linkMetaFactory;

    @Override
    protected Application configure() {

        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        MockitoAnnotations.initMocks(this);

        ResourceConfig rs = new ResourceConfig();
        rs.register(TestBinder.forAllMocksOf(this));
        rs.register(JacksonJaxbJsonProvider.class);
        rs.register(TestResource.class);
        rs.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, Boolean.TRUE);
        LinkFactoryResourceConfig.configure(rs);
        linkMetaFactory = LinkMetaFactory.createInsecureFactoryForTest();
        return rs;
    }

    @Test
    public void testSummary() {
        TestJson testJson = target("test/ba9d4569-ebb9-4722-b3f9-ddd3716ea1f2/summary").request()
                .get(TestJson.class);
        assertEquals(UUID_2, testJson.getId());
    }

    @Test
    public void testGet() {
        TestJson testJson = target("test/b7dfdbe0-7c9a-4613-bb2e-ff21c996cc18").request().get(
                TestJson.class);
        assertEquals(UUID_3, testJson.getId());
    }

    @Test(expected = BadRequestException.class)
    public void testSummary_invalidId() {
        TestJson testJson = target("test/notValidId/summary").request().get(TestJson.class);
        assertEquals(UUID_2, testJson.getId());
    }

    @Test(expected = BadRequestException.class)
    public void testGet_invalidId() {
        TestJson testJson = target("test/notValidId").request().get(TestJson.class);
        assertEquals(UUID_3, testJson.getId());
    }

    @Test
    public void testGetListing() {
        JsonNode testJson = target("test").request().get(JsonNode.class);
        assertEquals("6948baff-bd52-4ec9-a2f2-5a229fc38bac", testJson.get("members").get(0).get(
                "id").asText());
        assertEquals("test/6948baff-bd52-4ec9-a2f2-5a229fc38bac/summary",
                testJson.get("members").get(0).get("_schema").get("links").get(0).get("href")
                        .asText());
        assertEquals("test/6948baff-bd52-4ec9-a2f2-5a229fc38bac", testJson.get("members").get(0)
                .get("_schema").get("links").get(1).get("href").asText());
        JsonNode instanceLinkLdo = testJson.get("_schema").get("links").get(0);
        assertEquals("instance", instanceLinkLdo.get("rel").asText());
        assertTrue(instanceLinkLdo.get("href").asText().contains("{id}"));
    }
}
