package at.aau.ase.cl.api;

import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.service.LendingService;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

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
}
