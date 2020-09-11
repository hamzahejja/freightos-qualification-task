package core;

import components.*;
import enumerations.ExceptionMessage;
import enumerations.MachineState;
import enumerations.SnackItem;
import exception.CustomerRequestNotConfirmedException;
import exception.InsufficientChangeException;
import exception.ItemNotFullyPaidException;
import exception.SnackSoldOutException;
import interfaces.Payable;
import interfaces.VendingMachine;
import utils.Pair;
import java.math.BigDecimal;
import java.util.Map;

public class SnackVendingMachine implements VendingMachine<SnackItem, SnackSlot> {
    private int rowsCount;
    private int columnsCount;
    private boolean isFunctional;
    private boolean shouldStartProcessingRequest;

    private BigDecimal salesTotal;
    private BigDecimal accumulatedMoney;

    private Keypad keypad;
    private DisplayScreen displayScreen;

    private CoinSlot coinSlot;
    private NoteSlot noteSlot;
    private CardSlot cardSlot;
    private SnackSlot[][] snackSlots;
    private ChangeInventory changeInventory;
    private MachineState currentlyOperatingState;
    private SnackSlot currentlySelectedSnackSlot;

    public SnackVendingMachine() {
        this.initializeMachine();
    }

    /**
     * initialization method for the Snack Vending Machine's
     * attributes/fields including it's rowsCount, colsCount,
     * boolean flags and components: Keypad, DisplayScreen, NoteSlot,
     * CoinSlot, CardSlot, ChangeInventory, SnackSlots and finally it's
     * financial stats/indicators: accumulatedMoney, totalSales.
     */
    private void initializeMachine() {
        this.rowsCount = 5;
        this.columnsCount = 5;
        this.isFunctional = true;
        this.initializeSnackSlots();
        this.shouldStartProcessingRequest = false;
        this.setCurrentlyOperatingState(MachineState.IDLE_WAITING_CUSTOMER_MONEY_ENTRY);

        this.keypad = new Keypad(this);
        this.displayScreen = new DisplayScreen();
        this.changeInventory = new ChangeInventory();
        this.cardSlot = new CardSlot(this);
        this.coinSlot = new CoinSlot(this);
        this.noteSlot = new NoteSlot(this);

        this.currentlySelectedSnackSlot = null;
        this.salesTotal = BigDecimal.valueOf(0);
        this.accumulatedMoney = BigDecimal.valueOf(0);
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public void setRowsCount(int rowsCount) {
        this.rowsCount = rowsCount;
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    public boolean isFunctional() {
        return isFunctional;
    }

    public void setFunctional(boolean functional) {
        MachineState currentlyOperatingMachineState = functional ?
                MachineState.IDLE_WAITING_CUSTOMER_MONEY_ENTRY :
                MachineState.OUT_OF_SERVICE;

        isFunctional = functional;
        this.setCurrentlyOperatingState(currentlyOperatingMachineState);
    }

    public Keypad getKeypad() {
        return keypad;
    }

    public void setKeypad(Keypad keypad) {
        this.keypad = keypad;
    }

    public DisplayScreen getDisplayScreen() {
        return displayScreen;
    }

    public void setDisplayScreen(DisplayScreen displayScreen) {
        this.displayScreen = displayScreen;
    }

    public CoinSlot getCoinSlot() {
        return coinSlot;
    }

    public void setCoinSlot(CoinSlot coinSlot) {
        this.coinSlot = coinSlot;
    }

    public NoteSlot getNoteSlot() {
        return noteSlot;
    }

    public void setNoteSlot(NoteSlot noteSlot) {
        this.noteSlot = noteSlot;
    }

    public CardSlot getCardSlot() {
        return cardSlot;
    }

    public void setCardSlot(CardSlot cardSlot) {
        this.cardSlot = cardSlot;
    }

    public SnackSlot[][] getSnackSlots() {
        return snackSlots;
    }

    public void setSnackSlots(SnackSlot[][] snackSlots) {
        this.snackSlots = snackSlots;
    }

    public BigDecimal getSalesTotal() { return salesTotal; }

    public void setSalesTotal(BigDecimal salesTotal) { this.salesTotal = salesTotal; }

    public BigDecimal getAccumulatedMoney() {
        return accumulatedMoney;
    }

    public void setAccumulatedMoney(BigDecimal accumulatedMoney) {
        this.accumulatedMoney = accumulatedMoney;
    }

    public ChangeInventory getChangeInventory() {
        return changeInventory;
    }

    public void setChangeInventory(ChangeInventory changeInventory) {
        this.changeInventory = changeInventory;
    }

    public MachineState getCurrentlyOperatingState() {
        return currentlyOperatingState;
    }

    public void setCurrentlyOperatingState(MachineState currentlyOperatingState) {
        this.currentlyOperatingState = currentlyOperatingState;
        this.printCurrentlyOperatingState();
    }

    public SnackSlot getCurrentlySelectedSnackSlot() {
        return currentlySelectedSnackSlot;
    }

    public void setCurrentlySelectedSnackSlot(SnackSlot currentlySelectedSnackSlot) {
        this.currentlySelectedSnackSlot = currentlySelectedSnackSlot;
    }

    public boolean isShouldStartProcessingRequest() {
        return shouldStartProcessingRequest;
    }

    public void setShouldStartProcessingRequest(boolean shouldStartProcessingRequest) {
        this.shouldStartProcessingRequest = shouldStartProcessingRequest;
    }

    /**
     * Processes customer's request/order where he selected a SnackSlot
     * out of the Machine's 5x5 grid of Snack Slots and if the request is valid,
     * that is the request is confirmed and SnackSlot selected has items, then
     * it will return the SnackItem at the front of the selected snack slot.
     *
     * @param selectedSnackSlot the customer's selected SnackSlot.
     * @return the Snack Item which is in the customer's selected SnackSlot
     * @throws   CustomerRequestNotConfirmedException if customer's request is not confirmed.
     * @throws  SnackSoldOutException if selected snack slot is out-of-items.
     */
    public SnackItem processRequestAndReturnSelectedSnackItem(
            SnackSlot selectedSnackSlot
    ) throws CustomerRequestNotConfirmedException, SnackSoldOutException {
        this.currentlySelectedSnackSlot = selectedSnackSlot;
        this.setCurrentlyOperatingState(MachineState.PROCESSING_CUSTOMER_SELECTION);

        if (! this.shouldStartProcessingRequest) {
            throw new CustomerRequestNotConfirmedException(ExceptionMessage.CUSTOMER_REQUEST_NOT_CONFIRMED.getMessage());
        } else if (selectedSnackSlot.getQuantity() == 0) {
            throw new SnackSoldOutException(ExceptionMessage.EMPTY_SNACK_SLOT.getMessage());
        }

        this.shouldStartProcessingRequest = false;
        this.printCurrentlySelectedSnackItemInformation();

        return this.currentlySelectedSnackSlot.getItem();
    }

    /**
     * Inserts Payable [Coin, Note or Card] into it's corresponding/matching
     * MoneySlot on the Snack Vending Machine, validates the entry and then
     * adds it's worth to the machine's balance (accumulated money) and also
     * to the Change Inventory of the machine. Finally, it displays the accumulated
     * money to the customer, to inform him about how much he entered so far.
     *
     * @param moneySlot Slot to insert the money in.
     * @param payable Payable to insert into the corresponding MoneySlot.
     */
    public void insertMoney(MoneySlot moneySlot, Payable payable) {
        moneySlot.validate(payable);
        this.accumulatedMoney = this.accumulatedMoney.add(payable.getWorth());
        this.changeInventory.add(payable, 1);
        this.printCurrentlyAccumulatedMoney();
    }

    public Map<Payable, Integer> cancelRequestAndRefundCustomer() {
        return this.refund();
    }

    /**
     * Dispense the SnackItem at the front of the customer's selected snack slot
     * iff the selected item is FULLY PAID and the Machine's able to produce the change
     * that has to be refunded to the customer on successful purchase (if any).
     *
     * @return the SnackItem at the front of the customer's selected snack slot.
     * @throws ItemNotFullyPaidException if balance is less than the selected item's price.
     * @throws InsufficientChangeException if the machine cannot produce the change that's to be refunded.
     */
    private SnackItem dispenseSelectedSnackItem() throws ItemNotFullyPaidException, InsufficientChangeException {
        SnackItem selectedSnackItem = this.currentlySelectedSnackSlot.getItem();

        if (! this.isSelectedItemFullyPaid()) {
            throw new ItemNotFullyPaidException(ExceptionMessage.ITEM_NOT_FULLY_PAID.getMessage());
        } else if (! this.changeInventory.canProduceChangeForAmount(this.accumulatedMoney.subtract(selectedSnackItem.getPrice()))) {
            throw new InsufficientChangeException(ExceptionMessage.INSUFFICIENT_CHANGE_IN_INVENTORY.getMessage());
        }

        this.currentlySelectedSnackSlot.dispenseSnackItem();
        this.salesTotal = this.salesTotal.add(selectedSnackItem.getPrice());

        return selectedSnackItem;
    }

    /**
     * Calculates change that needs to be refunded/dispensed to
     * customer upon successful purchase and reflects deductions on
     * the Snack Vending Machine's internal Change Inventory component.
     *
     * @return a map of Payable -> Integer, where:
     * Payable could be Note, Coin, Slot and
     * Integer is the count of that certain type of Payable in the inventory.
     */
    private Map<Payable, Integer> calculateAndDispenseChange() {
        BigDecimal selectedSnackItemPrice = this.currentlySelectedSnackSlot.getItem().getPrice();
        Map<Payable, Integer> change = this.changeInventory.getChange(this.accumulatedMoney.subtract(selectedSnackItemPrice));
        this.getChangeInventory().reflectInventoryDeductionsForChange(change);

        this.currentlySelectedSnackSlot = null;
        this.accumulatedMoney = BigDecimal.valueOf(0);
        return change;
    }

    /**
     * Dispense the selected snack item and also
     * the calculated customer change upon successful purchase of snack item.
     *
     * @return a Pair of: SnackItem, Map<Payable, Integer> where the Map
     * represents the change refunded back to the customer upon purchase iff
     * the inserted money (i.e accumulated money) exceeds the price of selected item.
     */
    @Override
    public Pair<SnackItem, Map<Payable, Integer>> dispenseSelectedItemAndCustomerChange() {
        try {
            SnackItem dispensedSnackItem = this.dispenseSelectedSnackItem();
            Map<Payable, Integer> customerChange = this.calculateAndDispenseChange();
            this.printCalculatedCustomerChange(customerChange);
            this.setCurrentlyOperatingState(MachineState.DISPENSING_SELECTED_ITEM_AND_CUSTOMER_CHANGE);

            return new Pair<>(dispensedSnackItem, customerChange);
        } catch (InsufficientChangeException insufficientChangeException) {
            return new Pair<>(null, this.refund());
        }
    }

    /**
     * Calculates and returns amount to be refunded to customer
     * upon cancellation of request OR the machine's not being able
     * to produce customer's change upon successful purchase of item.
     *
     * @return Refunded Amount in form of Map<Payable, Integer>
     */
    @Override
    public Map<Payable, Integer> refund() {
        this.setCurrentlyOperatingState(MachineState.REFUNDING_CUSTOMER_MONEY);
        Map<Payable, Integer> refundAmount = this.changeInventory.getChange(this.accumulatedMoney);
        this.changeInventory.reflectInventoryDeductionsForChange(refundAmount);

        this.currentlySelectedSnackSlot = null;
        this.accumulatedMoney = BigDecimal.valueOf(0);
        return refundAmount;
    }

    @Override
    public void resetToInitialState() {
        this.clearAllSnackSlots();
        this.displayScreen.clear();
        this.changeInventory.clear();

        this.currentlySelectedSnackSlot = null;
        this.salesTotal = BigDecimal.valueOf(0);
        this.accumulatedMoney = BigDecimal.valueOf(0);
    }

    public void clearAllSnackSlots() {
        for (SnackSlot[] snackSlot : this.snackSlots) {
            for (SnackSlot slot : snackSlot) {
                slot.setItem(null);
                slot.setQuantity(0);
            }
        }
    }

    private boolean isSelectedItemFullyPaid() {
        BigDecimal selectedItemPrice = this.currentlySelectedSnackSlot
                .getItem()
                .getPrice();

        return this.accumulatedMoney.compareTo(selectedItemPrice) >= 0;
    }

    public void printCurrentlyOperatingState() {
        System.out.println(this.currentlyOperatingState.getDescription());
    }

    public void printCurrentlyAccumulatedMoney() {
        System.out.println("Balance - " + this.getAccumulatedMoney().toString() + "$");
    }

    private void printCurrentlySelectedSnackItemInformation() {
        SnackItem selectedSnackItem = this.currentlySelectedSnackSlot.getItem();
        System.out.println("- " + selectedSnackItem.getName());
        System.out.println("$ " + selectedSnackItem.getPrice());
    }

    private void printCalculatedCustomerChange(Map<Payable, Integer> change) {
        if (change.isEmpty()) {
            System.out.println("No Change! Thanks for Buying from Us.");
            return;
        }

        for (Payable payable: change.keySet()) {
            System.out.println(payable.getWorth() + " X " + change.get(payable));
        }
    }

    public void printMachineStats() {
        System.out.println("<<Accumulated Money>> $" + this.accumulatedMoney.toString());
        System.out.println("<<Sales Total>> $" + this.salesTotal.toString());
        System.out.println("<<Change Inventory>>");

        for (Payable payable: this.changeInventory.getInventory().keySet()) {
            System.out.println(payable.getWorth().toString() + " -> " + this.changeInventory.getCountOfPayable(payable));
        }
    }

    private void initializeSnackSlots() {
        this.snackSlots = new SnackSlot[this.rowsCount][this.columnsCount];

        for (int rowIndex = 0; rowIndex < this.rowsCount; rowIndex++) {
            for (int colIndex = 0; colIndex < this.columnsCount; colIndex++) {
                this.snackSlots[rowIndex][colIndex] = new SnackSlot();
            }
        }
    }
}
