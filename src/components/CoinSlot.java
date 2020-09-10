package components;

import core.SnackVendingMachine;
import enumerations.Coin;
import exception.InvalidEntryException;
import exception.UnsupportedPayableTypeException;
import core.Payable;

import java.util.Arrays;
import java.util.List;

public class CoinSlot extends MoneySlot {
    private final String UNSUPPORTED_COIN_TYPE_MESSAGE = "Sorry!$1 Coins are NOT Supported";
    private final String INVALID_ENTRY_MESSAGE = "Error! Please Insert Coins ONLY in the enumerations.Coin Slot";
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
        if (! Coin.class.isInstance(entry)) {
            throw new InvalidEntryException(INVALID_ENTRY_MESSAGE);
        } else {
            Coin insertedCoin = (Coin) entry;
            boolean isUnsupportedCoinType = CoinSlot.ALLOWED_COINS
                    .stream()
                    .noneMatch(coin -> coin.getWorth().compareTo(insertedCoin.getWorth()) == 0);

            if (isUnsupportedCoinType) {
                throw new UnsupportedPayableTypeException(
                        insertedCoin.getClass().getSimpleName(),
                        UNSUPPORTED_COIN_TYPE_MESSAGE.replace("$1", insertedCoin.getWorth().toString())
                );
            }

            return true;
        }
    }
}
