package com.hoanmy.football.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hoanmy.football.commons.Config;
import com.hoanmy.football.utils.NotificationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "onMessageReceived: " + remoteMessage);

        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        } else if (remoteMessage.getNotification() != null) {
            // Check if message contains a notification payload.
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String imageUrl = data.getString("image");
            String url = data.getString("url");
            String type = data.getString("type");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "url: " + url);
            Log.e(TAG, "type: " + type);


            if (type.equals("web")) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                showNotificationMessage(getApplicationContext(), title, browserIntent, imageUrl);
            } else if (type.equals("cpi")) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url)));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url)));
                }
            } else {
                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), VideoPlayerLandActivity.class);
//                resultIntent.putExtra("_video", url);
//                resultIntent.putExtra("_titlevideo", title);
//                showNotificationMessage(getApplicationContext(), title, resultIntent, imageUrl);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, Intent intent, String imageUrl) throws IOException {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotification(getApplicationContext(), title, intent, imageUrl);
    }

    /**
     * Showing notification with text and image
     */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }
}
