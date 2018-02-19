package com.maks.babyneeds.phase2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.maks.babyneeds.Activity.AboutUsActivity;
import com.maks.babyneeds.Activity.BrandListActivity;
import com.maks.babyneeds.Activity.CategoryActivity;
import com.maks.babyneeds.Activity.FeedbackActivity;
import com.maks.babyneeds.Activity.MyOrdersActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.ServicesCategoryActivity;
import com.maks.babyneeds.Activity.NewArrivalActivity;
import com.maks.babyneeds.phase2.cart.CartFragment;
import com.maks.babyneeds.phase2.services.ServicesFragment;
import com.maks.babyneeds.phase2.home.HomeFragment;
import com.maks.babyneeds.phase2.categories.CategoryFragment;
import com.maks.babyneeds.phase2.user.UserFragment;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navigation) BottomNavigationView bottomNavigation;
    @BindView(R.id.drawerLayout)  DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)  NavigationView  navigationView;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadHomeFragment();
                    return true;
                case R.id.navigation_dashboard:
                    loadOffersFragment();
                    return true;
                case R.id.navigation_notifications:
                    loadFavoriteFragment();
                    return true;
                case R.id.navigation_profile:
                    loadProfileFragment();
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        initToolbar();
       bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(bottomNavigation);
        initDrawer();
        loadHomeFragment();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START,true);
                return true;

            case R.id.drawer_home:
                drawerLayout.closeDrawer(GravityCompat.START,true);
                return true;
            case R.id.drawer_new_arrivals:

                startActivity(new Intent(this, NewArrivalActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START,true);

                return true;
            case R.id.drawer_shop_brand:

                startActivity(new Intent(this, BrandListActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START,true);

                return true;
            case R.id.drawer_shop_category:

                startActivity(new Intent(this, CategoryActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START,true);

                return true;

            case R.id.drawer_my_orders:

                startActivity(new Intent(this, MyOrdersActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START,true);

                return true;
            case R.id.drawer_services:

                startActivity(new Intent(this, ServicesCategoryActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START,true);

                return true;
            case R.id.drawer_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START,true);

                return true;
            case R.id.drawer_feedback:
                startActivity(new Intent(this, FeedbackActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START,true);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(drawerLayout)){
            drawerLayout.closeDrawer(GravityCompat.START,true);
        }else{
            super.onBackPressed();

        }
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("BabyNeeds");

            setSupportActionBar(toolbar);

        }


    }

    private void initDrawer() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return onOptionsItemSelected(item);
            }
        });
    }


    private void loadHomeFragment() {
        HomeFragment fragment = new HomeFragment();
        loadFragment(fragment);
    }

    private void loadProfileFragment() {
        UserFragment fragment = new UserFragment();
        loadFragment(fragment);
    }

    private void loadFavoriteFragment() {
        ServicesFragment fragment = new ServicesFragment();
        loadFragment(fragment);
    }

    private void loadOffersFragment() {
        CartFragment fragment = new CartFragment();
        loadFragment(fragment);
    }
    void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();

        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();

    }


    public void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }
}
