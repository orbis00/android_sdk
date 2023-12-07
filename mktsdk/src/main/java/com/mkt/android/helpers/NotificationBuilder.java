package com.mkt.android.helpers;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.CountDownTimer;
import android.os.Looper;
import android.view.View;
import android.widget.RemoteViews;
import androidx.core.app.NotificationCompat;
import com.mkt.android.base.MktApplication;
import com.squareup.picasso.Picasso;
import com.mkt.android.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NotificationBuilder {

    public static final String CAROUSEL = "carousel";
    public static final String SIMPLE_IMAGE = "image";

    public static NotificationCompat.Builder notificationBuilder(Map<String, String> notificationData, NotificationCompat.Builder notificationBuilder, Intent intent,
                                                                 PendingIntent pendingIntent) throws JSONException {

        String templateType = notificationData.get("type");
        JSONObject jsonobject = new JSONObject(templateType);
        String type = jsonobject.getString("type");

        NotificationCompat.Action action = null;

        Context context = MktApplication.getAppContext();

        ArrayList<String> urlList = new ArrayList<>();

        if(notificationData.get("button") != null) {
            JSONArray buttonArray = new JSONArray(notificationData.get("button"));
            for (int i = 0; i < buttonArray.length(); i++) {
                JSONObject buttonObject = buttonArray.getJSONObject(i);

                String buttonUrl = buttonObject.optString("buttonUrl");
                String buttonName = buttonObject.optString("buttonName");
                // Do something with the extracted button data

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(buttonUrl));// Replace with your activity class

                intent.putExtra("url", buttonUrl);
                intent.putExtra("buttonName",buttonName);// Pass the URL as an extra to the activity

                intent.setAction(Intent.ACTION_VIEW);
                pendingIntent = pendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

                action = new NotificationCompat.Action.Builder(
                        R.id.button1, buttonName, pendingIntent)
                        .build();
                notificationBuilder.addAction(action);

                if (i < buttonArray.length() - 1) {
                    Intent emptyIntent = new Intent();
                    PendingIntent emptyPendingIntent = PendingIntent.getActivity(context, i, emptyIntent, PendingIntent.FLAG_IMMUTABLE);

                    NotificationCompat.Action emptyAction = new NotificationCompat.Action.Builder(
                            0, "                               ", emptyPendingIntent) // Use spaces as the label for spacing
                            .build();

                    notificationBuilder.addAction(emptyAction);
                }

            }
        }
        if(notificationData.get("imageUrl") != null) {
            JSONArray jsonArray = new JSONArray(notificationData.get("imageUrl"));

            for (int i = 0; i < jsonArray.length(); i++) {
                String url = jsonArray.getString(i);
                urlList.add(url);
            }
        }
        if (type.equals(CAROUSEL)) {
            notificationBuilder = carouselNotification(context,notificationBuilder,urlList,notificationData.get("title"),notificationData.get("body"),pendingIntent);
        } else if (type.equals(SIMPLE_IMAGE)) {
            String image = urlList.get(0);
            notificationBuilder = imageNotification(notificationBuilder, image);
        }

        if (notificationData.get("timer_duration") != null) {
            long timerDurationMillis = Long.parseLong(notificationData.get("timer_duration"));
            startCountdownAndNotify(context, notificationBuilder, notificationData.get("title"), timerDurationMillis);
        }
        return notificationBuilder;
    }

    private static void startCountdownAndNotify(Context context, NotificationCompat.Builder notificationBuilder, String title, long timerDurationMillis) {

        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                CountDownTimer countDownTimer = new CountDownTimer(timerDurationMillis, 1000) { // Update every 1 second
                    public void onTick(long millisUntilFinished) {
                        long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;

                        String remainingTime = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

                        // Update the notification with the remaining time
                        notificationBuilder.setContentTitle(title + "                    " + remainingTime);
//                        // Build and notify the updated notification
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notificationBuilder.build());
                    }

                    public void onFinish() {
                        // Countdown finished, you can update the notification with an appropriate message
                        notificationBuilder.setContentTitle(title + "                    Sale Ended");
                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, notificationBuilder.build());
                    }
                };
                countDownTimer.start();
            }
        });
    }

    public static NotificationCompat.Builder imageNotification(NotificationCompat.Builder builder, String imageUrl) {
        // Load the image from the URL and create a Bitmap
        Bitmap imageBitmap = loadImageFromUrl(imageUrl);
        // Create a BigPictureStyle for the notification
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(imageBitmap);

        // Set the content of the notification to the image and message
        builder.setStyle(bigPictureStyle);
        return builder;
    }

    @SuppressLint("RestrictedApi")
    public static NotificationCompat.Builder carouselNotification(Context context, NotificationCompat.Builder builder,
                                                                  List<String> imagesList,
                                                                  String title,
                                                                  String desc, PendingIntent pendingIntent) {
        // Parent notification layout
        RemoteViews customLayout = new RemoteViews(context.getPackageName(), R.layout.notification_slider_layout);
//        // Set title and description
        customLayout.setTextViewText(R.id.textViewTitle, title);
        customLayout.setTextViewText(R.id.textViewMessage, desc);

        // ViewFlipper for images
        for (String imgUrl : imagesList) {
            RemoteViews viewFlipperImage = new RemoteViews(context.getPackageName(), R.layout.notification_slider_image);
            // Load and set the image from the URL
            Bitmap remotePicture = loadImageFromUrl(imgUrl);
            viewFlipperImage.setImageViewBitmap(R.id.imageView, remotePicture);
            // Adding each image view in the view flipper
            customLayout.addView(R.id.viewFlipper, viewFlipperImage);
        }
        if(!builder.mActions.isEmpty()) {
            customLayout.setTextViewText(R.id.button1, builder.mActions.get(0).title);
            customLayout.setOnClickPendingIntent(R.id.button1, builder.mActions.get(0).getActionIntent());
            if((builder.mActions.get(2) != null)) {
                customLayout.setTextViewText(R.id.button2, builder.mActions.get(2).title);
                customLayout.setOnClickPendingIntent(R.id.button2, builder.mActions.get(2).getActionIntent());
                customLayout.setViewVisibility(R.id.button2, View.VISIBLE);
            }
        }
        builder.setCustomBigContentView(customLayout).setContent(customLayout)
                .setContentIntent(pendingIntent);
        return builder;
    }

    private static Bitmap loadImageFromUrl(String imageUrl) {
        try {
            return Picasso.get().load(imageUrl).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}