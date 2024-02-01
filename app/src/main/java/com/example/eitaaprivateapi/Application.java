package com.example.eitaaprivateapi;

import android.content.Context;

public class Application extends android.app.Application {
    public static String APP_HASH = "0d870902a3e09e7cdc57e5d7bd68da6b";
    public static int APP_ID = 1782360;
    public static int BUILD_VERSION = 2454;
    public static String BUILD_VERSION_STRING = "6.3.4";

    public static Context applicationContext;
    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();
    }
}
