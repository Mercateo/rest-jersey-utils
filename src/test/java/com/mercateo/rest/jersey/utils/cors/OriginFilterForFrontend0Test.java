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
