package com.pub.expensecalculator.fragment;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.pub.expensecalculator.activity.HomeScreenActivity;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.adapter.FutureTransactionViewAdapter;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.model.Transaction;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.Constants;
import com.pub.expensecalculator.utils.FutureTransactionReceiver;
import com.pub.expensecalculator.utils.RecyclerViewItemDecorator;

import static android.content.Context.ALARM_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FutureTransactionFragment extends Fragment {
    private final String TAG = FutureTransactionFragment.class.toString();
    private HomeScreenActivity parentActivity;
    private FloatingActionMenu mFabTransactionMenu;
    private TextInputLayout mAmountTiL;
    private EditText mAmountEdt, mdescriptionEdt;
    private Button mSaveBtn, mCancelBtn, mRs50Btn, mRs200Btn, mRs500Btn, mRs2000Btn;
    private DatePicker mDatePicker;
    private TextView mStatusTv;
    SwitchCompat reminderSwitch;
    private RecyclerView mRecyclerView;
    private DBHelper dbHelper;
    private String amountStr, description, date;
    boolean reminder;
    double amount;
    private long exitTime;
    private FutureTransactionViewAdapter futureTransactionViewAdapter;
    private final FutureTransactionFragment THIS;


    public FutureTransactionFragment() {
        THIS = this;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_future_transaction, container, false);
        mFabTransactionMenu = (FloatingActionMenu) view.findViewById(R.id.fabmenu_future_transaction);
        // init recycler view and statusTextView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_View_future_transaction);
        mStatusTv = (TextView) view.findViewById(R.id.textview_no_result);

        mFabTransactionMenu.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewTransactionDialogue();
            }
        });
        // startAlert();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        futureTransactionViewAdapter = new FutureTransactionViewAdapter(THIS, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(getActivity(), 0));
        mRecyclerView.setAdapter(futureTransactionViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        futureTransactionViewAdapter.refreshUI();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (HomeScreenActivity) getActivity();
        parentActivity.hideFabButton(true);
        dbHelper = CommonUtilities.getDBObject(getActivity()); // get database reference

    }

    public void startAlert() {
        int i = 2;
        Intent intent = new Intent(getActivity(), FutureTransactionReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Toast.makeText(getActivity(), "Alarm set in " + i + " seconds", Toast.LENGTH_LONG).show();
    }

    private void showNewTransactionDialogue() {
        // Create custom dialog object
        final Dialog dialog = new Dialog(parentActivity);
        // Include dialog.xml file
        dialog.setContentView(R.layout.future_transaction_dialog);
        // Set dialog title
        dialog.setTitle("Future Transaction");
        // get all view references
        mAmountEdt = (EditText) dialog.findViewById(R.id.edit_text_amount);
        mdescriptionEdt = (EditText) dialog.findViewById(R.id.edit_text_description);
        mSaveBtn = (Button) dialog.findViewById(R.id.button_add);
        mCancelBtn = (Button) dialog.findViewById(R.id.button_cancle);
        mAmountTiL = (TextInputLayout) dialog.findViewById(R.id.text_input_layout_amount);
        mRs50Btn = (Button) dialog.findViewById(R.id.button_50rs);
        mRs200Btn = (Button) dialog.findViewById(R.id.button_200rs);
        mRs500Btn = (Button) dialog.findViewById(R.id.button_500rs);
        mRs2000Btn = (Button) dialog.findViewById(R.id.button_2000rs);
        mDatePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        reminderSwitch = (SwitchCompat) dialog.findViewById(R.id.timerSwitch);
        long now = System.currentTimeMillis() - 1000;
                /*
                * Note :  Restricting user to set min date as current date because
                *         New Tasks need to done in current date or in future days
                *         so There is no meaning to give past day option
                *
                *         "mDatePicker.setMinDate(now);"
                *
                * */
        mDatePicker.setMinDate(now);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // set onclick listener to save button


        mRs50Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoney(50);
            }
        });

        mRs200Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoney(200);
            }
        });

        mRs500Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoney(500);
            }
        });

        mRs2000Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMoney(2000);
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all values
                amountStr = mAmountEdt.getText().toString();
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (Exception e) {
                    mAmountTiL.setError("Please Enter Current Amount");
                }
                description = mdescriptionEdt.getText().toString();
                int day = mDatePicker.getDayOfMonth();
                int month = mDatePicker.getMonth();
                int year = mDatePicker.getYear();
                month = month + 1;
                date = day + "-" + month + "-" + year;
                reminder = reminderSwitch.isChecked();
                Log.d(TAG, "onClick: value" + amountStr + description + date);
                if (validateAndSave()) {            // if all values are current and save in DB
                    // After successful insertion refresh UI
                    mStatusTv.setVisibility(View.INVISIBLE);
                    futureTransactionViewAdapter.lightRefreshUI();
                    futureTransactionViewAdapter.refreshUI();
                    dialog.dismiss();  // close dialog window
                }
                //dialog.dismiss();
            }
        });

        // set onclick listener to cancel button
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void addMoney(int amount) {
        String currentAmtStr = mAmountEdt.getText().toString();
        if (currentAmtStr.isEmpty())
            currentAmtStr = "0";
        double currentAmt = Double.parseDouble(currentAmtStr);
        double newAmt = currentAmt + amount;
        mAmountEdt.setText("" + newAmt);
    }

    // This method will validate all value and save to Db
    private boolean validateAndSave() {
        String logs = " Amount = " + amount;
        Log.d(TAG, "validateAndSave: " + logs);

        if (validate(amountStr, mAmountTiL, "Please enter Amount")) {
            final ContentValues values = new ContentValues();
            values.put(Constants.TRANSACTION_DATE, date);
            values.put(Constants.TRANSACTION_DESCRIPTION, description);
            values.put(Constants.TRANSACTION_TYPE, Transaction.TYPE_FUTURE);
            values.put(Constants.TRANSACTION_AMOUNT, amount);
            if (reminder)
                values.put(Constants.SET_REMINDER, 1);
            else
                values.put(Constants.SET_REMINDER, 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                        /*
                        * save records to database with new Thread
                        * because database operation may take longer time
                        *
                        * */
                    dbHelper.insertContentVals(Constants.FUTURE_TRANSACTION_TABLE, values);
                }
            }).start();
            // show toast
            Toast.makeText(getActivity(), "Transaction Added", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    /*
    *
    * This method will check whether the fields are having values or not,
    * if not --> set error on respective view and return false
    *
    * */
    private boolean validate(String value, TextInputLayout view, String error) {
        Log.d(TAG, "Validate: Error " + error + " Value = " + value);

        if (value.equalsIgnoreCase("")) {
            view.setError(error);
            return false;
        } else {
            return true;
        }
    }

    /*
  *
  * This method will Show/Hide "NO COMPLETED TASK" Text based on record count
  * Show snackbar with 1 sec delay so that user can understand clearly
  * This Snack bar will show only if there is completed tasks list
  *
  * */
    public void showStatusText(boolean show) {
        if (show)
            mStatusTv.setVisibility(View.VISIBLE);
        else if (mStatusTv.getVisibility() == View.VISIBLE) {
            mStatusTv.setVisibility(View.INVISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getActivity().findViewById(R.id.content_layout), "Long Press to Delete Transation",
                            Snackbar.LENGTH_LONG)
                            .show();
                }
            }, 2000);
        }
    }


}
