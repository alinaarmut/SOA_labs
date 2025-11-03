package example;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/grammy")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GrammyResource {

    @Inject
    private GrammyService service;


    @GET
    @Path("/bands")
    public Response getBands(
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size,
            @QueryParam("sortBy") String sortBy,
            @QueryParam("filterName") String filterName
    ) {
        return service.getBands(page, size, sortBy, filterName);
    }

    @POST
    @Path("/bands")
    public Response addBand(String bandJson) {
        return service.addBand(bandJson);
    }

    @GET
    @Path("/bands/{id}")
    public Response getBandById(@PathParam("id") int id) {
        return service.getBandById(id);
    }

    @PUT
    @Path("/bands/{id}")
    public Response updateBand(
            @PathParam("id") long id,
            String bandJson
    ) {
        return service.updateBand(id, bandJson);
    }

    @DELETE
    @Path("/bands/{id}")
    public Response deleteBand(@PathParam("id") int id) {
        return service.deleteBandById(id);
    }

    @GET
    @Path("/bands/group-by-genre")
    public Response groupByGenre() {
        return service.groupByGenre();
    }

    @GET
    @Path("/bands/count-by-frontman")
    public Response countByFrontman(@QueryParam("frontMan") String frontMan) {
        return service.countByFrontman(frontMan);
    }

    @GET
    @Path("/bands/search-by-name")
    public Response searchByName(@QueryParam("prefix") String prefix) {
        return service.searchByName(prefix);
    }

    @POST
    @Path("/band/{band-id}/nominate/{genre}")
    public Response nominateBand(
            @PathParam("band-id") int bandId,
            @PathParam("genre") String genre
    ) {
        try {
            Response bandResponse = service.getBandById(bandId);
            if (bandResponse.getStatus() != 200) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"Band with id " + bandId + " not found!\"}").build();
            }

            boolean ok = service.nominateBand(bandId, genre);
            if (ok) return Response.ok().entity("{\"message\":\"Band nominated\"}").build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to nominate band\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Разработчик ушел играть на гитаре!\"}").build();
        }
    }

    @POST
    @Path("/band/{band-id}/reward/{genre}")
    public Response rewardBand(
            @PathParam("band-id") int bandId,
            @PathParam("genre") String genre
    ) {
        try {
            Response bandResponse = service.getBandById(bandId);
            if (bandResponse.getStatus() != 200) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"Band with id " + bandId + " not found!\"}").build();
            }

            boolean ok = service.rewardBand(bandId, genre);
            if (ok) return Response.ok().entity("{\"message\":\"Band rewarded\"}").build();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Failed to reward band\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\": \"Разработчик ушел играть на гитаре!\"}").build();
        }
    }


}
