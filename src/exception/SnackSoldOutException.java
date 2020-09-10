package exception;

public class SnackSoldOutException extends RuntimeException {
    private String message;

    public SnackSoldOutException (String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
