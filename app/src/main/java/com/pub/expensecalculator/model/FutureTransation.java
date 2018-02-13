package com.pub.expensecalculator.model;

/**
 * Created by prabhu on 13/2/18.
 */

public class FutureTransation {
    private final String TAG = FutureTransation.class.toString();
    int id;
    String date, description,type;
    double amount;
    boolean reminder;
    public static final String TYPE_FUTURE = "FUTURE";

    public FutureTransation(String date, String description, String type, double amount, boolean reminder) {
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.reminder = reminder;
    }

    public FutureTransation(int id, String date, String description, String type, double amount, boolean reminder) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.reminder = reminder;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isReminder() {
        return reminder;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }
}
