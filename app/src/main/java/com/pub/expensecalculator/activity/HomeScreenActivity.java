package com.pub.expensecalculator.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pub.expensecalculator.R;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.fragment.AccountStatmentFragment;
import com.pub.expensecalculator.fragment.AddIncomeFragment;
import com.pub.expensecalculator.fragment.ExpenseFragment;
import com.pub.expensecalculator.fragment.FutureTransactionFragment;
import com.pub.expensecalculator.fragment.HomeFragment;
import com.pub.expensecalculator.fragment.ManageSourceFragment;
import com.pub.expensecalculator.fragment.ManageCategoryFragment;
import com.pub.expensecalculator.utils.CommonUtilities;
import com.pub.expensecalculator.utils.Constants;
import com.pub.expensecalculator.utils.SharedPrefManager;

public class HomeScreenActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = HomeScreenActivity.class.toString();

    private FloatingActionMenu mFabTransactionMenu;
    private FloatingActionButton mFabIncomeBtn;
    private FloatingActionButton mFabExpenseBtn;
    private Toolbar mToolBar;
    private final int mContentlayout = R.id.content_layout;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment mFragment;
    private boolean isSplFragmentsLoaded = false;
    private long exitTime;
    private SharedPreferences pref;
    private boolean appLoaded = false;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init layout file
        setContentView(R.layout.activity_home_screen);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        // get DbHelper class instance
        dbHelper = CommonUtilities.getDBObject(this);
        // get SharedPreferences object
        pref = PreferenceManager.getDefaultSharedPreferences(HomeScreenActivity.this);
        appLoaded = pref.getBoolean("App_Loaded", false);
        // initialise FabMenu and fabButton
        mFabTransactionMenu = (FloatingActionMenu) findViewById(R.id.fabmenu_transaction);
        mFabTransactionMenu.setClosedOnTouchOutside(true);
        mFabIncomeBtn = (FloatingActionButton) findViewById(R.id.fab_button_income);
        mFabExpenseBtn = (FloatingActionButton) findViewById(R.id.fab_button_expense);
        // setOnclick listener on fab Buttons
        mFabIncomeBtn.setOnClickListener(clickListener);
        mFabExpenseBtn.setOnClickListener(clickListener);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView mNavigationView = drawer.findViewById(R.id.nav_view);
        mNavigationView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // This method will load HomeFragment when mainScreen is loaded
        loadHomeScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefManager = SharedPrefManager.getSharedPref(this);
        // Check whether the app is stating the first time
        if (!appLoaded) {
            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean("App_Loaded", true);
            edit.commit();
            // if this is the first time the load default Category and Source
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadCategoryAndSource("Food", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_food)), Constants.TRANSACTION_TYPE_DEBIT, true);
                    loadCategoryAndSource("Rent", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_rent)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, true);
                    loadCategoryAndSource("Salary", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_salary)), Constants.TRANSACTION_TYPE_CREDIT, true);
                    loadCategoryAndSource("Shopping", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_shopping_bag)), Constants.TRANSACTION_TYPE_DEBIT, true);
                    loadCategoryAndSource("Bike Service", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_bike_service)), Constants.TRANSACTION_TYPE_DEBIT, true);
                    loadCategoryAndSource("Car Service", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_car_service)), Constants.TRANSACTION_TYPE_DEBIT, true);

                    loadCategoryAndSource("Cash", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_cash)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                    loadCategoryAndSource("Check", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_check)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                    loadCategoryAndSource("PayTm Wallet", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_paytm)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                    loadCategoryAndSource("Online Banking", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_online_payment)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                    loadCategoryAndSource("Credit Card", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_credit_card)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                    loadCategoryAndSource("Debit Card", CommonUtilities.getByteArray(getResources().getDrawable(R.drawable.ic_credit_card)), Constants.TRANSACTION_TYPE_CREDIT_DEBIT, false);
                }
            }).start();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        // if the current screen is addIncome or addExpense screen the load HomeFragment
        if (isSplFragmentsLoaded) {
            loadHomeScreen();
        } else if (mFragment instanceof HomeFragment) {  // if it is already in HomeFragment then Show Toast
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "Press back again to exit",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        } else {
            loadHomeScreen();
        }
    }

    // This method will handle navigation drawer menu item click event
    // and load corresponding UI
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_Home:
                loadHomeScreen();
                break;

            case R.id.nav_future_Transaction:
                mFragment = new FutureTransactionFragment();
                mToolBar.setTitle("Future Transactions");
                inflateFragmentLayout(mFragment);
                break;
            case R.id.nav_account_statment:
                mFragment = new AccountStatmentFragment();
                mToolBar.setTitle("Account Statments");
                inflateFragmentLayout(mFragment);
                break;

            case R.id.nav_manage_category:
                mFragment = new ManageCategoryFragment();
                mToolBar.setTitle("Manage Category");
                inflateFragmentLayout(mFragment);
                break;

            case R.id.nav_manage_source:
                mFragment = new ManageSourceFragment();
                mToolBar.setTitle("Manage Source");
                inflateFragmentLayout(mFragment);
                break;

            case R.id.nav_menu_settings:
                Intent settingIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.nav_menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app ExpenseCalculator at: https://play.google.com/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // This method will handle fab_button click event
    // and load corresponding UI
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab_button_income:
                    mToolBar.setTitle("Add Income");
                    mFragment = new AddIncomeFragment();
                    inflateFragmentLayout(mFragment);
                    isSplFragmentsLoaded = true;
                    mFabTransactionMenu.close(true);
                    break;
                case R.id.fab_button_expense:
                    mToolBar.setTitle("Add Expense");
                    mFragment = new ExpenseFragment();
                    inflateFragmentLayout(mFragment);
                    isSplFragmentsLoaded = true;
                    mFabTransactionMenu.close(true);
                    break;
            }
        }
    };

    // This method will inflate Fragments
    public void inflateFragmentLayout(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mFragment instanceof HomeFragment)
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        else
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(mContentlayout, fragment).commit();
    }

    // This method will load HomeFragment
    public void loadHomeScreen() {
        isSplFragmentsLoaded = false;
        if (!(mFragment instanceof HomeFragment)) {
            mFragment = new HomeFragment();
            mToolBar.setTitle("Expense Calculator");
            inflateFragmentLayout(mFragment);
            hideFabButton(false);
        }
    }

    // This method will Control Fab button visibality
    public void hideFabButton(boolean hide) {
        if (hide)
            mFabTransactionMenu.hideMenuButton(true);
        else
            mFabTransactionMenu.showMenuButton(true);

    }

    public int isFabVisible() {
        if (mFabTransactionMenu.isMenuButtonHidden())
            return 1;
        else
            return 0;
    }

    public void setActionBarTitle(String title) {
        mToolBar.setTitle(title);
    }

    // This mentod is used by loading default values to Category and Source
    private void loadCategoryAndSource(String title, byte[] logo, String transactionType, boolean isCategory) {
        final ContentValues values = new ContentValues();

        if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_CREDIT)) {
            values.put(Constants.TRANSACTION_TYPE_CREDIT, 1);
            values.put(Constants.TRANSACTION_TYPE_DEBIT, 0);
        } else if (transactionType.equalsIgnoreCase(Constants.TRANSACTION_TYPE_DEBIT)) {
            values.put(Constants.TRANSACTION_TYPE_CREDIT, 0);
            values.put(Constants.TRANSACTION_TYPE_DEBIT, 1);
        } else {
            values.put(Constants.TRANSACTION_TYPE_CREDIT, 1);
            values.put(Constants.TRANSACTION_TYPE_DEBIT, 1);
        }

        if (isCategory) {
            values.put(Constants.CATEGORY_TITLE, title);
            values.put(Constants.CATEGORY_LOGO, logo);
            dbHelper.insertContentVals(Constants.TRANSACTION_CATEGORY_TABLE, values);
        } else {
            values.put(Constants.SOURCE_TITLE, title);
            values.put(Constants.SOURCE_LOGO, logo);
            dbHelper.insertContentVals(Constants.TRANSACTION_SOURCE_TABLE, values);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ManageCategoryFragment.PICK_IMAGE_REQUEST) {
            mFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}

