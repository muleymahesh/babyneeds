package com.maks.babyneeds.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.ProductReviewsAdapter;
import com.maks.babyneeds.phase2.services.FavProductAdapter;
import com.maks.babyneeds.phase2.services.FavoriteFragment;
import com.maks.model.Product;
import com.maks.model.Review;
import com.maks.model.ReviewDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReviewActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
List<Review> listCategory=new ArrayList<>();
    Toolbar toolbar;
    Product product;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFont();
        setContentView(R.layout.activity_review);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false  );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        button = (Button)findViewById(R.id.btnWriteReview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReviewActivity.this,WriteReviewActivity.class);
                i.putExtra("product",product);
                startActivity(i);

            }
        });
        initToolbar();
        product = (Product) getIntent().getSerializableExtra("product");
        getData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void setFont() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Product Reviews");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
    private void getData(){
        if(new ConnectionDetector(this).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_product_rating");
            json.addProperty("p_id", product.getP_id());

            pd.show();

            Ion.with(this)
                    .load(Constants.WS_URL)
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            if(pd!=null && pd.isShowing()){
                                pd.dismiss();
                            }
                            if(e==null){
                                ReviewDTO dto = new Gson().fromJson(result,ReviewDTO.class);

                                listCategory.addAll(dto.getData());
                                if(listCategory.isEmpty()){
//                                       Toast.makeText(getContext(), "No data. Click Heart on product detail to add to Wishlist", Toast.LENGTH_SHORT).show();
                                }
                                adapter = new ProductReviewsAdapter(listCategory,ReviewActivity.this);

                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });

        }else{
            Toast.makeText(this, "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onItemClick(View v, int position) {
    }
}
