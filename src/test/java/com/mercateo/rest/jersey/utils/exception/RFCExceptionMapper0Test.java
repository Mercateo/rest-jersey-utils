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

import static org.junit.Assert.assertEquals;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Response;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.google.common.testing.NullPointerTester;

public class RFCExceptionMapper0Test {

	RFCExceptionMapper uut = new RFCExceptionMapper();

	@Test
	public void testNullContracts() throws Exception {
		NullPointerTester nullPointerTester = new NullPointerTester();
		Class<?> clazz = uut.getClass();

		nullPointerTester.testAllPublicConstructors(clazz);
		nullPointerTester.testAllPublicStaticMethods(clazz);

		nullPointerTester.testInstanceMethods(uut, NullPointerTester.Visibility.PROTECTED);
		nullPointerTester.testStaticMethods(clazz, NullPointerTester.Visibility.PROTECTED);
	}

	@Test
	public void testContentTypeHeader() throws Exception{
		Response r = uut.toResponse(new BadRequestException());
		Assertions.assertThat(r.getHeaderString("Content-Type")).isEqualTo("application/problem+json");
	}

	@Test
	public void testToResponseClient() throws Exception {
		Response r = uut.toResponse(new BadRequestException());
		assertEquals(400, r.getStatus());
		assertEquals(new SimpleExceptionJson("Bad Request", 400, "HTTP 400 Bad Request"), r.getEntity());
	}

	@Test
	public void testToResponseServer() throws Exception {
		Response r = uut.toResponse(new ServiceUnavailableException());
		assertEquals(503, r.getStatus());
		assertEquals(new SimpleExceptionJson("Service Unavailable", 503, "HTTP 503 Service Unavailable"),
				r.getEntity());
	}

	@Test
	public void testToResponseNull() throws Exception {
		Response r = uut.toResponse(new NullPointerException());
		assertEquals(500, r.getStatus());
		assertEquals(new SimpleExceptionJson("Internal Server Error", 500, null), r.getEntity());
	}

    @Test
    public void testToResponseWithThrowable() throws Exception {
        Response r = uut.toResponse(new OutOfMemoryError());
        assertEquals(500, r.getStatus());
        assertEquals(new SimpleExceptionJson("Internal Server Error", 500, null), r.getEntity());
    }

	@Test
	public void testToResponseNoExceptionMessageIfNotWebAppException() throws Exception {
		// an exception with some sensitive data :D
		Response r = uut.toResponse(new IllegalArgumentException("Password x3$5oz#DD8 too short!"));
		assertEquals(500, r.getStatus());
		// detail should not contain exception message, but be null
		assertEquals(new SimpleExceptionJson("Internal Server Error", 500, null), r.getEntity());
	}

}
