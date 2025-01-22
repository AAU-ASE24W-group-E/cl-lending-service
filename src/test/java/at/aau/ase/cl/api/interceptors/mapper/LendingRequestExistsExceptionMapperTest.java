package at.aau.ase.cl.api.interceptors.mapper;

import at.aau.ase.cl.api.interceptor.exceptions.LendingRequestExistsException;
import at.aau.ase.cl.api.interceptor.mapper.LendingRequestExistsMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class LendingRequestExistsExceptionMapperTest {
    @Inject
    LendingRequestExistsMapper mapper;

    @Test
    void testToResponse() {
        LendingRequestExistsException exception = new LendingRequestExistsException("Test invalid argument");

        Response response = mapper.toResponse(exception);

        assertNotNull(response, "Response must not be null");
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus(), "Status code should be 409 (CONFLICT)");
    }
}
