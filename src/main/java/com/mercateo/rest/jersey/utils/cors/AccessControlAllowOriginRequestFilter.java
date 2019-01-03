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

import javax.annotation.Priority;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Priority(Priorities.AUTHENTICATION - 10)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class AccessControlAllowOriginRequestFilter implements ContainerRequestFilter {

	private static final String ORIGIN_HEADER_FIELD = "Origin";

	@NonNull
	private OriginFilter originFilter;

	@Override
	public void filter(ContainerRequestContext request) throws IOException {
		String origin = request.getHeaderString(ORIGIN_HEADER_FIELD);

		if (origin != null) {
			if (!originFilter.isOriginAllowed(origin)) {
				throw new BadRequestException("The origin ist not set accordingly");
			}
		}

	}

}
