package com.maks.babyneeds.phase2.cart;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maks.babyneeds.Activity.LoginActivity;
import com.maks.babyneeds.Activity.PlaceOrderActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.SQLite.SQLiteUtil;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.Utility.Utils;
import com.maks.babyneeds.adapter.CartAdapter;
import com.maks.babyneeds.phase2.DashboardActivity;
import com.maks.model.CartList;
import com.maks.model.Product;
import com.maks.model.ShoppingCart;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment implements

        CartAdapter.OnItemClickListener {

    private RecyclerView.Adapter adapter;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.txtTotal) TextView txtTotal;
    @BindView(R.id.txtRs) TextView txtRs;
    @BindView(R.id.btnCheckOut)Button btnCheckout;

    private float totalAmt, mrp, qty, grandTotal = 0;
    ArrayList<ShoppingCart> list;
    String strGrandTotal="0";
    SQLiteUtil dbUtil;

    public CartFragment() {
        // Required empty public constructor
    }
    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_my_cart, container, false);
        ButterKnife.bind(this,view);


        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);

        dbUtil = new SQLiteUtil();
        Log.e("TAG", "Inside My Cart ");
        loadData();
        setListener();

        return view;

    }

        private void setListener() {
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnCheckout.getText().equals("Start Shopping")) {
                        Intent intent = new Intent(getActivity(), DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//                        startActivity(intent);

                    } else {

                        if(new AppPreferences(getActivity()).isLogin()) {
                            Intent intent = new Intent(getActivity(), PlaceOrderActivity.class);
                            intent.putExtra("amount", txtRs.getText().toString().substring(4));
                            CartList.getInstance().setArrayListCart(list);
                            startActivity(intent);
                        }
                        else{
                            Intent intent4 = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent4);
                        }

                    }
                }
            });
        }


    private void loadData() {
        list = dbUtil.getData(this.getActivity());
        if (list.size() == 0) {
            btnCheckout.setText("Start Shopping");
            grandTotal=0;
            txtRs.setText(String.format( "Rs.  %.2f", grandTotal ));
        } else {
            grandTotal=0;
            for (int i = 0; i < list.size(); i++) {
                try {
                    mrp = Integer.parseInt(Utils.discountPrice(list.get(i).getProduct().getMrp(),list.get(i).getProduct().getPer_discount()));

                    totalAmt = mrp * Integer.parseInt(list.get(i).getQuantity());
                    grandTotal = grandTotal + totalAmt;
                    String item = list.get(i).getProduct().getShort_desc().toString();
                    Log.e("TAG", "My Cart:::Item: " + item);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        adapter = new CartAdapter(list,    this);
        recyclerView.setAdapter(adapter);

        txtRs.setText(String.format( "Rs.  %.2f", grandTotal ));

    }


    public void onItemClick(View v, int position){
        ShoppingCart sh = list.get(position);
        if(v.getId()==R.id.btnDel) {
            dbUtil.deleteCartItem(sh.getId(), this.getActivity());
        }
        if(v.getId()==R.id.btnMinus) {
            int q =Integer.parseInt(sh.getQuantity());

            if(q>1){

                dbUtil.deleteCartItem(sh.getId(),this.getActivity());
                sh.setQuantity(""+(q-1));
                dbUtil.insert(sh,this.getActivity());

            }
        }
        if(v.getId()==R.id.btnPlus) {
            int q =Integer.parseInt(sh.getQuantity());
            if(q<Integer.parseInt(sh.getProduct().getStock())) {

                dbUtil.deleteCartItem(sh.getId(),this.getActivity());
                sh.setQuantity(""+(q+1));
                dbUtil.insert(sh,this.getActivity());
            }else {
                Toast.makeText(this.getActivity(), "product quantity exceeds stock!", Toast.LENGTH_SHORT).show();
            }
        }



        loadData();

    }

}
