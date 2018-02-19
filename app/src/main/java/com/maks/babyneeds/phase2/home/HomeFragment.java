package com.maks.babyneeds.phase2.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.maks.babyneeds.Activity.ProductListActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.CatgoryAdapter;
import com.maks.babyneeds.phase2.DashboardActivity;
import com.maks.model.BannerPojo;
import com.maks.model.Category;
import com.maks.model.HomepageDTO;
import com.maks.model.Offer;
import com.maks.model.ProductDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;


public class HomeFragment extends Fragment implements CatgoryAdapter.OnItemClickListener{
    @BindView(R.id.recyclerView) RecyclerView offersRecyclerView;
    @BindView(R.id.recommendrecyclerView) RecyclerView recommendRecyclerView;
    @BindView(R.id.banner_slider1) BannerSlider bannerSlider;

    private GridLayoutManager layoutManager;
    private RecyclerView.Adapter productAdapter,offersAdapter;
    private List<Offer> listOffers = new ArrayList<>();
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
        getBannerOfferData();
        setUpRecyclerView();
        return view;
    }

    private void setUpRecyclerView() {

        layoutManager = new GridLayoutManager(getContext(),2);
       offersRecyclerView.setLayoutManager(layoutManager);
       offersRecyclerView.setItemAnimator(new DefaultItemAnimator());
       offersRecyclerView.setNestedScrollingEnabled(false);
        offersAdapter  = new OffersAdapter(listOffers,this);
        offersRecyclerView.setAdapter(offersAdapter);


    }


    private void parseData(String array){
        bannerList.clear();
        listOffers.clear();
        HomepageDTO dto = new Gson().fromJson(array,HomepageDTO.class);
        bannerList.addAll(dto.getData());
        listOffers.addAll(dto.getOffer_data());
        //Finally initializing our adapter
        setUpBanners();

        offersAdapter.notifyDataSetChanged();

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

    private void getBannerOfferData(){
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
                                    getNewArraivalData();
                                }catch(Exception ex)
                                {ex.printStackTrace();}
                            }
                        }
                    });

        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }


    private void getNewArraivalData(){
        if(new ConnectionDetector(getContext()).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_new_arrivals");

            Ion.with(getContext())
                    .load(Constants.WS_URL)
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            if(e==null){
                                try {
                                    Log.e("response",result.toString());

                                        ProductDTO arr = new Gson().fromJson(result.toString(), ProductDTO.class);

                                    //Adding adapter to recyclerview
                                    LinearLayoutManager playoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

                                    recommendRecyclerView.setLayoutManager(playoutManager);
                                    recommendRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recommendRecyclerView.setNestedScrollingEnabled(false);
                                    productAdapter  = new ProductAdapter(arr.getData(),HomeFragment.this);
                                    //Adding adapter to recyclerview
                                    recommendRecyclerView.setAdapter(productAdapter);

                                }catch(Exception ex)
                                {ex.printStackTrace();}
                            }
                        }
                    });

        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onItemClick(View view, int position) {
        Intent i  = new Intent(getActivity(), ProductListActivity.class);
        i.putExtra("offer_id",listOffers.get(position).getOfferId());
        i.putExtra("offer_name",listOffers.get(position).getName());
        startActivity(i);
    }


}
