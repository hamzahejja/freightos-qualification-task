package enumerations;

import interfaces.Payable;

import java.math.BigDecimal;

public class Card implements Payable {
    private String number;
    private String type;
    private double balance;

    public Card(String number, String type, double balance) {
        this.number = number;
        this.type = type;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public BigDecimal getWorth() {
        return BigDecimal.valueOf(balance);
    }

    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", balance=" + balance +
                '}';
    }
}
