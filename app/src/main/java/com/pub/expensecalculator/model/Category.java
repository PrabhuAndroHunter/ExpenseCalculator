package com.pub.expensecalculator.model;

/**
 * Created by prabhu on 15/2/18.
 */

public class Category {
    int id;
    String title;
    byte[] logo;

    public Category(int id, String title, byte[] logo) {
        this.id = id;
        this.title = title;
        this.logo = logo;
    }

    public Category(String title, byte[] logo) {
        this.title = title;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public byte[] getLogo() {
        return logo;
    }
}
