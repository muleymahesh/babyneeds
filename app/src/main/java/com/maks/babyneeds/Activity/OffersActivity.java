package com.maks.babyneeds.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.BrandAdapter;
import com.maks.babyneeds.adapter.ProductAdapter;
import com.maks.babyneeds.phase2.home.HomeFragment;
import com.maks.babyneeds.phase2.home.OffersAdapter;
import com.maks.model.Brand;
import com.maks.model.BrandDTO;
import com.maks.model.Offer;
import com.maks.model.OffersDTO;
import com.maks.model.ProductDTO;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OffersActivity extends AppCompatActivity implements OffersAdapter.OnItemClickListener {

    private Toolbar toolbar;
    //Creating a List of Category
    private List<Offer> listCategory = new ArrayList<>();

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private OffersAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        initToolbar();
        initView();

        getData();


    }

    private void getData() {
        if(new ConnectionDetector(this).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_all_offers");

            Ion.with(this)
                    .load(Constants.WS_URL)
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            if(e==null){
                                try {
                                    Log.e("response",result.toString());

                                    OffersDTO arr = new Gson().fromJson(result.toString(), OffersDTO.class);
                                    listCategory.clear();;
                                    listCategory.addAll(arr.getData());
                                    //Adding adapter to recyclerview
                                    GridLayoutManager layoutManager = new GridLayoutManager(OffersActivity.this,2);

                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setNestedScrollingEnabled(false);
                                    adapter = new OffersAdapter(arr.getData(),OffersActivity.this);
                                    //Adding adapter to recyclerview
                                    recyclerView.setAdapter(adapter);
                                    adapter.setOnItemClickListener(OffersActivity.this);

                                }catch(Exception ex)
                                {ex.printStackTrace();}
                            }

                        }
                    });

        }else{
            Toast.makeText(this, "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,MyCartActivity.class));
            return true;
        }

        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_brand_list, menu);
        return true;
    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Latest Offers");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


    }

    @Override
    public void onItemClick(View view, int position) {
        Offer product = listCategory.get(position);
        Intent intent=new Intent(this,ProductListActivity.class);
        intent.putExtra("offer_id", product.getOfferId());
        intent.putExtra("offer_name",product.getName());

        startActivity(intent);

    }
}

