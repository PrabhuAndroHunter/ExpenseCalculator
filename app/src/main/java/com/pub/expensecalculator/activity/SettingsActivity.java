package com.pub.expensecalculator.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.pub.expensecalculator.R;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.Constants;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = SettingsActivity.class.toString();
    View mResetView, mDeleteView;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout
        setContentView(R.layout.activity_settings);
        // get DbHelper instance
        dbHelper = CommonUtilities.getDBObject(this);
        // init all views
        mResetView = findViewById(R.id.layout_reset_all_category_source);
        mDeleteView = findViewById(R.id.layout_delete_all_transactions);
        // set onclick listeners to views
        mResetView.setOnClickListener(this);
        mDeleteView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.layout_reset_all_category_source:
                showResetDialogue();
                break;
            case R.id.layout_delete_all_transactions:
                showDeleteDialogue();
                break;
        }
    }

    // This method show dialogue to conform Reset event from user
    private void showResetDialogue() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Reset");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to reset all Category and Source?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_reset);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteAllCategory();
                dbHelper.deleteAllSource();
                reSetAllCategoryAndSource();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    // This method show dialogue to conform Delete event from user
    private void showDeleteDialogue() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delete All Transaction?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke YES event
                dbHelper.deleteAllTransactions(null);
                Toast.makeText(SettingsActivity.this, "All Transactions Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void reSetAllCategoryAndSource() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadCategoryAndSource("Food", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_food)), Constants.TRANSACTION_TYPE_DEBIT, true);
                loadCategoryAndSource("Rent", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_rent)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, true);
                loadCategoryAndSource("Salary", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_salary)), Constants.TRANSACTION_TYPE_CREDIT, true);
                loadCategoryAndSource("Shopping", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_shopping_bag)), Constants.TRANSACTION_TYPE_DEBIT, true);
                loadCategoryAndSource("Bike Service", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_bike_service)), Constants.TRANSACTION_TYPE_DEBIT, true);
                loadCategoryAndSource("Car Service", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_car_service)), Constants.TRANSACTION_TYPE_DEBIT, true);

                loadCategoryAndSource("Cash", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_cash)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                loadCategoryAndSource("Check", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_check)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                loadCategoryAndSource("PayTm Wallet", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_paytm)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                loadCategoryAndSource("Online Banking", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_online_payment)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                loadCategoryAndSource("Credit Card", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_credit_card)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                loadCategoryAndSource("Debit Card", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_credit_card)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
            }
        }).start();
        Toast.makeText(this, "Reset Successfully", Toast.LENGTH_SHORT).show();
    }

    // This mentod is used by loading default values to Category and Source
    private void loadCategoryAndSource(String title, byte[] logo, String transactionType, boolean isCategory) {
        final ContentValues values = new ContentValues();

        if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT)) {
            values.put(Constants.TRANSACTION_TYPE_CREDIT, 1);
            values.put(Constants.TRANSACTION_TYPE_DEBIT, 0);
        } else if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_DEBIT)) {
            values.put(Constants.TRANSACTION_TYPE_CREDIT, 0);
            values.put(Constants.TRANSACTION_TYPE_DEBIT, 1);
        } else {
            values.put(Constants.TRANSACTION_TYPE_CREDIT, 1);
            values.put(Constants.TRANSACTION_TYPE_DEBIT, 1);
        }

        if (isCategory) {
            values.put(Constants.CATEGORY_TITLE, title);
            values.put(Constants.CATEGORY_LOGO, logo);
            dbHelper.insertContentVals(Constants.TRANSACTION_CATEGORY_TABLE, values);
        } else {
            values.put(Constants.SOURCE_TITLE, title);
            values.put(Constants.SOURCE_LOGO, logo);
            dbHelper.insertContentVals(Constants.TRANSACTION_SOURCE_TABLE, values);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}
