package com.mercateo.rest.jersey.utils.listing;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;

public class IdParameterBean implements StringIdProvider {
	@PathParam("id")
	@NotNull
	private String id;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static IdParameterBean of(String id) {
		IdParameterBean result = new IdParameterBean();
		result.id = id;
		return result;
	}
}