package com.example.suitcase;

public class Gadget {

    private String name;
    private String description;
    private boolean isChecked;

    public Gadget(String name, String description) {
        this.name = name;
        this.description = description;
        this.isChecked = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
