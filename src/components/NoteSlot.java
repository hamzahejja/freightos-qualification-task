package components;

import interfaces.Payable;
import java.util.List;
import java.util.Arrays;
import enumerations.Note;
import core.SnackVendingMachine;
import exception.InvalidEntryException;
import exception.UnsupportedPayableTypeException;


public class NoteSlot extends MoneySlot {
    private final String UNSUPPORTED_NOTE_TYPE_MESSAGE = "Sorry!$1 Notes are NOT Supported";
    private final String INVALID_ENTRY_MESSAGE = "Error! Please Insert Notes ONLY in the enumerations.Note Slot";
    public static final List<Note> ALLOWED_NOTES = Arrays.asList(Note.TWENTY_DOLLARS_BILL, Note.FIFTY_DOLLARS_BILL);

    public NoteSlot(SnackVendingMachine snackVendingMachine) {
        super(snackVendingMachine);
    }

    @Override
    public boolean validate(Payable entry) throws InvalidEntryException, UnsupportedPayableTypeException{
        if (! Note.class.isInstance(entry)) {
            throw new InvalidEntryException(INVALID_ENTRY_MESSAGE);
        } else {
            Note insertedNote = (Note) entry;
            boolean isUnsupportedNoteType = NoteSlot.ALLOWED_NOTES
                    .stream()
                    .noneMatch(note -> note.getWorth().compareTo(insertedNote.getWorth()) == 0);

            if (isUnsupportedNoteType) {
                throw new UnsupportedPayableTypeException(
                        insertedNote.getClass().getSimpleName(),
                        UNSUPPORTED_NOTE_TYPE_MESSAGE.replace("$1", insertedNote.getWorth().toString())
                );
            }

            return true;
        }
    }
}
