package com.mercateo.rest.jersey.utils.exception;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.Test;

public class DuplicateExceptionMapperTest {

    DuplicateExceptionMapper uut = new DuplicateExceptionMapper();

    @Test
    public void test_ContentTypeHeader() throws Exception {
        Response response = uut.toResponse(new DuplicateValidationException("#/id"));
        Assertions.assertThat(response.getHeaderString("Content-Type")).isEqualTo(
                "application/problem+json");
    }

    @Test
    public void test_ErrorResponseFieldsId() throws Exception {
        Response response = uut.toResponse(new DuplicateValidationException("#/id"));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson.getString("title").equals("Invalid"));

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("DUPLICATE");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/id");
    }
    
    @Test
    public void test_ErrorResponseFieldsAppId() throws Exception {
        Response response = uut.toResponse(new DuplicateValidationException("#/appId"));
        JSONObject responseEntityJson = new JSONObject(response.getEntity());

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(responseEntityJson.getString("title").equals("Invalid"));

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("DUPLICATE");

        assertThat(responseEntityJson
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/appId");
    }    

}
