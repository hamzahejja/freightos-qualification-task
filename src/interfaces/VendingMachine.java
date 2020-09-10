package interfaces;

import components.MoneySlot;
import utils.Pair;

import java.util.Map;

public interface VendingMachine<ItemType, SlotType> {
    void resetToInitialState();

    Map<Payable, Integer> refund();

    void insertMoney(MoneySlot moneySlot, Payable payable);

    ItemType processRequestAndReturnSelectedSnackItem(SlotType slot);

    Pair<ItemType, Map<Payable,Integer>> dispenseSelectedItemAndCustomerChange();
}
