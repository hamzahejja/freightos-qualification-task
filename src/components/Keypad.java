package components;

import core.SnackVendingMachine;

import java.util.Arrays;

public class Keypad {
    private Button[] buttons;
    private SnackVendingMachine snackVendingMachine;

    public Keypad() {
        this.buttons = new Button[] {
                Button.A,
                Button.B,
                Button.C,
                Button.D,
                Button.E,
                Button.DIGIT_ONE,
                Button.DIGIT_TWO,
                Button.DIGIT_THREE,
                Button.DIGIT_FOUR,
                Button.DIGIT_FIVE,
                Button.DELETE,
                Button.CLEAR,
                Button.CONFIRM,
                Button.RESET,
        };

        this.snackVendingMachine = null;
    }

    public Keypad(SnackVendingMachine snackVendingMachine) {
        this.snackVendingMachine = snackVendingMachine;
    }

    public Keypad(Button[] buttons, SnackVendingMachine snackVendingMachine) {
        this.buttons = Arrays.copyOf(buttons, buttons.length);
        this.snackVendingMachine = snackVendingMachine;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public void setButtons(Button[] buttons) {
        this.buttons = buttons;
    }

    public SnackVendingMachine getSnackVendingMachine() {
        return snackVendingMachine;
    }

    public void setSnackVendingMachine(SnackVendingMachine snackVendingMachine) {
        this.snackVendingMachine = snackVendingMachine;
    }

    public int pressButton (Button button) {
        SnackVendingMachine snackVendingMachine = this.getSnackVendingMachine();
        DisplayScreen displayScreen = snackVendingMachine.getDisplayScreen();
        displayScreen.appendText(button.getLabel());

        if (button.getIndex() == Button.DELETE.getIndex()) {
            displayScreen.delete();
        } else if (button.getIndex() == Button.CLEAR.getIndex()) {
            displayScreen.clear();
        } else if (button.getIndex() == Button.CONFIRM.getIndex()) {
            snackVendingMachine.setShouldStartProcessingRequest(true);
        } else if (button.getIndex() == Button.RESET.getIndex()) {
            snackVendingMachine.resetToInitialState();
        } else if (button.getIndex() == Button.CANCEL.getIndex()) {
            snackVendingMachine.cancelRequestAndRefundCustomer();
        }

        return button.getIndex();
    }
}
