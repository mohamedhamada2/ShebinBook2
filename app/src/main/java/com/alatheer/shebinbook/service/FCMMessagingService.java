package com.alatheer.shebinbook.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.alatheer.shebinbook.R;
import com.alatheer.shebinbook.comments.CommentActivity;
import com.alatheer.shebinbook.message.MessageActivity;
import com.alatheer.shebinbook.subcategory.SubCategoryActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationManagerCompat;



public class FCMMessagingService extends FirebaseMessagingService {
    String type = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getData().get("moredata"),remoteMessage.getNotification().getClickAction());
            super.onMessageReceived(remoteMessage);
        }catch (Exception e){
            Log.e("llll",e.getMessage());
        }

    }

    private void sendNotification(String title2,String messageBody,String data,String click_action) {
        //Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "heads up notification", importance);
            getSystemService(NotificationManager.class).createNotificationChannel(mChannel);
            Notification.Builder notification = new Notification.Builder(this,CHANNEL_ID);
            notification.setContentTitle(title2);
            notification.setContentText(messageBody);
            notification.setSmallIcon(R.drawable.shebinbook_notification);
            Log.e("click_action",click_action);
            if (click_action.equals("com.alatheer.shebinbook_FCM-POST")){
                    Intent notifyIntent = new Intent(this, CommentActivity.class);
                    notifyIntent.putExtra("flag",2);
                    notifyIntent.putExtra("title",title2);
                    notifyIntent.putExtra("message",messageBody);
                    notifyIntent.putExtra("moredata",  data);
                    //notifyIntent.putExtra("flag",2);
// Set the Activity to start in a new, empty task
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                            this, 0, notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );
                    notification.setContentIntent(notifyPendingIntent);
                }else if (click_action.equals("com.alatheer.shebinbook_FCM-MESSAGE")){
                    Intent notifyIntent = new Intent(this, MessageActivity.class);
                notifyIntent.putExtra("flag",2);
                notifyIntent.putExtra("title",title2);
                notifyIntent.putExtra("message",messageBody);
                notifyIntent.putExtra("moredata",  data);
                    //notifyIntent.putExtra("flag",2);
// Set the Activity to start in a new, empty task
                    notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
                    PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                            this, 0, notifyIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                    );
                    notification.setContentIntent(notifyPendingIntent);
                }else if (click_action.equals("com.alatheer.shebinbook_FCM-CATEGORY")){
                Intent notifyIntent = new Intent(this, SubCategoryActivity.class);
                notifyIntent.putExtra("flag",2);
                notifyIntent.putExtra("moredata",  data);
                //notifyIntent.putExtra("flag",2);
// Set the Activity to start in a new, empty task
                notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
// Create the PendingIntent
                PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                        this, 0, notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );
                notification.setContentIntent(notifyPendingIntent);
            }

            notification.setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1,notification.build());
        }
        /*Intent intent = new Intent("com.alatheer.noamany_FCM-MESSAGE");
        intent.putExtra("title",title2);
        intent.putExtra("message",messageBody);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.sendBroadcast(intent);*/
    }
}