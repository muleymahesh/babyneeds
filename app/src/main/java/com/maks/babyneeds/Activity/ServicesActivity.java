package com.maks.babyneeds.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.CatgoryAdapter;
import com.maks.babyneeds.adapter.DoctorAdapter;
import com.maks.model.Category;
import com.maks.model.Doctor;
import com.maks.model.Service;
import com.maks.model.ServiceDTO;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity implements DoctorAdapter.OnItemClickListener
{

    private List<Service> listDoctor = new ArrayList<>();
    private Toolbar toolbar;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);ddd

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getData();
    }

    private void initToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("Our services");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
       }

    }

    private void getData(){
        new BannerPojoTask(this).execute(Constants.WS_URL,"{\"method\":\"get_services_by_cat\",\"category\":\""+getIntent().getStringExtra("cat_name")+"\"}");
    }

    @Override
    public void onItemClick(View view, int position) {

        Service doctor = listDoctor.get(position);
        Intent intent=new Intent(this,ServiceDetailActivity.class);
        intent.putExtra("doctor", doctor);
        startActivity(intent);
    }

    class BannerPojoTask extends AsyncTask<String, Void,String> {

        ProgressDialog pd;

        public BannerPojoTask(Context context) {
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
                    parseData(s);

                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
    }

    private void parseData(String array){
            try {
                ServiceDTO dto = new Gson().fromJson(array,ServiceDTO.class);
                listDoctor.addAll(dto.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }

        if(listDoctor!=null && !listDoctor.isEmpty()) {
            //Finally initializing our adapter
            adapter = new DoctorAdapter(listDoctor, this);

            //Adding adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }else{
            Toast.makeText(ServicesActivity.this, "No data", Toast.LENGTH_LONG).show();
        }
    }

}
