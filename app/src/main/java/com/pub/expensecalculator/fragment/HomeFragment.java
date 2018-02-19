package com.pub.expensecalculator.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pub.expensecalculator.activity.HomeScreenActivity;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.adapter.LastTenTransactionAdapter;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.RecyclerViewItemDecorator;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private final String TAG = HomeFragment.class.toString();
    private TextView mBalanceTv, mTransactionHeaderTv;
    private DBHelper dbHelper;
    private RecyclerView mRecyclerView;
    private TextView mStatusTv;
    private LastTenTransactionAdapter lastTenTransactionAdapter;
    private HomeScreenActivity parentActivity;
    private HomeFragment homeFragment;


    public HomeFragment() {
        homeFragment = this;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mBalanceTv = (TextView) view.findViewById(R.id.text_view_balanse);
        mTransactionHeaderTv = (TextView) view.findViewById(R.id.transaction_header);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_View_transaction);
        mStatusTv = (TextView) view.findViewById(R.id.textview_no_result);
        lastTenTransactionAdapter = new LastTenTransactionAdapter(homeFragment,getActivity());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(getActivity(), 0));
        mRecyclerView.setAdapter(lastTenTransactionAdapter); // set adapter to recycleview

        // set Recyclerview OnScrollListener
        // so that whren last entry came just hide fab Button and when is came to first then show Fab button
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: dy = " + dy + " Dx = " + dx);
                if (dy > 0 && parentActivity.isFabVisible() == View.VISIBLE) {
                    parentActivity.hideFabButton(true);
                    mTransactionHeaderTv.setVisibility(View.INVISIBLE);
                } else if (dy < 0 && parentActivity.isFabVisible() != View.VISIBLE) {
                    parentActivity.hideFabButton(false);
                    mTransactionHeaderTv.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = CommonUtilities.getDBObject(getActivity());
        parentActivity = (HomeScreenActivity) getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        double balance = dbHelper.getBalance();
        Log.d(TAG, "onStart: " + balance);
        mBalanceTv.setText(getString(R.string.Rs) + " " + balance);
        lastTenTransactionAdapter.refreshUI();
    }

    public void showStatusText(boolean show) {
        if (show)
            mStatusTv.setVisibility(View.VISIBLE);
        else if (mStatusTv.getVisibility() == View.VISIBLE) {
            mStatusTv.setVisibility(View.INVISIBLE);
        }
    }
}
