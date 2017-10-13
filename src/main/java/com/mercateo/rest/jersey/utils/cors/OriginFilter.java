package com.mercateo.rest.jersey.utils.cors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

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

	@EqualsAndHashCode
	public static class Default implements OriginFilter {
		@NonNull
		private final List<String> allowedOrigins;

		@NonNull
		private final List<String> allowedOriginHosts;

		@Override
		public boolean isOriginAllowed(@NonNull URL origin) {
			return allowedOrigins.contains(origin.toExternalForm()) || allowedOriginHosts.contains(origin.getHost());
		}

		public Default(@NonNull List<URL> allowedOrigins, @NonNull List<String> allowedOriginHosts) {
			this.allowedOrigins = allowedOrigins.stream().map(URL::toExternalForm).collect(Collectors.toList());
			this.allowedOriginHosts = allowedOriginHosts;
		}

	}
}