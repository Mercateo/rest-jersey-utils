package com.mercateo.rest.jersey.utils.listing;

import javax.ws.rs.PathParam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

// constructors have to be public for HK2, id field has to be non-final for the same reason
// IdProvider<String> because Jersey cannot handle a generic type at this point
@NoArgsConstructor
@AllArgsConstructor
public class IdParameterBean implements IdProvider<String> {

    @Getter
    @PathParam("id")
    private String id;

    public static <IdType> IdParameterBean of(@NonNull IdType id) {
        return new IdParameterBean(id.toString());
    }

    static IdParameterBean forTemplating() {
        return new IdParameterBean();
    }
}