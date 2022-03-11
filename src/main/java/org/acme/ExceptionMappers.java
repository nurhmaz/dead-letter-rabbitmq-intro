package org.acme;

import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.core.Response;

public class ExceptionMappers {
    @ServerExceptionMapper
    public Uni<RestResponse<ResponseData>> mapException(RuntimeException exception) {
        return Uni.createFrom().item(RestResponse.status(Response.Status.BAD_REQUEST
                , ResponseData.failed(Response.Status.BAD_REQUEST.getStatusCode()
                        ,exception.getMessage())));
    }
}
