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
import com.pub.expensecalculator.model.Source;
import com.pub.expensecalculator.utils.CommonUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 16/2/18.
 */

public class AvailableSourceAdapter extends RecyclerView.Adapter <AvailableSourceAdapter.MyViewAdapter> {
    private final String TAG = AvailableCategoryAdapter.class.toString();
    Context context;
    private DBHelper dbHelper;
    private List <Source> sourceList = new ArrayList <Source>();

    public AvailableSourceAdapter(Context context) {
        this.context = context;
        this.dbHelper = CommonUtilities.getDBObject(context);
    }

    @Override
    public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_source, parent, false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(MyViewAdapter holder, int position) {
        final Source currentSource = sourceList.get(position);
        holder.mSourceTitleTv.setText(currentSource.getTitle());
        byte[] categoryLogo = currentSource.getLogo();
        Bitmap bitmap = BitmapFactory.decodeByteArray(categoryLogo, 0, categoryLogo.length);
        holder.mSourceLogoIm.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    public class MyViewAdapter extends RecyclerView.ViewHolder {
        private ImageView mSourceLogoIm;
        private TextView mSourceTitleTv;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewAdapter(View itemView) {
            super(itemView);
            mSourceTitleTv = (TextView) itemView.findViewById(R.id.text_view_category_title);
            mSourceLogoIm = (ImageView) itemView.findViewById(R.id.imageView_category_logo);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            /*Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Thin.ttf");
            mDescriptionTv.setTypeface(face);*/
        }
    }

    public void refreshCreditSourceUI() {
        sourceList.clear();
        sourceList = dbHelper.getDebitSource();
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void refreshDebitSourceUI() {
        sourceList.clear();
        sourceList = dbHelper.getDebitSource();
        ((HomeScreenActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void removeItem(int position) {
        Log.d(TAG, "removeItem: "+position);
        dbHelper.deleteTransactionSource(sourceList.get(position).getId());
        sourceList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Source item, int position) {
        sourceList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public List <Source> getSourceList() {
        return sourceList;
    }
}

