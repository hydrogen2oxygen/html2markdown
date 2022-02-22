package net.hydrogen2oxygen.html2markdown.generator;

public class MarkdownObject {

    private MarkdownEnum type;
    private String value;

    public MarkdownObject(MarkdownEnum type, String value) {
        this.type = type;
        this.value = value;
    }

    public MarkdownEnum getType() {
        return type;
    }

    public void setType(MarkdownEnum type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
