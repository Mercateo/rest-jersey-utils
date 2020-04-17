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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import jersey.repackaged.com.google.common.collect.Lists;
import lombok.SneakyThrows;

public class CorsFeatureIntegrationTest extends JerseyTest {
	private static boolean requestGoneThrough = false;

	@Path("/")
	public static final class TestResource {
		@GET
		public void TestException() {
			requestGoneThrough = true;
		}
	}

	@Override
	@SneakyThrows
	protected Application configure() {
		ResourceConfig rs = new ResourceConfig(TestResource.class, JacksonFeature.class);

		CORSFeature corsFeature = new CORSFeature(new OriginFilter.Default(
				Lists.newArrayList(new URL("http://localhost:8080")), Lists.newArrayList("0.0.0.0")));
		rs.register(corsFeature);
		return rs;
	}

	@Test
	public void getWithoutOriginSet() {
		requestGoneThrough = false;
		Response resp = target("/").request().get();
		assertTrue(requestGoneThrough);
		assertEquals(204, resp.getStatus());

		MultivaluedMap<String, Object> headers = resp.getHeaders();

		assertEquals("origin, content-type, accept, authorization", headers.get("Access-Control-Allow-Headers").get(0));
		assertEquals("true", headers.get("Access-Control-Allow-Credentials").get(0));
		assertEquals("GET, POST, PUT, DELETE, OPTIONS", headers.get("Access-Control-Allow-Methods").get(0));
	}

	@Test
	public void getWithoutFalseOriginSet() {
		requestGoneThrough = false;
		Response resp = target("/").request().header("Origin", "http://localhost:8081").get();
		assertFalse(requestGoneThrough);
		assertEquals(400, resp.getStatus());
	}

	@Test
	public void getWithoutRightOriginSet() {

		requestGoneThrough = false;
		Response resp = target("/").request().header("Origin", "http://localhost:8080").get();
		assertTrue(requestGoneThrough);
		assertEquals(204, resp.getStatus());

		MultivaluedMap<String, Object> headers = resp.getHeaders();

		assertEquals("origin, content-type, accept, authorization", headers.get("Access-Control-Allow-Headers").get(0));
		assertEquals("true", headers.get("Access-Control-Allow-Credentials").get(0));
		assertEquals("GET, POST, PUT, DELETE, OPTIONS", headers.get("Access-Control-Allow-Methods").get(0));
	}
}
