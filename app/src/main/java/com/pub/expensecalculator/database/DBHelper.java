package com.pub.expensecalculator.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.pub.expensecalculator.model.FutureTransation;
import com.pub.expensecalculator.model.Transaction;
import com.pub.expensecalculator.utils.Constants;
import com.pub.expensecalculator.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 24/1/18.
 * <p>
 * This class manages database Operations
 */

public class DBHelper {
    private final String TAG = DBHelper.class.toString();
    private SQLiteDatabase db;
    private SharedPreferences sharedPreferences;
    private final Context context;
    private final TablesClass dbHelper;
    public static int no;
    public static DBHelper db_helper = null;

    int total_transactions;
    double total_amount = 0;

    /*
    *
    * This method will return instance of DBHelper class
    * @param context to use to open or create the database
    *
    * */
    public static DBHelper getInstance(Context context) {
        try {
            if (db_helper == null) {
                db_helper = new DBHelper(context);
                db_helper.open();
            }
        } catch (IllegalStateException e) {
            //db_helper already open
        }
        return db_helper;
    }

    /*
     * set context of the class and initialize TableClass Object
     * @param context to use to open or create the database
     *
	 */

    public DBHelper(Context c) {
        context = c;
        sharedPreferences = SharedPrefManager.getSharedPref(c);
        dbHelper = new TablesClass(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    /*
     * close databse.
     */
    public void close() {
        if (db.isOpen()) {
            db.close();
        }
    }

    public boolean dbOpenCheck() {
        try {
            return db.isOpen();
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * open database
     */
    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            Log.v("open database Exception", "error==" + e.getMessage());
            db = dbHelper.getReadableDatabase();
        }
    }

    public double getBalance() {
        double balance = 0.0;
        Log.d(TAG, "getBalance: ");
        total_transactions = getTotalTransactions(Constants.TRANSACTION_TABLE, null);
        Log.d(TAG, "getBalance: total_transactions : " + total_transactions);
        if (total_transactions != 0) {
            Cursor cursor = db.rawQuery("SELECT " + Constants.BALANCE + " FROM " + Constants.TRANSACTION_TABLE + " WHERE " + Constants.ID + " = (SELECT MAX ( " + Constants.ID + " )" + " FROM " + Constants.TRANSACTION_TABLE + ");", null);
            cursor.moveToFirst();
            balance = cursor.getDouble(cursor.getColumnIndex(Constants.BALANCE));
            Log.d(TAG, "getBalance: " + balance);
        }
        return balance;
    }

    public long addTransaction(ContentValues values) {
        String tType = values.getAsString(Constants.TRANSACTION_TYPE);
        if (tType.equalsIgnoreCase(Transaction.TYPE_CREDIT)) {
            Log.d(TAG, "addTransaction: Credit");
            total_amount = getBalance() + values.getAsDouble(Constants.TRANSACTION_AMOUNT);
        } else {
            Log.d(TAG, "addTransaction: Debit");
            total_amount = getBalance() - values.getAsDouble(Constants.TRANSACTION_AMOUNT);
        }
        values.put(Constants.BALANCE, total_amount);
        return insertContentVals(Constants.TRANSACTION_TABLE, values);
    }

    /*
    *
    * This method will insert records
    * @param tableName to use to insert into that specified TableName
    * @param content  this map contains the initial column values for the row.
    *                 The keys should be the column names and the values the column values
    *
    * */

    public long insertContentVals(String tableName, ContentValues content) {
        long id = 0;
        try {
            db.beginTransaction();
            id = db.insert(tableName, null, content);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return id;
    }

     /*
    *
    * This method will Get count of all records in a table as per the condition
    * @param table to use to search that specified TableName
    * @param where this is optional WHERE clause to apply when get records.
    *            Passing null will get all rows.
    *
    * */

    public int getTotalTransactions(String table, String where) {
        Cursor cursor = db.query(false, table, null, where, null, null, null, null, null);
        try {
            if (cursor != null) {
                cursor.moveToFirst();
                no = cursor.getCount();
                cursor.close();
            }
        } finally {
            cursor.close();
        }
        return no;
    }

    /*
    *
    * This will get all records from task_table
    *
    * */

    public List <Transaction> getTransactionsList() throws SQLException { // Creating method
        List <Transaction> transactionList = new ArrayList <Transaction>();

        //Cursor exposes results from a query on a SQLiteDatabase.
        Cursor cursor = db.query(false, Constants.TRANSACTION_TABLE,
                new String[]{Constants.ID,
                        Constants.TRANSACTION_DATE,
                        Constants.TRANSACTION_DESCRIPTION,
                        Constants.TRANSACTION_CATEGORY,
                        Constants.TRANSACTION_SOURCE,
                        Constants.TRANSACTION_TYPE,
                        Constants.TRANSACTION_AMOUNT,
                        Constants.BALANCE}, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int tId = cursor.getInt(cursor.getColumnIndex(Constants.ID));
                String tDate = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_DATE));
                String tDescription = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_DESCRIPTION));
                String tCategory = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_CATEGORY));
                String tSource = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_SOURCE));
                String tType = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_TYPE));
                double tAmount = cursor.getDouble(cursor.getColumnIndex(Constants.TRANSACTION_AMOUNT));
                double tBalance = cursor.getInt(cursor.getColumnIndex(Constants.BALANCE));
                transactionList.add(new Transaction(tId, tDate, tDescription, tCategory, tSource, tType, tAmount, tBalance));
            } while (cursor.moveToNext());
            cursor.close();
            return transactionList;
        }
        cursor.close();
        return transactionList; // Return statement
    }

    public List <Transaction> getLastTenTransactions() {
        List <Transaction> transactionList = new ArrayList <Transaction>();
        //Cursor exposes results from a query on a SQLiteDatabase.
        Cursor cursor = db.query(false, Constants.TRANSACTION_TABLE,
                new String[]{Constants.ID,
                        Constants.TRANSACTION_DATE,
                        Constants.TRANSACTION_DESCRIPTION,
                        Constants.TRANSACTION_CATEGORY,
                        Constants.TRANSACTION_SOURCE,
                        Constants.TRANSACTION_TYPE,
                        Constants.TRANSACTION_AMOUNT,
                        Constants.BALANCE}, null, null, null, null, Constants.ID + " DESC", "10");

        if (cursor.moveToFirst()) {
            do {
                int tId = cursor.getInt(cursor.getColumnIndex(Constants.ID));
                String tDate = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_DATE));
                String tDescription = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_DESCRIPTION));
                String tCategory = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_CATEGORY));
                String tSource = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_SOURCE));
                String tType = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_TYPE));
                double tAmount = cursor.getDouble(cursor.getColumnIndex(Constants.TRANSACTION_AMOUNT));
                double tBalance = cursor.getInt(cursor.getColumnIndex(Constants.BALANCE));
                transactionList.add(new Transaction(tId, tDate, tDescription, tCategory, tSource, tType, tAmount, tBalance));
            } while (cursor.moveToNext());
            cursor.close();
            return transactionList;
        }
        cursor.close();
        return transactionList;
    }

    /*
    *
    * This method will update records from TABLE : TRANSACTION_TABLE
    *
    * @param id is the transaction id will use to update records
    * @param values a map from column names to new column values. null is a
     *            valid value that will be translated to NULL.
    * */

    public int updateRecords(int id, ContentValues values) {
        Log.d(TAG, "updateRecords: ");
        int a = 0;
        try {
            db.beginTransaction();
            a = db.update(Constants.TRANSACTION_TABLE, values, Constants.ID + "=" + id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return a;
    }

     /*
    *
    * This method will update records from TABLE : TRANSACTION_TABLE
    *
    * @param id is the transaction id will use to update records
    * @param values a map from column names to new column values. null is a
     *            valid value that will be translated to NULL.
    * */

    public int updateFutureTransaction(int id, ContentValues values) {
        Log.d(TAG, "updateRecords: ");
        int a = 0;
        try {
            db.beginTransaction();
            a = db.update(Constants.FUTURE_TRANSACTION_TABLE, values, Constants.ID + "=" + id, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return a;
    }


    /*
    *
    *This method will delete task
    * @param id will use to delete
    *
    * */
    public boolean deleteAllTransactions(String condition) {
        return db.delete(Constants.TRANSACTION_TABLE, condition, null) > 0;
    }

    public boolean deleteFuturedTransaction(int id) {
        return db.delete(Constants.FUTURE_TRANSACTION_TABLE, Constants.ID + "=" + id, null) > 0;
    }

    public List <FutureTransation> getFutureTransactionList() throws SQLException { // Creating method
        List <FutureTransation> transactionList = new ArrayList <FutureTransation>();

        //Cursor exposes results from a query on a SQLiteDatabase.
        Cursor cursor = db.query(true, Constants.FUTURE_TRANSACTION_TABLE, new String[]{Constants.ID, Constants.TRANSACTION_DATE, Constants.TRANSACTION_DESCRIPTION,
                Constants.TRANSACTION_AMOUNT, Constants.TRANSACTION_TYPE, Constants.SET_REMINDER}, null, null, null, null, Constants.TRANSACTION_DATE + " DESC", null);
        if (cursor.moveToFirst()) {
            do {
                int tId = cursor.getInt(cursor.getColumnIndex(Constants.ID));
                String tDate = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_DATE));
                String tDescription = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_DESCRIPTION));
                String tType = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_TYPE));
                double tAmount = cursor.getDouble(cursor.getColumnIndex(Constants.TRANSACTION_AMOUNT));
                int setReminder = cursor.getInt(cursor.getColumnIndex(Constants.SET_REMINDER));
                boolean reminder;
                if (setReminder == 1)
                    reminder = true;
                else
                    reminder = false;
                transactionList.add(new FutureTransation(tId, tDate, tDescription, tType, tAmount, reminder));
            } while (cursor.moveToNext());
            cursor.close();
            return transactionList;
        }
        cursor.close();
        return transactionList; // Return statement
    }

     /*
    *
    * This method will get distinctdates based on order by 'DESC'
    *
    * */

    public List <String> getDistinctDates() {
        List <String> dates = new ArrayList <String>();
        Cursor cursor = db.query(true, Constants.FUTURE_TRANSACTION_TABLE, new String[]{Constants.TRANSACTION_DATE}, null, null, Constants.TRANSACTION_DATE, null, Constants.TRANSACTION_DATE + " DESC", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String taskDate = cursor.getString(cursor.getColumnIndex(Constants.TRANSACTION_DATE));
                    dates.add(taskDate);
                    Log.d(TAG, "getDistinctDates: " + taskDate);
                } while (cursor.moveToNext());
                cursor.close();
                return dates;
            }
            // Return statement
        }
        cursor.close();
        return dates;
    }
}