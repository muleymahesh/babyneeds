package com.maks.babyneeds.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.txtShortdesc) TextView txtShortDesc;
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
            Picasso.with(this).load(Constants.PRODUCT_IMG_PATH+product.getImgs().get(0).getImg_url()).placeholder(R.drawable.baby_bg).error(R.drawable.baby_bg).into(imgProduct);
            txtProductName.setText(product.getProduct_name());
            txtShortDesc.setText(product.getShort_desc());

        }catch (Exception e){}
    }


    private void initToolbar() {
        Toolbar   toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Product review");
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
        final ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        Ion.with(this)
                .load(Constants.WS_URL)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                          pd.dismiss();
                        // do stuff with the result or error
                        if(e==null){
                            try {
                                etReview.setText("");
                                etTitle.setText("");
                                rating.setRating(0);
                                Log.e("response",result.toString());
                                Toast.makeText(WriteReviewActivity.this,result.get("responseMessage").getAsString(),Toast.LENGTH_LONG).show();
                            }catch(Exception ex)
                            {ex.printStackTrace();}
                        }


                    }
                });
    }
}
