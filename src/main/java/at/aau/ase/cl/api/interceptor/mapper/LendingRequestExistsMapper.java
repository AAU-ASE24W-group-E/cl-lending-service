package at.aau.ase.cl.api.interceptor.mapper;

import at.aau.ase.cl.api.interceptor.ErrorResponse;
import at.aau.ase.cl.api.interceptor.exceptions.InvalidOwnerReaderException;
import at.aau.ase.cl.api.interceptor.exceptions.LendingRequestExistsException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@jakarta.ws.rs.Produces(MediaType.APPLICATION_JSON)
public class LendingRequestExistsMapper implements ExceptionMapper<LendingRequestExistsException> {
    @Override
    public Response toResponse(LendingRequestExistsException exception) {
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON)
                .entity(ErrorResponse.of(exception))
                .build();
    }
}
