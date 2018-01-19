package com.mercateo.rest.jersey.utils.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class SimpleExceptionJson {
	@NonNull
	String title;
	int status;
	String detail;
}
