package com.wigzo.android.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.wigzo.android.Wigzo;
import com.wigzo.android.base.WigzoApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Device {

    public static String fetchIpAddress(){
        String ipAddress = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(Configuration.WIGZO_SDK_TAG.value,"No device ipAddress found!");
        }
        return ipAddress;
    }

    public static String fetchAppVersion() {
        String appVersion = Configuration.DEFAULT_APP_VERSION.value;
        try {
            Context context = WigzoApplication.getAppContext();
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            Log.i(Configuration.WIGZO_SDK_TAG.value, "No app version found");
        }
        Log.i(Configuration.WIGZO_SDK_TAG.value, "App version :"+appVersion);

        return appVersion;
    }

    public static JSONObject fetchDeviceLocation() {
        String location = Wigzo.getSharedStorage().getString(Configuration.DEVICE_LOCATION_KEY.value, null);
        if (StringUtils.isNoneEmpty(location)) {
            try {
                return new JSONObject(location);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        String orgToken = Wigzo.getOrgToken();
        final String url = WigzoUrlWrapper.addQueryParam(
                WigzoUrlWrapper.addQueryParam(
                        Configuration.BASE_URL.value + Configuration.USER_LOCATION_URL.value,
                        "orgId", orgToken), Configuration.SITE_ID.value, orgToken);

        //Initialise Executor Service to get device location from server
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(new Callable<String>() {
            public String call() {
                //Get response from server
                String response = ConnectionStream.getRequest(url);

                //Check if response is not null
                if (StringUtils.isNotEmpty(response)) {
                    //Save device location
                    Wigzo.getSharedStorage()
                            .edit()
                            .putString(Configuration.DEVICE_LOCATION_KEY.value, response.toString())
                            .apply();
                }
                return response;
            }
        });
        JSONObject res = null;
        try {
            res = new JSONObject(future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return res;
    }
}
