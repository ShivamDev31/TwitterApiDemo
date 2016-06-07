package com.shivamdev.apidemo.main;

import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.shivamdev.apidemo.dagger.DaggerServiceComponent;
import com.shivamdev.apidemo.dagger.ServiceComponent;

/**
 * Created by shivamchopra on 03/06/16.
 */
public class MainApplication extends Application {
    private static MainApplication instance;
    private ServiceComponent component;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        component = DaggerServiceComponent.builder().build();
    }

    public ServiceComponent component() {
        return component;
    }

    public boolean isNetworkConnectedOrConnecting() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
