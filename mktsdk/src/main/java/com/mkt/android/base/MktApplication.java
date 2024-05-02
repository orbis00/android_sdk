package com.mkt.android.base;

import android.app.Application;
import android.content.Context;

public class MktApplication extends Application {

    private static Context context;
    public static LooperThread mainLooperThread;

    public void onCreate() {
        this.context = getApplicationContext();
        mainLooperThread = new LooperThread();
        mainLooperThread.start();
        super.onCreate();
    }

    public static Context getAppContext() {
        return MktApplication.context;
    }

    public static void setAppContext(Context context) {
        MktApplication.context = context;
    }
}
