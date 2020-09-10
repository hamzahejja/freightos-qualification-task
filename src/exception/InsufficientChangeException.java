package exception;

public class InsufficientChangeException extends RuntimeException {
    private String message;

    public InsufficientChangeException (String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
