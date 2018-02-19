package com.pub.expensecalculator.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.pub.expensecalculator.activity.HomeScreenActivity;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.activity.TransactionDetailsActivity;
import com.pub.expensecalculator.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountStatmentFragment extends Fragment {
    private final String TAG = AccountStatmentFragment.class.toString();
    private HomeScreenActivity parentActivity;
    private RadioGroup mRadiocategoryGroup;
    private AppCompatButton mProceedBtn;


    public AccountStatmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_statment, container, false);
        mRadiocategoryGroup = (RadioGroup) view.findViewById(R.id.radio_statment_category);
        mProceedBtn = (AppCompatButton) view.findViewById(R.id.button_proceed);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = mRadiocategoryGroup.getCheckedRadioButtonId();
                Intent intent = new Intent(getActivity(), TransactionDetailsActivity.class);
                if (selectedId == R.id.radioButton_all) {
                    intent.putExtra(Constants.PERIOD, Constants.ALL_TRANSACTION);
                } else if (selectedId == R.id.radioButton_last_month) {
                    intent.putExtra(Constants.PERIOD, Constants.ONE_MONTH);
                } else if (selectedId == R.id.radioButton_last_three_month) {
                    intent.putExtra(Constants.PERIOD, Constants.THREE_MONTH);
                } else if (selectedId == R.id.radioButton_last_six_month) {
                    intent.putExtra(Constants.PERIOD, Constants.SIX_MONTH);
                }
                parentActivity.startActivity(intent);
                parentActivity.overridePendingTransition(R.anim.slide_up, R.anim.zoom_out);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parentActivity = (HomeScreenActivity) getActivity();
        parentActivity.hideFabButton(true);
    }
}
