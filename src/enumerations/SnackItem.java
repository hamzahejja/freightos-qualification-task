package enumerations;

import java.math.BigDecimal;

public enum SnackItem {
    BAGEL("BAGEL", BigDecimal.valueOf(1.5)),
    DONUT("DONUT", BigDecimal.valueOf(2.5)),
    OATS_BAR("OATS_BAR", BigDecimal.valueOf(2.0)),
    PEANUTS("PEANUTS", BigDecimal.valueOf(3)),
    TURKEY_SANDWICH("TURKEY_SANDWICH" , BigDecimal.valueOf(3.5));

    private String name;
    private BigDecimal price;

    SnackItem(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "enumerations.SnackItem{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
