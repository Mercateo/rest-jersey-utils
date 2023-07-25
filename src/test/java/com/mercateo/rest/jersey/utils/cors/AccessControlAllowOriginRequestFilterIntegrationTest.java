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
package com.mercateo.rest.jersey.utils.cors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.util.JacksonFeature;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;

public class AccessControlAllowOriginRequestFilterIntegrationTest extends JerseyTest {
	private static boolean requestGoneThrough = false;

	@Path("/")
	public static final class TestResource {
		@GET
		public void TestException() {
			requestGoneThrough = true;
		}
	}

	private OriginFilter originFilter;

	@Override
	protected Application configure() {
		ResourceConfig rs = new ResourceConfig(TestResource.class, JacksonFeature.class);
		originFilter = mock(OriginFilter.class);
		rs.register(new AccessControlAllowOriginRequestFilter(originFilter));
		return rs;
	}

	@Test
	public void getWithoutOriginSet() {
		requestGoneThrough = false;
		Response resp = target("/").request().get();
		assertTrue(requestGoneThrough);
		verifyNoMoreInteractions(originFilter);
		assertEquals(204, resp.getStatus());
	}

	@Test
	public void getWithoutFalseOriginSet() {
		when(originFilter.isOriginAllowed(anyString())).thenReturn(false);
		requestGoneThrough = false;
		Response resp = target("/").request().header("Origin", "http://localhost:8080").get();
		assertFalse(requestGoneThrough);
		assertEquals(400, resp.getStatus());
	}

	@Test
	public void getWithoutRightOriginSet() {
		when(originFilter.isOriginAllowed(anyString())).thenReturn(true);
		requestGoneThrough = false;
		Response resp = target("/").request().header("Origin", "http://localhost:8080").get();
		assertTrue(requestGoneThrough);
		assertEquals(204, resp.getStatus());
	}
}
