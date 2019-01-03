/*
 * Copyright © 2017 Mercateo AG (http://www.mercateo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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