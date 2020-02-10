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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Provider
public class RFCExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(@NonNull Throwable exception) {
		ResponseBuilder builder;
		StatusType statusInfo;

		private final String accessDeniedExceptionClassName = "org.springframework.security.access.AccessDeniedException";

		final String exceptionMessage;

		if (exception instanceof WebApplicationException) {
			Response response = ((WebApplicationException) exception).getResponse();
			builder = Response.fromResponse(response);
			statusInfo = response.getStatusInfo();
			exceptionMessage = exception.getMessage();
		} else if (exception.getClass().getName().contentEquals(accessDeniedExceptionClassName)) {
			statusInfo = Status.FORBIDDEN;
			exceptionMessage = exception.getMessage();
			builder = Response.status(statusInfo);
		} else {
			builder = Response.serverError();
			statusInfo = Status.INTERNAL_SERVER_ERROR;
			exceptionMessage = null;
		}

		SimpleExceptionJson simpleExceptionJson = new SimpleExceptionJson(statusInfo.getReasonPhrase(),
				statusInfo.getStatusCode(), exceptionMessage);
		builder.entity(simpleExceptionJson);
		builder.type("application/problem+json");

		if (statusInfo.getFamily() == Family.CLIENT_ERROR) {
			log.debug("Got client Exception", exception);
		} else {
			log.error("Sending error to client", exception);
		}

		return builder.build();
	}

}
