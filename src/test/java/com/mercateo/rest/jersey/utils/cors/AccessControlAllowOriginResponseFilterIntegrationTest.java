package com.mercateo.rest.jersey.utils.cors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class AccessControlAllowOriginResponseFilterIntegrationTest extends JerseyTest {

	@Path("/")
	public static final class TestResource {
		@GET
		public void TestException() {

		}
	}

	private OriginFilter originFilter;

	@Override
	protected Application configure() {
		ResourceConfig rs = new ResourceConfig(TestResource.class, JacksonFeature.class);
		originFilter = mock(OriginFilter.class);
		rs.register(new AccessControlAllowOriginResponseFilter(originFilter));
		return rs;
	}

	@Test
	public void getWithoutOriginSet() {
		Response resp = target("/").request().get();
		verifyNoMoreInteractions(originFilter);
		assertEquals(204, resp.getStatus());
		assertNull(resp.getHeaders().get("Access-Control-Allow-Origin"));
	}

	@Test
	public void getWithoutFalseOriginSet() {
		when(originFilter.isOriginAllowed(anyString())).thenReturn(false);
		Response resp = target("/").request().header("Origin", "http://localhost:8080").get();
		assertEquals(400, resp.getStatus());
		assertNull(resp.getHeaders().get("Access-Control-Allow-Origin"));
	}

	@Test
	public void getWithoutRightOriginSet() {
		when(originFilter.isOriginAllowed(anyString())).thenReturn(true);
		Response resp = target("/").request().header("Origin", "http://localhost:8080").get();
		assertEquals(204, resp.getStatus());
		assertEquals("http://localhost:8080", resp.getHeaders().get("Access-Control-Allow-Origin").get(0));
	}
}
