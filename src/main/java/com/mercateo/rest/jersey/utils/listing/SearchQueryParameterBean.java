package com.mercateo.rest.jersey.utils.listing;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import com.mercateo.common.rest.schemagen.IgnoreInRestSchema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class SearchQueryParameterBean {
	@Getter
	@Setter
	@IgnoreInRestSchema
	private int defaultLimit = 20;

	@IgnoreInRestSchema
	private int defaultOffset = 0;

	@QueryParam("offset")
	@DefaultValue("0")
	private Integer offset;

	@QueryParam("limit")
	private Integer limit;

	public SearchQueryParameterBean(int offset, int limit) {
		super();
		this.offset = Integer.valueOf(offset);
		this.limit = Integer.valueOf(limit);
	}

	public int getOffset() {
		if (offset == null) {
			return defaultOffset;
		}
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
