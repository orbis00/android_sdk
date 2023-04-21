package com.wigzo.android.InAppMessage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.wigzo.android.base.WigzoApplication;
import com.wigzo.android.helpers.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

public class InAppMessageHandler {

    public interface OnDismissListener{
        void onDismiss(Map<String, String> remoteMessageData);
    }

    public interface OnCancelListener{
        void onCancel(Map<String, String> remoteMessageData);
    }

    private String title;
    private String body;
    private String imageUrl;
    private JSONObject payload;
    private Bitmap remotePicture;
    private boolean canceledOnTouchOutside = false;
    private Class<? extends AppCompatActivity> positiveResponseTargetActivity;
    private Class<? extends AppCompatActivity> negativeResponseTargetActivity;
    private String layoutId;
    private Map<String, String> remoteMessageData;

    private OnDismissListener dismissListenerContext = null;
    private OnCancelListener cancelListenerContext = null;

    public InAppMessageHandler() {}

    public InAppMessageHandler(Map<String, String> remoteMessageData,
                               Class<? extends AppCompatActivity> positiveResponseTargetActivity,
                               Class<? extends AppCompatActivity> negativeResponseTargetActivity) {
        this.title = remoteMessageData.get("title");
        this.body = remoteMessageData.get("body");
        this.imageUrl = remoteMessageData.get("image_url");
        this.payload = getPayloadJSONFromPayloadString(remoteMessageData.get("intent_data"));
        this.remotePicture = generateRemotePicture(this.imageUrl);
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        this.layoutId = remoteMessageData.get("layoutId");
        this.positiveResponseTargetActivity = positiveResponseTargetActivity;
        this.negativeResponseTargetActivity = negativeResponseTargetActivity;
        this.remoteMessageData = remoteMessageData;
    }

    private static JSONObject getPayloadJSONFromPayloadString(String payload) {
        if (!StringUtils.isEmpty(payload)) {
            try {
                return new JSONObject(payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public InAppMessageHandler setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public InAppMessageHandler setTitle(String title) {
        this.title = title;
        return this;
    }

    public InAppMessageHandler setBody(String body) {
        this.body = body;
        return this;
    }

    public InAppMessageHandler setPayload(JSONObject payload) {
        this.payload = payload;
        return this;
    }

    public InAppMessageHandler setRemotePicture(Bitmap remotePicture) {
        this.remotePicture = remotePicture;
        return this;
    }

    public InAppMessageHandler setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public InAppMessageHandler setOnDismissContext(Context context) {
        if (!OnDismissListener.class.isInstance(context)) {
            throw new RuntimeException("dismissListenerContext should be an instance of interface InAppMessageHandler.OnDismissListener");
        }
        this.dismissListenerContext = (OnDismissListener) context;
        return this;
    }

    public InAppMessageHandler setOnCancelContext(Context context) {
        if (!OnCancelListener.class.isInstance(context)) {
            throw new RuntimeException("cancelListenerContext should be an instance of interface InAppMessageHandler.OnCancelListener");
        }
        this.cancelListenerContext = (OnCancelListener) context;
        return this;
    }

    public InAppMessageHandler setPositiveResponseTargetActivity(Class<? extends AppCompatActivity> positiveResponseTargetActivity) {
        this.positiveResponseTargetActivity = positiveResponseTargetActivity;
        return this;
    }

    public InAppMessageHandler setNegativeResponseTargetActivity(Class<? extends AppCompatActivity> negativeResponseTargetActivity) {
        this.negativeResponseTargetActivity = negativeResponseTargetActivity;
        return this;
    }

    public InAppMessageHandler setLayoutID(String layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return this.body;
    }

    public JSONObject getPayload() {
        return this.payload;
    }

    public Bitmap getRemotePicture() {
        return this.remotePicture;
    }

    public Class<? extends AppCompatActivity> getPositiveResponseTargetActivity() {
        return this.positiveResponseTargetActivity;
    }

    public Class<? extends AppCompatActivity> getNegativeResponseTargetActivity() {
        return this.negativeResponseTargetActivity;
    }

    public String getLayoutID() {
        return this.layoutId;
    }

    public void createNotification() {
        if (positiveResponseTargetActivity == null ||
                negativeResponseTargetActivity == null ||
                cancelListenerContext == null ||
                dismissListenerContext == null) {
            throw new RuntimeException("positiveResponseTargetActivity, negativeResponseTargetActivity, cancelListenerContext, dismissListenerContext cannot be null");
        }
        ((AppCompatActivity) WigzoApplication.getAppContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                WigzoDialogTemplate wigzoDialogTemplate = null;

                if(StringUtils.isNotEmpty(imageUrl)) {
                    wigzoDialogTemplate
                            = new WigzoDialogTemplate(title,
                            body,
                            payload,
                            remotePicture,
                            positiveResponseTargetActivity,
                            negativeResponseTargetActivity,
                            layoutId,
                            remoteMessageData);
                }
                else {
                    wigzoDialogTemplate
                            = new WigzoDialogTemplate(title,
                            body,
                            payload,
                            positiveResponseTargetActivity,
                            negativeResponseTargetActivity,
                            remoteMessageData);
                }
                wigzoDialogTemplate.setCanceledOnTouchOutside(canceledOnTouchOutside);
                wigzoDialogTemplate.show();

                wigzoDialogTemplate.setOnCancelListener(dialogInterface -> {
                    if (null != cancelListenerContext) {
                        cancelListenerContext.onCancel(remoteMessageData);
                    }
                });

                wigzoDialogTemplate.setOnDismissListener(dialogInterface -> {
                    if(dismissListenerContext != null) {
                        dismissListenerContext.onDismiss(remoteMessageData);
                    }
                });
            }
        });
    }

    private Bitmap generateRemotePicture(String imageUrl) {

        if (!StringUtils.isEmpty(imageUrl)) {
            try {
                return BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
