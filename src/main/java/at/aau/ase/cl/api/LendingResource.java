package at.aau.ase.cl.api;

import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.mapper.LendingMapper;
import at.aau.ase.cl.model.LendingEntity;
import at.aau.ase.cl.service.LendingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.UUID;

@Path("/lendings")
public class LendingResource {
    @Inject
    LendingService lendingService;

    @POST
    @Path("/")
    public Response createLending(LendingModel lendingModel) {
        LendingModel createdLending = lendingService.createLending(lendingModel);

        return Response.ok(createdLending).build();
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LendingModel.class))})
    public Response getLending(@PathParam("id") UUID id) {
        LendingEntity lending = lendingService.getLendingById(id);
        LendingModel model = LendingMapper.INSTANCE.map(lending);
        return Response.ok(model).build();
    }
}
