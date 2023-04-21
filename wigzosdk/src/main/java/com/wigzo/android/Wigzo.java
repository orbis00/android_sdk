package com.wigzo.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.wigzo.android.base.Task;
import com.wigzo.android.base.WigzoApplication;
import com.wigzo.android.helpers.Configuration;
import com.wigzo.android.helpers.StringUtils;
import com.wigzo.android.helpers.WigzoSharedStorage;
import com.wigzo.android.tasks.DeviceMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Wigzo {
    private static WigzoSharedStorage wigzoSharedStorage;
    private static List tasks = Collections.synchronizedList(new ArrayList<Task>());

    private static void initializeWigzoSharedStorage (Context context) {
        wigzoSharedStorage = new WigzoSharedStorage(WigzoApplication.getAppContext());
    }

    public static SharedPreferences getSharedStorage (){
        return wigzoSharedStorage.getSharedStorage();
    }

    private static void setOrgToken(String orgToken) {
        getSharedStorage().edit().putString(Configuration.ORG_TOKEN_KEY.value, orgToken).apply();
    }

    public static String getOrCreateDeviceId(){
        String deviceId = getSharedStorage().getString(Configuration.DEVICE_ID_KEY.value, "");
        if (StringUtils.isEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString();
            getSharedStorage().edit().putString(Configuration.DEVICE_ID_KEY.value, deviceId).apply();
        }
        return deviceId;
    }

    public static String getOrCreateAppKey(){
        String appKey = getSharedStorage().getString(Configuration.APP_KEY.value, "");
        if (StringUtils.isEmpty(appKey)) {
            appKey = UUID.randomUUID().toString();
            getSharedStorage().edit().putString(Configuration.APP_KEY.value, appKey).apply();
        }
        return appKey;
    }

    public static String getOrgToken() {
        return getSharedStorage().getString(Configuration.ORG_TOKEN_KEY.value, "");
    }

    public static void initialize(String orgToken, Context context) {
        initializeWigzoSharedStorage(context);
        setOrgToken(orgToken);
        setDeviceMappingTask();
    }

    public static void setDeviceMappingTask() {
        (new DeviceMapper()).push();
    }
}
