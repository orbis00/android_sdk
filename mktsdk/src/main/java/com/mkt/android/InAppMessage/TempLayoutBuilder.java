package com.mkt.android.InAppMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mkt.android.InAppMessage.MktLayoutAttributes.MktLayoutProperties;
import com.squareup.picasso.Picasso;
import com.mkt.android.R;
import com.mkt.android.base.MktApplication;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Map;


public class TempLayoutBuilder extends Dialog implements View.OnClickListener {

    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";
    public static final String CENTER = "center";
    public static final String FULLSCREEN = "fullScreen";
    public static final String IMAGETEXT = "image&text";

    public static final String IMAGE = "image";
    public static final String TEXT = "text";

    public static final String VERTICAL = "vertical";
    public static final String HORIZONTAL = "horizontal";


    private Map<String, String> remoteMessageData;


    private Class<? extends AppCompatActivity> positiveResponseTargetActivity;
    private Class<? extends AppCompatActivity> negativeResponseTargetActivity;

    public TempLayoutBuilder(Class<? extends AppCompatActivity> positiveResponseTargetActivity,
                             Class<? extends AppCompatActivity> negativeResponseTargetActivity, Map<String, String> remoteMessageData) {
        super(MktApplication.getAppContext());
        this.positiveResponseTargetActivity = positiveResponseTargetActivity;
        this.negativeResponseTargetActivity = negativeResponseTargetActivity;
        this.remoteMessageData = remoteMessageData;

    }

    @Override
    public void onCreate(Bundle bundleSavedInstance) {
        super.onCreate(bundleSavedInstance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Context context  = MktApplication.getAppContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View customNotificationLayout = layoutInflater.inflate(MktLayoutProperties.getMktLayoutId(remoteMessageData.get("layoutId")),null);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(16f); // Adjust the radius to control corner roundness

        // Set the GradientDrawable as the background of the view
        customNotificationLayout.setBackground(gradientDrawable);

        String notificationDetails = remoteMessageData.get("notification_details");
        String templateType = remoteMessageData.get("type");
        try {
            JSONObject notificationData = new JSONObject(notificationDetails);
            String orientation = notificationData.getString("templateOrientation");

            JSONObject typeObject = new JSONObject(templateType);
            String type = typeObject.getString("type");
            String buttonOrientation = notificationData.getString("buttonOrientation");

            switch (orientation) {
                case TOP:
                case BOTTOM:
                    onCreateAlertDialog(customNotificationLayout,notificationData,orientation,type,buttonOrientation);
                    break;
                case FULLSCREEN:
                    onCreateFullScreenDialog(customNotificationLayout,notificationData,orientation,type,buttonOrientation);
                    break;
                case CENTER:
                    onCreateCenterDialog(customNotificationLayout,notificationData,orientation,type,buttonOrientation);
                    break;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCreateCenterDialog(View customNotificationLayout,JSONObject notifiacationDetails,String orientation,String type,String buttonOrientation) throws JSONException {
        ImageView imageView = customNotificationLayout.findViewById(R.id.imageView);
        TextView title = customNotificationLayout.findViewById(R.id.notification_title);
        TextView notificationText = customNotificationLayout.findViewById(R.id.notification_message);
        customNotificationLayout.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
//         Adding the text View in the Layout
        title.setText(remoteMessageData.get("title"));
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if(type.equals(IMAGE) || type.equals(IMAGETEXT)) {
            addImage(imageView,orientation,notifiacationDetails);
        }
        textParams.addRule(RelativeLayout.BELOW,imageView.getId());
        textParams.leftMargin = 20;
        textParams.topMargin = 20;

        // Adding the message in the layout
        title.setLayoutParams(textParams);

        notificationText.setText(remoteMessageData.get("body"));

        RelativeLayout.LayoutParams bodyMessageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        bodyMessageParams.addRule(RelativeLayout.BELOW, title.getId());
        bodyMessageParams.leftMargin = 20;

        String textAllignment = notifiacationDetails.getString("textAllignment");

        if(textAllignment.equals( "center")){
            textParams.addRule(RelativeLayout.CENTER_HORIZONTAL,imageView.getId());
            bodyMessageParams.addRule(RelativeLayout.CENTER_HORIZONTAL,title.getId());
        }

        notificationText.setLayoutParams(bodyMessageParams);

        addLayoutProperties(notifiacationDetails,title,notificationText,customNotificationLayout);

        if(remoteMessageData.containsKey("button")) {
            addButtons(customNotificationLayout,imageView,title,notificationText,type,buttonOrientation,orientation);
        }else {
            LinearLayout layout = customNotificationLayout.findViewById(R.id.buttons);
            layout.setVisibility(View.GONE);
        }

        setContentView(customNotificationLayout);
    }

    public void onCreateAlertDialog(View customNotificationLayout,JSONObject notificationData,String orientation,String type,String buttonOrientation) throws JSONException {

        AlertDialog.Builder builder = new AlertDialog.Builder(MktApplication.getAppContext());
        ImageView imageView = customNotificationLayout.findViewById(R.id.imageView);

        TextView title = customNotificationLayout.findViewById(R.id.notification_title);
        title.setText(remoteMessageData.get("title"));

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.addRule(RelativeLayout.END_OF, imageView.getId());
        textParams.topMargin = 20;
        textParams.leftMargin = 20;
        textParams.addRule(RelativeLayout.ALIGN_TOP, imageView.getId());

        // Adding the message in the layout
        title.setLayoutParams(textParams);

        TextView notificationText = customNotificationLayout.findViewById(R.id.notification_message);
        notificationText.setText(remoteMessageData.get("body"));

        RelativeLayout.LayoutParams bodyMessageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        bodyMessageParams.addRule(RelativeLayout.END_OF, imageView.getId());
        bodyMessageParams.addRule(RelativeLayout.BELOW, title.getId());
        bodyMessageParams.addRule(RelativeLayout.ALIGN_RIGHT);
        bodyMessageParams.rightMargin=20;
        bodyMessageParams.leftMargin=20;
        notificationText.setLayoutParams(bodyMessageParams);

        if (type.equals(IMAGE) || type.equals(IMAGETEXT)) {
            addImage(imageView,orientation,notificationData);
        }else {
            imageView.setVisibility(View.GONE);
        }
        addLayoutProperties(notificationData,title,notificationText,customNotificationLayout);

        if(remoteMessageData.containsKey("button") ) {
            addButtons(customNotificationLayout,imageView,title,notificationText,type,buttonOrientation,orientation);
        } else  {
            LinearLayout layout = customNotificationLayout.findViewById(R.id.buttons);
            layout.setVisibility(View.GONE);
        }
        builder.setView(customNotificationLayout);


        AlertDialog dialog = builder.create();
//        dialog.setView(customNotificationLayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();

        if (window != null) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(window.getAttributes());
            if (orientation.equals(TOP)) layoutParams.gravity = Gravity.TOP;
            if (orientation.equals(BOTTOM)) layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
        }
        dialog.show();
    }

    public void addLayoutProperties(JSONObject notificationDetails,TextView textView,TextView notificationText,View customNotificationLayout)throws JSONException {
        textView.setTextColor(Color.parseColor(notificationDetails.getString("titleColor")));
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        customNotificationLayout.setBackgroundColor(Color.parseColor(notificationDetails.getString("templateBackground")));
        notificationText.setTextColor((Color.parseColor(notificationDetails.getString("titleColor"))));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(notificationDetails.getString("titleFontSize"))); // Set text size to 18sp
        notificationText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(notificationDetails.getString("descriptionFontSize")));// Set text size to 14sp
        notificationText.setTextColor(Color.parseColor((notificationDetails.getString("descriptionFontColor"))));

        if (notificationDetails.getBoolean("isclose")) {
            ImageView xIcon = new ImageView(MktApplication.getAppContext());
            xIcon.setImageResource(R.drawable.group_3);
            RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
                    60,
                    60
            );
            iconParams.topMargin = 20;
            iconParams.rightMargin = 20;
            iconParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            iconParams.addRule(RelativeLayout.ALIGN_PARENT_END);

            xIcon.setLayoutParams(iconParams);

            xIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Finish the current activity when the icon is clicked
                    customNotificationLayout.setVisibility(View.GONE);
                }
            });
            ((RelativeLayout) customNotificationLayout).addView(xIcon);
        }
    }

    public void addImage(ImageView imageView,String orientation,JSONObject notificationDetails) throws JSONException {

        JSONArray image = new JSONArray(remoteMessageData.get("imageUrl"));
        Picasso.get().
                load(image.getString(0)).
                into(imageView);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        if(orientation.equals(TOP) || orientation.equals(BOTTOM)){
            imageParams = new RelativeLayout.LayoutParams(250,200);
            imageParams.topMargin = 20;
            imageParams.leftMargin = 20;
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }else if(orientation.equals(FULLSCREEN)) {

            imageParams  = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
        }else if(orientation.equals(CENTER)){
            if(notificationDetails.get("imageOrientation").equals("fullLayout")) {
                imageParams.topMargin = 0;
                imageParams = new RelativeLayout.LayoutParams(1000,800);
            }else {
                imageParams = new RelativeLayout.LayoutParams(600,
                        500);
                imageParams.topMargin = 50;

            }
            imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }
        imageView.setLayoutParams(imageParams);
    }



    public void addButtons(View customNotificationLayout,ImageView imageView, TextView title,
                           TextView notificationText,String type,String buttonOrientation,String orientation) throws JSONException {

        JSONArray buttonArray = new JSONArray(remoteMessageData.get("button"));

        Button button1 = customNotificationLayout.findViewById(R.id.notification_button1);
        Button button2 = customNotificationLayout.findViewById(R.id.notification_button2);

        LinearLayout layout  = customNotificationLayout.findViewById(R.id.buttons);

        if(buttonOrientation.equals(VERTICAL)) {
            layout.setOrientation(LinearLayout.VERTICAL);
        }else if(buttonOrientation.equals(HORIZONTAL)) {
            layout.setOrientation(LinearLayout.HORIZONTAL);
            // Adjust the layout parameters for horizontal orientation
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f // Equal weight for both buttons
            );
            button1.setLayoutParams(params);
            button2.setLayoutParams(params);

            // Show both buttons in horizontal orientation
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
        }

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(orientation.equals(FULLSCREEN)) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        } else if(orientation.equals(TOP) || orientation.equals(BOTTOM)) {
            layoutParams.addRule(RelativeLayout.BELOW, imageView.getId());
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        } else if(orientation.equals(CENTER)) {
            layoutParams.addRule(RelativeLayout.BELOW, notificationText.getId());
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }
        layout.setLayoutParams(layoutParams);

        if (buttonArray.length() == 1) {
            JSONObject buttonObject = buttonArray.getJSONObject(0);
            button1.setText(buttonObject.getString("buttonName"));
            button1.setBackgroundColor(Color.parseColor(buttonObject.getString("buttonColor")));
            button1.setTextColor(Color.parseColor(buttonObject.getString("buttonFontColor")));
            button1.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(buttonObject.getString("buttonFontSize")));


            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(buttonObject.getString("buttonUrl")));
                        getContext().startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // Ensure button2 is hidden when only one button is displayed
            button2.setVisibility(View.GONE);
        }else {
            JSONObject buttonObject1 = buttonArray.getJSONObject(0);
            JSONObject buttonObject2 = buttonArray.getJSONObject(1);

            button1.setText(buttonObject1.getString("buttonName"));
            button1.setBackgroundColor(Color.parseColor(buttonObject1.getString("buttonColor")));
            button1.setTextColor(Color.parseColor(buttonObject1.getString("buttonFontColor")));
            button1.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(buttonObject1.getString("buttonFontSize")));
            button1.setVisibility(View.VISIBLE);

            button2.setText(buttonObject2.getString("buttonName"));
            button2.setBackgroundColor(Color.parseColor(buttonObject2.getString("buttonColor")));
            button2.setTextColor(Color.parseColor(buttonObject2.getString("buttonFontColor")));
            button2.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(buttonObject2.getString("buttonFontSize")));
            button2.setVisibility(View.VISIBLE);

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(buttonObject1.getString("buttonUrl")));
                        getContext().startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(buttonObject2.getString("buttonUrl")));
                        getContext().startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
        }


    }

    public void onCreateFullScreenDialog(View customNotificationLayout,JSONObject notificationData,String orientation,String type,String buttonOrientation) throws JSONException {
        Dialog dialog = new Dialog(MktApplication.getAppContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ImageView imageView = customNotificationLayout.findViewById(R.id.imageView);

        TextView title = customNotificationLayout.findViewById(R.id.notification_title);
        TextView notificationText = customNotificationLayout.findViewById(R.id.notification_message);

        title.setText(remoteMessageData.get("title"));

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        textParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        textParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, imageView.getId());
        textParams.topMargin = 200;
        textParams.leftMargin = 40;
        textParams.rightMargin = 30;

        // Adding the message in the layout
        title.setLayoutParams(textParams);

        notificationText.setText(remoteMessageData.get("body"));

        RelativeLayout.LayoutParams bodyMessageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        bodyMessageParams.topMargin = 30;
        bodyMessageParams.leftMargin = 40;
        bodyMessageParams.rightMargin = 30;

        bodyMessageParams.addRule(RelativeLayout.BELOW, title.getId());
        bodyMessageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        notificationText.setLayoutParams(bodyMessageParams);

        addLayoutProperties(notificationData, title, notificationText, customNotificationLayout);

        if (type.equals(IMAGE)) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            JSONArray image = new JSONArray(remoteMessageData.get("imageUrl"));
            try {
                URL url = new URL(image.getString(0));
                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Drawable imageUrl = new BitmapDrawable(MktApplication.getAppContext().getResources(), bitmap);
                customNotificationLayout.setBackground(imageUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (remoteMessageData.containsKey("button")) {
            addButtons(customNotificationLayout, imageView, title, notificationText, type, buttonOrientation, orientation);
        } else {
            LinearLayout layout = customNotificationLayout.findViewById(R.id.buttons);
            layout.setVisibility(View.GONE);
        }
        dialog.setContentView(customNotificationLayout);
        dialog.show();

    }


    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        Intent targetActivityIntent = new Intent(MktApplication.getAppContext(), negativeResponseTargetActivity);
        targetActivityIntent.putExtra("isResponsePositive",false);

        JSONObject remoteMessageDataJson = new JSONObject(this.remoteMessageData);

        if(viewID == R.id.btn_yes && null != positiveResponseTargetActivity ) {
            targetActivityIntent = new Intent(MktApplication.getAppContext(),positiveResponseTargetActivity);
            targetActivityIntent.putExtra("isResponsePositive",true);
        }
        targetActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        targetActivityIntent.putExtra("notificationData", remoteMessageDataJson != null ? remoteMessageDataJson.toString() : null);

        MktApplication.getAppContext().startActivity(targetActivityIntent);

    }
}