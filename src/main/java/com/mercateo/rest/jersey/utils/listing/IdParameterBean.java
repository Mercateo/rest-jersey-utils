package com.mercateo.rest.jersey.utils.listing;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;

import lombok.Data;

@Data
public class IdParameterBean<IdType> implements IdProvider<IdType> {
	@PathParam("id")
	@NotNull
	private IdType id;

	public static <IdType> IdParameterBean<IdType> of(IdType id) {
		IdParameterBean<IdType> result = new IdParameterBean<>();
		result.id = id;
		return result;
	}
}