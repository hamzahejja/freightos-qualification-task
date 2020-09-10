package core;

import components.*;
import enumerations.MachineState;
import enumerations.SnackItem;
import exception.CustomerRequestNotConfirmedException;
import exception.InsufficientChangeException;
import exception.ItemNotFullyPaidException;
import exception.SnackSoldOutException;
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
        this.rowsCount = 5;
        this.columnsCount = 5;
        this.isFunctional = true;
        this.initializeSnackSlots();
        this.shouldStartProcessingRequest = false;
        this.setCurrentlyOperatingState(MachineState.WAITING_CUSTOMER_ENTRY);

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
        isFunctional = functional;
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

    public SnackItem processRequestAndReturnSelectedSnackItem(
            SnackSlot selectedSnackSlot
    ) throws CustomerRequestNotConfirmedException, SnackSoldOutException {
        this.currentlySelectedSnackSlot = selectedSnackSlot;
        this.setCurrentlyOperatingState(MachineState.PROCESSING_CUSTOMER_ENTRY);

        if (! this.shouldStartProcessingRequest) {
            throw new CustomerRequestNotConfirmedException("Sorry! Did you forget to Confirm Your Selection?");
        } else if (selectedSnackSlot.getQuantity() == 0) {
            throw new SnackSoldOutException("SOLD OUT! Selected Snack Slot is out of items");
        }

        this.shouldStartProcessingRequest = false;
        this.printCurrentlySelectedSnackItemInformation();

        return this.currentlySelectedSnackSlot.getItem();
    }

    public void insertMoney(MoneySlot moneySlot, Payable payable) {
        this.setCurrentlyOperatingState(MachineState.VALIDATING_INSERTED_MONEY);

        moneySlot.validate(payable);
        this.setCurrentlyOperatingState(MachineState.ACCEPTED_INSERTED_MONEY);
        this.accumulatedMoney = this.accumulatedMoney.add(payable.getWorth());
        this.changeInventory.add(payable, 1);
        this.printCurrentlyAccumulatedMoney();
    }

    private SnackItem dispenseSelectedSnackItem() throws ItemNotFullyPaidException, InsufficientChangeException {
        SnackItem selectedSnackItem = this.currentlySelectedSnackSlot.getItem();

        if (! this.isSelectedItemFullyPaid()) {
            throw new ItemNotFullyPaidException("Sorry! Selected Snack Item NOT FULLY PAID.");
        } else if (! this.changeInventory.canProduceChangeForAmount(this.accumulatedMoney.subtract(selectedSnackItem.getPrice()))) {
            throw new InsufficientChangeException("Insufficient Change! Try to select another snack item Please.");
        }

        this.setCurrentlyOperatingState(MachineState.DISPENSING_SELECTED_ITEM);
        this.currentlySelectedSnackSlot.dispenseSnackItem();
        this.salesTotal = this.salesTotal.add(selectedSnackItem.getPrice());

        return selectedSnackItem;
    }

    private Map<Payable, Integer> calculateAndDispenseChange() {
        this.setCurrentlyOperatingState(MachineState.CALCULATING_CUSTOMER_CHANGE);

        BigDecimal selectedSnackItemPrice = this.currentlySelectedSnackSlot.getItem().getPrice();
        Map<Payable, Integer> change = this.changeInventory.getChange(this.accumulatedMoney.subtract(selectedSnackItemPrice));
        this.getChangeInventory().reflectInventoryDeductionsForChange(change);

        this.currentlySelectedSnackSlot = null;
        this.accumulatedMoney = BigDecimal.valueOf(0);
        this.setCurrentlyOperatingState(MachineState.DISPENSING_CUSTOMER_CHANGE);

        return change;
    }

    @Override
    public Pair<SnackItem, Map<Payable, Integer>> dispenseSelectedItemAndCustomerChange() {
        try {
            SnackItem dispensedSnackItem = this.dispenseSelectedSnackItem();
            Map<Payable, Integer> customerChange = this.calculateAndDispenseChange();
            this.printCalculatedCustomerChange(customerChange);

            return new Pair<>(dispensedSnackItem, customerChange);
        } catch (InsufficientChangeException insufficientChangeException) {
            return new Pair<>(null, this.refund());
        }
    }

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
        this.setCurrentlyOperatingState(MachineState.RESETTING_INITIAL_STATE);

        this.clearAllSnackSlots();
        this.displayScreen.clear();
        this.changeInventory.clear();

        this.currentlySelectedSnackSlot = null;
        this.salesTotal = BigDecimal.valueOf(0);
        this.accumulatedMoney = BigDecimal.valueOf(0);
    }

    public void clearAllSnackSlots() {
        for (int rowIndex = 0; rowIndex < this.snackSlots.length; rowIndex++) {
            for (int colIndex = 0; colIndex < this.snackSlots[rowIndex].length; colIndex++) {
                this.snackSlots[rowIndex][colIndex].setItem(null);
                this.snackSlots[rowIndex][colIndex].setQuantity(0);
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
