package com.mercateo.rest.jersey.utils.cors;

import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import lombok.EqualsAndHashCode;

@Provider
@PreMatching
@EqualsAndHashCode
/**
 * This filter allows OPTIONS-requests ignoring any other request filters.
 */
public class OptionsRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        // allow all OPTIONS-requests independent of the requested resource
        Request request = context.getRequest();
        if (request != null && HttpMethod.OPTIONS.equals(request.getMethod())) {

            // send a "200 - OK" back to the browser
            context.abortWith(Response.ok().build());
        }
    }
}