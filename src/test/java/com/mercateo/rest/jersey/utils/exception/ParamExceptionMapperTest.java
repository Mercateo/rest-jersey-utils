package com.mercateo.rest.jersey.utils.exception;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ParamException.PathParamException;
import org.glassfish.jersey.server.ParamException.QueryParamException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mock;

public class ParamExceptionMapperTest {

    private final ParamExceptionMapper uut = new ParamExceptionMapper();

    @Mock
    private Throwable cause;

    private final QueryParamException queryParamException = new QueryParamException(cause,
            "qparam", "default qparam");

    private final PathParamException pathParamException = new PathParamException(cause,
            "pparam", "default pparam");

    @Test
    public void test_queryParamExceptionContentTypeHeader() {

        // given

        // when
        Response response = uut.toResponse(queryParamException);

        // then
        assertThat(response.getHeaderString("Content-Type")).isEqualTo("application/problem+json");

    }

    @Test
    public void test_pathParamExceptionContentTypeHeader() {

        // given

        // when
        Response response = uut.toResponse(pathParamException);

        // then
        assertThat(response.getHeaderString("Content-Type")).isEqualTo("text/plain");

    }

    @Test
    public void test_queryParamExceptionErrorResponseFieldsId() {

        // given

        // when
        Response response = uut.toResponse(queryParamException);

        // then
        assertThat(response.getStatus()).isEqualTo(400);

        JSONObject body = new JSONObject(response.getEntity());
        assertThat(body.getString("detail")).isEqualTo("The filter query is not valid.");
        assertThat(body.getString("type")).isEqualTo(
                "https://developers.unite.eu/errors/invalid-query-param");
        assertThat(body.getString("title")).isEqualTo("Invalid Query Parameter");

        assertThat(body
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("code").toString()).isEqualTo("INVALID");

        assertThat(body
                .getJSONArray("errors")
                .getJSONObject(0)
                .get("path").toString()).isEqualTo("#/expression/qparam");

    }

    @Test
    public void test_pathParamExceptionResponseFieldsId() {

        // given

        // when
        Response response = uut.toResponse(pathParamException);

        // then
        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getEntity().toString()).isEqualTo("HTTP 404 Not Found");

    }

}
