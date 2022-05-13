package org.gs;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/players")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    PlayerProducer producer;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Player> players = Player.listAll();
        return Response.ok(players).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Long id) {
        return Player.findByIdOptional(id)
                .map(player -> Response.ok(player).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByName(@PathParam("name") String name) {
        List<Player> players = Player.list("SELECT m FROM Player m WHERE m.name = ?1 ORDER BY id DESC", name);
        return Response.ok(players).build();
    }

    @GET
    @Path("/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByTitle(@PathParam("email") String email) {
        return Player.find("email", email)
                .singleResultOptional()
                .map(player -> Response.ok(player).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Player player) {
        producer.send(player);
        // Return an 202 - Accepted response.
        return Response.accepted().build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteBydId(@PathParam("id") Long id) {
        boolean deleted = Player.deleteById(id);
        if (deleted) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}