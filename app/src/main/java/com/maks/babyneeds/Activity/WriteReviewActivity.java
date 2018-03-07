package com.maks.babyneeds.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.phase2.home.HomeFragment;
import com.maks.babyneeds.phase2.home.ProductAdapter;
import com.maks.model.Product;
import com.maks.model.ProductDTO;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteReviewActivity extends AppCompatActivity {

    @BindView(R.id.imgProduct) ImageView imgProduct;
    @BindView(R.id.txtProductName) TextView txtProductName;
    @BindView(R.id.etTitle) EditText etTitle;
    @BindView(R.id.etReview) EditText etReview;
    @BindView(R.id.ratingBar) RatingBar rating;
    Product product;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        ButterKnife.bind(this);
        try {
            product = (Product) getIntent().getSerializableExtra("product");
            Picasso.with(this).load(Constants.PRODUCT_IMG_PATH+product.getImg_url()).into(imgProduct);
            txtProductName.setText(product.getProduct_name());

        }catch (Exception e){}
    }

    @OnClick(R.id.btnSubmit)
    public void submit(){
        JsonObject json = new JsonObject();
        json.addProperty("method", "rate_product");
        json.addProperty("p_id", product.getP_id());
        json.addProperty("review", etReview.getText().toString());
        json.addProperty("rating", rating.getRating());
        json.addProperty("user_id", new AppPreferences(this).getEmail());
        json.addProperty("name", new AppPreferences(this).getFname());
        json.addProperty("title",etTitle.getText().toString() );

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

                            }catch(Exception ex)
                            {ex.printStackTrace();}
                        }

                    }
                });
    }
}
