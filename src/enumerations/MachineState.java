package enumerations;

public enum MachineState {
    OUT_OF_SERVICE("MACHINE_IS_NOT_FUNCTIONAL"),
    RESETTING_INITIAL_STATE("RESETTING_MACHINE_TO_INITIAL_STATE"),
    WAITING_CUSTOMER_ENTRY("READY_AND_WAITING_CUSTOMER_ENTRY"),
    PROCESSING_CUSTOMER_ENTRY("PROCESSING_CUSTOMER_ENTRY"),
    VALIDATING_INSERTED_MONEY("VALIDATING_RECEIVED_MONEY"),
    ACCEPTED_INSERTED_MONEY("ACCEPTED_CUSTOMER_MONEY"),
    REFUNDING_CUSTOMER_MONEY("REFUNDING_MONEY_REQUEST_CANCELLED"),
    DISPENSING_SELECTED_ITEM("DISPENSING_SELECTED_ITEM"),
    CALCULATING_CUSTOMER_CHANGE("CALCULATING_CHANGE"),
    DISPENSING_CUSTOMER_CHANGE("DISPENSING_CUSTOMER_CHANGE");

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
