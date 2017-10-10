package com.mercateo.rest.jersey.utils.cors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Priority(Priorities.HEADER_DECORATOR)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
/*
 * This filter simply adds static header for CORS
 */
public class SimpleAccessControlAllowHeaderFilter implements ContainerResponseFilter {

	private static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
	@NonNull
	private final List<String> customHeaders;

	SimpleAccessControlAllowHeaderFilter() {
		this(new ArrayList<>());
	}

	@Override
	public void filter(ContainerRequestContext containerRequestContext,
			ContainerResponseContext containerResponseContext) throws IOException {

		MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();
		headers.add(ACCESS_CONTROL_ALLOW_HEADERS, "orgin");
		headers.add(ACCESS_CONTROL_ALLOW_HEADERS, "content-type");
		headers.add(ACCESS_CONTROL_ALLOW_HEADERS, "accept");
		headers.add(ACCESS_CONTROL_ALLOW_HEADERS, "authorization");

		customHeaders.forEach(s -> headers.add(ACCESS_CONTROL_ALLOW_HEADERS, s));

		// make sure there's only one header with this name
		String headerString = (String) headers.get(ACCESS_CONTROL_ALLOW_HEADERS).stream()
				.reduce((a, b) -> a.toString() + ", " + b.toString()).get();
		headers.putSingle(ACCESS_CONTROL_ALLOW_HEADERS, headerString);

		headers.putSingle("Access-Control-Allow-Credentials", "true");
		headers.putSingle("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
	}
}
