package com.pub.expensecalculator.utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.pub.expensecalculator.adapter.AvailableCategoryAdapter;
import com.pub.expensecalculator.adapter.AvailableSourceAdapter;

/**
 * Created by prabhu on 16/2/18.
 */

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private final String TAG = RecyclerItemTouchHelper.class.toString();
    private RecyclerItemTouchHelperListener listener;
    private boolean isCategory;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener, boolean isCategory) {
        super(dragDirs, swipeDirs);
        Log.d(TAG, "RecyclerItemTouchHelper: listener "+listener.toString());
        this.listener = listener;
        this.isCategory = isCategory;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView;
            if (isCategory) {
                foregroundView = ((AvailableCategoryAdapter.MyViewAdapter) viewHolder).viewForeground;
            } else {
                foregroundView = ((AvailableSourceAdapter.MyViewAdapter) viewHolder).viewForeground;
            }
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
                                RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                int actionState, boolean isCurrentlyActive) {
        View foregroundView;
        if (isCategory) {
            foregroundView = ((AvailableCategoryAdapter.MyViewAdapter) viewHolder).viewForeground;
        } else {
            foregroundView = ((AvailableSourceAdapter.MyViewAdapter) viewHolder).viewForeground;
        }
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        View foregroundView;
        if (isCategory) {
            foregroundView = ((AvailableCategoryAdapter.MyViewAdapter) viewHolder).viewForeground;
        } else {
            foregroundView = ((AvailableSourceAdapter.MyViewAdapter) viewHolder).viewForeground;
        }
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        View foregroundView;
        if (isCategory) {
            foregroundView = ((AvailableCategoryAdapter.MyViewAdapter) viewHolder).viewForeground;
        } else {
            foregroundView = ((AvailableSourceAdapter.MyViewAdapter) viewHolder).viewForeground;
        }
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG, "onSwiped: in listener state : listener "+ listener.toString());
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }
}