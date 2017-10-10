package com.mercateo.rest.jersey.utils.cors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import jersey.repackaged.com.google.common.collect.Lists;

public class OriginFilterTest {

	@Test
	public void testIsOriginAllowedString() throws Exception {

		OriginFilter originFilter = new OriginFilter(Lists.newArrayList(new URL("http://localhost:8080")),
				Lists.newArrayList("0.0.0.0"));
		assertTrue(originFilter.isOriginAllowed("http://localhost:8080"));
		assertTrue(originFilter.isOriginAllowed("http://0.0.0.0:8080"));

		assertFalse(originFilter.isOriginAllowed("http://localhost:8081"));
		assertFalse(originFilter.isOriginAllowed("this is no URL"));
	}

	@Test
	public void testIsOriginAllowedURL() throws Exception {
		OriginFilter originFilter = new OriginFilter(Lists.newArrayList(new URL("http://localhost:8080")),
				Lists.newArrayList("0.0.0.0"));
		assertTrue(originFilter.isOriginAllowed(new URL("http://localhost:8080")));
		assertTrue(originFilter.isOriginAllowed(new URL("http://0.0.0.0:8080")));

		assertFalse(originFilter.isOriginAllowed(new URL("http://localhost:8081")));

	}

}
