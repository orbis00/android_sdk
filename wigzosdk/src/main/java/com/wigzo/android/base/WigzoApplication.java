package com.wigzo.android.base;

import android.app.Application;
import android.content.Context;

public class WigzoApplication extends Application {

    private static Context context;
    public static LooperThread mainLooperThread;

    public void onCreate() {
        this.context = getApplicationContext();
        mainLooperThread = new LooperThread();
        mainLooperThread.start();
        super.onCreate();
    }

    public static Context getAppContext() {
        return WigzoApplication.context;
    }

    public static void setAppContext(Context context) {
        WigzoApplication.context = context;
    }
}
