package com.example.babyneedsadmin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.babyneeds.delivery.R;
import com.example.babyneedsadmin.model.OrderPojo;
import com.example.babyneedsadmin.utils.Constants;

import org.json.JSONObject;

import java.util.HashMap;


public class OrderDetailActivity extends AppCompatActivity {
    public TextView txtOrderDate, txtOrderId, txtOrderPrice, txtShippingType;
    public RelativeLayout layoutMyOrder;
    TextView txtAddress;
    Button btnSuccess,btnFail;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        final OrderPojo category = (OrderPojo) getIntent().getExtras().getSerializable("order");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("My orders");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


        btnSuccess = (Button) findViewById(R.id.button);
        btnFail = (Button) findViewById(R.id.button2);
        txtOrderDate = (TextView) findViewById(R.id.txtMyOrderDate);
        txtOrderId = (TextView) findViewById(R.id.txtMyOrderOrderId);
        txtOrderPrice = (TextView) findViewById(R.id.txtMyOrderRupee);
        txtAddress = (TextView) findViewById(R.id.textViewAddress);
        txtShippingType = (TextView) findViewById(R.id.txtShippingType);

        btnSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markAsDeliered(category.getOId());
            }
        });

        txtOrderDate.setText("Date: " + category.getDate());
        txtOrderPrice.setText("Amount Rs. " + category.getAmount());
        txtOrderId.setText("Order ID: " + category.getOId());
        txtShippingType.setText("Payment mode : " + category.getShippingType());
        txtAddress.setText("Address: \n" + Html.fromHtml(category.getAddress()));
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

    private void markAsDeliered(String id) {
       pd = new ProgressDialog(OrderDetailActivity.this);
        pd.show();
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", "update_order");
        params.put("o_id", ""+id);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Constants.WS_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG","sucess"+ response.toString());
                        pd.dismiss();
                        finish();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                // hide the progress dialog
                pd.dismiss();
            }
        });

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, "tag_json_obj");

    }

}
