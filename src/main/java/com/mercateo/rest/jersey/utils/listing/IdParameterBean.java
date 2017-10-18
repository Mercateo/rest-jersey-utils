package com.mercateo.rest.jersey.utils.listing;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;

import lombok.Data;

@Data
public class IdParameterBean implements StringIdProvider {
	@PathParam("id")
	@NotNull
	private String id;

	public static IdParameterBean of(String id) {
		IdParameterBean result = new IdParameterBean();
		result.id = id;
		return result;
	}
}