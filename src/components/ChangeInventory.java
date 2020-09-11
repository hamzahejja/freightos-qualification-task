package components;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import interfaces.Payable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import enumerations.ExceptionMessage;
import exception.InsufficientChangeException;

public class ChangeInventory {
    private Map<Payable, Integer> inventory;

    public ChangeInventory() {
        this.inventory = new HashMap<Payable, Integer>();
    }

    public Map<Payable, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<Payable, Integer> inventory) {
        this.inventory = inventory;
    }

    public int getCountOfPayable(Payable payable) {
        return this.inventory.getOrDefault(payable, 0);
    }

    public void add(Payable payable, int additionCount) {
        int count = this.inventory.getOrDefault(payable, 0);
        this.inventory.put(payable, count + additionCount);
    }

    public void deduct(Payable payable, int deductionCount) {
        if (this.hasSufficientCountOfPayable(payable, deductionCount)) {
            inventory.put(payable, inventory.get(payable) - deductionCount);
        }
    }

    public void clear() {
        this.inventory.clear();
    }

    public void putPayableWithCount(Payable payable, int count) {
        this.inventory.put(payable, count);
    }

    public boolean hasSufficientCountOfPayable(Payable payable, int count) {
        return this.getCountOfPayable(payable) >= count ;
    }

    public Map<Payable, Integer> getChange(BigDecimal amount) throws InsufficientChangeException {
        Map<Payable, Integer> change = new LinkedHashMap<>();

        final List<Payable> ALLOWED_PAYABLES = Stream.concat(NoteSlot.ALLOWED_NOTES.stream(), CoinSlot.ALLOWED_COINS.stream())
                .sorted(Comparator.comparing(Payable::getWorth).reversed())
                .collect(Collectors.toList());

        if (amount.compareTo(BigDecimal.valueOf(0)) > 0) {
            BigDecimal balance = amount;
            BigDecimal inventoryAvailablePayableCount, calculatedPayableCount;

            for (Payable allowedPayable: ALLOWED_PAYABLES) {
                if (balance.compareTo(allowedPayable.getWorth()) >= 0) {
                    calculatedPayableCount = balance.divideToIntegralValue(allowedPayable.getWorth());
                    inventoryAvailablePayableCount = BigDecimal.valueOf(this.getCountOfPayable(allowedPayable));

                    int minCountAvailable = calculatedPayableCount.compareTo(inventoryAvailablePayableCount) < 0 ?
                            calculatedPayableCount.intValue():
                            inventoryAvailablePayableCount.intValue();

                    change.put(allowedPayable, minCountAvailable);
                    balance = balance.subtract(allowedPayable.getWorth().multiply(BigDecimal.valueOf(minCountAvailable)));
                }
            }


            if (balance.compareTo(BigDecimal.ZERO) > 0) {
                Payable payable = ALLOWED_PAYABLES.stream().filter(p -> {
                    BigDecimal[] values = amount.divideAndRemainder(p.getWorth());
                    return values[1].compareTo(BigDecimal.ZERO) == 0 &&
                            this.getCountOfPayable(p) == values[0].intValue();
                }).findFirst().orElse(null);

                if (payable != null) {
                    change.clear();
                    change.put(payable, this.getCountOfPayable(payable));

                    return change;
                } else {
                    throw new InsufficientChangeException(
                            ExceptionMessage.INSUFFICIENT_CHANGE_IN_INVENTORY.getMessage()
                    );
                }
            }
        }

        return change;
    }

    public boolean canProduceChangeForAmount(BigDecimal amount) {
        try {
            getChange(amount);
            return true;
        } catch (InsufficientChangeException insufficientChangeException) {
            return false;
        }
    }

    public void reflectInventoryDeductionsForChange(Map<Payable, Integer> change) {
        for (Payable payable: change.keySet()) {
            this.deduct(payable, change.get(payable));
        }
    }

}
