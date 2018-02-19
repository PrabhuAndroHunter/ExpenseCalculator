package com.pub.expensecalculator.model;

/**
 * Created by prabhu on 16/2/18.
 */

public class Source {
    int id;
    String title;
    byte[] logo;

    public Source(int id, String title, byte[] logo) {
        this.id = id;
        this.title = title;
        this.logo = logo;
    }

    public Source(String title, byte[] logo) {
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
