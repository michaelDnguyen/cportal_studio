package com.dlvn.mcustomerportal.services;

import android.os.Bundle;

import com.dlvn.mcustomerportal.utils.NotificationUtil;
import com.dlvn.mcustomerportal.utils.myLog;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyFirebaseJobService extends JobService {

    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        myLog.e(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.

        // TODO(developer): add long running task here.
        if (jobParameters.getExtras() != null) {
            Bundle bundle = jobParameters.getExtras();

            String scode = bundle.getString(MyFirebaseMessagingService.KEY_GCM_CODE);
            String category = bundle.getString(MyFirebaseMessagingService.KEY_GCM_CATEGORY);
            String message = bundle.getString(MyFirebaseMessagingService.KEY_GCM_MESSAGE);
            int code = 0;
            try {
                code = Integer.parseInt(scode);
            } catch (NumberFormatException e) {
                myLog.printTrace(e);
                code = 0;
            }

            NotificationUtil.sendNotification(getApplicationContext(), message, code, category);
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
