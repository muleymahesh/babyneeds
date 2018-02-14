package com.maks.babyneeds.Activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.maks.babyneeds.adapter.CatgoryAdapter;
import com.maks.babyneeds.phase2.services.ServicesFragment;
import com.maks.babyneeds.phase2.home.HomeFragment;
import com.maks.babyneeds.phase2.categories.CategoryFragment;
import com.maks.babyneeds.phase2.user.UserFragment;

import java.util.ArrayList;
import java.util.List;

import me.riddhimanadib.library.BottomBarHolderActivity;
import me.riddhimanadib.library.NavigationPage;

public class MainActivityBottomBar extends BottomBarHolderActivity implements CatgoryAdapter.OnItemClickListener{
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationPage page1 = new NavigationPage("Home", ContextCompat.getDrawable(this, R.drawable.ic_bottombar_home), HomeFragment.newInstance());
        NavigationPage page2 = new NavigationPage("Categories", ContextCompat.getDrawable(this, R.drawable.ic_bottombar_offer), CategoryFragment.newInstance());
        NavigationPage page3 = new NavigationPage("Billing", ContextCompat.getDrawable(this, R.drawable.ic_bottombar_wishlist), ServicesFragment.newInstance());
        NavigationPage page4 = new NavigationPage("Profile", ContextCompat.getDrawable(this, R.drawable.ic_bottombar_profile), UserFragment.newInstance());

        // add them in a list
        List<NavigationPage> navigationPages = new ArrayList<>();
        navigationPages.add(page1);
        navigationPages.add(page2);
        navigationPages.add(page3);
        navigationPages.add(page4);

        // pass them to super method
        super.setupBottomBarHolderActivity(navigationPages);


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
