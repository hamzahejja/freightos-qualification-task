package enumerations;

public enum ExceptionMessage {
    FULL_SNACK_SLOT("SNACK SLOT IS FULL OF ITEMS!"),
    EMPTY_SNACK_SLOT("SNACK SLOT IS OUT OF ITEMS!"),
    ITEM_NOT_FULLY_PAID("SELECTED ITEM IS NOT FULLY PAID!"),
    CUSTOMER_REQUEST_NOT_CONFIRMED("REQUEST NOT CONFIRMED!"),
    INSUFFICIENT_CHANGE_IN_INVENTORY("INSUFFICIENT CHANGE IN INVENTORY");

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
