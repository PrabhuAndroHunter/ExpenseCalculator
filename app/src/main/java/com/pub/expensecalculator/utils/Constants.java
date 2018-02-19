package com.pub.expensecalculator.utils;

import com.pub.expensecalculator.R;

/**
 * Created by prabhu on 24/1/18.
 */

public class Constants {
    public static final String DATABASE_NAME = "transaction.db";

    public static final int DATABASE_VERSION = 2;
    public static final String TRANSACTION_TABLE = "transaction_table";
    public static final String FUTURE_TRANSACTION_TABLE = "future_transaction_table";
    public static final String TRANSACTION_CATEGORY_TABLE = "transaction_category_table";
    public static final String TRANSACTION_SOURCE_TABLE = "transaction_source_table";

    public static final String ID = "id";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_DESCRIPTION = "transaction_description";
    public static final String TRANSACTION_CATEGORY = "transaction_category";
    public static final String TRANSACTION_SOURCE = "transaction_source";   // cash, check, netbanking, paytm
    public static final String TRANSACTION_TYPE = "transaction_type";       // credit or debit
    public static final String TRANSACTION_TYPE_DEBIT = "transaction_type_debit";
    public static final String TRANSACTION_TYPE_CREDIT = "transaction_type_credit";
    public static final String TRANSACTION_TYPE_CREDIT_DEBIT = "transaction_type_credit_credit_debit";
    public static final String TRANSACTION_AMOUNT = "transaction_amount";
    public static final String BALANCE = "balance";
    public static final String SET_REMINDER = "set_reminder";
    public static final String CATEGORY_TITLE = "category_title";
    public static final String CATEGORY_LOGO = "category_logo";
    public static final String SOURCE_TITLE = "source_title";
    public static final String SOURCE_LOGO = "source_logo";

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

    public static final String TABLE3 = "CREATE TABLE " + TRANSACTION_CATEGORY_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CATEGORY_TITLE + " text,"
            + TRANSACTION_TYPE_CREDIT + " INTEGER,"
            + TRANSACTION_TYPE_DEBIT + " INTEGER,"
            + CATEGORY_LOGO + " BLOB"
            + " ); ";

    public static final String TABLE4 = "CREATE TABLE " + TRANSACTION_SOURCE_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SOURCE_TITLE + " text,"
            + TRANSACTION_TYPE_CREDIT + " INTEGER,"
            + TRANSACTION_TYPE_DEBIT + " INTEGER,"
            + SOURCE_LOGO + " BLOB"
            + " ); ";


    // appActivity Constants
    public static final String PERIOD = "PERIOD";
    public static final String ALL_TRANSACTION = "ALL_TRANSACTION";
    public static final String ONE_MONTH = "ONE_MONTH";
    public static final String THREE_MONTH = "THREE_MONTH";
    public static final String SIX_MONTH = "SIX_MONTH";
    public static final String CUSTOM_PEROID = "CUSTOM_PEROID";

}

