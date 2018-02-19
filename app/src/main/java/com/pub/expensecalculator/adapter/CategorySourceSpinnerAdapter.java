package com.pub.expensecalculator.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pub.expensecalculator.R;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.model.Category;
import com.pub.expensecalculator.model.Source;
import com.pub.expensecalculator.utils.CommonUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 16/2/18.
 */

public class CategorySourceSpinnerAdapter extends BaseAdapter {
    private final String TAG = CategorySourceSpinnerAdapter.class.toString();
    private Context context;
    private LayoutInflater inflter;
    private boolean isCategory;
    private List <Category> categoryList = new ArrayList <Category>();
    private List <Source> sourceList = new ArrayList <Source>();

    private DBHelper dbHelper;

    public CategorySourceSpinnerAdapter(Context applicationContext, boolean isCategory) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
        this.isCategory = isCategory;
        dbHelper = CommonUtilities.getDBObject(context);
    }

    @Override
    public int getCount() {
        if (isCategory)
            return categoryList.size();
        else
            return sourceList.size();
    }

    @Override
    public Object getItem(int i) {
//        return null;
        if (isCategory) {
            return categoryList.get(i);
        } else {
            return sourceList.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
       /* return i;*/
        return 2;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_category_source_spiner, null);
        ImageView logo = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);

        if (isCategory) {
            Category curCategory = categoryList.get(position);
            logo.setImageBitmap(CommonUtilities.getBitmap(curCategory.getLogo()));
            names.setText(curCategory.getTitle());
        } else {
            Source curSource = sourceList.get(position);
            logo.setImageBitmap(CommonUtilities.getBitmap(curSource.getLogo()));
            names.setText(curSource.getTitle());
        }

        return view;
    }

    public void refreshIncomeUI() {
        if (isCategory) {
            categoryList.clear();
            categoryList = dbHelper.getCreditCategory();
            categoryList.add(0, new Category("Please Select Category", CommonUtilities.getByteArray(context.getResources().getDrawable(R.drawable.ic_category))));
            categoryList.add(new Category("Others", CommonUtilities.getByteArray(context.getResources().getDrawable(R.drawable.ic_category))));
        } else {
            sourceList.clear();
            sourceList = dbHelper.getCreditSource();
            sourceList.add(0, new Source("Please Select Source", CommonUtilities.getByteArray(context.getResources().getDrawable(R.drawable.ic_source))));
            sourceList.add(new Source("Others", CommonUtilities.getByteArray(context.getResources().getDrawable(R.drawable.ic_source))));

        }
        notifyDataSetChanged();
    }

    public void refreshExpenseUI() {
        if (isCategory) {
            categoryList.clear();
            categoryList = dbHelper.getDebitCategory();
            categoryList.add(0, new Category("Please Select Category", CommonUtilities.getByteArray(context.getResources().getDrawable(R.drawable.ic_category))));
            categoryList.add(new Category("Others", CommonUtilities.getByteArray(context.getResources().getDrawable(R.drawable.ic_category))));
        } else {
            sourceList.clear();
            sourceList = dbHelper.getDebitSource();
            sourceList.add(0, new Source("Please Select Source", CommonUtilities.getByteArray(context.getResources().getDrawable(R.drawable.ic_source))));
            sourceList.add(new Source("Others", CommonUtilities.getByteArray(context.getResources().getDrawable(R.drawable.ic_source))));

        }
        notifyDataSetChanged();
    }
}