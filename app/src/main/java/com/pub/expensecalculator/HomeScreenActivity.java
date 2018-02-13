package com.pub.expensecalculator;

import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.pub.expensecalculator.database.DBHelper;
import com.pub.expensecalculator.fragment.AddIncomeFragment;
import com.pub.expensecalculator.fragment.ExpenseFragment;
import com.pub.expensecalculator.fragment.HomeFragment;
import com.pub.expensecalculator.utils.CommonUtilities;

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

    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        dbHelper = CommonUtilities.getDBObject(this);

        mFabTransactionMenu = (FloatingActionMenu) findViewById(R.id.fabmenu_transaction);
        mFabTransactionMenu.setClosedOnTouchOutside(true);
        mFabIncomeBtn = (FloatingActionButton) findViewById(R.id.fab_button_income);
        mFabExpenseBtn = (FloatingActionButton) findViewById(R.id.fab_button_expense);
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

        dbHelper = CommonUtilities.getDBObject(this);
        loadHomeScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (isSplFragmentsLoaded) {
            loadHomeScreen();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_Home:
                loadHomeScreen();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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

    private void inflateFragmentLayout(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (mFragment instanceof HomeFragment)
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        else
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(mContentlayout, fragment).commit();
    }

    public void loadHomeScreen() {
        mFragment = new HomeFragment();
        mToolBar.setTitle("Expense Calculator");
        inflateFragmentLayout(mFragment);
    }

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
}
