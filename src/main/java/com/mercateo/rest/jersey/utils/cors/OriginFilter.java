package com.mercateo.rest.jersey.utils.cors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.common.annotations.VisibleForTesting;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
public class OriginFilter {
	@NonNull
	private final List<URL> allowedOrigins;

	@NonNull
	private final List<String> allowedOriginHosts;

	@VisibleForTesting
	public OriginFilter() {
		allowedOriginHosts = new ArrayList<>();
		allowedOrigins = new ArrayList<>();
	}

	public boolean isOriginAllowed(@NonNull String originHeaderField) {
		try {
			URL originURL = new URL(originHeaderField);
			return isOriginAllowed(originURL);
		} catch (MalformedURLException e) {
			return false;
		}
	}

	public boolean isOriginAllowed(@NonNull URL origin) {
		return allowedOrigins.contains(origin) || allowedOriginHosts.contains(origin.getHost());
	}

}
