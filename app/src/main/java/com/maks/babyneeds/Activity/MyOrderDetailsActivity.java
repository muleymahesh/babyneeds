package com.maks.babyneeds.Activity;

/**
 * Created by maks on 3/19/2017.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.MyOrderAdapter;
import com.maks.babyneeds.adapter.ProductAdapter;
import com.maks.model.OrderDetail;
import com.maks.model.OrderPojo;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by Deva on 09/03/2017.
 */

public class MyOrderDetailsActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private Toolbar toolbar;
    //Creating a List of Category
    private List<OrderDetail> listCategory = new ArrayList<OrderDetail>();
    OrderPojo order ;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private TextView txtAmount;
    Button btncancelorder,btnRequestReturn,btnEmailInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        initToolbar();
        initView();
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        btncancelorder = (Button) findViewById(R.id.btncancelorder);
        btnRequestReturn = (Button) findViewById(R.id.btnRequestReturn);
        btnEmailInvoice = (Button) findViewById(R.id.btnEmailInvoice);


        btncancelorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        v.getContext());

                // set title
                alertDialogBuilder.setTitle("Cancel Order");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to cancel this order!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity

                                cancelOrder();
                            }

                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        btnRequestReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        v.getContext());

                // set title
                alertDialogBuilder.setTitle("Return order");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to return this order!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity

                                requestReturn();
                            }

                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
        btnEmailInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             emailInvoice(order.getOId());
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            order = (OrderPojo) extras.getSerializable("order");
            if(order.getOrder_status().equalsIgnoreCase("canceled") ||
                    order.getOrder_status().equalsIgnoreCase("delivered")||
                    order.getOrder_status().equalsIgnoreCase("returned")){
                btncancelorder.setVisibility(View.GONE);
            }else{
                btncancelorder.setVisibility(View.VISIBLE);
            }

            if(order.getOrder_status().equalsIgnoreCase("delivered"))
            {
                btnRequestReturn.setVisibility(View.VISIBLE);
                btnEmailInvoice.setVisibility(View.VISIBLE);

            }else{
                btnRequestReturn.setVisibility(View.GONE);
                btnEmailInvoice.setVisibility(View.GONE);
            }



            listCategory = order.getDetails();
            txtAmount.setVisibility(View.VISIBLE);
            txtAmount.setText("Total bill: Rs." + order.getAmount());
            adapter = new MyOrderAdapter(listCategory, MyOrderDetailsActivity.this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void emailInvoice(String oId) {
        final ProgressDialog pd = new ProgressDialog(MyOrderDetailsActivity.this);
        pd.show();
        Ion.with(MyOrderDetailsActivity.this)
                .load(Constants.INVOICE_URL+oId)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(e==null){
                            try {
                                Log.e("response",result.toString());
                                Toast.makeText(MyOrderDetailsActivity.this, "Order invoice sent to your registered email id...!", Toast.LENGTH_SHORT).show();
                                finish();
                            }catch(Exception ex)
                            {ex.printStackTrace();}
                        }else{
                            e.printStackTrace();
                        }
                    }
                });

    }

    void cancelOrder(){
        JsonObject json = new JsonObject();
        json.addProperty("method", "cancel_order");
        json.addProperty("order_id", order.getOId().replace("OD",""));

        Log.e("request",json.toString());
        final ProgressDialog pd = new ProgressDialog(MyOrderDetailsActivity.this);
        pd.show();
        Ion.with(MyOrderDetailsActivity.this)
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
                                Log.e("response",result.toString());
                                Toast.makeText(MyOrderDetailsActivity.this, ""+result.get("responseMessage").getAsString(), Toast.LENGTH_SHORT).show();
                                finish();
                            }catch(Exception ex)
                            {ex.printStackTrace();}
                        }else{
                            e.printStackTrace();
                        }

                    }
                });

    }

    void requestReturn(){
        JsonObject json = new JsonObject();
        json.addProperty("method", "request_return");
        json.addProperty("order_id", order.getOId().replace("OD",""));
        json.addProperty("user_email", new AppPreferences(this).getEmail());

        Log.e("request",json.toString());
        final ProgressDialog pd = new ProgressDialog(MyOrderDetailsActivity.this);
        pd.show();
        Ion.with(MyOrderDetailsActivity.this)
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
                                Log.e("response",result.toString());
                                Toast.makeText(MyOrderDetailsActivity.this, ""+result.get("responseMessage").getAsString(), Toast.LENGTH_SHORT).show();
                            finish();
                            }catch(Exception ex)
                            {ex.printStackTrace();}
                        }

                    }
                });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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