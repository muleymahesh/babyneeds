package com.maks.babyneeds.Utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Dell on 11/03/2016.
 */
public class AppPreferences {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AppPreferences(Context context) {
        // TODO Auto-generated constructor stub
        sharedPreferences = context.getSharedPreferences("Babyneeds",
                Context.MODE_PRIVATE);
    }

    public boolean isLogin(){
        return sharedPreferences.getBoolean("login",false);

    }
    public void setLogin(boolean flag){
        SharedPreferences.Editor et = sharedPreferences.edit();
        et.putBoolean("login",flag);
        et.commit();

    }

    public String getEmail() {
        return sharedPreferences.getString("email","");
    }

    public void setEmail(String email) {

        SharedPreferences.Editor et = sharedPreferences.edit();
        et.putString("email",email);
        et.commit();
    }
    public String getFname() {
        return sharedPreferences.getString("fname","Guest");
    }

    public void setFname(String fname) {

        SharedPreferences.Editor et = sharedPreferences.edit();
        et.putString("fname",fname);
        et.commit();
    }
}