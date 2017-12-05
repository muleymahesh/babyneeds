package com.maks.babyneeds.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maks.babyneeds.Activity.MainActivityBottomBar;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.CatgoryAdapter;
import com.maks.model.BannerPojo;
import com.maks.model.Category;
import com.maks.model.HomepageDTO;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private RecyclerView.Adapter productAdapter;
    private List<Category> listCategory = new ArrayList<>();
    List<BannerPojo> bannerList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getData();
        recyclerView = (RecyclerView)view. findViewById(R.id.recyclerView);

        layoutManager = new GridLayoutManager(getContext(),2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        productAdapter  = new CatgoryAdapter(listCategory, (MainActivityBottomBar) getActivity());
        //Adding adapter to recyclerview
        recyclerView.setAdapter(productAdapter);
        return view;
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
                    Log.e("response",s);
                    bannerList.clear();
                    parseData(s);

                }catch(Exception e)
                {e.printStackTrace();}
            }
        }
    }

    private void parseData(String array){

        bannerList.clear();
        listCategory.clear();
        HomepageDTO dto = new Gson().fromJson(array,HomepageDTO.class);
        bannerList.addAll(dto.getData());
        listCategory.addAll(dto.getNew_data());
        //Finally initializing our adapter

        productAdapter.notifyDataSetChanged();


    }
    private void getData(){
        if(new ConnectionDetector(getContext()).isConnectingToInternet()) {
            new BannerPojoTask(getContext()).execute(Constants.WS_URL,"{\"method\":\"get_all_banner\"}");
        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }

}
