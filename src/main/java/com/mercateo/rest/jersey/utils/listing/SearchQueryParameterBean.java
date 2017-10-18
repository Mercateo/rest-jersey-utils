package com.mercateo.rest.jersey.utils.listing;

import javax.ws.rs.QueryParam;

import com.google.common.annotations.VisibleForTesting;

public class SearchQueryParameterBean {
	static final int DEFAULT_OFFSET = 0;

	@VisibleForTesting
	static final int DEFAULT_LIMIT = 20;

	@QueryParam("offset")
	private Integer offset;

	@QueryParam("limit")
	private Integer limit;

	public SearchQueryParameterBean(int offset, int limit) {
		super();
		this.offset = Integer.valueOf(offset);
		this.limit = Integer.valueOf(limit);
	}

	public SearchQueryParameterBean() {
		super();
	}

	public int getOffset() {
		if (offset == null) {
			return DEFAULT_OFFSET;
		}
		return offset.intValue();
	}

	public void setOffset(int offset) {
		this.offset = Integer.valueOf(offset);
	}

	public int getLimit() {
		if (limit == null) {
			return DEFAULT_LIMIT;
		}
		return limit.intValue();
	}

	public boolean isLimitSet() {
		return limit != null;
	}

	public void setLimit(int limit) {
		this.limit = Integer.valueOf(limit);
	}
}
