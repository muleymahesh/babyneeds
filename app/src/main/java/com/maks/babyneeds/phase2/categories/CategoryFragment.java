package com.maks.babyneeds.phase2.categories;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.maks.babyneeds.Activity.ProductListActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.phase2.DashboardActivity;
import com.maks.model.BannerPojo;
import com.maks.model.Category;
import com.maks.model.HomepageDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements CatgoryAdapter.OnItemClickListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    private GridLayoutManager layoutManager;
    private RecyclerView.Adapter productAdapter;
    private List<Category> listCategory = new ArrayList<>();
    List<BannerPojo> bannerList = new ArrayList<>();

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        getData();
        setUpRecyclerView();
        return view;
    }
    private void setUpRecyclerView() {

        layoutManager = new GridLayoutManager(getContext(),2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        productAdapter  = new CatgoryAdapter(listCategory, this);
        //Adding adapter to recyclerview
        recyclerView.setAdapter(productAdapter);
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

        productAdapter.notifyDataSetChanged();


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

    @Override
    public void onItemClick(View view, int position) {
        Intent i  = new Intent(getActivity(), ProductListActivity.class);
        i.putExtra("cat_id",listCategory.get(position).getCat_id());
        i.putExtra("cat_name",listCategory.get(position).getCat_name());
        startActivity(i);
    }
}
