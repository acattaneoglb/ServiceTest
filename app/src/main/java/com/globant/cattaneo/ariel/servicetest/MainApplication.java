package com.globant.cattaneo.ariel.servicetest;

import android.app.Application;

/**
 * Created by ariel.cattaneo on 08/04/2015.
 */
public class MainApplication extends Application {
    private static MainApplication ourInstance = new MainApplication();

    ServiceManager mServiceManager = null;

    public ServiceManager getServiceManager() {
        return mServiceManager;
    }

    public static MainApplication getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;

        mServiceManager = new ServiceManager(this);
    }
}
