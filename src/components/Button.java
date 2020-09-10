package components;

public enum Button {
    A('A', 0),
    B('B', 1),
    C('C', 2),
    D('D', 3),
    E('E', 4),
    DIGIT_ONE('1', 0),
    DIGIT_TWO('2', 1),
    DIGIT_THREE('3', 2),
    DIGIT_FOUR('4', 3),
    DIGIT_FIVE('5', 4),
    CONFIRM('K', -1),
    CLEAR('C', -2),
    DELETE('D', -3),
    RESET('R', -4);

    private char label;
    private int index;

    private Button(char label, int index) {
        this.label = label;
        this.index = index;
    }

    public char getLabel() {
        return label;
    }

    public void setLabel(char label) {
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
                '}';
    }
}
