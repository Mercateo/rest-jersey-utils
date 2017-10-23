package com.mercateo.rest.jersey.utils.cors;

import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.collect.Lists;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Delegate;

public class OriginFilterForFrontend implements OriginFilter {

    @Delegate
    private final OriginFilter.Default defaultOriginFilter;

    @SneakyThrows(MalformedURLException.class)
    public OriginFilterForFrontend(@NonNull String frontendDomain) {
        defaultOriginFilter = new OriginFilter.Default(Lists.newArrayList(new URL("https://www."
                + frontendDomain), new URL("https://" + frontendDomain)), Lists.newArrayList(
                        "localhost", "0.0.0.0", "127.0.0.1"));
    }

}
