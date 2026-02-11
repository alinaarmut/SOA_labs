package example.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@Path("/api/grammy")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GrammyResource {

    private final GrammyService service;

    public GrammyResource(GrammyService service) {
        this.service = service;
    }
    @Value("${server.port}")
    private String port;

    @GetMapping("/test")
    public String test() {
        return "SERVICE-GRAMMY на порту" + port;
    }
    @GET
    @Path("/health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response health() {
        return Response.ok("{\"status\":\"UP\"}").build();
    }
}