package com.mercateo.rest.jersey.utils.exception;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.core.Response;

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

}
