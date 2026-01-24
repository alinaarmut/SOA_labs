package example;

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
    @GET
    public Response getBands(
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("filterName") String filterName
    ) {
        try {
            String result = service.getBands(page, size, sortBy, filterName);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    public Response addBand(String bandJson) {
        try {
            String result = service.addBand(bandJson);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBandById(@PathParam("id") int id) {
        try {
            String result = service.getBandById(id);
            System.out.println("Result from service: " + result);
            if (result == null ||result.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"Band with id " + id + " not found\"}")
                        .build();
            }
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Внутренняя ошибка сервера\"}")
                    .build();
        }
    }



    @PUT
    @Path("/{id}")
    public Response updateBand(
            @PathParam("id") long id,
            String bandJson
    ) {
        try {
            String result = service.getBandById((int) id);
            if (result == null || result.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"Band with id " + id + " not found\"}")
                        .build();
            }
            service.updateBand(id, bandJson);
            return Response.ok("{\"message\":\"Band updated\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBand(@PathParam("id") int id) {
        try {
            service.deleteBandById(id);
            return Response.ok("{\"message\":\"Band deleted\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/group-by-genre")
    public Response groupByGenre() {
        try {
            String result = service.groupByGenre();
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/count-by-frontman")
    public Response countByFrontman(@QueryParam("frontMan") String frontMan) {
        try {
            String result = service.countByFrontman(frontMan);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/search-by-name")
    public Response searchByName(@QueryParam("prefix") String prefix) {
        try {
            String result = service.searchByName(prefix);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"" + e.getMessage() + "\"}").build();
        }
    }


    @POST
    @Path("/band/{bandId}/nominate/{genre}")
    public Response nominateBand(
            @PathParam("bandId") int bandId,
            @PathParam("genre") String genre
    ) {
        try {
            String band = service.getBandById(bandId);
            if (band == null || band.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"Band with id " + bandId + " not found\"}")
                        .build();
            }

            boolean ok = service.nominateBand(bandId, genre);
            if (ok) {
                return Response.ok("{\"message\":\"Band nominated\"}").build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Failed to nominate band\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(    e.getMessage()).build();
        }
    }


    @POST
    @Path("/band/{bandId}/reward/{genre}")
    public Response rewardBand(
            @PathParam("bandId") int bandId,
            @PathParam("genre") String genre
    ) {
        try {
            String band = service.getBandById(bandId);
            if (band == null || band.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"Band with id " + bandId + " not found\"}")
                        .build();
            }

            boolean ok = service.rewardBand(bandId, genre);
            if (ok) {
                return Response.ok("{\"message\":\"Band rewarded\"}").build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Failed to reward band\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).build();
        }

    }

}