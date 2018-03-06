package com.maks.babyneeds.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.phase2.home.HomeFragment;
import com.maks.babyneeds.phase2.home.ProductAdapter;
import com.maks.model.Product;
import com.maks.model.ProductDTO;

import butterknife.OnClick;

public class WriteReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
    }
    @OnClick(R.id.buttonSueview)
    public void submit(){
        JsonObject json = new JsonObject();
        json.addProperty("method", "get_new_arrivals");

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
