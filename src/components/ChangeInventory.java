package components;

import enumerations.ExceptionMessage;
import exception.InsufficientChangeException;
import interfaces.Payable;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        final List<Payable> ALLOWED_PAYABLES = Stream.concat(NoteSlot.ALLOWED_NOTES.stream(), CoinSlot.ALLOWED_COINS.stream())
                .sorted(Comparator.comparing(Payable::getWorth).reversed())
                .collect(Collectors.toList());

        boolean flag = false;
        Map<Payable, Integer> changes = new HashMap<>();

        if (amount.compareTo(BigDecimal.valueOf(0)) > 0) {
            BigDecimal balance = amount;

            while (balance.compareTo(BigDecimal.valueOf(0)) > 0) {
                flag = false;

                for (Payable allowedPayable: ALLOWED_PAYABLES) {
                    if (balance.compareTo(allowedPayable.getWorth()) >= 0 &&
                            this.getCountOfPayable(allowedPayable) - changes.getOrDefault(allowedPayable, 0) > 0) {

                        flag = true;
                        balance = balance.subtract(allowedPayable.getWorth());
                        changes.put(allowedPayable, changes.getOrDefault(allowedPayable, 0) + 1);
                        break;
                    }
                }

                if (! flag) {
                    throw new InsufficientChangeException(ExceptionMessage.INSUFFICIENT_CHANGE_IN_INVENTORY.getMessage());
                }
            }
        }

        return changes;
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
