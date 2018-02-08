package com.maks.babyneeds.Utility;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by Dell on 11/02/2016.
 */
public class Utils {

    public static Typeface setLatoFont(Activity activity) {
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), Constants.ROBOTO);
        return tf;
    }

    public static Typeface setLatoFontBold(Activity activity) {
        Typeface tf = Typeface.createFromAsset(activity.getAssets(), Constants.ROBOTO);
        return tf;
    }

    public static String discountPrice(String strprice, String disc) {

        double price = Double.parseDouble(strprice);

        int dis = Integer.parseInt(disc);
        Log.e(""+price, ""+dis);

        double oneper = price/100;

        double rs1 = price - (oneper*dis);

        Log.e("final price",""+rs1);

        return ""+ Math.round(rs1);
    }
}
