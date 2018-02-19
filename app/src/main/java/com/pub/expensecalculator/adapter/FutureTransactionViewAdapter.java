package com.pub.expensecalculator.adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pub.expensecalculator.activity.HomeScreenActivity;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.fragment.FutureTransactionFragment;
import com.pub.expensecalculator.model.FutureTransation;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by prabhu on 13/2/18.
 */

public class FutureTransactionViewAdapter extends RecyclerView.Adapter <FutureTransactionViewAdapter.MyViewAdapter> {
    private final String TAG = FutureTransactionViewAdapter.class.toString();
    Context context;
    private TextInputLayout mAmountTiL;
    private EditText mAmountEdt, mdescriptionEdt;
    private Button mUpdateBtn, mCancelBtn, mRs50Btn, mRs200Btn, mRs500Btn, mRs2000Btn;
    private DatePicker mDatePicker;
    SwitchCompat reminderSwitch;
    private DBHelper dbHelper;
    private String amountStr, description, date;
    private double amount;
    private List <FutureTransation> transactionList = new ArrayList <FutureTransation>();
    private List <FutureTransation> unSortedTransactionList = new ArrayList <FutureTransation>();
    private List <String> datesList;
    private int position;
    boolean reminder;
    private FutureTransactionFragment fragment;

    public FutureTransactionViewAdapter(FutureTransactionFragment fragment, Context context) {
        this.fragment = fragment;
        this.context = context;
        this.dbHelper = CommonUtilities.getDBObject(context);
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_future_transaction, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(final MyViewAdapter holder, final int position) {
        final FutureTransation currentTransaction = transactionList.get(position);
        // set values to all views
        holder.mAmountTv.setText(context.getString(R.string.Rs) + currentTransaction.getAmount());    // set title
        holder.mDescriptionTv.setText(currentTransaction.getDescription());  // set Description
        holder.mDateTv.setText(currentTransaction.getDate());  // set date
        holder.mDateHeader.setText(currentTransaction.getDate());

        // check whether the task is completed or not
        if (currentTransaction.isReminder()) {
            holder.mReminderBtn.setImageResource(R.drawable.ic_alarm_set);  // if completed set (R.drawable.complete) this image
        } else {
            holder.mReminderBtn.setImageResource(R.drawable.ic_alarm_not_set); // if incompleted set (R.drawable.incomplete) this image
        }


        // set on listitem click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when this event happen show dialog with prestored values
                showDialogue(currentTransaction);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: ");
                setPosition(holder.getAdapterPosition());
                // When long click event happen Delete that Task
                dbHelper.deleteFuturedTransaction(currentTransaction.getId());
                // After Deleting refresh Records and UI
                refreshUI();
                return true;
            }
        });
    }

    // return total record count
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + transactionList.size());
        return transactionList.size();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    class MyViewAdapter extends RecyclerView.ViewHolder {
        private TextView mAmountTv, mDescriptionTv, mDateTv, mDateHeader;
        private ImageButton mReminderBtn;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mAmountTv = (TextView) itemView.findViewById(R.id.text_view_title);
            mDescriptionTv = (TextView) itemView.findViewById(R.id.text_view_description);
            mDateTv = (TextView) itemView.findViewById(R.id.text_view_date);
            mDateHeader = (TextView) itemView.findViewById(R.id.date_header);
            mReminderBtn = (ImageButton) itemView.findViewById(R.id.image_button_status);

            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Thin.ttf");
            mDescriptionTv.setTypeface(face);
        }
    }

    public void lightRefreshUI() {
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void refreshUI() {
        // clear current values
        transactionList.clear();
        unSortedTransactionList.clear();
        // get fresh recods from DB
        unSortedTransactionList = dbHelper.getFutureTransactionList();
        datesList = dbHelper.getDistinctDates();
        // Sort dates in Ascending order
        Collections.sort(datesList, new DateAscComparator());

        // Sort Task records in Ascending order based on date
        for (String date : datesList) {
            for (FutureTransation transaction : unSortedTransactionList) {
                if (date.equalsIgnoreCase(transaction.getDate()))
                    transactionList.add(transaction);
            }
        }

        if (transactionList.size() == 0) {
            fragment.showStatusText(true);
        } else {
            fragment.showStatusText(false);
        }

        // refresh UI
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    /*
    *
    * This class helps to sort date in Ascending order
    *
    * */
    public class DateAscComparator implements Comparator <String> {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int n = 0;

        public int compare(String lhs, String rhs) {
            try {
                n = dateFormat.parse(lhs).compareTo(dateFormat.parse(rhs));
            } catch (Exception e) {
                Log.e(TAG, "compare: " + e.getMessage());
            }
            return n;
        }
    }

    private void showDialogue(final FutureTransation transation) {
        // Create custom dialog object
        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.future_transaction_dialog);
        // Set dialog title
        dialog.setTitle("Update Task");
        // Get all view references
        mAmountTiL = (TextInputLayout) dialog.findViewById(R.id.text_input_layout_amount);
        mAmountEdt = (EditText) dialog.findViewById(R.id.edit_text_amount);
        mdescriptionEdt = (EditText) dialog.findViewById(R.id.edit_text_description);
        mUpdateBtn = (Button) dialog.findViewById(R.id.button_add);
        mUpdateBtn.setText("Update");
        mCancelBtn = (Button) dialog.findViewById(R.id.button_cancle);
        mDatePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        mRs50Btn = (Button) dialog.findViewById(R.id.button_50rs);
        mRs200Btn = (Button) dialog.findViewById(R.id.button_200rs);
        mRs500Btn = (Button) dialog.findViewById(R.id.button_500rs);
        mRs2000Btn = (Button) dialog.findViewById(R.id.button_2000rs);
        reminderSwitch = (SwitchCompat) dialog.findViewById(R.id.timerSwitch);
        mAmountEdt.setText("" + transation.getAmount());   // Set Title
        mdescriptionEdt.setText(transation.getDescription()); // Set Description
        String[] newDate = transation.getDate().split("-");

        int day = Integer.parseInt(newDate[0]);
        int month = Integer.parseInt(newDate[1]);
        month = month - 1;  // month starts from 0 so it will be always +1 and -1
        int year = Integer.parseInt(newDate[2]);
        mDatePicker.updateDate(year, month, day);   // set date
        reminderSwitch.setChecked(transation.isReminder());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        // set onclick listener to save button
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all values
                amountStr = mAmountEdt.getText().toString();
                description = mdescriptionEdt.getText().toString();
                int day = mDatePicker.getDayOfMonth();
                int month = mDatePicker.getMonth();
                int year = mDatePicker.getYear();
                month = month + 1;
                date = day + "-" + month + "-" + year;
                reminder = reminderSwitch.isChecked();
                Log.d(TAG, "onClick: value" + amount + description + date);
                if (validateAndSave(transation)) {            // if all values are current and save in DB
                    lightRefreshUI();
                    refreshUI();
                    dialog.dismiss();
                }
            }
        });

        // set onclick listener to Cancel button
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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

    }

    // This method will check all fields are having values or not
    private boolean validateAndSave(final FutureTransation transaction) {
        if (validate(transaction)) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(Constants.TRANSACTION_DATE, date);
            contentValues.put(Constants.TRANSACTION_DESCRIPTION, description);
            contentValues.put(Constants.TRANSACTION_AMOUNT, amount);
            if (reminder)
                contentValues.put(Constants.SET_REMINDER, 1);
            else
                contentValues.put(Constants.SET_REMINDER, 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                     /*
                        * Update records to database with new Thread
                        * because database operation may take longer time
                        *
                        * */
                    dbHelper.updateFutureTransaction(transaction.getId(), contentValues);
                }
            }).start();
        }
        return true;
    }

    // This method will check whether the fields are having values or not if not return false
    private boolean validate(FutureTransation transation) {
        if (amountStr.equalsIgnoreCase("")) {  // If Title field is emplty the take old value
            amountStr = "" + transation.getAmount(); //
        } else {
            try {
                amount = Double.parseDouble(amountStr);
            } catch (Exception e) {
                mAmountTiL.setError("Please Enter Current Amount");
            }
            transation.setAmount(amount);
        }

        if (description.equalsIgnoreCase("")) { // If Description field is emplty the take old value
            description = transation.getDescription();
        } else {
            transation.setDescription(description);
        }

        transation.setReminder(reminder);

        transation.setDate(date);
        return true;
    }

    private void addMoney(int amount) {
        String currentAmtStr = mAmountEdt.getText().toString();
        if (currentAmtStr.isEmpty())
            currentAmtStr = "0";
        double currentAmt = Double.parseDouble(currentAmtStr);
        double newAmt = currentAmt + amount;
        mAmountEdt.setText("" + newAmt);
    }
}
