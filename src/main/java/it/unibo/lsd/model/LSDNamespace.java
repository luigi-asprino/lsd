package it.unibo.lsd.model;

public class LSDNamespace {

    private String prefix;
    private String value;

    public LSDNamespace(String prefix, String value) {
        this.prefix = prefix;
        this.value = value;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
