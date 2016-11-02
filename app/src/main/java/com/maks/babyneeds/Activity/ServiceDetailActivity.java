package com.maks.babyneeds.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.maks.babyneeds.Utility.Utils;
import com.maks.model.Service;

public class ServiceDetailActivity extends AppCompatActivity {

    public TextView textViewName;
    public TextView textViewAddress;
    public TextView textViewContact;
    public TextView textViewDetail;

    public ImageView callImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Baby Needs");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }
        textViewName = (TextView) findViewById(R.id.doc_name);
        textViewAddress = (TextView) findViewById(R.id.address);
        textViewContact = (TextView) findViewById(R.id.contact_no);
        textViewDetail = (TextView) findViewById(R.id.detail);
        callImg=(ImageView) findViewById(R.id.imgCall);
        textViewName.setTypeface(Utils.setLatoFontBold(this));
        textViewAddress.setTypeface(Utils.setLatoFontBold(this));

        callImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+textViewContact.getText().toString()));
//                    activity.startActivity(callIntent);
            }
        });


        Service doctor = (Service) getIntent().getSerializableExtra("doctor");

        //  Picasso.with(context).load(category.getCatImg_url()).resize(400,220).centerCrop().into(holder.imageView);
        textViewName.setText(doctor.getName());
        textViewAddress.setText(doctor.getAddress());
        textViewContact.setText(doctor.getContact());
        textViewDetail.setText(doctor.getDetail());

        }
}
