package com.mercateo.rest.jersey.utils.exception;

import lombok.NonNull;
import lombok.Value;

@Value
public class SimpleExceptionJson {
	@NonNull
	String title;
	int status;
	String detail;
}
