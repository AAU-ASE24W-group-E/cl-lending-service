package at.aau.ase.cl.api.interceptors.mapper;

import at.aau.ase.cl.api.interceptor.exceptions.InvalidOwnerReaderException;
import at.aau.ase.cl.api.interceptor.mapper.InvalidOwnerReaderExceptionMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class InvalidOwnerReaderExceptionMapperTest {
    @Inject
    InvalidOwnerReaderExceptionMapper mapper;

    @Test
    void testToResponse() {
        InvalidOwnerReaderException exception = new InvalidOwnerReaderException("Test invalid argument");

        Response response = mapper.toResponse(exception);

        assertNotNull(response, "Response must not be null");
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus(), "Status code should be 409 (CONFLICT)");
    }
}
