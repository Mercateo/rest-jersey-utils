package com.mercateo.rest.jersey.utils.listing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

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
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.link.injection.LinkFactoryResourceConfig;

import lombok.Data;
import lombok.NonNull;

public class AbstractListingResourceTest extends JerseyTest {
	@JsonInclude(Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	@Data
	public static class TestJson implements StringIdProvider {
		public TestJson(@JsonProperty("id") String id) {
			super();
			this.id = id;
		}

		private String id;

	}

	@Path("test")
	public static class TestResource
			extends AbstractListingResource<TestJson, TestJson, SearchQueryParameterBean, TestResource> {

		public TestResource() {
			super(TestResource.class);
		}

		@Override
		protected ListingResult<TestJson> getSummaryListing(SearchQueryParameterBean searchQueryParameterBean) {
			return new ListingResult<>(Arrays.asList(new TestJson("1")), 1);

		}

		@Override
		protected TestJson getSummaryForId(@NonNull String id) {
			return new TestJson("2");
		}

		@Override
		protected TestJson getForId(@NonNull String id) {
			return new TestJson("3");
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
		TestJson testJson = target("test/2/summary").request().get(TestJson.class);
		assertEquals("2", testJson.getId());
	}

	@Test
	public void testGet() {
		TestJson testJson = target("test/3").request().get(TestJson.class);
		assertEquals("3", testJson.getId());
	}

	@Test
	public void testGetListing() {
		JsonNode testJson = target("test").request().get(JsonNode.class);
		assertEquals("1", testJson.get("members").get(0).get("id").asText());
		assertEquals("test/1/summary",
				testJson.get("members").get(0).get("_schema").get("links").get(0).get("href").asText());
		assertEquals("test/1", testJson.get("members").get(0).get("_schema").get("links").get(1).get("href").asText());
		JsonNode instanceLinkLdo = testJson.get("_schema").get("links").get(0);
		assertEquals("instance", instanceLinkLdo.get("rel").asText());
		assertTrue(instanceLinkLdo.get("href").asText().contains("{id}"));
	}
}
