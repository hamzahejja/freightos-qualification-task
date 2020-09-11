package components;

import core.SnackVendingMachine;
import enumerations.Coin;
import enumerations.Note;
import exception.InvalidEntryException;
import exception.UnsupportedPayableTypeException;
import interfaces.Payable;
import utils.ValidationExceptionMessageHandler;

import java.util.Arrays;
import java.util.List;

public class CoinSlot extends MoneySlot {
    private final String UNSUPPORTED_COIN_TYPE_MESSAGE = "Sorry!$1 Coins are NOT Supported";
    private final String INVALID_ENTRY_MESSAGE = "Error! Please Insert Coins ONLY in the Coin Slot";
    public static final List<Coin> ALLOWED_COINS = Arrays.asList(
            Coin.TEN_CENTS,
            Coin.TWENTY_FIVE_CENTS,
            Coin.FIFTY_CENTS,
            Coin.ONE_DOLLAR
    );

    public CoinSlot(SnackVendingMachine snackVendingMachine) {
        super(snackVendingMachine);
    }

    @Override
    public boolean validate(Payable entry) throws InvalidEntryException, UnsupportedPayableTypeException {
        if (! (entry instanceof Coin)) {
            throw new InvalidEntryException(
                    ValidationExceptionMessageHandler.getInvalidPayableEnteredMessage(entry)
            );
        } else if (! isSupportedPayableType(entry)) {
            throw new UnsupportedPayableTypeException(
                    ValidationExceptionMessageHandler.getUnsupportedPayableTypeMessage(entry)
            );
        }

        return true;
    }

    @Override
    public boolean isSupportedPayableType(Payable payable) {
        return CoinSlot.ALLOWED_COINS.stream()
                .anyMatch(c -> c.getWorth().compareTo(payable.getWorth()) == 0);
    }
}
