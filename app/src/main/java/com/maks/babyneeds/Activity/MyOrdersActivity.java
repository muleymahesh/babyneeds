package com.maks.babyneeds.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maks.babyneeds.SQLite.DBHelper;
import com.maks.babyneeds.SQLite.SQLiteUtil;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.Utility.HttpUtils;
import com.maks.babyneeds.Utility.Utils;
import com.maks.babyneeds.adapter.MyOrderAdapter;
import com.maks.babyneeds.adapter.NewProductAdapter;
import com.maks.babyneeds.adapter.ProductAdapter;
import com.maks.model.Address;
import com.maks.model.CartList;
import com.maks.model.OrderDTO;
import com.maks.model.OrderPojo;
import com.maks.model.Product;
import com.maks.model.ProductDTO;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private Toolbar toolbar;
    //Creating a List of Category
    private List<OrderPojo> listCategory = new ArrayList<OrderPojo>();

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

        adapter = new MyOrderAdapter(listCategory, MyOrdersActivity.this);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

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


    class ProductTask extends AsyncTask<String, Void,String>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(MyOrdersActivity.this);
            pd.setMessage("Loading...");
            pd.show();
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... ulr) {
            Response response = null;
            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            Log.e("request",ulr[1]);
            RequestBody body = RequestBody.create(JSON, ulr[1]);
            Request request = new Request.Builder()
                    .url (ulr[0])
                    .post(body)
                    .build();

            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(s!=null){
                try {
                    Log.e("response",s);
                    parseData(s);//new JSONObject(s).getJSONArray("data")

                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
    }

    private void getData(){
        new ProductTask().execute(Constants.WS_URL,"{\"method\":\"get_order\",\"user_id\":\""+new AppPreferences(MyOrdersActivity.this).getEmail()+"\"}");
    }


    //This method will parse json data
    private void parseData(String array){

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
