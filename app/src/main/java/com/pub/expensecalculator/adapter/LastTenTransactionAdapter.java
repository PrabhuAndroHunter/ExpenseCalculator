package com.pub.expensecalculator.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pub.expensecalculator.activity.HomeScreenActivity;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.fragment.HomeFragment;
import com.pub.expensecalculator.model.Transaction;
import com.pub.expensecalculator.utils.CommonUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 9/2/18.
 */

public class LastTenTransactionAdapter extends RecyclerView.Adapter <LastTenTransactionAdapter.MyViewAdapter> {
    private final String TAG = LastTenTransactionAdapter.class.toString();
    Context context;
    private DBHelper dbHelper;
    List <Transaction> transactions = new ArrayList <Transaction>();
    HomeFragment homeFragment;

    public LastTenTransactionAdapter(HomeFragment homeFragment, Context context) {
        this.homeFragment = homeFragment;
        this.context = context;
        this.dbHelper = CommonUtilities.getDBObject(context);
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_last_ten_transaction, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyViewAdapter holder, int position) {
        final Transaction currentTransaction = transactions.get(position);
        holder.mCategoryTv.setText(getCategoryText(currentTransaction.getType(), currentTransaction.getCategory()));
        holder.mAmountTv.setText("" + currentTransaction.getSignedAmount());
        holder.mDesctiptionTv.setText(currentTransaction.getDescription());
        holder.mDateTv.setText(currentTransaction.getDate());
        if ((currentTransaction.getType()).equalsIgnoreCase(Transaction.TYPE_CREDIT)) {
            holder.mTransactionTypeImageImV.setImageResource(R.drawable.credit);
        } else {
            holder.mTransactionTypeImageImV.setImageResource(R.drawable.debit);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }


    class MyViewAdapter extends RecyclerView.ViewHolder {
        private AppCompatImageView mTransactionTypeImageImV;
        private TextView mCategoryTv, mAmountTv, mDesctiptionTv, mDateTv;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mTransactionTypeImageImV = (AppCompatImageView) itemView.findViewById(R.id.image_view_transaction_type);
            mCategoryTv = (TextView) itemView.findViewById(R.id.text_view_title_category);
            mAmountTv = (TextView) itemView.findViewById(R.id.text_view_amount);
            mDesctiptionTv = (TextView) itemView.findViewById(R.id.text_view_description);
            mDateTv = (TextView) itemView.findViewById(R.id.text_view_date);

            /*Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Thin.ttf");
            mDescriptionTv.setTypeface(face);*/
        }
    }

    public void refreshUI() {
        transactions.clear();
        transactions = dbHelper.getLastTenTransactions();

        if (transactions.size() == 0) {
            homeFragment.showStatusText(true);
        } else {
            homeFragment.showStatusText(false);
        }
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    private String getCategoryText(String transactionType, String category) {
        if (transactionType.equalsIgnoreCase(Transaction.TYPE_CREDIT))
            return "Credited By " + category;
        else
            return "Paid For " + category;
    }
}
