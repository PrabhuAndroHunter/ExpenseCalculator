package com.pub.expensecalculator.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pub.expensecalculator.activity.HomeScreenActivity;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.model.Category;
import com.pub.expensecalculator.utils.CommonUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 15/2/18.
 */

public class AvailableCategoryAdapter extends RecyclerView.Adapter <AvailableCategoryAdapter.MyViewAdapter> {
    private final String TAG = AvailableCategoryAdapter.class.toString();
    Context context;
    private DBHelper dbHelper;
    private List <Category> categoriesList = new ArrayList <Category>();

    public AvailableCategoryAdapter(Context context) {
        this.context = context;
        this.dbHelper = CommonUtilities.getDBObject(context);
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_source, parent, false);
        return new AvailableCategoryAdapter.MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyViewAdapter holder, int position) {
        final Category currentCategory = categoriesList.get(position);
        holder.mCategoryTitleTv.setText(currentCategory.getTitle());
        byte[] categoryLogo = currentCategory.getLogo();
        Bitmap bitmap = BitmapFactory.decodeByteArray(categoryLogo, 0, categoryLogo.length);
        holder.mCategoryLogoIm.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        private ImageView mCategoryLogoIm;
        private TextView mCategoryTitleTv;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mCategoryTitleTv = (TextView) itemView.findViewById(R.id.text_view_category_title);
            mCategoryLogoIm = (ImageView) itemView.findViewById(R.id.imageView_category_logo);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            /*Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Thin.ttf");
            mDescriptionTv.setTypeface(face);*/
        }
    }

    public void refreshCreditCategoryUI() {
        categoriesList.clear();
        categoriesList = dbHelper.getCreditCategory();
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void refreshDebitCategoryUI() {
        categoriesList.clear();
        categoriesList = dbHelper.getDebitCategory();
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void removeItem(int position) {
        Log.d(TAG, "removeItem: "+position);
        dbHelper.deleteTransactionCategory(categoriesList.get(position).getId());
        categoriesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Category item, int position) {
        categoriesList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public List <Category> getCategoriesList() {
        return categoriesList;
    }

}
