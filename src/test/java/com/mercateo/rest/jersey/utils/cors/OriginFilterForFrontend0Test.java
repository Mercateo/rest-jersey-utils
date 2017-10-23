package com.mercateo.rest.jersey.utils.cors;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
@SuppressWarnings("boxing")
public class OriginFilterForFrontend0Test {

	@DataProvider
	public static Object[][] data() {
		return new Object[][] {
				// @formatter:off
				{ "stage03.frontend.domain", "http://localhost:8080", true },
				{ "stage03.frontend.domain", "http://localhost:8081", true },
				{ "stage03.frontend.domain", "http://0.0.0.0:8080", true },
				{ "stage03.frontend.domain", "http://127.0.0.1:8080", true },
				{ "stage03.frontend.domain", "https://integration.frontend.domain", false },
				{ "stage03.frontend.domain", "https://optimus.stage03.frontend.domain", false },
				{ "stage03.frontend.domain", "https://optimus.frontend.domain", false },
				{ "stage03.frontend.domain", "https://stage03frontend.domain", false },
				{ "stage03.frontend.domain", "https://stage03.frontend.domain", true },
				{ "stage03.frontend.domain", "https://frontend.cn", false },
				{ "stage03.frontend.domain", "https://frontend.domain", false },
				{ "stage03.frontend.domain", "https://www.integration.frontend.domain", false },
				{ "stage03.frontend.domain", "https://www.stage03.frontend.domain", true },
				{ "stage03.frontend.domain", "https://www.frontend.cn", false },
				{ "stage03.frontend.domain", "https://www.frontend.domain", false },
				{ "stage03.frontend.domain", "https://www.frontend.stage03.domain", false },

				{ "integration.frontend.domain", "http://localhost:8080", true },
				{ "integration.frontend.domain", "http://localhost:8081", true },
				{ "integration.frontend.domain", "http://0.0.0.0:8080", true },
				{ "integration.frontend.domain", "http://127.0.0.1:8080", true },
				{ "integration.frontend.domain", "https://integration.frontend.domain", true },
				{ "integration.frontend.domain", "https://optimus.stage03.frontend.domain", false },
				{ "integration.frontend.domain", "https://optimus.frontend.domain", false },
				{ "integration.frontend.domain", "https://stage03frontend.domain", false },
				{ "integration.frontend.domain", "https://stage03.frontend.domain", false },
				{ "integration.frontend.domain", "https://frontend.cn", false },
				{ "integration.frontend.domain", "https://frontend.domain", false },
				{ "integration.frontend.domain", "https://www.integration.frontend.domain", true },
				{ "integration.frontend.domain", "https://www.stage03.frontend.domain", false },
				{ "integration.frontend.domain", "https://www.frontend.cn", false },
				{ "integration.frontend.domain", "https://www.frontend.domain", false },
				{ "integration.frontend.domain", "https://www.frontend.stage03.domain", false },

				{ "frontend.domain", "http://localhost:8080", true },
				{ "frontend.domain", "http://localhost:8081", true },
				{ "frontend.domain", "http://0.0.0.0:8080", true },
				{ "frontend.domain", "http://127.0.0.1:8080", true },
				{ "frontend.domain", "https://integration.frontend.domain", false },
				{ "frontend.domain", "https://optimus.stage03.frontend.domain", false },
				{ "frontend.domain", "https://optimus.frontend.domain", false },
				{ "frontend.domain", "https://stage03frontend.domain", false },
				{ "frontend.domain", "https://stage03.frontend.domain", false },
				{ "frontend.domain", "https://frontend.cn", false },
				{ "frontend.domain", "https://frontend.domain", true },
				{ "frontend.domain", "https://www.integration.frontend.domain", false },
				{ "frontend.domain", "https://www.stage03.frontend.domain", false },
				{ "frontend.domain", "https://www.frontend.cn", false },
				{ "frontend.domain", "https://www.frontend.domain", true },
				{ "frontend.domain", "https://www.frontend.stage03.domain", false },
				// @formatter:on
		};
	}

	@Test
	@UseDataProvider("data")
	public void testIsOriginAllowed(String domain, String origin, boolean expected) {

		OriginFilterForFrontend uut = new OriginFilterForFrontend(domain);

		boolean originAllowed = uut.isOriginAllowed(origin);

		assertEquals(expected, originAllowed);

	}

	public static void main(String[] args) throws MalformedURLException {
		System.out.println(new URL("https://optimus.stage03.frontend.domain")
				.equals(new URL("https://www.stage03.frontend.domain")));
	}
}
