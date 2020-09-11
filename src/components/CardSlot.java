package components;
import core.SnackVendingMachine;
import enumerations.Card;
import exception.InvalidEntryException;
import interfaces.Payable;
import utils.ValidationExceptionMessageHandler;

public class CardSlot extends MoneySlot {
    public CardSlot(SnackVendingMachine snackVendingMachine) {
        super(snackVendingMachine);
    }

    @Override
    public boolean validate(Payable entry) throws InvalidEntryException {
        if (! (entry instanceof Card)) {
            throw new InvalidEntryException(
                    ValidationExceptionMessageHandler.getInvalidPayableEnteredMessage(entry)
            );
        }

        return true;
    }

    @Override
    public boolean isSupportedPayableType(Payable payable) {
        return true;
    }
}
