package components;
import core.SnackVendingMachine;
import enumerations.Card;
import exception.InvalidEntryException;
import core.Payable;

public class CardSlot extends MoneySlot {
    private final String INVALID_ENTRY_MESSAGE = "Error! Please Be Careful to Insert Cards ONLY in enumerations.Card Slot.";

    public CardSlot(SnackVendingMachine snackVendingMachine) {
        super(snackVendingMachine);
    }

    @Override
    public boolean validate(Payable entry) throws InvalidEntryException {
        if (! Card.class.isInstance(entry)) {
            throw new InvalidEntryException(INVALID_ENTRY_MESSAGE);
        }

        return true;
    }
}
