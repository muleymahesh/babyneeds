package com.maks.deliveryapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String WS_URL = "http://babyneeds.co.in/babyneeds/ws/del_api.php";

    private Toolbar toolbar;
    //Creating a List of Category
    private List<OrderPojo> listCategory = new ArrayList<OrderPojo>();
    ProgressDialog pd;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initView();

        adapter = new MyOrderListAdapter(listCategory, MainActivity.this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderData();

    }

    private void getOrderData(){
        if(new ConnectionDetector(this).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(this);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            JsonObject json = new JsonObject();
            json.addProperty("method", "get_orders");
            json.addProperty("date", ""+sdf.format(new Date()));

            pd.show();

            Ion.with(this)
                    .load(WS_URL)
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
                                try {

                                    parseData(result.toString());
                                }catch(Exception ex)
                                {ex.printStackTrace();}
                            }
                        }
                    });

        }else{
            Toast.makeText(this, "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }

 public void completeOrder(String order_id){
        if(new ConnectionDetector(this).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(this);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            JsonObject json = new JsonObject();
            json.addProperty("method", "update_order");
            json.addProperty("o_id", order_id);
            json.addProperty("date", sdf.format(new Date()));

            pd.show();

            Ion.with(this)
                    .load(WS_URL)
                    .setJsonObjectBody(json)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if(pd!=null && pd.isShowing()){
                                pd.dismiss();
                            }
                            if(e==null){
                                try {

                                    if(result.contains("result\":\"success")){
                                        Toast.makeText(MainActivity.this, "order updated successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }catch(Exception ex)
                                {ex.printStackTrace();}
                                getOrderData();

                            }

                        }
                    });

        }else{
            Toast.makeText(this, "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }




    //This method will parse json data
    private void parseData(String array) {

        OrderDTO arr = new Gson().fromJson(array.toString(), OrderDTO.class);

        listCategory.addAll(arr.getOrders());
        //Finally initializig our adapter
        adapter.notifyDataSetChanged();
        if(arr.getOrders().isEmpty()){
            Toast.makeText(this, "No orders today!!!", Toast.LENGTH_LONG).show();
        }
    }


    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            toolbar.setTitle("My orders");
////            setSupportActionBar(toolbar);
////            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        }
    }

}
