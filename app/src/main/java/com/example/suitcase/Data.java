package com.example.suitcase;


public class Data {
    private String title;
    private int imageResId;

    public Data(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }

}
