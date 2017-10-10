package com.mercateo.rest.jersey.utils.cors;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class SimpleAccessControlAllowHeaderFilterIntegrationTest extends JerseyTest {
	@Path("/")
	public static final class TestResource {
		@GET
		public void TestException() {

		}
	}

	@Override
	protected Application configure() {
		ResourceConfig rs = new ResourceConfig(TestResource.class, JacksonFeature.class);
		rs.register(new SimpleAccessControlAllowHeaderFilter());
		return rs;
	}

	@Test
	public void getTest() {

		MultivaluedMap<String, Object> headers = target("/").request().get().getHeaders();

		assertEquals("orgin, content-type, accept, authorization", headers.get("Access-Control-Allow-Headers").get(0));
		assertEquals("true", headers.get("Access-Control-Allow-Credentials").get(0));
		assertEquals("GET, POST, PUT, DELETE, OPTIONS", headers.get("Access-Control-Allow-Methods").get(0));

	}
}
