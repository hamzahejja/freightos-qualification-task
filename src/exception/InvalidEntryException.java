package exception;

public class InvalidEntryException extends RuntimeException {
    private String message;

    public InvalidEntryException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
