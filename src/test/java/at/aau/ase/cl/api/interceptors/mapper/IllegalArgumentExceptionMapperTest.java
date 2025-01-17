package at.aau.ase.cl.api.interceptors.mapper;

import at.aau.ase.cl.api.interceptor.mapper.IllegalArgumentExceptionMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class IllegalArgumentExceptionMapperTest {
    @Inject
    IllegalArgumentExceptionMapper mapper;

    @Test
    void testToResponse() {
        IllegalArgumentException exception = new IllegalArgumentException("Test invalid argument");

        Response response = mapper.toResponse(exception);

        assertNotNull(response, "Response must not be null");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus(), "Status code should be 400 (BAD_REQUEST)");
    }
}
