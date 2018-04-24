package com.mercateo.rest.jersey.utils.listing;

import java.util.function.Function;

public abstract class AbstractListingResource<SummaryJsonType extends IdProvider<String>, FullJsonType extends IdProvider<String>, SearchQueryBean extends SearchQueryParameterBean, ImplementationType extends AbstractListingResource<SummaryJsonType, FullJsonType, SearchQueryBean, ImplementationType>>
        extends AbstractListingResourceWithGenericId<SummaryJsonType, FullJsonType, SearchQueryBean, ImplementationType, String> {

    protected AbstractListingResource(Class<ImplementationType> implementationClass) {
        super(implementationClass, Function.identity());
    }
}