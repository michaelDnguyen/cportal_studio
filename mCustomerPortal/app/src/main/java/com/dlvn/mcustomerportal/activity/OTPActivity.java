package com.dlvn.mcustomerportal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.view.otpview.OnOtpCompletionListener;
import com.dlvn.mcustomerportal.view.otpview.OtpView;

public class OTPActivity extends BaseActivity {

    private static final String TAG = "OTPActivity";

    OtpView otpview;
    ImageButton btnClose;
    TextView tvTitle;
    Button btnTieptuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        otpview = findViewById(R.id.otpview);
        btnClose = findViewById(R.id.btnClose);
        tvTitle = findViewById(R.id.tvTitle);
        btnTieptuc = findViewById(R.id.btnTieptuc);
    }

    private void initData() {
    }

    private void setListener() {
        otpview.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Toast.makeText(OTPActivity.this, "OTP Input = " + otp, Toast.LENGTH_LONG).show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnTieptuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
