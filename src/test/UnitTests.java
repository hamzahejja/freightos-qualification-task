package test;

import components.Button;
import components.SnackSlot;
import core.SnackVendingMachine;
import enumerations.*;
import exception.*;
import org.junit.*;
import interfaces.Payable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

public class UnitTests {
    private static SnackVendingMachine snackVendingMachine;

    @BeforeClass
    public static void setUp() {
        snackVendingMachine = new SnackVendingMachine();
    }

    @AfterClass
    public static void tearDown() {
        snackVendingMachine.printMachineStats();
        snackVendingMachine = null;
    }

    @Test(expected = InvalidEntryException.class)
    public void Should_ThrowInvalidEntryException_When_InsertingCoinIntoNoteSlot(){
        /**
         * Throws InvalidEntryException when
         * trying to insert coin(s) into the Snack
         * Vending Machine's Note Slot.
         */
        snackVendingMachine.insertMoney(snackVendingMachine.getNoteSlot(), Coin.TEN_CENTS);
    }

    @Test(expected = InvalidEntryException.class)
    public void Should_ThrowInvalidEntryException_When_InsertingCoinIntoCardSlot(){
        /**
         * Throws InvalidEntryException when
         * trying to insert coin(s) into the Snack
         * Vending Machine's Card Slot.
         */
        snackVendingMachine.insertMoney(snackVendingMachine.getCardSlot(), Coin.TEN_CENTS);
    }

    @Test(expected = InvalidEntryException.class)
    public void Should_ThrowInvalidEntryException_When_InsertingNotesIntoCardSlot(){
        /**
         * Throws InvalidEntryException when
         * trying to insert note(s) into the Snack
         * Vending Machine's Card Slot.
         */
        snackVendingMachine.insertMoney(snackVendingMachine.getCardSlot(), Note.FIFTY_DOLLARS_BILL);
    }

    @Test(expected = InvalidEntryException.class)
    public void Should_ThrowInvalidEntryException_When_InsertingCardIntoNoteSlot(){
        /**
         * Throws InvalidEntryException when
         * trying to insert card(s) into the Snack
         * Vending Machine's Note Slot.
         */
        Card visa = new Card("4002123456789999", "VISA_CARD", 100);
        snackVendingMachine.insertMoney(snackVendingMachine.getNoteSlot(), visa);
    }

    @Test(expected = CustomerRequestNotConfirmedException.class)
    public void Should_ThrowCustomerRequestNotConfirmedException_When_ConfirmButtonNotPressed() {
        snackVendingMachine.getDisplayScreen().clear();
        int rowIndex = snackVendingMachine.getKeypad().pressButton(Button.C);
        int colIndex = snackVendingMachine.getKeypad().pressButton(Button.DIGIT_TWO);

        /** Button.CONFIRM Was NOT Pressed, Hence Customer Request Not Confirmed. **/
        snackVendingMachine.processRequestAndReturnSelectedSnackItem(snackVendingMachine.getSnackSlots()[rowIndex][colIndex]);
    }

    @Test(expected = FullSnackSlotException.class)
    public void Should_ThrowFullSnackSlotException_When_AddingSnackItemToFullSlot() {
        Arrays.fill(snackVendingMachine.getSnackSlots()[0], new SnackSlot(SnackItem.BAGEL, 5, 5));

        /** FullSnackSlotException should be thrown **/
        snackVendingMachine.getSnackSlots()[0][0].addSnackItems(SnackItem.BAGEL, SnackItem.BAGEL);
    }

    @Test(expected = UnsupportedPayableTypeException.class)
    public void Should_ThrowUnsupportedPayableTypeException_When_InsertingUnsupportedNote() {
        Payable[] nonAllowedNotes = { Note.HUNDRED_DOLLARS_BILL, Note.TEN_DOLLARS_BILL, Note.FIVE_DOLLARS_BILL};

        snackVendingMachine.insertMoney(
                snackVendingMachine.getNoteSlot(),
                nonAllowedNotes[new Random().nextInt(nonAllowedNotes.length)]
        );
    }

    @Test
    public void Should_ResetInitialStateOfSnackVendingMachine_When_ResetButtonIsPressed() {
        snackVendingMachine.getKeypad().pressButton(Button.RESET);

        Assert.assertNull(snackVendingMachine.getCurrentlySelectedSnackSlot());
        Assert.assertEquals(0, snackVendingMachine.getSalesTotal().compareTo(BigDecimal.ZERO));
        Assert.assertEquals(0, snackVendingMachine.getAccumulatedMoney().compareTo(BigDecimal.ZERO));
        Assert.assertEquals(MachineState.RESETTING_INITIAL_STATE, snackVendingMachine.getCurrentlyOperatingState());

        boolean hasEmptySnackSlots =  Arrays.stream(snackVendingMachine.getSnackSlots())
                .allMatch(slots -> Arrays.stream(slots).allMatch(s -> s.getQuantity() == 0 && s.getItem() == null));

        Assert.assertTrue(hasEmptySnackSlots);
    }


}
