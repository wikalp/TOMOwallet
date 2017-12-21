package com.tomoapp.tomowallet.base;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by macbook on 12/21/17.
 */

public class MainApplication extends MultiDexApplication {


    private static MainApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
    }


    public static MainApplication get(){
        return instance;
    }
}
