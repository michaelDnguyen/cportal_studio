package com.dlvn.mcustomerportal.activity.prototype;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.model.response.GetPointAccountModel;
import com.dlvn.mcustomerportal.utils.DateUtils;
import com.dlvn.mcustomerportal.utils.Utilities;

public class PointAccountDetailActivity extends BaseActivity {

    private static final String TAG = "PointAccountDetailActivity";

    LinearLayout lloBack;

    TextView tvDateTrans, tvTransID, tvTransStatus, tvTotalPoint, tvTransPolicy, tvTransDesc;
    TextView tvTransName, tvTransValue;
    LinearLayout lloIN, lloDEN,lloDEN2;

    GetPointAccountModel pointModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_point_account_detail);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        lloBack = findViewById(R.id.lloBack);

        tvDateTrans = findViewById(R.id.tvDateTrans);
        tvTransID = findViewById(R.id.tvTransID);
        tvTransStatus = findViewById(R.id.tvTransStatus);
        tvTotalPoint = findViewById(R.id.tvTotalPoint);

        tvTransName = findViewById(R.id.tvTransName);
        tvTransValue = findViewById(R.id.tvTransValue);

        tvTransPolicy = findViewById(R.id.tvTransPolicy);
        tvTransDesc = findViewById(R.id.tvTransDesc);

        lloDEN = findViewById(R.id.lloDEN);
        lloDEN2 = findViewById(R.id.lloDEN2);
        lloIN = findViewById(R.id.lloIN);
    }

    private void initData() {
        if(getIntent().getExtras() != null)
            if(getIntent().getExtras().containsKey(Constant.INT_KEY_POINT_ACCOUNT))
                pointModel = getIntent().getParcelableExtra(Constant.INT_KEY_POINT_ACCOUNT);

        if(pointModel != null){

            if(pointModel.getTransTypeCD().equalsIgnoreCase(Constant.POINTACCOUNT_DEGEN)) {
                tvTransID.setText(pointModel.getPointID() + "");
                tvDateTrans.setText(DateUtils.parseDateForMCP(pointModel.getCreateDate()));
                tvTotalPoint.setText("Tổng điểm sử dụng: " + Utilities.formatMoneyToVND("" + pointModel.getPoint() / 1000));

                tvTransName.setText(pointModel.getDescription());
                tvTransValue.setText(Utilities.formatMoneyToVND("" + pointModel.getPoint() / 1000));

                lloIN.setVisibility(View.GONE);
                lloDEN.setVisibility(View.VISIBLE);
                lloDEN2.setVisibility(View.VISIBLE);
            } else {

                tvTransID.setText(pointModel.getPointID() + "");
                tvDateTrans.setText(DateUtils.parseDateForMCP(pointModel.getCreateDate()));
                tvTransPolicy.setText(pointModel.getPolicyNo());
                tvTransDesc.setText(pointModel.getDescription());
                tvTotalPoint.setText("Tổng điểm thưởng: " + Utilities.formatMoneyToVND("" + pointModel.getPoint() / 1000));

                lloIN.setVisibility(View.VISIBLE);
                lloDEN.setVisibility(View.GONE);
                lloDEN2.setVisibility(View.GONE);
            }
        }
    }

    private void setListener(){
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
