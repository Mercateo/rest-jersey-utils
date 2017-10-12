package com.mercateo.rest.jersey.utils.cors;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import lombok.NonNull;

public class CORSFeature implements Feature {

	private final List<String> allowedHeaders;
	private final OriginFilter originFilter;

	public CORSFeature(@NonNull List<String> allowedHeaders, @NonNull OriginFilter originFilter) {
		this.originFilter = originFilter;
		this.allowedHeaders = allowedHeaders;
	}

	public CORSFeature(@NonNull OriginFilter originFilter) {
		this(new ArrayList<>(), originFilter);
	}

	@Override
	public boolean configure(FeatureContext context) {
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
