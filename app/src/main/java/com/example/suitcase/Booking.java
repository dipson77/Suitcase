package com.example.suitcase;

public class Booking {
    private String type; // E.g., "Flight", "Hotel", "Car Rental"
    private String details; // Specific details like flight number, hotel name, etc.
    private boolean isCompleted; // To mark if the booking is completed

    public Booking(String type, String details) {
        this.type = type;
        this.details = details;
        this.isCompleted = false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
