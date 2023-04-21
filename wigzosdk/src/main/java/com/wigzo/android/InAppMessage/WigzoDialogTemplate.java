package com.wigzo.android.InAppMessage;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wigzo.android.InAppMessage.WigzoLayoutAttributes.WigzoLayoutProperties;
import com.wigzo.android.R;
import com.wigzo.android.base.WigzoApplication;
import com.wigzo.android.helpers.StringUtils;

import org.json.JSONObject;

import java.util.Map;

public class WigzoDialogTemplate extends Dialog implements View.OnClickListener {

    private TextView notification_body;
    private TextView notification_title;

    private Button yes;
    private Button no;

    private ImageView notification_image;

    private Bitmap remotePicture = null;
    private String body = "";
    private String title = "";
    private String layoutIdStr = "001";
    private JSONObject payload;
    private int layoutId = 0;
    private boolean hasImageView = false;
    private WigzoLayoutProperties wigzoLayoutProperties;
    private Map<String, String> remoteMessageData;

    private Class<? extends AppCompatActivity> positiveResponseTargetActivity = null;
    private Class<? extends AppCompatActivity> negativeResponseTargetActivity = null;

    public WigzoDialogTemplate(String title, String body, JSONObject payload,
                               Class<? extends AppCompatActivity> positiveResponseTargetActivity,
                               Class<? extends AppCompatActivity> negativeResponseTargetActivity,
                               Map<String, String> remoteMessageData) {
        super(WigzoApplication.getAppContext());
        this.title = title;
        this.body = body;
        this.positiveResponseTargetActivity = positiveResponseTargetActivity;
        this.negativeResponseTargetActivity = negativeResponseTargetActivity;
        this.payload = payload;
        this.remoteMessageData = remoteMessageData;
    }

    public WigzoDialogTemplate(String title, String body, JSONObject payload,
                               Bitmap remote_picture,
                               Class<? extends AppCompatActivity> positiveResponseTargetActivity,
                               Class<? extends AppCompatActivity> negativeResponseTargetActivity,
                               String layoutIdStr, Map<String, String> remoteMessageData) {
        super(WigzoApplication.getAppContext());
        this.title = title;
        this.body = body;
        this.remotePicture = remote_picture;
        this.positiveResponseTargetActivity = positiveResponseTargetActivity;
        this.negativeResponseTargetActivity = negativeResponseTargetActivity;
        this.payload = payload;
        this.layoutIdStr = layoutIdStr;
        this.remoteMessageData = remoteMessageData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (StringUtils.isNotEmpty(this.layoutIdStr)) {
            layoutId = WigzoLayoutProperties.getWigzoLayoutId(this.layoutIdStr);
        } else {
            layoutId = R.layout.wigzo_dialog_template_1;
        }

        setContentView(layoutId);

        wigzoLayoutProperties = WigzoLayoutProperties.getWigzoLayoutProperties(layoutId, WigzoApplication.getAppContext());

        hasImageView = wigzoLayoutProperties.hasImage;

        if (hasImageView) {
            notification_image = (ImageView) findViewById(R.id.notification_image);
        }

        if (null != remotePicture && hasImageView) {
                notification_image.setVisibility(View.VISIBLE);
                notification_image.setImageBitmap(remotePicture);
        }

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);

        notification_title = (TextView) findViewById(R.id.notification_title);
        notification_body = (TextView) findViewById(R.id.notification_body);

        notification_title.setText(title);
        notification_body.setText(body);

        yes.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        Intent targetActivityIntent = new Intent(WigzoApplication.getAppContext(), negativeResponseTargetActivity);
        targetActivityIntent.putExtra("isResponsePositive", false);

        JSONObject remoteMessageDataJson = new JSONObject(this.remoteMessageData);

        if (viewId == R.id.btn_yes && null != positiveResponseTargetActivity) {
            targetActivityIntent = new Intent(WigzoApplication.getAppContext(), positiveResponseTargetActivity);
            targetActivityIntent.putExtra("isResponsePositive", true);
        }

        targetActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        targetActivityIntent.putExtra("notificationData", remoteMessageDataJson != null ?  remoteMessageDataJson.toString() : null);

        WigzoApplication.getAppContext().startActivity(targetActivityIntent);
        this.dismiss();
    }
}
