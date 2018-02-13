package com.pub.expensecalculator.utils;

/**
 * Created by prabhu on 24/1/18.
 */

public class Constants {
    public static final String DATABASE_NAME = "transaction.db";

    public static final int DATABASE_VERSION = 2;
    public static final String TRANSACTION_TABLE = "transaction_table";
    public static final String FUTURE_TRANSACTION_TABLE = "future_transaction_table";

    public static final String ID = "id";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_DESCRIPTION = "transaction_description";
    public static final String TRANSACTION_CATEGORY = "transaction_category";
    public static final String TRANSACTION_SOURCE = "transaction_source";   // cash, check, netbanking, paytm
    public static final String TRANSACTION_TYPE = "transaction_type";       // credit or debit
    public static final String TRANSACTION_AMOUNT = "transaction_amount";
    public static final String BALANCE = "balance";
    public static final String SET_REMINDER = "set_reminder";

    // tables
    public static final String TABLE1 = "CREATE TABLE " + TRANSACTION_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TRANSACTION_DATE + " text,"
            + TRANSACTION_DESCRIPTION + " text,"
            + TRANSACTION_CATEGORY + " text,"
            + TRANSACTION_SOURCE + " text,"
            + TRANSACTION_TYPE + " text,"
            + TRANSACTION_AMOUNT + " REAL,"
            + BALANCE + " REAL"
            + " ); ";

    public static final String TABLE2 = "CREATE TABLE " + FUTURE_TRANSACTION_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TRANSACTION_DATE + " text,"
            + TRANSACTION_DESCRIPTION + " text,"
            + TRANSACTION_TYPE + " text,"
            + TRANSACTION_AMOUNT + " REAL,"
            + SET_REMINDER + " INTEGER"
            + " ); ";
}

