package com.maks.babyneeds.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;


public class HomeFragment extends Fragment {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.banner_slider1) BannerSlider bannerSlider;
    @BindView(R.id.toolbar) Toolbar toolbar;

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
        ButterKnife.bind(this, view);
        initToolbar(toolbar);
        getData();
        return view;
    }

    private void setUpRecyclerView() {

        layoutManager = new GridLayoutManager(getContext(),2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        productAdapter  = new CatgoryAdapter(listCategory, (MainActivityBottomBar) getActivity());
        //Adding adapter to recyclerview
        recyclerView.setAdapter(productAdapter);
    }

    private void initToolbar(Toolbar toolbar) {
        if (toolbar != null) {
            toolbar.setTitle("Baby Needs");
            ((MainActivityBottomBar)getActivity()).setSupportActionBar(toolbar);
        }


    }

    private void getCategories(){

    }


    private void parseData(String array){
        bannerList.clear();
        listCategory.clear();
        HomepageDTO dto = new Gson().fromJson(array,HomepageDTO.class);
        bannerList.addAll(dto.getData());
        listCategory.addAll(dto.getNew_data());
        //Finally initializing our adapter
        setUpRecyclerView();
        setUpBanners();

        productAdapter.notifyDataSetChanged();


    }

    private void setUpBanners() {

        List<Banner> banners=new ArrayList<>();
        //add banner using image url
        for (BannerPojo b: bannerList
             ) {
            RemoteBanner banner = new RemoteBanner(Constants.PRODUCT_IMG_PATH+b.getImagePath());
            banner.setScaleType(ImageView.ScaleType.CENTER_CROP);
            banners.add(banner);
        }

        bannerSlider.setBanners(banners);
    }

    private void getData(){
        if(new ConnectionDetector(getContext()).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_all_banner");

            pd.show();

            Ion.with(getContext())
                    .load(Constants.WS_URL)
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            if(pd!=null && pd.isShowing()){
                                pd.dismiss();
                            }
                            if(e==null){
                                try {
                                    Log.e("response",result.toString());
                                    bannerList.clear();
                                    parseData(result.toString());

                                }catch(Exception ex)
                                {ex.printStackTrace();}
                            }
                        }
                    });

        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }

}
