package at.aau.ase.cl.api.interceptors.mapper;

import at.aau.ase.cl.api.interceptor.exceptions.NotFoundException;
import at.aau.ase.cl.api.interceptor.mapper.NotFoundExceptionMapper;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotFoundExceptionMapperTest {
    NotFoundExceptionMapper mapper = new NotFoundExceptionMapper();

    @Test
    void testToResponse() {
        NotFoundException exception = new NotFoundException("Test invalid argument");

        Response response = mapper.toResponse(exception);

        assertNotNull(response, "Response must not be null");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus(), "Status code should be 404 (NOT_FOUND)");
    }
}
