package com.mercateo.rest.jersey.utils.cors;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class OptionsRequestFilterIntegrationTest extends JerseyTest {
    @Path("/")
    public static final class TestResource {
        @GET
        public void TestException() {

        }
    }

    @Override
    protected Application configure() {
        ResourceConfig rs = new ResourceConfig(TestResource.class, JacksonFeature.class);

        rs.register(new ContainerRequestFilter() {

            @Override
            public void filter(ContainerRequestContext requestContext) throws IOException {
                // this simulates an authorization filter, that denies access to
                // the TestResource
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
        });

        rs.register(new OptionsRequestFilter());
        return rs;
    }

    @Test
    public void optionsOnTest() {
        // GET should not be allowed
        int statusGet = target("/").request().get().getStatus();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), statusGet);

        // OPTIONS should be allowed
        int statusOptions = target("/").request().options().getStatus();
        assertEquals(Response.Status.OK.getStatusCode(), statusOptions);
    }
}
