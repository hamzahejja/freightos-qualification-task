package enumerations;

import interfaces.Payable;

import java.math.BigDecimal;

public enum Note implements Payable {
    TWO_DOLLARS_BILL("TWO_DOLLARS", BigDecimal.valueOf(2)),
    FIVE_DOLLARS_BILL("FIVE_DOLLARS", BigDecimal.valueOf(5)),
    TEN_DOLLARS_BILL("TEN_DOLLARS", BigDecimal.valueOf(10)),
    TWENTY_DOLLARS_BILL("TWENTY_DOLLARS", BigDecimal.valueOf(20)),
    FIFTY_DOLLARS_BILL("FIFTY_DOLLARS", BigDecimal.valueOf(50)),
    HUNDRED_DOLLARS_BILL("HUNDRED_DOLLARS", BigDecimal.valueOf(100));

    private String name;
    private BigDecimal worth;

    private Note(String name, BigDecimal worth) {
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
        return "enumerations.Note{" +
                "name='" + name + '\'' +
                ", worth=" + worth +
                '}';
    }
}
