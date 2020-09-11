package enumerations;

import interfaces.Payable;

import java.math.BigDecimal;

public enum Coin implements Payable {
    ONE_CENT("PENNY", BigDecimal.valueOf(0.01)), // PENNY IS WORTH 1 CENT, WIDE CIRCULATION IN THE U.S
    FIVE_CENTS("NICKEL", BigDecimal.valueOf(0.05)), // NICKEL IS WORTH 5 CENTS, WIDE CIRCULATION IN THE U.S
    TEN_CENTS("DIME", BigDecimal.valueOf(0.10)), // DIME IS WORTH 10 CENTS OR 10 PENNIES, WIDE CIRCULATION IN THE U.S.
    TWENTY_FIVE_CENTS("QUARTER", BigDecimal.valueOf(0.25)), // QUARTER-DOLLAR IS WORTH 25 CENTS OR 25 PENNIES, WIDE CIRCULATION IN THE U.S
    FIFTY_CENTS("HALF", BigDecimal.valueOf(0.50)), // HALF-DOLLAR COIN IS WORTH 50 CENTS OR 50 PENNIES, WIDE CIRCULATION IN THE U.S.
    ONE_DOLLAR("DOLLAR", BigDecimal.valueOf(1.0));

    private String name;
    private BigDecimal worth;

    Coin(String name, BigDecimal worth) {
        this.name = name;
        this.worth = worth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWorth() {
        return worth;
    }

    public void setWorth(BigDecimal worth) {
        this.worth = worth;
    }

    @Override
    public String toString() {
        return "enumerations.Coin{" +
                "name='" + name + '\'' +
                ", worth=" + worth +
                '}';
    }
}
