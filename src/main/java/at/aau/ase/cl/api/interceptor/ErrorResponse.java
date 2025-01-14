package at.aau.ase.cl.api.interceptor;

// TODO improve error handling
public record ErrorResponse(String type, String message) {
    public static ErrorResponse of(Throwable error) {
        return new ErrorResponse(error.getClass().getSimpleName(), error.getMessage());
    }
}
