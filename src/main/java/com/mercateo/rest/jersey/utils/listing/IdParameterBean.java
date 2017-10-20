package com.mercateo.rest.jersey.utils.listing;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;

import lombok.Data;

@Data
public class IdParameterBean implements StringIdProvider {

	@NotNull
	@PathParam("id")
	private String id;

	public static <IdType> IdParameterBean of(String id) {
		IdParameterBean result = new IdParameterBean();
		result.id = id;
		return result;
	}
}