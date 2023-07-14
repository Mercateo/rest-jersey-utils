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

import java.io.IOException;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Priority(Priorities.HEADER_DECORATOR)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
/*
 * This filter simply adds static cache header for CORS
 */
public class AccessControlMaxAgeHeaderFilter implements ContainerResponseFilter {

	private static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

	private long numberOfSeconds;

	@Override
	public void filter(ContainerRequestContext containerRequestContext,
			ContainerResponseContext containerResponseContext) throws IOException {
		if (numberOfSeconds > 0) {
			MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();
			headers.putSingle(ACCESS_CONTROL_MAX_AGE, Long.toString(numberOfSeconds));
		}
	}
}
