package com.maks.babyneeds.phase2.services;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.maks.babyneeds.Activity.ProductDetailScreenActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.ServicesActivity;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.ConnectionDetector;
import com.maks.babyneeds.Utility.Constants;
import com.maks.babyneeds.adapter.ProductAdapter;
import com.maks.model.Product;
import com.maks.model.ProductDTO;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements FavProductAdapter.OnItemClickListener{

    private List<Product> listCategory = new ArrayList<>();
    //Creating Views
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }
    public static FavoriteFragment newInstance() {
        FavoriteFragment fragment = new FavoriteFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);
        ButterKnife.bind(this,view);
        layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(new AppPreferences(getActivity()).isLogin()){
            getData();
        }else{
            Toast.makeText(getContext(), "Login to see your wishlist items", Toast.LENGTH_SHORT).show();
        }
        return view;

    }

    private void getData(){
        if(new ConnectionDetector(getContext()).isConnectingToInternet()) {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("method", "get_fav");
            json.addProperty("user_id", new AppPreferences(getContext()).getEmail());

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
                                ProductDTO dto = new Gson().fromJson(result,ProductDTO.class);

                                listCategory.addAll(dto.getData());
                                   if(listCategory.isEmpty()){
//                                       Toast.makeText(getContext(), "No data. Click Heart on product detail to add to Wishlist", Toast.LENGTH_SHORT).show();
                                   }
                                adapter = new FavProductAdapter(listCategory,FavoriteFragment.this);

                                recyclerView.setAdapter(adapter);
                            }
                        }
                    });

        }else{
            Toast.makeText(getContext(), "You are offline!.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Product product = listCategory.get(position);
        Intent intent=new Intent(getActivity(),ProductDetailScreenActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }
}
