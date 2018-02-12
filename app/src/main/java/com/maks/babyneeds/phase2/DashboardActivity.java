package com.maks.babyneeds.phase2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.phase2.services.ServicesFragment;
import com.maks.babyneeds.phase2.home.HomeFragment;
import com.maks.babyneeds.phase2.categories.OffersFragment;
import com.maks.babyneeds.phase2.user.UserFragment;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.navigation) BottomNavigationView navigation;

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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);
        loadHomeFragment();

    }
    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("Baby Needs");

            setSupportActionBar(toolbar);

        }


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
        OffersFragment fragment = new OffersFragment();
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
