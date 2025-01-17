package at.aau.ase.cl.api.interceptors;

import at.aau.ase.cl.api.interceptor.ErrorResponse;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ErrorResponseTest {
    @Test
    void testOfWithException() {
        Throwable exception = new IllegalArgumentException("Test exception message");

        ErrorResponse response = ErrorResponse.of(exception);

        assertEquals("IllegalArgumentException", response.type(), "Exception type should match");
        assertEquals("Test exception message", response.message(), "Exception message should match");
    }
}