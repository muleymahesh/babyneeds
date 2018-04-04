package com.maks.babyneeds.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.maks.babyneeds.Utility.ZoomableImageView;
import com.squareup.picasso.Picasso;

public class ImageZoomActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);
        initToolbar();
        ZoomableImageView imageView =(ZoomableImageView)findViewById(R.id.imgZoom);
        Picasso.with(this).load(getIntent().getStringExtra("url")).resize(500,400).centerInside().placeholder(R.drawable.baby_bg).into(imageView);


    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Product Detail");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_settings: {
                startActivity(new Intent(this, MyCartActivity.class));
                return true;
            }

        }
        return super.onOptionsItemSelected(item);

    }

}
