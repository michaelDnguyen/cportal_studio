package com.dlvn.mcustomerportal.activity.prototype;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ClaimsRequesterInfoActivity extends BaseActivity {

    private static final String TAG = "ClaimsRequesterInfoActivity";

    LinearLayout lloBack;

    TextView tvFullName, tvClientID, tvCMND, tvPhoneNumber, tvEmail, tvNgayCapCMND;
    ClearableEditText cedtNoiCapCMND, cedtAddress;

    AppCompatSeekBar sbStep;
    Button btnCapNhat;
    TextView tvStep;

    ServicesRequest svRequester;
    ClaimsFromRequest claimsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_requester_info);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        getViews();
        initData();
        setListener();

    }

    private void getViews() {
        lloBack = findViewById(R.id.lloBack);
        tvStep = findViewById(R.id.tvStep);
        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);
        sbStep.setMax(4);
        sbStep.setProgress(1);

        btnCapNhat = findViewById(R.id.btnCapNhat);

        tvFullName = findViewById(R.id.tvFullName);
        tvClientID = findViewById(R.id.tvClientID);
        tvCMND = findViewById(R.id.tvCMND);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        tvEmail = findViewById(R.id.tvEmail);
        tvNgayCapCMND = findViewById(R.id.tvNgayCapCMND);

        cedtNoiCapCMND = findViewById(R.id.cedtNoiCapCMND);
        cedtAddress = findViewById(R.id.cedtAddress);
    }

    private void initData() {

        //get claims form request if exist
        claimsRequest = CustomPref.getClaimsTemp(this);

        try {
            if (claimsRequest != null) {
                tvFullName.setText(claimsRequest.getFullName());
                tvClientID.setText(claimsRequest.getClientID());
                tvPhoneNumber.setText(claimsRequest.getCellPhone());
                tvEmail.setText(claimsRequest.getEmail());
                tvCMND.setText(claimsRequest.getIDNum());
                cedtAddress.setText(claimsRequest.getAddress());
                cedtNoiCapCMND.setText(claimsRequest.getPlaceIssueOfIDNum());
                tvNgayCapCMND.setText(claimsRequest.getDateIssueOfIDNum());
            } else {
                claimsRequest = new ClaimsFromRequest();

                if (!TextUtils.isEmpty(CustomPref.getUserID(this))) {
                    ClientProfile user = CustomPref.getUserLogin(this);

                    tvFullName.setText(user.getFullName());
                    tvClientID.setText(user.getClientID());
                    tvPhoneNumber.setText(user.getCellPhone());
                    tvEmail.setText(user.getEmail());
                    tvCMND.setText(user.getPOID());
                    cedtAddress.setText(user.getAddress());

                    claimsRequest.setFullName(user.getFullName());
                    claimsRequest.setClientID(user.getClientID());
                    claimsRequest.setCellPhone(user.getCellPhone());
                    claimsRequest.setEmail(user.getEmail());
                    claimsRequest.setIDNum(user.getPOID());
                    claimsRequest.setAddress(user.getAddress());
                }
            }
        } catch (Exception e) {
            myLog.printTrace(e);
        }
    }

    private void setListener() {
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillDataToRequest();
            }
        });

        tvNgayCapCMND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR) - 1;
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog dialog = new DatePickerDialog(ClaimsRequesterInfoActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        tvNgayCapCMND.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });
    }

    private void fillDataToRequest() {
        claimsRequest.setDateIssueOfIDNum(tvNgayCapCMND.getText().toString());
        claimsRequest.setPlaceIssueOfIDNum(cedtNoiCapCMND.getText().toString());
        claimsRequest.setAddress(cedtAddress.getText().toString());

        //save temp claims
        CustomPref.saveClaimsTemp(ClaimsRequesterInfoActivity.this, claimsRequest);

        Intent intent = new Intent(ClaimsRequesterInfoActivity.this, ClaimsLifeInsureActivity.class);
//        intent.putExtra(Constant.CLAIMS_INTKEY_MAIN_REQUEST, claimsRequest);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CPSaveLogTask(this, Constant.CPLOG_CLAIMREQUEST_OPEN).execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        new CPSaveLogTask(this, Constant.CPLOG_CLAIMREQUEST_CLOSE).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Utilities.hideSoftInputKeyboard(this, v);
        }
        return super.dispatchTouchEvent(ev);
    }
}
