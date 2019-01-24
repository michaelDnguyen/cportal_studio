package com.dlvn.mcustomerportal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.utils.myLog;

public class WelcomeScreenActivity extends BaseActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {

                    startActivity(new Intent(WelcomeScreenActivity.this, DashboardActivity.class));
                    finish();
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
