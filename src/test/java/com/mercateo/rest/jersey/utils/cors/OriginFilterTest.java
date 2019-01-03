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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import jersey.repackaged.com.google.common.collect.Lists;

public class OriginFilterTest {

	@Test
	public void testIsOriginAllowedString() throws Exception {

		OriginFilter originFilter = new OriginFilter.Default(Lists.newArrayList(new URL("http://localhost:8080")),
				Lists.newArrayList("0.0.0.0"));
		assertTrue(originFilter.isOriginAllowed("http://localhost:8080"));
		assertTrue(originFilter.isOriginAllowed("http://0.0.0.0:8080"));

		assertFalse(originFilter.isOriginAllowed("http://localhost:8081"));
		assertFalse(originFilter.isOriginAllowed("this is no URL"));
	}

	@Test
	public void testIsOriginAllowedURL() throws Exception {
		OriginFilter originFilter = new OriginFilter.Default(Lists.newArrayList(new URL("http://localhost:8080")),
				Lists.newArrayList("0.0.0.0"));
		assertTrue(originFilter.isOriginAllowed(new URL("http://localhost:8080")));
		assertTrue(originFilter.isOriginAllowed(new URL("http://0.0.0.0:8080")));

		assertFalse(originFilter.isOriginAllowed(new URL("http://localhost:8081")));

	}

}
