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
import android.widget.Toast;

import com.google.gson.Gson;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.ProductAdapter;
import com.maks.babyneeds.adapter.ProductAdapter.OnItemClickListener;
import com.maks.model.Product;
import com.maks.model.ProductDTO;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductListActivity extends AppCompatActivity implements OnItemClickListener {

    private Toolbar toolbar;
    //Creating a List of Category
    private List<Product> listCategory = new ArrayList<>();

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


        adapter = new ProductAdapter(listCategory, ProductListActivity.this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();

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

        if((id==R.id.action_sort)){

            final CharSequence[] items = {
                    "Sort by price", "Sort by discount", "Sort by name"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Make your selection");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    if(item==0) {
                        Collections.sort(listCategory, new Comparator<Product>() {
                            @Override
                            public int compare(Product lhs, Product rhs) {
                               try {
                                   if (Double.parseDouble(lhs.getMrp()) > Double.parseDouble(rhs.getMrp())) {
                                       return 1;
                                   } else if (Double.parseDouble(lhs.getMrp()) < Double.parseDouble(rhs.getMrp())) {
                                       return -1;
                                   }
                               }catch (Exception e){

                               }
                                return 0;
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }

                    if(item==1) {
                        Collections.sort(listCategory, new Comparator<Product>() {
                            @Override
                            public int compare(Product lhs, Product rhs) {
                                if (Double.parseDouble(lhs.getPer_discount()) > Double.parseDouble(rhs.getPer_discount())) {
                                    return -1;
                                } else if (Double.parseDouble(lhs.getPer_discount()) < Double.parseDouble(rhs.getPer_discount())) {
                                    return 1;
                                }

                                return 0;
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }

                    if(item==2) {
                        Collections.sort(listCategory, new Comparator<Product>() {
                            @Override
                            public int compare(Product lhs, Product rhs) {

                                return lhs.getProduct_name().compareTo(rhs.getProduct_name());
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        return true;
    }

    class ProductTask extends AsyncTask<String, Void,String>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(ProductListActivity.this);
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
        if(getIntent().hasExtra("brand_id")){
            new ProductTask().execute(Constants.WS_URL,"{\"method\":\"get_product_by_brand\",\"brand_id\":\""+getIntent().getStringExtra("brand_id")+"\"}");
        }else if(getIntent().hasExtra("getfav")){
            new ProductTask().execute(Constants.WS_URL,"{\"method\":\"get_fav\",\"user_id\":\""+ new AppPreferences(ProductListActivity.this).getEmail()+"\"}");
        }else if(getIntent().hasExtra("offer_id")){
            new ProductTask().execute(Constants.WS_URL,"{\"method\":\"get_product_by_offer\",\"offer_id\":\""+getIntent().getStringExtra("offer_id")+"\"}");
        }
        else{
            new ProductTask().execute(Constants.WS_URL,"{\"method\":\"get_product_by_cat\",\"cat_id\":\""+getIntent().getStringExtra("cat_id")+"\"}");
        }
    }


    //This method will parse json data
    private void parseData(String array){

        try {
            ProductDTO arr = new Gson().fromJson(array.toString(), ProductDTO.class);

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

            if(getIntent().hasExtra("brand_id")){
                toolbar.setTitle(""+getIntent().getStringExtra("name"));
            }else if(getIntent().hasExtra("getfav")){
                toolbar.setTitle("My Favorites");
            }else if(getIntent().hasExtra("offer_name")){
                toolbar.setTitle(""+getIntent().getStringExtra("offer_name"));
            }else{
                toolbar.setTitle(""+getIntent().getStringExtra("cat_name"));

            }
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }

    @Override
    public void onItemClick(View view, int position) {
        Product product = listCategory.get(position);
        Intent intent=new Intent(this,ProductDetailScreenActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);

    }
}
