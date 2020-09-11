package components;

public class DisplayScreen {
    private StringBuilder textBuilder;

    public DisplayScreen() {
        this.textBuilder = new StringBuilder("");
        this.display();
    }

    public DisplayScreen(String text) {
        this.textBuilder = new StringBuilder(text);
        this.display();
    }

    public DisplayScreen(StringBuilder textBuilder) {
        this.textBuilder = textBuilder;
        this.display();
    }

    public void appendText(String text) {
        this.textBuilder.append(text);
        this.display();
    }

    public void delete() {
        this.textBuilder = new StringBuilder(this.textBuilder.substring(0, this.textBuilder.length() - 1));
        this.display();
    }

    public void clear() {
        this.textBuilder.replace(0, textBuilder.length(), "");
        this.display();
    }

    public void display() {
        System.out.println(getDisplayedText());
    }

    public StringBuilder getTextBuilder() {
        return textBuilder;
    }

    public void setTextBuilder(StringBuilder textBuilder) {
        this.textBuilder = textBuilder;
        this.display();
    }

    public String getDisplayedText() {
        return getTextBuilder().toString();
    }
}
