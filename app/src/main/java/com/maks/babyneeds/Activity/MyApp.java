package com.maks.babyneeds.Activity;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by maks on 10/5/16.
 */
public class MyApp extends MultiDexApplication {
    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(MyApp.this);
    }
}
