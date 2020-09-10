package exception;

public class ItemNotFullyPaidException extends RuntimeException {
    private String message;

    public ItemNotFullyPaidException (String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
