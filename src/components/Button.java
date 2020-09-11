package components;

public enum Button {
    A("A", 0),
    B("B", 1),
    C("C", 2),
    D("D", 3),
    E("E", 4),
    DIGIT_ONE("1", 0),
    DIGIT_TWO("2", 1),
    DIGIT_THREE("3", 2),
    DIGIT_FOUR("4", 3),
    DIGIT_FIVE("5", 4),
    CONFIRM("CONFIRM", -1),
    CLEAR("CLEAR", -2),
    DELETE("DELETE", -3),
    RESET("RESET", -4),
    CANCEL("CANCEL", -5);

    private String label;
    private int index;

    private Button(String label, int index) {
        this.label = label;
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "components.Button{" +
                "label=" + label +
                ", index=" + index +
                "}";
    }
}
