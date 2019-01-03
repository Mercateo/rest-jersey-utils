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
