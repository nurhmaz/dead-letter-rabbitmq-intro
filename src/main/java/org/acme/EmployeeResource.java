package org.acme;

import io.smallrye.mutiny.Uni;
import lombok.Data;
import org.jboss.resteasy.reactive.RestResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/employee")
@Data
public class EmployeeResource {

    private final EmployeeStatusService service;

    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<RestResponse<ResponseData<Employee>>> hello(Employee employee) {
        return service.processEmployee(employee)
                .onItem()
                .transform(newEmployee -> RestResponse.ResponseBuilder.ok(ResponseData.success(newEmployee)).build());
    }
}