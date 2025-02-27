package com.example.suitcase;

public class HealthSupply {
    private String name;
    private int quantity;
    private boolean isPacked;
    private boolean packed;

    public HealthSupply(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public void setPacked(boolean packed) {
        this.packed = packed;
    }

    public boolean isPacked() {
        return packed;
    }
}
