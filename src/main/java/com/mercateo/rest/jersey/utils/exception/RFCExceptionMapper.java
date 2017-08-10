package com.mercateo.rest.jersey.utils.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RFCExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(@NonNull Exception exception) {
		ResponseBuilder builder;
		StatusType statusInfo;
		if (exception instanceof WebApplicationException) {
			Response response = ((WebApplicationException) exception).getResponse();
			builder = Response.fromResponse(response);
			statusInfo = response.getStatusInfo();
		} else {
			builder = Response.serverError();
			statusInfo = Status.INTERNAL_SERVER_ERROR;
		}

		SimpleExceptionJson simpleExceptionJson = new SimpleExceptionJson(statusInfo.getReasonPhrase(),
				statusInfo.getStatusCode(), exception.getMessage());
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
