package com.mercateo.rest.jersey.utils.cors;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response.Status.Family;

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
			} else if (containerResponseContext.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
				throw new BadRequestException("The origin ist not set accordingly");
			}
		}

	}

}
