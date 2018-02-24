package com.pub.expensecalculator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.pub.expensecalculator.utils.Constants;

import java.io.File;

/**
 * Created by prabhu on 24/1/18.
 */

public class TablesClass extends SQLiteOpenHelper {
    private final String TAG = TablesClass.class.toString();

    /**
     * Write all create table statements here in this class on oncreate method
     * If any changes in table structure go for onUpgrade method
     */

    Context context;

    public TablesClass(Context context, String DatabaseName, String nullColumnHack, int databaseVersion) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
       /* super(context, Environment.getExternalStorageDirectory()
                + File.separator + "PRABHU"
                + File.separator + Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()
                + File.separator + "PRABHU"
                + File.separator + Constants.DATABASE_NAME, null);
        this.context = context;*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constants.TABLE1);
        db.execSQL(Constants.TABLE2);
        db.execSQL(Constants.TABLE3);
        db.execSQL(Constants.TABLE4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "onUpgrade:  DateBase is upgraded to " + newVersion);
        context.deleteDatabase(Constants.DATABASE_NAME);
        onCreate(db);
    }
}