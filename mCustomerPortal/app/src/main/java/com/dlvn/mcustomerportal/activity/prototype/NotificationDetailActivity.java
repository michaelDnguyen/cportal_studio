package com.dlvn.mcustomerportal.activity.prototype;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.NotificationModel;
import com.dlvn.mcustomerportal.base.BaseActivity;

public class NotificationDetailActivity extends BaseActivity {

    private static final String TAG = "NotificationDetailActivity";

    public static final String INT_DETAILS_INFO = "NOTIFICATION_DETAILS_INFO";

    LinearLayout lloBack;
    TextView tvTitle, tvContent;
    NotificationModel modelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);

        lloBack = findViewById(R.id.lloBack);
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvContent = findViewById(R.id.tvContent);
    }

    private void initData() {
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(INT_DETAILS_INFO))
                modelData = getIntent().getParcelableExtra(INT_DETAILS_INFO);

        if (modelData != null) {
            tvTitle.setText(modelData.getTitle());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvContent.setText(Html.fromHtml(modelData.getContent(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                tvContent.setText(Html.fromHtml(modelData.getContent()));
            }
        }
    }

    private void setListener() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
