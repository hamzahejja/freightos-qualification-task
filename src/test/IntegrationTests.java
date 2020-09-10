package test;

import components.*;
import core.SnackVendingMachine;
import enumerations.Card;
import enumerations.Coin;
import enumerations.Note;
import enumerations.SnackItem;
import exception.ItemNotFullyPaidException;
import exception.SnackSoldOutException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.Pair;
import core.Payable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

/**
 * Author's Note:
 * Please be sure to add `JUnit 4` to classpath of project before running any tests in /test directory
 * [IntelliJ IDEA]:
 * You can do that by scrolling down to any of the test methods below (annotated with @Test)
 * and use Intellij's Quick-Fix prompt to add `JUnit 4` (The Testing Framework) to classpath.
 * If for any case you were not able to do that, go to file -> Project Structure -> Modules
 * and be sure to include/add the JARs: junit-4-12.jar and hamcrest-core-1.3 either from Source Folder where
 * you downloaded Java JDK OR you could fetch junit-4 and hamcrest-core 1.3 from Maven Repository.
 * For further information, please check: https://www.jetbrains.com/help/idea/testing.html#add-testing-libraries
 *
 */

public class IntegrationTests {
    private static SnackVendingMachine snackVendingMachine;

    @BeforeClass
    public static void setUp() {
        snackVendingMachine = new SnackVendingMachine();
        populateSnackSlotsWithInitialItems();
    }

    @AfterClass
    public static void tearDown() {
        snackVendingMachine.printMachineStats();
        snackVendingMachine = null;
    }

    @Test(expected = SnackSoldOutException.class)
    public void Should_ThrowSnackSoldOutException_When_SelectingOutOfStockItem() {
        snackVendingMachine.getDisplayScreen().clear();
        snackVendingMachine.getSnackSlots()[1][0].setQuantity(0); // Snack Slot Selection = B1

        int rowIndex = snackVendingMachine.getKeypad().pressButton(Button.B);
        int colIndex = snackVendingMachine.getKeypad().pressButton(Button.DIGIT_ONE);
        snackVendingMachine.getKeypad().pressButton(Button.CONFIRM);
        snackVendingMachine.processRequestAndReturnSelectedSnackItem(snackVendingMachine.getSnackSlots()[rowIndex][colIndex]);
    }

    @Test(expected = ItemNotFullyPaidException.class)
    public void Should_ThrowItemNotFullyPaidException_When_InsertedMoneyIsBelowItemPrice() {
        // C3 -> [2][2] -> PEANUTS = 3, QTY = 3
        int rowIndex = snackVendingMachine.getKeypad().pressButton(Button.C);
        int colIndex = snackVendingMachine.getKeypad().pressButton(Button.DIGIT_THREE);
        snackVendingMachine.getKeypad().pressButton(Button.CONFIRM);
        snackVendingMachine.processRequestAndReturnSelectedSnackItem(snackVendingMachine.getSnackSlots()[rowIndex][colIndex]);

        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.TEN_CENTS);
        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.TWENTY_FIVE_CENTS);
        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.FIFTY_CENTS);

        snackVendingMachine.dispenseSelectedItemAndCustomerChange();
    }

    @Test(expected = ItemNotFullyPaidException.class)
    public void Should_ThrowItemNotFullyPaidException_When_UsingCardWithInsufficientBalance() {
        // C5 -> [2][4] -> PEANUTS, PRICE = 3
        int rowIndex = snackVendingMachine.getKeypad().pressButton(Button.C);
        int colIndex = snackVendingMachine.getKeypad().pressButton(Button.DIGIT_FIVE);
        snackVendingMachine.getKeypad().pressButton(Button.CONFIRM);
        snackVendingMachine.processRequestAndReturnSelectedSnackItem(snackVendingMachine.getSnackSlots()[rowIndex][colIndex]);

        snackVendingMachine.insertMoney(snackVendingMachine.getCardSlot(), new Card("4002123456789900", "VISA", 1));
        snackVendingMachine.dispenseSelectedItemAndCustomerChange();
    }

    @Test
    public void Should_RefundAccumulatedMoneyAsChange_When_CustomerChangeIsNonProducible() {
        // RESET TO ISOLATE TESTS
        snackVendingMachine.getChangeInventory().clear();
        snackVendingMachine.setAccumulatedMoney(BigDecimal.valueOf(0));

        snackVendingMachine.getChangeInventory().add(Coin.TEN_CENTS, 3);
        snackVendingMachine.getChangeInventory().add(Coin.ONE_DOLLAR, 15);
        snackVendingMachine.getChangeInventory().add(Note.HUNDRED_DOLLARS_BILL, 2);
        snackVendingMachine.getChangeInventory().add(Note.TWENTY_DOLLARS_BILL, 2);

        // C3 -> [2][2] -> PEANUTS = 3
        int rowIndex = snackVendingMachine.getKeypad().pressButton(Button.C);
        int colIndex = snackVendingMachine.getKeypad().pressButton(Button.DIGIT_THREE);
        snackVendingMachine.getKeypad().pressButton(Button.CONFIRM);
        snackVendingMachine.processRequestAndReturnSelectedSnackItem(snackVendingMachine.getSnackSlots()[rowIndex][colIndex]);

        snackVendingMachine.insertMoney(snackVendingMachine.getNoteSlot(), Note.TWENTY_DOLLARS_BILL);
        BigDecimal tempAccumulatedMoney = snackVendingMachine.getAccumulatedMoney(); //BECAUSE REFUND WILL INTERNALLY RESET BALANCE TO 0

        Pair<SnackItem, Map<Payable, Integer>> result = snackVendingMachine.dispenseSelectedItemAndCustomerChange();
        BigDecimal totalRefundedAmount = calculateTotalChangeAmount(result.getSecond());

        Assert.assertNull(result.getFirst());
        Assert.assertEquals(0, totalRefundedAmount.compareTo(tempAccumulatedMoney));
    }

    @Test
    public void Should_DispenseItemWithNoChange_When_BalanceEqualsItemPrice() {
        // RESET STATS TO ISOLATE TEST
        snackVendingMachine.setSalesTotal(BigDecimal.valueOf(0));
        snackVendingMachine.setAccumulatedMoney(BigDecimal.valueOf(0));


        // E1 -> [4][0] -> TURKEY_SANDWICH = 3.5
        int rowIndex = snackVendingMachine.getKeypad().pressButton(Button.E);
        int colIndex = snackVendingMachine.getKeypad().pressButton(Button.DIGIT_ONE);
        snackVendingMachine.getKeypad().pressButton(Button.CONFIRM);

        SnackItem selectedSnackItem = snackVendingMachine.processRequestAndReturnSelectedSnackItem(
                snackVendingMachine.getSnackSlots()[rowIndex][colIndex]
        );

        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.ONE_DOLLAR);
        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.ONE_DOLLAR);
        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.FIFTY_CENTS);
        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.FIFTY_CENTS);
        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.FIFTY_CENTS);

        Pair<SnackItem, Map<Payable, Integer>> result = snackVendingMachine.dispenseSelectedItemAndCustomerChange();
        SnackItem dispensedSnackItem = result.getFirst();

        Assert.assertTrue(result.getSecond().isEmpty()); // NO CHANGE
        Assert.assertEquals(selectedSnackItem.getName(), dispensedSnackItem.getName());
        Assert.assertEquals(dispensedSnackItem.getPrice(), snackVendingMachine.getSalesTotal());
    }

    @Test
    public void Should_DispenseItemAndCustomerChange_When_BalanceExceedsPrice() {
        // RESET
        snackVendingMachine.setSalesTotal(BigDecimal.valueOf(0));
        snackVendingMachine.setAccumulatedMoney(BigDecimal.valueOf(0));

        // TOP-UP CHANGE INVENTORY
        snackVendingMachine.getChangeInventory().add(Coin.TEN_CENTS, 20);
        snackVendingMachine.getChangeInventory().add(Coin.ONE_DOLLAR, 20);
        snackVendingMachine.getChangeInventory().add(Coin.TWENTY_FIVE_CENTS, 20);
        snackVendingMachine.getChangeInventory().add(Note.HUNDRED_DOLLARS_BILL, 5);
        snackVendingMachine.getChangeInventory().add(Note.TWENTY_DOLLARS_BILL, 5);

        // E2 -> [4][1] -> TURKEY_SANDWICH = 3.5
        int rowIndex = snackVendingMachine.getKeypad().pressButton(Button.E);
        int colIndex = snackVendingMachine.getKeypad().pressButton(Button.DIGIT_TWO);
        snackVendingMachine.getKeypad().pressButton(Button.CONFIRM);

        SnackItem selectedSnackItem = snackVendingMachine.processRequestAndReturnSelectedSnackItem(
                snackVendingMachine.getSnackSlots()[rowIndex][colIndex]
        );

        snackVendingMachine.insertMoney(snackVendingMachine.getCoinSlot(), Coin.TEN_CENTS);
        snackVendingMachine.insertMoney(snackVendingMachine.getNoteSlot(), Note.FIFTY_DOLLARS_BILL);
        BigDecimal balance = snackVendingMachine.getAccumulatedMoney();

        Pair<SnackItem, Map<Payable, Integer>> result = snackVendingMachine.dispenseSelectedItemAndCustomerChange();
        SnackItem dispensedSnackItem = result.getFirst();
        BigDecimal totalChangeAmount = calculateTotalChangeAmount(result.getSecond());

        Assert.assertEquals(selectedSnackItem.getName(), dispensedSnackItem.getName());
        Assert.assertEquals(balance.subtract(dispensedSnackItem.getPrice()), totalChangeAmount);
        Assert.assertEquals(dispensedSnackItem.getPrice(), snackVendingMachine.getSalesTotal());
    }

    public BigDecimal calculateTotalChangeAmount(Map<Payable, Integer> change) {
        BigDecimal totalChangeAmount = BigDecimal.valueOf(0);

        for (Payable payable: change.keySet()) {
            totalChangeAmount = totalChangeAmount.add(payable.getWorth().multiply(BigDecimal.valueOf(change.get(payable))));
        }

        return totalChangeAmount;
    }

    public static void populateSnackSlotsWithInitialItems() {
        SnackSlot bagelsSnackSlot = new SnackSlot();
        bagelsSnackSlot.setCapacity(5);
        bagelsSnackSlot.setItem(SnackItem.BAGEL);
        bagelsSnackSlot.addSnackItems(SnackItem.BAGEL, SnackItem.BAGEL);

        SnackSlot donutsSnackSlot = new SnackSlot();
        donutsSnackSlot.setCapacity(3);
        donutsSnackSlot.setItem(SnackItem.DONUT);
        donutsSnackSlot.addSnackItems(SnackItem.DONUT, SnackItem.DONUT, SnackItem.DONUT);

        SnackSlot peanutsSnackSlot = new SnackSlot();
        peanutsSnackSlot.setCapacity(4);
        peanutsSnackSlot.setItem(SnackItem.PEANUTS);
        peanutsSnackSlot.addSnackItems(SnackItem.PEANUTS, SnackItem.PEANUTS, SnackItem.PEANUTS);

        SnackSlot oatsBarsSnackSlot = new SnackSlot();
        oatsBarsSnackSlot.setCapacity(5);
        oatsBarsSnackSlot.setItem(SnackItem.OATS_BAR);
        oatsBarsSnackSlot.addSnackItems(SnackItem.OATS_BAR);

        SnackSlot turkeySandwichSnackSlot = new SnackSlot();
        turkeySandwichSnackSlot.setCapacity(3);
        turkeySandwichSnackSlot.setItem(SnackItem.TURKEY_SANDWICH);
        turkeySandwichSnackSlot.addSnackItems(SnackItem.TURKEY_SANDWICH, SnackItem.TURKEY_SANDWICH);

        SnackSlot[] snackSlots = {
                bagelsSnackSlot,
                donutsSnackSlot,
                peanutsSnackSlot,
                oatsBarsSnackSlot,
                turkeySandwichSnackSlot
        };

        for (int rowIndex = 0; rowIndex < snackVendingMachine.getRowsCount(); rowIndex++) {
            Arrays.fill(snackVendingMachine.getSnackSlots()[rowIndex], snackSlots[rowIndex]);
        }
    }
}
