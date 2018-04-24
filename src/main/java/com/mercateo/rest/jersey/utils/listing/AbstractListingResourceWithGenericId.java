package com.mercateo.rest.jersey.utils.listing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;

import com.google.common.collect.Lists;
import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.JsonHyperSchema;
import com.mercateo.common.rest.schemagen.PaginationLinkBuilder;
import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.link.relation.Rel;
import com.mercateo.common.rest.schemagen.link.relation.RelType;
import com.mercateo.common.rest.schemagen.link.relation.Relation;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.PaginatedResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

public abstract class AbstractListingResourceWithGenericId<SummaryJsonType extends IdProvider<IdType>, FullJsonType extends IdProvider<IdType>, SearchQueryBean extends SearchQueryParameterBean, ImplementationType extends AbstractListingResourceWithGenericId<SummaryJsonType, FullJsonType, SearchQueryBean, ImplementationType, IdType>, IdType>
		implements JerseyResource {

	private static final Relation REL_INSTANCE = Relation.of("instance", RelType.OTHER);

	@Value
	public static class ListingResult<ResultType> {
		List<ResultType> resultList;
		int totalNumberOfHits;
	}

	private Class<ImplementationType> implementationClass;

	private Function<String, IdType> idConverter;

	@Inject
	@Getter(AccessLevel.PROTECTED)
	private LinkMetaFactory linkMetaFactory;

	protected AbstractListingResourceWithGenericId(@NonNull Class<ImplementationType> implementationClass, Function<String, IdType> idConverter) {
		this.implementationClass = implementationClass;
		this.idConverter = idConverter;
	}

	protected abstract ListingResult<SummaryJsonType> getSummaryListing(SearchQueryBean searchQueryBean);

	protected abstract SummaryJsonType getSummaryForId(IdType id);

	protected abstract FullJsonType getForId(IdType id);

	@SuppressWarnings("boxing")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public PaginatedResponse<SummaryJsonType> getListing(@BeanParam @NotNull @Valid SearchQueryBean searchQueryBean) {
		final ListingResult<SummaryJsonType> summaries = getSummaryListing(searchQueryBean);

		final LinkFactory<ImplementationType> lf = getImplementationLinkFactory();

		final List<ObjectWithSchema<SummaryJsonType>> listForResponse = summaries.getResultList().stream()
				.map(r -> getResponse(r, lf)).collect(Collectors.toList());

		List<Link> links = Lists.newArrayList();
		lf.forCall(REL_INSTANCE, r -> r.get(IdParameterBean.forTemplating())).ifPresent(links::add);
		int offset = searchQueryBean.getOffset();
		int limit = searchQueryBean.getLimit();
		PaginationLinkBuilder paginationLinkBuilder = PaginationLinkBuilder.of(summaries.getTotalNumberOfHits(),
				searchQueryBean.getOffset(), searchQueryBean.getLimit());
		links.addAll(paginationLinkBuilder.generateLinks((rel, off, lim) -> {
			searchQueryBean.setLimit(lim);
			searchQueryBean.setOffset(off);
			return lf.forCall(rel, r -> r.getListing(searchQueryBean));
		}));
		links.addAll(createAdditionalLinksForListing(searchQueryBean));

		return PaginatedResponse.create(listForResponse, summaries.getTotalNumberOfHits(), offset, limit,
				JsonHyperSchema.from(links));
	}

	protected List<Link> createAdditionalLinksForListing(SearchQueryBean searchQueryBean) {
		return Lists.newArrayList();
	}

	protected LinkFactory<ImplementationType> getImplementationLinkFactory() {
		return linkMetaFactory.createFactoryFor(implementationClass);
	}

	@GET
	@Path("{id}/summary")
	@Produces(MediaType.APPLICATION_JSON)
	public ObjectWithSchema<SummaryJsonType> getSummary(@NotNull @BeanParam @Valid IdParameterBean idParamBean) {
		IdType id = tryToConvertId(idParamBean.getId());
		final SummaryJsonType summaryJson = getSummaryForId(id);
		return getResponse(summaryJson, getImplementationLinkFactory());
	}

	protected ObjectWithSchema<SummaryJsonType> getResponse(SummaryJsonType summaryJson,
			LinkFactory<ImplementationType> factoryForImplementation) {
		final JsonHyperSchema jsonHyperSchema = JsonHyperSchema
				.fromOptional(createSchemaForSummary(summaryJson, factoryForImplementation));
		return ObjectWithSchema.create(summaryJson, jsonHyperSchema);
	}

	private List<Optional<Link>> createSchemaForSummary(SummaryJsonType summaryJson,
			LinkFactory<ImplementationType> factoryForImplementation) {
		final Optional<Link> selfLink = factoryForImplementation.forCall(Rel.SELF,
				r -> r.getSummary(IdParameterBean.of(summaryJson.getId())));
		final Optional<Link> canonicalLink = factoryForImplementation.forCall(Rel.CANONICAL,
				r -> r.get(IdParameterBean.of(summaryJson.getId())));

		final ArrayList<Optional<Link>> result = Lists.newArrayList(selfLink, canonicalLink);

		final List<Optional<Link>> additionalLinks = createAdditionalLinksForSummaryType(summaryJson,
				factoryForImplementation);
		result.addAll(additionalLinks);
		return result;
	}

	protected List<Optional<Link>> createAdditionalLinksForSummaryType(SummaryJsonType summaryJson,
			LinkFactory<ImplementationType> factoryForImplementation) {
		return new ArrayList<>();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ObjectWithSchema<FullJsonType> get(@NotNull @Valid @BeanParam IdParameterBean idParameterBean) {
		IdType id = tryToConvertId(idParameterBean.getId());
		final FullJsonType json = getForId(id);
		return getResponse(json);
	}

	protected ObjectWithSchema<FullJsonType> getResponse(FullJsonType json) {
		final LinkFactory<ImplementationType> factoryForImplementation = getImplementationLinkFactory();
		final Optional<Link> selfLink = factoryForImplementation.forCall(Rel.SELF,
				r -> r.get(IdParameterBean.of(json.getId())));

		final ArrayList<Optional<Link>> result = Lists.newArrayList(selfLink);

		final List<Optional<Link>> additionalLinks = createAdditionalLinksForFullType(json, factoryForImplementation);

		result.addAll(additionalLinks);

		final JsonHyperSchema hyperSchema = JsonHyperSchema.fromOptional(result);
		return ObjectWithSchema.create(json, hyperSchema);
	}

	protected List<Optional<Link>> createAdditionalLinksForFullType(FullJsonType json,
			LinkFactory<ImplementationType> factoryForImplementation) {
		return new ArrayList<Optional<Link>>();
	}

	protected IdType tryToConvertId(String id) {
		try {
			return idConverter.apply(id);
		} catch (Exception ex) {
			throw new BadRequestException(ex);
		}
	}
}