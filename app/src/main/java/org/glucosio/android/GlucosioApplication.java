package org.glucosio.android;

import android.app.Application;

import io.smooch.core.Smooch;


public class GlucosioApplication extends Application {
    String APP_TOKEN = "5b1d0b7fa711c4002227a14d";
    @Override
    public void onCreate() {
        super.onCreate();
        Smooch.init(this, APP_TOKEN);
    }
}