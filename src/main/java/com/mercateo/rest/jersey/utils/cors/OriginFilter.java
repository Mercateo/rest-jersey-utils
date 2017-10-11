package com.mercateo.rest.jersey.utils.cors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public interface OriginFilter {

	public default boolean isOriginAllowed(@NonNull String originHeaderField) {
		try {
			URL originURL = new URL(originHeaderField);
			return isOriginAllowed(originURL);
		} catch (MalformedURLException e) {
			return false;
		}
	}

	public boolean isOriginAllowed(URL origin);

	@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
	@EqualsAndHashCode
	public static class Default implements OriginFilter {
		@NonNull
		private final List<URL> allowedOrigins;

		@NonNull
		private final List<String> allowedOriginHosts;

		public boolean isOriginAllowed(@NonNull URL origin) {
			return allowedOrigins.contains(origin) || allowedOriginHosts.contains(origin.getHost());
		}

	}
}