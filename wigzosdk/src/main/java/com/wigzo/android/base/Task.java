package com.wigzo.android.base;

import android.util.Log;

import com.wigzo.android.Wigzo;
import com.wigzo.android.helpers.ConnectionStream;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class Task implements Serializable {

    protected abstract String buildUrl();
    
    public abstract JSONObject toJson();

    protected JSONObject prepareData () {
        JSONObject data = this.toJson();
        try {
            data.put("deviceId", Wigzo.getOrCreateDeviceId());
            data.put("appKey", Wigzo.getOrCreateAppKey());
            data.put("orgToken", Wigzo.getOrgToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    protected abstract boolean validate() throws ValidationException;

    public String takeAction() {
        String postUrl = this.buildUrl();
        String response = "";

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(
                () -> ConnectionStream.postRequest(postUrl, prepareData().toString())
        );

        try {
            response = future.get();
            Log.d("Action taken:", this.getClass().getName());
            Log.d("Response:", response != null ? response : "");
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return response;
    }

    public void push() {
        try {
            if (this.validate()) {
                LooperThread.sendMessage(this);
            }
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
