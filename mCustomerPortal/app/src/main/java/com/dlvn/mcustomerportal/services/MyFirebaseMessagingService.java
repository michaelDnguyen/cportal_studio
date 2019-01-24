package com.dlvn.mcustomerportal.services;

import android.os.Bundle;

import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.utils.myLog;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

    public static final String KEY_GCM_CATEGORY = "category";
    public static final String KEY_GCM_CODE = "badge";
    public static final String KEY_GCM_MESSAGE = "body";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        myLog.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            myLog.e(TAG, "Message Data payload(max 4kb): " + remoteMessage.getData());

            try {
                Map<String, String> data = remoteMessage.getData();
                if (data != null) {
                    String category = data.get(KEY_GCM_CATEGORY);
                    String code = data.get(KEY_GCM_CODE);
                    String message = data.get(KEY_GCM_MESSAGE);

                    Bundle bundle = new Bundle();
                    bundle.putString(KEY_GCM_CATEGORY, category);
                    bundle.putString(KEY_GCM_CODE, code);
                    bundle.putString(KEY_GCM_MESSAGE, message);
                    scheduleJob(bundle);
                }
            } catch (Exception e) {
                myLog.printTrace(e);
            }

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            myLog.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            myLog.e(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            myLog.e(TAG, "Message Notification Sound: " + remoteMessage.getNotification().getSound());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        myLog.e("onNewToken = " + token);
        CustomPref.setFirebaseToken(getBaseContext(), token);

        sendRegistrationToServer(token);
    }


    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob(Bundle bundle) {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyFirebaseJobService.class)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.NOW)
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setExtras(bundle)
                .setTag("mCP-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        myLog.D(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.


    }

}
