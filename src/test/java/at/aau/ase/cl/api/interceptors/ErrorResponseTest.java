package at.aau.ase.cl.api.interceptors;

import at.aau.ase.cl.api.interceptor.ErrorResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {
    @Test
    void testOfWithException() {
        Throwable exception = new IllegalArgumentException("Test exception message");

        ErrorResponse response = ErrorResponse.of(exception);

        assertEquals("IllegalArgumentException", response.type(), "Exception type should match");
        assertEquals("Test exception message", response.message(), "Exception message should match");
    }
}