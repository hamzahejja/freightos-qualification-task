package components;

import core.SnackVendingMachine;
import exception.InvalidEntryException;
import exception.UnsupportedPayableTypeException;
import interfaces.Payable;

public abstract class MoneySlot {
    protected SnackVendingMachine owner;
    protected boolean isFunctional = true;

    public MoneySlot() {
    }

    public MoneySlot(SnackVendingMachine owner) {
        this.owner = owner;
    }

    public SnackVendingMachine getOwner() {
        return owner;
    }

    public void setOwner(SnackVendingMachine owner) {
        this.owner = owner;
    }

    public boolean isFunctional() {
        return isFunctional;
    }

    public void setFunctional(boolean functional) {
        isFunctional = functional;
    }

    public abstract boolean validate(Payable entry) throws InvalidEntryException, UnsupportedPayableTypeException;

    public abstract boolean isSupportedPayableType(Payable payable);
}
