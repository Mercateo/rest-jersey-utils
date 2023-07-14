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
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Priority(Priorities.HEADER_DECORATOR)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class AccessControlAllowOriginResponseFilter implements ContainerResponseFilter {
	private static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_FIELD = "Access-Control-Allow-Origin";

	private static final String ORIGIN_HEADER_FIELD = "Origin";

	@NonNull
	private OriginFilter originFilter;

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext containerResponseContext)
			throws IOException {
		String origin = request.getHeaderString(ORIGIN_HEADER_FIELD);

		if (origin != null) {
			if (originFilter.isOriginAllowed(origin)) {
				containerResponseContext.getHeaders().add(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER_FIELD, origin);
			}
		}
	}

}
