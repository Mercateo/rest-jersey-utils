package com.mercateo.rest.jersey.utils.cors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import lombok.NonNull;

public class CORSFeature implements Feature {

	private List<String> allowedHeaders;
	private List<String> allowedOriginHosts;
	private List<URL> allowedOrigins;

	public CORSFeature(@NonNull List<String> allowedHeaders, @NonNull List<URL> allowedOrigins,
			@NonNull List<String> allowedOriginHosts) {
		this.allowedOrigins = allowedOrigins;
		this.allowedOriginHosts = allowedOriginHosts;
		this.allowedHeaders = allowedHeaders;
	}

	public CORSFeature(@NonNull List<URL> allowedOrigins, @NonNull List<String> allowedOriginHosts) {
		this(new ArrayList<>(), allowedOrigins, allowedOriginHosts);
	}

	@Override
	public boolean configure(FeatureContext context) {
		OriginFilter originFilter = new OriginFilter(allowedOrigins, allowedOriginHosts);
		boolean modified = false;
		AccessControlAllowOriginRequestFilter accessControlAllowOriginRequestFilter = new AccessControlAllowOriginRequestFilter(
				originFilter);
		if (!context.getConfiguration().isRegistered(accessControlAllowOriginRequestFilter)) {
			context.register(accessControlAllowOriginRequestFilter);
			modified = true;
		}

		AccessControlAllowOriginResponseFilter accessControlAllowOriginResponseFilter = new AccessControlAllowOriginResponseFilter(
				originFilter);
		if (!context.getConfiguration().isRegistered(accessControlAllowOriginResponseFilter)) {
			context.register(accessControlAllowOriginResponseFilter);
			modified = true;
		}

		SimpleAccessControlAllowHeaderFilter simpleAccessControlAllowHeaderFilter = new SimpleAccessControlAllowHeaderFilter(
				allowedHeaders);
		if (!context.getConfiguration().isRegistered(simpleAccessControlAllowHeaderFilter)) {
			context.register(simpleAccessControlAllowHeaderFilter);
			modified = true;
		}
		return modified;
	}

}
