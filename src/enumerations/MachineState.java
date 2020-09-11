package enumerations;

public enum MachineState {
    OUT_OF_SERVICE("MACHINE_IS_NOT_FUNCTIONAL"),
    IDLE_WAITING_CUSTOMER_MONEY_ENTRY("IDLE_WAITING_CUSTOMER_MONEY_ENTRY"),
    PROCESSING_CUSTOMER_SELECTION("PROCESSING_CUSTOMER_SELECTION"),
    DISPENSING_SELECTED_ITEM_AND_CUSTOMER_CHANGE("DISPENSING_SELECTED_ITEM_AND_CUSTOMER_CHANGE"),
    REFUNDING_CUSTOMER_MONEY("REFUNDING_MONEY_REQUEST_CANCELLED");

    private String description;

    private MachineState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "enumerations.MachineState{" +
                "description='" + description + '\'' +
                '}';
    }
}
