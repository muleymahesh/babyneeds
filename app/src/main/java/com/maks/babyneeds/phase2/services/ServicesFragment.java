package com.maks.babyneeds.phase2.services;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.ServicesCategoryActivity;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.model.ServicesCategory;
import com.maks.model.ServicesCategoryDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServicesFragment extends Fragment {

    private List<ServicesCategory> listCategory = new ArrayList<>();
    //Creating Views
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public ServicesFragment() {
        // Required empty public constructor
    }
    public static ServicesFragment newInstance() {
        ServicesFragment fragment = new ServicesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this,view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getData();
        return view;

    }

    private void getData(){
        if(new ConnectionDetector(getContext()).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_services_category");

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
                                ServicesCategoryDTO dto = new Gson().fromJson(result,ServicesCategoryDTO.class);

                                listCategory.addAll(dto.getData());

                                adapter = new ServicesCatgoryAdapter(listCategory,ServicesFragment.this);
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });

        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }
}
