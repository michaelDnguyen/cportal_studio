package com.dlvn.mcustomerportal.utils;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.MyFirebaseMessagingService;

import java.util.Calendar;

public class NotificationUtil {

    private static final String TAG = "NotificationUtil";

    public static String createNotificationChannel(Context context, String channelID, String channelname, String channelDesc, int important, boolean channelVibrate, int channelLockView) {

        // NotificationChannels are required for Notifications on O (API 26) and above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // The id of the channel.
            String channelId = channelID;
            // The user-visible name of the channel.
            CharSequence channelName = channelname;
            // The user-visible description of the channel.
            String channelDescription = channelDesc;
            int channelImportance = important;
            boolean channelEnableVibrate = channelVibrate;
            int channelLockscreenVisibility = channelLockView;

            // Initializes NotificationChannel.
            NotificationChannel notificationChannel =
                    new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            notificationChannel.enableVibration(channelEnableVibrate);
            notificationChannel.setLockscreenVisibility(channelLockscreenVisibility);

            // Adds NotificationChannel to system. Attempting to create an existing notification
            // channel with its original values performs no operation, so it's safe to perform the
            // below sequence.
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            // Returns null for pre-O (26) devices.
            return null;
        }
    }

    public static void sendNotification(Context context, String msg, int code, String catagory) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channel_ID = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Do something for lollipop and above versions
            int importance = NotificationManager.IMPORTANCE_HIGH;
            channel_ID = context.getString(R.string.default_notification_channel_id);
            String channel_Name = context.getString(R.string.default_notification_channel_name);
            NotificationChannel mChannel = new NotificationChannel(channel_ID, channel_Name, importance);

            notificationManager.createNotificationChannel(mChannel);
        }

        // check case
        Intent myintent;
        myintent = new Intent(context, DashboardActivity.class);
        myintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myintent.putExtra(Constant.FCM_DESTINATION_ID, code);
        myintent.putExtra(Constant.FCM_DESTINATION_CATEGORY, catagory);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channel_ID)
                .setSmallIcon(R.drawable.daiichilife)
                .setContentTitle("mCP - Cổng thông tin Khách hàng")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);

        notificationManager.notify(Calendar.getInstance().get(Calendar.SECOND), mBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void sendNotification(Context context, String msg, int code, String catagory, String channelID, String channelName) {

        int notification_id = 1;

        int importance = NotificationManager.IMPORTANCE_HIGH;
        String channel_ID, channel_Name;
        if (!TextUtils.isEmpty(channelID))
            channel_ID = channelID;
        else
            channel_ID = context.getString(R.string.default_notification_channel_id);

        if (!TextUtils.isEmpty(channelName))
            channel_Name = channelName;
        else
            channel_Name = context.getString(R.string.default_notification_channel_name);

        NotificationChannel mChannel = new NotificationChannel(channel_ID, channel_Name, importance);

        // check case
        Intent myintent;
        myintent = new Intent(context, DashboardActivity.class);
        myintent.putExtra(Constant.FCM_DESTINATION_ID, code);
        myintent.putExtra(Constant.FCM_DESTINATION_CATEGORY, catagory);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(mChannel);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channel_ID)
                .setSmallIcon(R.drawable.daiichilife)
                .setContentTitle(msg)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText("mCP - Cổng thông tin Khách hàng")
                .setTicker(msg)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);
        myLog.e(TAG, "id notification = " + notification_id);

//        notificationManager.notify(notification_id, mBuilder.build());
        notificationManager.notify(Calendar.getInstance().get(Calendar.SECOND), mBuilder.build());
    }
}
