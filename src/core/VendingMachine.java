package core;

import components.MoneySlot;
import utils.Pair;

import java.util.Map;

public interface VendingMachine<ItemType, SlotType> {
    public abstract void resetToInitialState();

    public abstract Map<Payable, Integer> refund();

    public abstract void insertMoney(MoneySlot moneySlot, Payable payable);

    public abstract ItemType processRequestAndReturnSelectedSnackItem(SlotType slot);

    public abstract Pair<ItemType, Map<Payable,Integer>> dispenseSelectedItemAndCustomerChange();
}
