package com.pub.expensecalculator.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.pub.expensecalculator.R;
import com.pub.expensecalculator.adapter.TransactionDetailsAdapter;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.Constants;
import com.pub.expensecalculator.utils.RecyclerViewItemDecorator;

public class TransactionDetailsActivity extends AppCompatActivity {
    private final String TAG = TransactionDetailsActivity.class.toString();
    private DBHelper dbHelper;
    private RecyclerView mRecyclerView;
    private TransactionDetailsAdapter transactionDetailsAdapter;
    private String period;
    private ActionBar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout file
        setContentView(R.layout.activity_transaction_details);
        mToolBar = getSupportActionBar();
        // get DbHelper instance
        dbHelper = CommonUtilities.getDBObject(this);
        // get value of period from Intent
        period = getIntent().getStringExtra(Constants.PERIOD);
        // init RecyclerView
        mRecyclerView = findViewById(R.id.recycler_View_transaction_details);
        // initalise TransactionDetailsAdapter
        transactionDetailsAdapter = new TransactionDetailsAdapter(this);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(this, 0));
        // set adapter to recycleview
        mRecyclerView.setAdapter(transactionDetailsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        // load UI correspond to period
        // and set toolBar Title
        transactionDetailsAdapter.refreshUI(period);
        if (period.equalsIgnoreCase(Constants.ALL_TRANSACTION)) {
            mToolBar.setTitle("All Transactions");
        } else if (period.equalsIgnoreCase(Constants.ONE_MONTH)) {
            mToolBar.setTitle("Last One Month Transactions");
        } else if (period.equalsIgnoreCase(Constants.THREE_MONTH)) {
            mToolBar.setTitle("Last Three Month Transactions");
        } else if (period.equalsIgnoreCase(Constants.SIX_MONTH)) {
            mToolBar.setTitle("Last Six Month Transactions");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
