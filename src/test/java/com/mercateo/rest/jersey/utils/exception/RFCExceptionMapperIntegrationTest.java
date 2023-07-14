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
package com.mercateo.rest.jersey.utils.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
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
		fail();
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
		fail();
    }

}
