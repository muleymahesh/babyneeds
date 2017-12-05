package com.maks.babyneeds.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.maks.babyneeds.adapter.CatgoryAdapter;
import com.maks.babyneeds.fragment.FavouriteFragment;
import com.maks.babyneeds.fragment.HomeFragment;
import com.maks.babyneeds.fragment.OffersFragment;
import com.maks.babyneeds.fragment.UserFragment;

public class MainActivityBottomBar extends AppCompatActivity  implements CatgoryAdapter.OnItemClickListener{
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initToolbar();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_bottombar_home:
                                selectedFragment = HomeFragment.newInstance();

                                break;
                            case R.id.navigation_bottombar_offers:
                                selectedFragment = OffersFragment.newInstance();
                                break;
                            case R.id.navigation_bottombar_wishlist:
                                selectedFragment = FavouriteFragment.newInstance();
                                break;
                         /*   case R.id.navigation_bottombar_cart:
                                selectedFragment = OffersFragment.newInstance();
                                break;*/
                            case R.id.navigation_bottombar_profile:
                                selectedFragment = UserFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, HomeFragment.newInstance());
        transaction.commit();


    }

    @Override
    public void onItemClick(View view, int position) {

    }




    private void initView() {

        LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.header_main,null);

        toolbar = (Toolbar) findViewById(R.id.toolbar);


    }
     
    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("Baby Needs");
            setSupportActionBar(toolbar);
        }


    }

}
