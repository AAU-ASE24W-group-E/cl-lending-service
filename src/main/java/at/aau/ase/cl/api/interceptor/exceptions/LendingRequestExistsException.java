package at.aau.ase.cl.api.interceptor.exceptions;

public class LendingRequestExistsException extends RuntimeException {
    public LendingRequestExistsException(String message) {
        super(message);
    }
}
