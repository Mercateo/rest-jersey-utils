package com.mercateo.rest.jersey.utils.listing;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class SearchQueryParameterBean {
	@Getter
	@Setter
	private int defaultLimit = 20;

	@QueryParam("offset")
	@DefaultValue("0")
	private Integer offset = 0;

	@QueryParam("limit")
	private Integer limit;

	public SearchQueryParameterBean(int offset, int limit) {
		super();
		this.offset = Integer.valueOf(offset);
		this.limit = Integer.valueOf(limit);
	}

	public int getOffset() {
		return offset.intValue();
	}

	public void setOffset(int offset) {
		this.offset = Integer.valueOf(offset);
	}

	public int getLimit() {
		if (limit == null) {
			return defaultLimit;
		}
		return limit.intValue();
	}

	public void setLimit(int limit) {
		this.limit = Integer.valueOf(limit);
	}

}
