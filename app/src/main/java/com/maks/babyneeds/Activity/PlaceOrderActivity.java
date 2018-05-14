package com.maks.babyneeds.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.maks.babyneeds.SQLite.SQLiteUtil;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.Utils;
import com.maks.babyneeds.adapter.CartSummeryAdapter;
import com.maks.model.Address;
import com.maks.model.CartList;
import com.maks.model.ShoppingCart;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaceOrderActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.radiogroup) RadioGroup rbPaymentType;
    @BindView(R.id.textView3) TextView txtaddr;
    @BindView(R.id.txtdatetime) TextView txtdatetime;
    @BindView(R.id.productRecycler) RecyclerView productRecycler;
    @BindView(R.id.scroll) ScrollView scrollView;

    ArrayList<ShoppingCart> list;
    String amount, selected_date, timeslot,addr ;
    Address address;
    CartSummeryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        ButterKnife.bind(this);
        initToolbar();

        selected_date = getIntent().getStringExtra("date");
        timeslot =getIntent().getStringExtra("slot");
        amount =getIntent().getStringExtra("amount");

        address = new SQLiteUtil().getAddress(this);
        addr = address.getFname()+ " "+address.getLname()+ ",\n"+address.getAddr()+", "+address.getArea()+", "+" pincode "+address.getZipcode()+",\n"+address.getPhone();
        txtaddr.setText(addr);
        txtdatetime.setText("Delivery date: "+selected_date+" time: "+timeslot);
        list = new SQLiteUtil().getData(this);
        adapter = new CartSummeryAdapter(list,this);
        productRecycler.setLayoutManager(new LinearLayoutManager(this));
        productRecycler.setAdapter(adapter);
//        productRecycler.setItemAnimator(new DefaultItemAnimator());
        scrollView.scrollTo(0,0);
    }

    @OnClick(R.id.btnAddr)
    public void editaddress(){
        startActivity(new Intent(this,AddressActivity.class));
    }
    @OnClick(R.id.orderBtn)
    public void placeorder(){
        if (rbPaymentType.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(this,"Select payment method", Toast.LENGTH_SHORT).show();
            return;
        }
                String p_id="",qty="";
        for(int i=0;i<list.size();i++){
            p_id+=list.get(i).getProduct_id()+",";
            qty+=list.get(i).getQuantity()+",";
        }
        if(new ConnectionDetector(this).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(this);
            JsonObject json = new JsonObject();
            json.addProperty("method", "add_oder");
            json.addProperty("first_name", address.getFname());
            json.addProperty("last_name", address.getLname());
            json.addProperty("gender", "XXX");
            json.addProperty("email", new AppPreferences(PlaceOrderActivity.this).getEmail());
            json.addProperty("amount", String.format("%s",amount));
            json.addProperty("shipping_type", ((RadioButton)findViewById(rbPaymentType.getCheckedRadioButtonId())).getText().toString());
            json.addProperty("street", address.getArea());
            json.addProperty("city", address.getAddr());
            json.addProperty("state", address.getLandmark());
            json.addProperty("country","Noida, UP.");
            json.addProperty("zipcode", address.getZipcode());
            json.addProperty("phone", address.getPhone());
            json.addProperty("order_detail", "Delivery Date "+selected_date+", between "+timeslot);
            json.addProperty("user_id", address.getPhone());
            json.addProperty("p_id", p_id);
            json.addProperty("qty", qty);

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
                            if(e==null && result.get("result").getAsString().equalsIgnoreCase("success")){
                                try {
                                    AlertDialog alert = new AlertDialog.Builder(PlaceOrderActivity.this).create();
                                    alert.setTitle("Order status");
                                    alert.setMessage("You order has been placed successfully!");
                                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            PlaceOrderActivity.this.finish();
                                        }
                                    });
                                    alert.show();

                                    new SQLiteUtil().emptyCart(PlaceOrderActivity.this);
                                    }catch(Exception ex)
                                {ex.printStackTrace();}
                            }else{
                                Toast.makeText(PlaceOrderActivity.this, "Error occured. try again!.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else{
            Toast.makeText(this, "You are offline!.", Toast.LENGTH_SHORT).show();
        }

    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("Order Summary");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Order Summary");

        }
    }

}

