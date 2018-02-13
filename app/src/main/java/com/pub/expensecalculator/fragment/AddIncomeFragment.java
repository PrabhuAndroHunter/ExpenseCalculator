package com.pub.expensecalculator.fragment;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pub.expensecalculator.HomeScreenActivity;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.model.Transaction;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.Constants;

import java.util.Calendar;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddIncomeFragment extends Fragment implements View.OnClickListener {
    private final String TAG = AddIncomeFragment.class.toString();
    private double amount;
    private String amountStr;
    private String category, source, description, date;
    private TextInputLayout mAmountTiL, mDescriptionTiL;
    private EditText mAmountEdT, mDescriptionEdT;
    private MaterialSpinner mCategorySpinner, mSourceSpinner;
    private Button mRs50Btn, mRs200Btn, mRs500Btn, mRs2000Btn, mSaveBtn;
    private TextView mDateTv;

    private DBHelper dbHelper;
    private HomeScreenActivity parentActivity;

    String[] CATEGORYLIST = {"Please Select Category", "Shopping", "Traveling", "Food", "Bike Service", "Rent"};
    String[] SOURCELIST = {"Please Select Source", "Cash", "Check", "PayTm Wallet", "OnlineBanking"};


    public AddIncomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_income, container, false);
        initViews(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = CommonUtilities.getDBObject(getActivity());
        parentActivity = (HomeScreenActivity)getActivity();
        parentActivity.hideFabButton(true);
    }

    private void initViews(View view) {
        mAmountEdT = (EditText) view.findViewById(R.id.edit_text_amount);
        mAmountTiL = (TextInputLayout) view.findViewById(R.id.text_input_layout_amount);
        mRs50Btn = (Button) view.findViewById(R.id.button_50rs);
        mRs200Btn = (Button) view.findViewById(R.id.button_200rs);
        mRs500Btn = (Button) view.findViewById(R.id.button_500rs);
        mRs2000Btn = (Button) view.findViewById(R.id.button_2000rs);
        mCategorySpinner = (MaterialSpinner) view.findViewById(R.id.spinner_category);
        mSourceSpinner = (MaterialSpinner) view.findViewById(R.id.spinner_source);
        mDescriptionEdT = (EditText) view.findViewById(R.id.edit_text_description);
        mDescriptionTiL = (TextInputLayout) view.findViewById(R.id.text_input_layout_description);
        mDateTv = (TextView) view.findViewById(R.id.text_view_date);
        mSaveBtn = (Button) view.findViewById(R.id.button_save);

        mRs50Btn.setOnClickListener(this);
        mRs200Btn.setOnClickListener(this);
        mRs500Btn.setOnClickListener(this);
        mRs2000Btn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mCategorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener <String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                category = item;
            }
        });

        mSourceSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener <String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                source = item;
            }
        });

        mDateTv.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayAdapter <String> categoryAdapter = new ArrayAdapter <String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, CATEGORYLIST);
        ArrayAdapter <String> sourceAdapter = new ArrayAdapter <String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, SOURCELIST);
        mCategorySpinner.setAdapter(categoryAdapter);
        mSourceSpinner.setAdapter(sourceAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_50rs:
                addMoney(50);
                break;
            case R.id.button_200rs:
                addMoney(200);
                break;
            case R.id.button_500rs:
                addMoney(500);
                break;
            case R.id.button_2000rs:
                addMoney(2000);
                break;
            case R.id.button_save:
                validateAndSave();
                break;
            case R.id.text_view_date:
                Calendar cal = Calendar.getInstance(TimeZone.getDefault()); // Get current date
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),
                        R.style.AppTheme, pickerListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                datePicker.setCancelable(false);
                datePicker.setTitle("Select the date");
                datePicker.show();
                break;
        }
    }

    private void addMoney(int amount) {
        String currentAmtStr = mAmountEdT.getText().toString();
        if (currentAmtStr.isEmpty())
            currentAmtStr = "0";
        double currentAmt = Double.parseDouble(currentAmtStr);
        double newAmt = currentAmt + amount;
        mAmountEdT.setText("" + newAmt);
    }

    private void validateAndSave() {
        amountStr = mAmountEdT.getText().toString();
        try{
            amount = Double.parseDouble(amountStr);
        }catch (Exception e){
            mAmountTiL.setError("Please Enter Current Amount");
        }
        description = mDescriptionEdT.getText().toString();
        date = mDateTv.getText().toString();

        String logs = " Amount = " + amount +
                ", \n cat = " + category +
                ", \n src = " + source;
        Log.d(TAG, "validateAndSave: " + logs);

        if (validate(amountStr, mAmountTiL, "Please enter Amount"))
            if (validate(date, mDateTv, "Please enter Date")) {
                final ContentValues values = new ContentValues();
                values.put(Constants.TRANSACTION_DATE, date);
                values.put(Constants.TRANSACTION_DESCRIPTION, description);
                values.put(Constants.TRANSACTION_CATEGORY, category);
                values.put(Constants.TRANSACTION_SOURCE, source);
                values.put(Constants.TRANSACTION_TYPE, Transaction.TYPE_CREDIT);
                values.put(Constants.TRANSACTION_AMOUNT, amount);

                AsyncDBTransaction asyncDBTransaction = new AsyncDBTransaction();
                asyncDBTransaction.execute(new MyOwndata(dbHelper, values));
    /*            new Thread(new Runnable() {
                    @Override
                    public void run() {
                        *//*
                        * save records to database with new Thread
                        * because database operation may take longer time
                        *
                        * *//*


//                        dbHelper.addTransaction(values);
                    }
                }).start();*/
            }
    }

    /*
    *
    * This method will check whether the fields are having values or not,
    * if not --> set error on respective view and return false
    *
    * */
    private boolean validate(String value, View view, String error) {
        Log.d(TAG, "Validate: Error " + error + " Value = " + value);

        if (value.equalsIgnoreCase("")) {
            if (view instanceof TextInputLayout)
                ((TextInputLayout) view).setError(error);
            else {
                Log.d(TAG, "validate: set error to date");
                ((TextView) view).setError(error);
                Toast.makeText(getActivity(), "Please enter Date", Toast.LENGTH_SHORT).show();
            }
            return false;
        } else {
            return true;
        }
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            int year = selectedYear;
            int month = selectedMonth;
            int day = selectedDay;
            // Show selected date
            date = "" + new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" ");
            mDateTv.setText(date);
        }
    };

    private class AsyncDBTransaction extends AsyncTask <MyOwndata, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSaveBtn.setEnabled(false);
        }

        @Override
        protected String doInBackground(MyOwndata... myOwndata) {
            long result = dbHelper.addTransaction(myOwndata[0].getContentValues());
            if (result == 0) {
                return "FAIL";
            } else {
                return "DONE";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: " + s);
            Toast.makeText(getActivity(), "Record is stored", Toast.LENGTH_SHORT).show();
            clearFields();
            ((HomeScreenActivity)getActivity()).loadHomeScreen();
           /* getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });*/
        }

        private void clearFields(){
            mAmountEdT.setText("");
            mCategorySpinner.setSelectedIndex(0);
            mSourceSpinner.setSelectedIndex(0);
            mDateTv.setText("");
            mDescriptionEdT.setText("");
            mSaveBtn.setEnabled(true);
        }
    }

    private class MyOwndata {
        private DBHelper dbHelper;
        private ContentValues contentValues;

        public MyOwndata(DBHelper dbHelper, ContentValues contentValues) {
            this.dbHelper = dbHelper;
            this.contentValues = contentValues;
        }

        public DBHelper getDbHelper() {
            return dbHelper;
        }

        public ContentValues getContentValues() {
            return contentValues;
        }
    }

}
