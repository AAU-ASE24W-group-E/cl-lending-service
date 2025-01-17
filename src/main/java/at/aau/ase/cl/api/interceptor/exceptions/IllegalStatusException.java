package at.aau.ase.cl.api.interceptor.exceptions;

public class IllegalStatusException extends RuntimeException {
    public IllegalStatusException(String message) {
        super(message);
    }
}
