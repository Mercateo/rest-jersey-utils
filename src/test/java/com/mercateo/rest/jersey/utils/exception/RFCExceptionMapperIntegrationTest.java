package com.mercateo.rest.jersey.utils.exception;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
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

	    @GET
	    @Path("/throwable")
	    public void TestThrowable() {
	        throw new OutOfMemoryError("crashed");
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

    @Test
    public void throwableTest() {
        try {
            target("/throwable").request().get(String.class);
        } catch (InternalServerErrorException e) {
            SimpleExceptionJson se = e.getResponse().readEntity(SimpleExceptionJson.class);
            assertEquals("Internal Server Error", se.getTitle());
            assertEquals(500, se.getStatus());
            assertNull(se.getDetail());
            return;
        }
        Assert.fail();
    }

}
