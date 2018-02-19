package com.pub.expensecalculator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pub.expensecalculator.R;
import com.pub.expensecalculator.activity.TransactionDetailsActivity;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.model.Transaction;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by prabhu on 14/2/18.
 */

public class TransactionDetailsAdapter extends RecyclerView.Adapter <TransactionDetailsAdapter.MyViewAdapter> {
    private final String TAG = TransactionDetailsAdapter.class.toString();
    Context context;
    private DBHelper dbHelper;
    List <Transaction> transactions = new ArrayList <Transaction>();
    List <Transaction> allTransactions = new ArrayList <Transaction>();


    public TransactionDetailsAdapter(Context context) {
        this.context = context;
        this.dbHelper = CommonUtilities.getDBObject(context);
    }

    @Override
    public TransactionDetailsAdapter.MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_details, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(TransactionDetailsAdapter.MyViewAdapter holder, int position) {
        final Transaction currentTransaction = transactions.get(position);
        holder.mDateTv.setText(currentTransaction.getDate());
        holder.mDesctiptionTv.setText(currentTransaction.getDescription());
        holder.mCategoryTv.setText(currentTransaction.getCategory());
        if ((currentTransaction.getType()).equalsIgnoreCase(Transaction.TYPE_CREDIT)) {
            holder.mIncomeTv.setText("" + currentTransaction.getAmount());
        } else {
            holder.mExpenseTv.setText("" + currentTransaction.getAmount());
        }
        holder.mBalanceTv.setText("" + currentTransaction.getBalance());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    class MyViewAdapter extends RecyclerView.ViewHolder {
        private TextView mDateTv, mDesctiptionTv, mCategoryTv, mExpenseTv, mIncomeTv, mBalanceTv;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mDateTv = (TextView) itemView.findViewById(R.id.text_view_account_details_date);
            mDesctiptionTv = (TextView) itemView.findViewById(R.id.text_view_account_details_description);
            mCategoryTv = (TextView) itemView.findViewById(R.id.text_view_account_details_category);
            mExpenseTv = (TextView) itemView.findViewById(R.id.text_view_account_details_expense);
            mIncomeTv = (TextView) itemView.findViewById(R.id.text_view_account_details_income);
            mBalanceTv = (TextView) itemView.findViewById(R.id.text_view_account_details_balance);
        }
    }

    public void refreshUI(String period) {
        transactions.clear();
        allTransactions.clear();
        allTransactions = dbHelper.getTransactionsList();
        getTransactionPeriod(period);
        ((TransactionDetailsActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private void getTransactionPeriod(String period) {
        Log.d(TAG, "getTransactionPeriod: " + period);
        String lastMonth = "";
        if (period.equalsIgnoreCase(Constants.ALL_TRANSACTION)) {
            transactions = allTransactions;
        } else if (period.equalsIgnoreCase(Constants.ONE_MONTH)) {
            lastMonth = CommonUtilities.getLastMonth(-1);
            Log.d("Prabhu lastMonth", "getTransactionPeriod: " + lastMonth);
            for (Transaction testTransaction : allTransactions) {
                String transactiondate = testTransaction.getDate();
                int result = new DateAscComparator().compare(lastMonth, transactiondate);
                if (result <= 0)
                    transactions.add(testTransaction);
            }
        } else if (period.equalsIgnoreCase(Constants.THREE_MONTH)) {
            lastMonth = CommonUtilities.getLastMonth(-3);
            Log.d("Prabhu lastThreeMonth", "getTransactionPeriod: " + lastMonth);
            for (Transaction testTransaction : allTransactions) {
                String transactiondate = testTransaction.getDate();
                int result = new DateAscComparator().compare(lastMonth, transactiondate);
                if (result <= 0)
                    transactions.add(testTransaction);
            }
        } else if (period.equalsIgnoreCase(Constants.SIX_MONTH)) {
            lastMonth = CommonUtilities.getLastMonth(-6);
            Log.d("Prabhu lastSixMonth", "getTransactionPeriod: " + lastMonth);
            for (Transaction testTransaction : allTransactions) {
                String transactiondate = testTransaction.getDate();
                int result = new DateAscComparator().compare(lastMonth, transactiondate);
                if (result <= 0)
                    transactions.add(testTransaction);
            }
        }
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
}

