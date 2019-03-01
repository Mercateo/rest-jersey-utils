package com.mercateo.rest.jersey.utils.exception;

public interface RFCExceptionDetailsProvider {

    public String getDetail();

    public String getTitle();

    public int getStatus();

    public String getType();

}
