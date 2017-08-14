package com.mercateo.rest.jersey.utils.exception;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

public class RFCExceptionMapperIntegrationTest extends JerseyTest {
	@Path("/")
	public static final class TestResource {
		@GET
		public void TestException() {
			throw new BadRequestException("hello");
		}
	}

	@Data
	@NoArgsConstructor
	// this is here, because of default constructor needed for deserialization
	public static class SimpleExceptionJson {
		@NonNull
		String title;
		int status;
		String detail;
	}

	@Override
	protected Application configure() {
		return new ResourceConfig(TestResource.class, RFCExceptionMapper.class, JacksonFeature.class);
	}

	@Test
	public void exceptionTest() {
		try {
			target("/").request().get(String.class);
		} catch (BadRequestException e) {
			SimpleExceptionJson se = e.getResponse().readEntity(SimpleExceptionJson.class);
			assertEquals("Bad Request", se.getTitle());
			assertEquals("hello", se.getDetail());
			return;
		}
		Assert.fail();
	}

}
