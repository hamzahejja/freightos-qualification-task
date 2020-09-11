package components;

import interfaces.Payable;
import java.util.List;
import java.util.Arrays;
import enumerations.Note;
import core.SnackVendingMachine;
import exception.InvalidEntryException;
import exception.UnsupportedPayableTypeException;
import utils.ValidationExceptionMessageHandler;

public class NoteSlot extends MoneySlot {
    public static final List<Note> ALLOWED_NOTES = Arrays.asList(Note.TWENTY_DOLLARS_BILL, Note.FIFTY_DOLLARS_BILL);

    public NoteSlot(SnackVendingMachine snackVendingMachine) {
        super(snackVendingMachine);
    }

    @Override
    public boolean validate(Payable entry) throws InvalidEntryException, UnsupportedPayableTypeException{
        if (! (entry instanceof Note)) {
            throw new InvalidEntryException(
                    ValidationExceptionMessageHandler.getInvalidPayableEnteredMessage(entry)
            );
        } else if (! this.isSupportedPayableType(entry) ) {
            throw new UnsupportedPayableTypeException(
                    ValidationExceptionMessageHandler.getUnsupportedPayableTypeMessage(entry)
            );
        }

        return true;
    }

    @Override
    public boolean isSupportedPayableType(Payable payable) {
        return NoteSlot.ALLOWED_NOTES.stream()
                .anyMatch(n -> n.getWorth().compareTo(payable.getWorth()) == 0);
    }
}
