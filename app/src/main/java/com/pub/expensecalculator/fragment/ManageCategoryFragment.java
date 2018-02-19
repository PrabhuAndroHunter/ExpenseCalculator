package com.pub.expensecalculator.fragment;


import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.pub.expensecalculator.activity.HomeScreenActivity;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.adapter.AvailableCategoryAdapter;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.model.Category;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.Constants;
import com.pub.expensecalculator.utils.RecyclerItemTouchHelper;
import com.pub.expensecalculator.utils.RecyclerViewItemDecorator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageCategoryFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private final String TAG = ManageCategoryFragment.class.toString();
    HomeScreenActivity parenActivity;
    private TextInputLayout mTitleTiL;
    private EditText mTitleEdt;
    private TextView mcategoryListHeaderTv;
    private FloatingActionButton mFabIncomeBtn;
    private FloatingActionButton mFabExpenseBtn;
    private RadioGroup mRadioCategoryGroup;
    private RadioButton mCreditRadioBtn;
    private AppCompatButton mSaveBtn;
    private ImageView mCategoryLogo;
    private byte[] categoryLogo;
    private String title, transactionType;
    private RecyclerView mRecyclerView;
    private AvailableCategoryAdapter availableCategoryAdapter;
    private ManageCategoryFragment manageCategoryFragment;
    private DBHelper dbHelper;
    Bitmap defalutbitmap;
    public static int PICK_IMAGE_REQUEST = 1992;

    private final String TITLE_INCOME = "Available Category (Income)";
    private final String TITLE_EXPENSE = "Available Category (Expense)";

    public ManageCategoryFragment() {
        manageCategoryFragment = this;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_category, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parenActivity = (HomeScreenActivity) getActivity();
        parenActivity.hideFabButton(true);
        dbHelper = CommonUtilities.getDBObject(getActivity());
        availableCategoryAdapter = new AvailableCategoryAdapter(getActivity());
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new RecyclerViewItemDecorator(getActivity(), 0));
        mRecyclerView.setAdapter(availableCategoryAdapter); // set adapter to recycleview
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, manageCategoryFragment, true);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        availableCategoryAdapter.refreshCreditCategoryUI();// load dataand refress UI
        updateHeader(TITLE_INCOME);
    }

    // This method will init all views
    private void initViews(View view) {
        mCategoryLogo = (ImageView) view.findViewById(R.id.imageView_category_logo);
        mTitleTiL = (TextInputLayout) view.findViewById(R.id.text_input_layout_title);
        mTitleEdt = (EditText) view.findViewById(R.id.edit_text_title);
        mRadioCategoryGroup = (RadioGroup) view.findViewById(R.id.radio_manage_category);
        mCreditRadioBtn = (RadioButton) view.findViewById(R.id.radioButton_income_category);
        mSaveBtn = (AppCompatButton) view.findViewById(R.id.button_save_category);
        mcategoryListHeaderTv = (TextView) view.findViewById(R.id.text_view_category_list_header);
        mFabIncomeBtn = (FloatingActionButton) view.findViewById(R.id.fab_button_income_category);
        mFabExpenseBtn = (FloatingActionButton) view.findViewById(R.id.fab_button_expense_category);
        mFabIncomeBtn.setOnClickListener(clickListener);
        mFabExpenseBtn.setOnClickListener(clickListener);

        Drawable drawableDefault = getActivity().getResources().getDrawable(R.drawable.ic_photo_logo);
        // convert drawable to bitmap
        defalutbitmap = ((BitmapDrawable) drawableDefault).getBitmap();

        Drawable drawable = getActivity().getResources().getDrawable(R.drawable.ic_defalut_category);
        // convert drawable to bitmap
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        categoryLogo = stream.toByteArray();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_View_transaction_category_source);

        // Set onclick listener on saveButton
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = mTitleEdt.getText().toString();
                ValidateAndSave();
            }
        });

        mCategoryLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");     /* get Only images */
                intent.setAction(Intent.ACTION_GET_CONTENT);
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
    }

    // This method will validate all value and save to Db
    private void ValidateAndSave() {
        if (validate(title, mTitleTiL, "Please enter Title")) {
            final ContentValues values = new ContentValues();
            values.put(Constants.CATEGORY_TITLE, title);
            values.put(Constants.CATEGORY_LOGO, categoryLogo);
            int selectedId = mRadioCategoryGroup.getCheckedRadioButtonId();

            if (selectedId == R.id.radioButton_income_category) {
                values.put(Constants.TRANSACTION_TYPE_CREDIT, 1);
                values.put(Constants.TRANSACTION_TYPE_DEBIT, 0);
            } else if (selectedId == R.id.radioButton_expense_category) {
                values.put(Constants.TRANSACTION_TYPE_CREDIT, 0);
                values.put(Constants.TRANSACTION_TYPE_DEBIT, 1);
            } else {
                values.put(Constants.TRANSACTION_TYPE_CREDIT, 1);
                values.put(Constants.TRANSACTION_TYPE_DEBIT, 1);
            }
            dbHelper.insertContentVals(Constants.TRANSACTION_CATEGORY_TABLE, values);
            Toast.makeText(parenActivity, "Saved", Toast.LENGTH_SHORT).show();
            clearFields();
        }
    }

    /*
   *
   * This method will check whether the fields are having values or not,
   * if not --> set error on respective view and return false
   *
   * */
    private boolean validate(String value, View view, String error) {
        Log.d(TAG, "Validate: Error " + error + " Value = " + value);

        if (value.equalsIgnoreCase("")) {
            if (view instanceof TextInputLayout)
                ((TextInputLayout) view).setError(error);
            return false;
        } else {
            return true;
        }
    }

    // This method will clear all fields with default values
    private void clearFields() {
        mTitleEdt.setText("");
        mCreditRadioBtn.setSelected(true);
        availableCategoryAdapter.refreshCreditCategoryUI();
        updateHeader(TITLE_INCOME);
        mCategoryLogo.setImageBitmap(defalutbitmap);
    }

    // and load corresponding UI
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_button_income_category:
                    // mToolBar.setTitle("Add Income");
                    availableCategoryAdapter.refreshCreditCategoryUI();
                    updateHeader(TITLE_INCOME);
                    break;
                case R.id.fab_button_expense_category:
                    // mToolBar.setTitle("Add Expense");
                    availableCategoryAdapter.refreshDebitCategoryUI();
                    updateHeader(TITLE_EXPENSE);
                    break;
            }
        }
    };

    // This method will handle onSwiped listener on Recycle view
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AvailableCategoryAdapter.MyViewAdapter) {
            // get the removed item name to display it in snack bar
            List <Category> categoryList = availableCategoryAdapter.getCategoriesList();
            String name = categoryList.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Category deletedItem = categoryList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            availableCategoryAdapter.removeItem(viewHolder.getAdapterPosition());

          /*  // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(R.id.content_layout), name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    availableCategoryAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();*/
        }
    }

    public void updateHeader(String title) {
        if (title.equalsIgnoreCase(TITLE_INCOME)) {
            mFabIncomeBtn.setImageResource(R.drawable.ic_credit);
            mFabExpenseBtn.setImageResource(R.drawable.ic_debit_dim);
            mFabIncomeBtn.setColorNormal(getActivity().getResources().getColor(R.color.colorFabButtonNormal));
            mFabExpenseBtn.setColorNormal(getActivity().getResources().getColor(R.color.colorPrimary));
        } else {
            mFabIncomeBtn.setImageResource(R.drawable.ic_credit_dim);
            mFabExpenseBtn.setImageResource(R.drawable.ic_debit);
            mFabIncomeBtn.setColorNormal(getActivity().getResources().getColor(R.color.colorPrimary));
            mFabExpenseBtn.setColorNormal(getActivity().getResources().getColor(R.color.colorFabButtonNormal));
        }
    }

    // After select image from gallery load that image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri); // convert into bitmap
                mCategoryLogo.setImageBitmap(bitmap);          /* get selected images and set into ImageView */
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                categoryLogo = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
