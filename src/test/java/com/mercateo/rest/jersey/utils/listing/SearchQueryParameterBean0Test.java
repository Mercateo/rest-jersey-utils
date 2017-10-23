package com.mercateo.rest.jersey.utils.listing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SearchQueryParameterBean0Test {
	@Test
	public void useCaseDefaultValue() {
		// no setting of default value needed
		SearchQueryParameterBean searchQueryParameterBean = new SearchQueryParameterBean(1, 20);
		searchQueryParameterBean.setDefaultLimit(200);
		assertEquals(20, searchQueryParameterBean.getLimit());
		assertEquals(1, searchQueryParameterBean.getOffset());

		// setting of default needed
		SearchQueryParameterBean searchQueryParameterBean2 = new SearchQueryParameterBean();
		searchQueryParameterBean2.setDefaultLimit(200);
		assertEquals(200, searchQueryParameterBean2.getLimit());
		assertEquals(0, searchQueryParameterBean2.getOffset());
	}

}
