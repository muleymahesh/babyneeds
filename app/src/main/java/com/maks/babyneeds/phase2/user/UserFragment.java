package com.maks.babyneeds.phase2.user;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maks.babyneeds.Activity.LoginActivity;
import com.maks.babyneeds.Activity.MyOrdersActivity;
import com.maks.babyneeds.Activity.PasswordResetActivity;
import com.maks.babyneeds.Activity.R;
import com.maks.babyneeds.Activity.SplashScreen;
import com.maks.babyneeds.Utility.AppPreferences;
import com.maks.babyneeds.phase2.DashboardActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtName)
    TextView txtName;
    @BindView(R.id.btnLogin)
    TextView btnLogin;


    public UserFragment() {
        // Required empty public constructor
    }
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this,v);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(new AppPreferences(getActivity()).isLogin()) {
            txtEmail.setText(new AppPreferences(getActivity()).getEmail());
            txtName.setText(new AppPreferences(getActivity()).getFname());
            btnLogin.setVisibility(View.GONE);
        }else{
            txtEmail.setText("");
            txtName.setText("Welcome Guest");
        }
    }

    @OnClick(R.id.btnMyOrders)
    public void showMyOrder(){
        Intent mainIntent;
        if(new AppPreferences(getActivity()).isLogin()) {
            mainIntent = new Intent(getActivity(), MyOrdersActivity.class);//LoginActivity
        }
        else{
            mainIntent = new Intent(getActivity(), LoginActivity.class);//LoginActivity

        }
        startActivity(mainIntent);
    }

    @OnClick(R.id.btnLogin)
    public void showLogin(){
        Intent mainIntent;
            mainIntent = new Intent(getActivity(), LoginActivity.class);//LoginActivity

        startActivity(mainIntent);
    }


    @OnClick(R.id.btnChangePass)
    public void changePassword(){
        Intent mainIntent;
        if(new AppPreferences(getActivity()).isLogin()) {
            mainIntent = new Intent(getActivity(), PasswordResetActivity.class);//LoginActivity
        }
        else{
            mainIntent = new Intent(getActivity(), LoginActivity.class);//LoginActivity

        }
        startActivity(mainIntent);
    }
}
