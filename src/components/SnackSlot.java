package components;

import enumerations.ExceptionMessage;
import enumerations.SnackItem;
import exception.EmptySnackSlotException;
import exception.FullSnackSlotException;

public class SnackSlot {
    private SnackItem item;
    private int quantity;
    private int capacity;

    public SnackSlot() { }

    public SnackSlot(SnackItem item, int quantity, int capacity) {
        this.item = item;
        this.quantity = quantity;
        this.capacity = capacity;
    }

    public SnackItem getItem() {
        return item;
    }

    public void setItem(SnackItem item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void addSnackItems(SnackItem... items) {
        if (this.quantity + items.length > this.capacity) {
            throw new FullSnackSlotException(ExceptionMessage.FULL_SNACK_SLOT.getMessage());
        }

        this.setQuantity(this.getQuantity() + items.length);
    }

    public void dispenseSnackItem() throws EmptySnackSlotException {
        if (this.quantity == 0) {
            throw new EmptySnackSlotException(ExceptionMessage.EMPTY_SNACK_SLOT.getMessage());
        }

        this.quantity -= 1;
    }

    @Override
    public String toString() {
        return "components.SnackSlot{" +
                "item=" + item +
                ", quantity=" + quantity +
                ", capacity=" + capacity +
                '}';
    }
}
