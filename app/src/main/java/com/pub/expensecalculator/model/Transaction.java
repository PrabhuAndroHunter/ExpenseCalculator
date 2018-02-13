package com.pub.expensecalculator.model;

/**
 * Created by prabhu on 7/2/18.
 */

public class Transaction {
    private final String TAG = Transaction.class.toString();
    int id;
    String date, description, category, source, type;
    double amount, balance;

    public static final String TYPE_DEBIT = "debit", TYPE_CREDIT = "credit", TYPE_FUTURE = "FUTURE";

    public Transaction(String date, String description, String category, String source, String type, double amount) {
        this.date = date;
        this.description = description;
        this.category = category;
        this.source = source;
        this.type = type;
        this.amount = amount;
    }

    public Transaction(int id, String date, String description, String category, String source, String type, double amount, double balance) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.category = category;
        this.source = source;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
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

    public String getCategory() {
        return category;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getSignedAmount() {
        if ((getType()).equalsIgnoreCase(TYPE_CREDIT))
            return "+" + amount;
        else
            return "-" + amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
