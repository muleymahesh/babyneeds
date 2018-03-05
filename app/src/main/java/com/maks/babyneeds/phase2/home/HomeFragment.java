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
import com.maks.babyneeds.Activity.BrandListActivity;
import com.maks.babyneeds.Activity.CategoryActivity;
import com.maks.babyneeds.Activity.OffersActivity;
import com.maks.babyneeds.Activity.ProductDetailScreenActivity;
import com.maks.babyneeds.Activity.ProductListActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.model.BannerPojo;
import com.maks.model.Brand;
import com.maks.model.BrandDTO;
import com.maks.model.Category;
import com.maks.model.CategoryDTO;
import com.maks.model.HomepageDTO;
import com.maks.model.Offer;
import com.maks.model.Product;
import com.maks.model.ProductDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.DrawableBanner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;


public class HomeFragment extends Fragment implements CatgoryAdapter.OnItemClickListener{
    @BindView(R.id.recyclerView) RecyclerView offersRecyclerView;
    @BindView(R.id.recommendrecyclerView) RecyclerView recommendRecyclerView;
    @BindView(R.id.brandsRecyclerView) RecyclerView branndsRecyclerView;
    @BindView(R.id.categoryRecyclerView) RecyclerView categoryRecyclerView;
    @BindView(R.id.banner_slider1) BannerSlider bannerSlider;

    private GridLayoutManager layoutManager;
    private ProductAdapter productAdapter;
    OffersAdapter offersAdapter;
            ;
    BrandAdapter brandsAdapter;
    CatgoryAdapter categoryAdapter;
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
        Collections.shuffle(listOffers);
        layoutManager = new GridLayoutManager(getContext(),2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 3 == 0 ? 2 : 1);
            }
        });
       offersRecyclerView.setLayoutManager(layoutManager);
       offersRecyclerView.setItemAnimator(new DefaultItemAnimator());
       offersRecyclerView.setNestedScrollingEnabled(false);
        offersAdapter  = new OffersAdapter(listOffers,this.getContext());
        offersRecyclerView.setAdapter(offersAdapter);


    }
@OnClick(R.id.offer_more)
    public void onOfferClick(){
    startActivity(new Intent(getActivity(), OffersActivity.class));
    }
    @OnClick(R.id.brand_more)
    public void onBrandClick(){
        startActivity(new Intent(getActivity(), BrandListActivity.class));
    }
    @OnClick(R.id.category_more)
    public void onCategoryClick(){
        startActivity(new Intent(getActivity(), CategoryActivity.class));
    }

    private void parseData(String array){
        bannerList.clear();
        listOffers.clear();
        HomepageDTO dto = new Gson().fromJson(array,HomepageDTO.class);
        bannerList.addAll(dto.getData());
        for (int i = 0; i < (dto.getOffer_data().size()>4 ? 4 : dto.getOffer_data().size()); i++) {

            listOffers.add(dto.getOffer_data().get(i));
        }
        //Finally initializing our adapter
        setUpBanners();
        offersAdapter.setOnItemClickListener(new OffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i  = new Intent(getActivity(), ProductListActivity.class);
                i.putExtra("offer_id",listOffers.get(position).getOfferId());
                i.putExtra("offer_name",listOffers.get(position).getName());
                startActivity(i);
            }
        });
        offersAdapter.notifyDataSetChanged();

    }

    private void setUpBanners() {

        List<Banner> banners=new ArrayList<>();
        //add banner using image url
        for (BannerPojo b: bannerList
             ) {
            RemoteBanner banner = new RemoteBanner(Constants.PRODUCT_IMG_PATH+b.getImagePath());
            banner.setScaleType(ImageView.ScaleType.FIT_CENTER);
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

                                        final ProductDTO arr = new Gson().fromJson(result.toString(), ProductDTO.class);

                                    //Adding adapter to recyclerview
                                    LinearLayoutManager playoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

                                    recommendRecyclerView.setLayoutManager(playoutManager);
                                    recommendRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recommendRecyclerView.setNestedScrollingEnabled(false);
                                    productAdapter  = new ProductAdapter(arr.getData(),HomeFragment.this);
                                    //Adding adapter to recyclerview
                                    recommendRecyclerView.setAdapter(productAdapter);
                                    productAdapter.SetOnItemClickListener(new ProductAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Product product = arr.getData().get(position);
                                            Intent intent=new Intent(getActivity(),ProductDetailScreenActivity.class);
                                            intent.putExtra("product", product);
                                            startActivity(intent);

                                        }
                                    });
                                }catch(Exception ex)
                                {ex.printStackTrace();}
                            }
                            getBrandsData();
                        }
                    });

        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getBrandsData(){
        if(new ConnectionDetector(getContext()).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_all_brand");

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

                                    final BrandDTO arr = new Gson().fromJson(result.toString(), BrandDTO.class);

                                    //Adding adapter to recyclerview
                                    GridLayoutManager playoutManager = new GridLayoutManager(getContext(),3);

                                    branndsRecyclerView.setLayoutManager(playoutManager);
                                    branndsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                    branndsRecyclerView.setNestedScrollingEnabled(false);
                                    brandsAdapter  = new BrandAdapter(arr.getData(),HomeFragment.this);
                                    //Adding adapter to recyclerview
                                    brandsAdapter.setOnItemClickListener(new BrandAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Brand product = arr.getData().get(position);
                                            Intent intent=new Intent(getActivity(),ProductListActivity.class);
                                            intent.putExtra("brand_id", product.getBrandId());
                                            intent.putExtra("name",arr.getData().get(position).getName());

                                            startActivity(intent);

                                        }
                                    });
                                    branndsRecyclerView.setAdapter(brandsAdapter);

                                }catch(Exception ex)
                                {ex.printStackTrace();}
                            }
                            getCategoryData();
                        }
                    });

        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }
 private void getCategoryData(){
        if(new ConnectionDetector(getContext()).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_all_category");

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

                                    final CategoryDTO arr = new Gson().fromJson(result.toString(), CategoryDTO.class);

                                    //Adding adapter to recyclerview
                                    GridLayoutManager playoutManager = new GridLayoutManager(getContext(),1);

                                    categoryRecyclerView.setLayoutManager(playoutManager);
                                    categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                    categoryRecyclerView.setNestedScrollingEnabled(false);
                                    categoryAdapter = new CatgoryAdapter(arr.getData().subList(0,5),HomeFragment.this);
                                    //Adding adapter to recyclerview
                                    categoryAdapter.setOnItemClickListener(new CatgoryAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {

                                            Intent i  = new Intent(getActivity(), ProductListActivity.class);
                                            i.putExtra("cat_id",arr.getData().get(position).getCat_id());
                                            i.putExtra("cat_name",arr.getData().get(position).getCat_name());
                                            startActivity(i);

                                        }
                                    });
                                    categoryRecyclerView.setAdapter(categoryAdapter);

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
