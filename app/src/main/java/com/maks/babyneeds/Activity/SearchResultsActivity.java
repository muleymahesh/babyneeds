package com.maks.babyneeds.Activity;


import android.app.ProgressDialog;
import android.app.SearchManager;
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
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.ProductAdapter;
import com.maks.babyneeds.adapter.ProductSearchAdapter;
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

public class SearchResultsActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

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
        setContentView(R.layout.activity_search_results);
        initToolbar();
        initView();

        getData(getIntent().getStringExtra(SearchManager.QUERY));

        adapter = new ProductSearchAdapter(listCategory, SearchResultsActivity.this);

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

    class ProductTask extends AsyncTask<String, Void,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(SearchResultsActivity.this);
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
                    parseData(s);

                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
    }

    private void getData(String query){
        new ProductTask().execute(Constants.WS_URL,"{\"method\":\"search_product\",\"query\":\""+query+"\"}");
    }


    //This method will parse json data
    private void parseData(String array){
        listCategory.clear();

        ProductDTO arr = new Gson().fromJson(array, ProductDTO.class);

        listCategory.addAll(arr.getData());

        //Finally initializig our adapter
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
            toolbar.setTitle("Products");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }


    }

    @Override
    public void onItemClick(View view, int position) {
        Product product = listCategory.get(position);
        Intent intent=new Intent(this,ProductDetailScreenActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        Toast.makeText(SearchResultsActivity.this, "new intent", Toast.LENGTH_SHORT).show();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
            getData(query.replace(" ","%"));

        }
    }
}