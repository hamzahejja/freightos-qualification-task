package exception;

public class UnsupportedPayableTypeException extends RuntimeException {
    private String message;

    public UnsupportedPayableTypeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
