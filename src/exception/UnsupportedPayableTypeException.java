package exception;

public class UnsupportedPayableTypeException extends RuntimeException {
    private String payable;
    private String message;

    public UnsupportedPayableTypeException(String payable, String message) {
        this.payable = payable;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public String getPayable() {
        return this.payable;
    }
}
