package com.maks.babyneeds.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.CatgoryAdapter;
import com.maks.babyneeds.adapter.DrawerListAdapter;
import com.maks.model.Category;
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
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements CatgoryAdapter.OnItemClickListener{

    private Toolbar toolbar;

    //Creating a List of Category
    private List<Category> listCategory = new ArrayList<>();

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        initView();
        initToolbar();
        getData();


    }

    private void getData(){

        new CategoryTask(this).execute(Constants.WS_URL,"{\"method\":\"get_all_category\"}");
    }

    class CategoryTask extends AsyncTask<String, Void,String> {

        ProgressDialog pd;

        public CategoryTask(Context context) {
            this.pd = pd;
            pd= new ProgressDialog(context);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                    Log.e("request",s);
                    parseData(new JSONObject(s).getJSONArray("data"));

                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
    }

    //This method will parse json data
    private void parseData(JSONArray array){
        for(int i = 0; i<array.length(); i++) {
            Category superHero = new Category();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                //   superHero.setCat_img(json.getString(Constants.TAG_CAT_IMG));
                superHero.setCat_img(json.getString(Constants.TAG_CAT_IMG));
                superHero.setCat_name(json.getString(Constants.TAG_CAT_NAME));
                superHero.setCat_id(json.getString(Constants.TAG_CAT_ID));
                ArrayList<String> powers = new ArrayList<String>();


            } catch (JSONException e) {
                e.printStackTrace();
            }
            listCategory.add(superHero);
        }

        //Finally initializing our adapter
//        adapter = new CatgoryAdapter(listCategory, this);

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        return true;
    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("Baby Needs");
            setSupportActionBar(toolbar);
        }


    }

    @Override
    public void onItemClick(View view, int position) {
        Intent i  = new Intent(this, ProductListActivity.class);
        i.putExtra("cat_id",listCategory.get(position).getCat_id());
        i.putExtra("cat_name",listCategory.get(position).getCat_name());
        startActivity(i);
    }

}
