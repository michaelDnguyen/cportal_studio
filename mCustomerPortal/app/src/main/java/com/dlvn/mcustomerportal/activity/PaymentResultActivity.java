package com.dlvn.mcustomerportal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;

public class PaymentResultActivity extends BaseActivity {

    LinearLayout lloBack;

    TextView tvMaGiaoDich, tvTenKhachHang, tvSoHopDong, tvTongSoTien, tvStatus;
    ImageView imvIcon, imvImage;

    PaymentDetailModel moPayment;
    String urlNapas;

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        lloBack = findViewById(R.id.lloBack);

        tvStatus = findViewById(R.id.tvStatus);
        tvMaGiaoDich = findViewById(R.id.tvMaGiaoDich);
        tvTenKhachHang = findViewById(R.id.tvTenKhachHang);
        tvSoHopDong = findViewById(R.id.tvMaHopDong);
        tvTongSoTien = findViewById(R.id.tvTongTien);

        imvIcon = findViewById(R.id.imvIcon);
        imvImage = findViewById(R.id.imvImage);
        Glide.with(this).load(R.drawable.ngan_hang_frame).thumbnail(0.7f).into(imvImage);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    private void initData() {
        // TODO Auto-generated method stub
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Constant.KEY_NAPAS_PAYMENT_TRANSID)) {
                String tranID = getIntent().getExtras().getString(Constant.KEY_NAPAS_PAYMENT_TRANSID);
                tvMaGiaoDich.setText(tranID);
            }

            if (getIntent().getExtras().containsKey(Constant.KEY_NAPAS_PAYMENT_MODEL)) {
                moPayment = getIntent().getExtras().getParcelable(Constant.KEY_NAPAS_PAYMENT_MODEL);
            }

            if (getIntent().getExtras().containsKey(Constant.KEY_NAPAS_PAYMENT_URL)) {
                urlNapas = getIntent().getExtras().getString(Constant.KEY_NAPAS_PAYMENT_URL);
            }
        }

        if (moPayment != null) {
            tvSoHopDong.setText(moPayment.getSoHopDong());
            tvTongSoTien.setText(moPayment.getTongSoTien());
            tvTenKhachHang.setText(moPayment.getTenKhachHang());
            if (moPayment.getStatus() == 0) {
                tvStatus.setText("Giao dịch hoàn tất");
                imvIcon.setImageResource(R.drawable.icon_success);
            } else {
                tvStatus.setText("Giao dịch không thành công");
                imvIcon.setImageResource(R.drawable.icon_failed);
            }

//            doPaymentResponse(PaymentResultActivity.this);
        }

    }

    private void setListener() {
        // TODO Auto-generated method stub
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

//    private void doPaymentResponse(Context context) {
//        GetPaymentResponseRequest request = new GetPaymentResponseRequest();
//        request.setClientId(PayPref.getUserID(context));
//        request.setResponseCode(moPayment.getStatus() + "");
//        request.setRequest(urlNapas.replace(Constant.URL_PAYMENT + "?", ""));
//
//        Call<GetPaymentResponseResponse> call = svRequester.getPaymentResponse(request);
//        call.enqueue(new Callback<GetPaymentResponseResponse>() {
//
//            @Override
//            public void onResponse(Call<GetPaymentResponseResponse> call, Response<GetPaymentResponseResponse> res) {
//                // TODO Auto-generated method stub
//                if (res.isSuccessful())
//                    if (res.body() != null) {
//                        GetPaymentResponseResult result = ((GetPaymentResponseResponse) res.body()).getResponse();
//                        if (!TextUtils.isEmpty(result.getResult())) {
//
//                        }
//                    }
//            }
//
//            @Override
//            public void onFailure(Call<GetPaymentResponseResponse> call, Throwable t) {
//                // TODO Auto-generated method stub
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PaymentResultActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
//                onBackPressed();
                Intent intent = new Intent(PaymentResultActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
