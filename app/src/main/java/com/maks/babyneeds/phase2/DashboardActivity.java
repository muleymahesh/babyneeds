package com.maks.babyneeds.phase2;

import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.messaging.FirebaseMessaging;
import com.maks.babyneeds.Activity.AboutUsActivity;
import com.maks.babyneeds.Activity.BrandListActivity;
import com.maks.babyneeds.Activity.CategoryActivity;
import com.maks.babyneeds.Activity.FeedbackActivity;
import com.maks.babyneeds.Activity.MyOrdersActivity;
import com.maks.babyneeds.Activity.OffersActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.ServicesCategoryActivity;
import com.maks.babyneeds.Activity.NewArrivalActivity;
import com.maks.babyneeds.phase2.cart.CartFragment;
import com.maks.babyneeds.phase2.services.FavoriteFragment;
import com.maks.babyneeds.phase2.home.HomeFragment;
import com.maks.babyneeds.phase2.user.UserFragment;
import com.vorlonsoft.android.rate.AppRate;
import com.vorlonsoft.android.rate.StoreType;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        initToolbar();
       bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(bottomNavigation);
        initDrawer();
        loadHomeFragment();
        new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseMessaging.getInstance().subscribeToTopic("babyneeds");
            }
        }).start();


        AppRate.with(this)
                .setStoreType(StoreType.GOOGLEPLAY) // default GOOGLEPLAY (Google Play), other options are AMAZON (Amazon Appstore), BAZAAR (Cafe Bazaar),
                //         CHINESESTORES (19 chinese app stores), MI (Mi Appstore), SAMSUNG (Samsung Galaxy Apps),
                //         SLIDEME (SlideME), TENCENT (Tencent App Store), YANDEX (Yandex.Store),
                //         setStoreType(int) (BlackBerry World, int - your application ID),
                //         setStoreType(String) (Apple App Store, String - a full URI (only http/https)) and
                //         setStoreType(String) (Any other store, String - a full URI to your app)
                .setInstallDays((byte) 0)           // default 10, 0 means install day.
                .setLaunchTimes((byte) 10)          // default 10 times.
                .setRemindInterval((byte) 2)        // default 1 day.
                .setRemindLaunchTimes ((byte) 2)    // default 1 (each launch).
                .setShowLaterButton(true)
                .setShowNeverButton(false)// default true.
                .setDebug(true)                     // default false.
                .setCancelable(false)               // default false.
                .setOnClickButtonListener(which -> Log.d("", "RateButton: " + Byte.toString(which) + ""))
                // comment to use library strings instead app strings
                .setTitle(R.string.new_rate_dialog_title)
                .setTextLater(R.string.new_rate_dialog_later)
                // uncomment to use app string instead library string
                //.setMessage(R.string.new_rate_dialog_message)
                // comment to use library strings instead app strings
                .setTextRateNow(R.string.new_rate_dialog_ok)
                .monitor();

        //noinspection ConstantConditions
        if (AppRate.with(this).getStoreType() == StoreType.GOOGLEPLAY) {
            //Check that Google Play is available
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) != ConnectionResult.SERVICE_MISSING) {
                // Show a dialog if meets conditions
                AppRate.showRateDialogIfMeetsConditions(this);
            }
        } else {
            // Show a dialog if meets conditions
            AppRate.showRateDialogIfMeetsConditions(this);
        }

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
                case R.id.drawer_offers:

                startActivity(new Intent(this, OffersActivity.class));
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
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
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
        FavoriteFragment fragment = new FavoriteFragment();
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
