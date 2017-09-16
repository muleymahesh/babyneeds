package com.example.babyneedsadmin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.babyneeds.delivery.R;
import com.example.babyneedsadmin.adapter.MyOrderListAdapter;
import com.example.babyneedsadmin.adapter.ProductAdapter;
import com.example.babyneedsadmin.model.OrderDTO;
import com.example.babyneedsadmin.model.OrderPojo;
import com.example.babyneedsadmin.utils.AppPreferences;
import com.example.babyneedsadmin.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

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
        setContentView(R.layout.activity_my_orders);
        initToolbar();
        initView();

        getData();

        adapter = new MyOrderListAdapter(listCategory, MainActivity.this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }


    private void getData() {
        pd = new ProgressDialog(MainActivity.this);

        pd.show();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "get_orders");
        params.put("del_boy", ""+sdf.format(new Date()));

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Constants.WS_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG","sucess"+ response.toString());
                        pd.hide();
                        parseData(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                // hide the progress dialog
                pd.hide();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");

    }


    //This method will parse json data
    private void parseData(String array) {

        OrderDTO arr = new Gson().fromJson(array.toString(), OrderDTO.class);

        listCategory.addAll(arr.getOrders());
        //Finally initializig our adapter
        adapter.notifyDataSetChanged();
    }


    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("My orders");
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public void onItemClick(View view, int position) {
//        OrderDTO product = listCategory.get(position);
//        Intent intent=new Intent(this,ProductDetailScreenActivity.class);
//        intent.putExtra("product", product);
//        startActivity(intent);

    }
}
