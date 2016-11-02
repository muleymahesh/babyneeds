package com.maks.babyneeds.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.BrandAdapter;
import com.maks.babyneeds.adapter.ProductAdapter;
import com.maks.babyneeds.adapter.ProductAdapter.OnItemClickListener;
import com.maks.model.Brand;
import com.maks.model.BrandDTO;
import com.maks.model.Product;
import com.maks.model.ProductDTO;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BrandListActivity extends AppCompatActivity implements OnItemClickListener {

    private Toolbar toolbar;
    //Creating a List of Category
    private List<Brand> listCategory = new ArrayList<>();

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        initToolbar();
        initView();

        getData();

        adapter = new BrandAdapter(listCategory, BrandListActivity.this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_brand_list, menu);
        return true;
    }

    class ProductTask extends AsyncTask<String, Void,String>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(BrandListActivity.this);
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
     new ProductTask().execute(Constants.WS_URL,"{\"method\":\"get_all_brand\"}");
    }


    //This method will parse json data
    private void parseData(String array){

        try {
            BrandDTO arr = new Gson().fromJson(array.toString(), BrandDTO.class);

            listCategory.clear();
            listCategory.addAll(arr.getData());

        }catch (Exception e){e.printStackTrace();}//Finally initializig our adapter
        adapter.notifyDataSetChanged();
    }


    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Brands");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }


    }

    @Override
    public void onItemClick(View view, int position) {
        Brand product = listCategory.get(position);
        Intent intent=new Intent(this,ProductListActivity.class);
        intent.putExtra("brand_id", product.getBrandId());
        intent.putExtra("name",listCategory.get(position).getName());

        startActivity(intent);

    }
}
