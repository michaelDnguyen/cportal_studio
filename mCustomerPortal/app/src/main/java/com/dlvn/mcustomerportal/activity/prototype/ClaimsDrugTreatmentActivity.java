package com.dlvn.mcustomerportal.activity.prototype;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ClaimDrugTreatmentAdapter;
import com.dlvn.mcustomerportal.adapter.ClaimTreatmentListAdapter;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.claims.TreatmentHistory;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Thông tin toa thuốc điều trị/ phương pháp điều trị của bác sĩ - địa chỉ công ty
 */
public class ClaimsDrugTreatmentActivity extends BaseActivity {

    private static final String TAG = "ClaimsDrugTreatmentActivity";

    LinearLayout lloBack, lloMedicine, lloAddress;
    Button btnThem;
    ListView lvMedicine;
    ClearableEditText cedtMedicine, cedtMethodTreatment, cedtCompany, cedtAddress, cedtPhone;

    ClaimDrugTreatmentAdapter adapter;
    List<TreatmentHistory> lsMedicine;

    AppCompatSeekBar sbStep;
    Button btnCapNhat;
    TextView tvStep;

    ServicesRequest svRequester;
    ClaimsFromRequest claimsFromRequest;
    boolean isAddress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_doc_presc);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);

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
        tvStep = findViewById(R.id.tvStep);
        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);
        sbStep.setMax(4);
        sbStep.setProgress(1);

        btnCapNhat = findViewById(R.id.btnCapNhat);

        cedtMedicine = findViewById(R.id.cedtMedicine);
        cedtMethodTreatment = findViewById(R.id.cedtMethodTreatment);

        cedtCompany = findViewById(R.id.cedtCompany);
        cedtAddress = findViewById(R.id.cedtAddress);
        cedtPhone = findViewById(R.id.cedtPhone);

        lloMedicine = findViewById(R.id.lloMedicine);
        lloAddress = findViewById(R.id.lloAddress);
        lloAddress.setVisibility(View.GONE);

        btnThem = findViewById(R.id.btnAdd);
        lvMedicine = findViewById(R.id.lvMedicine);
    }

    private void initData() {

        claimsFromRequest = CustomPref.getClaimsTemp(this);

        if (claimsFromRequest != null) {
            if (claimsFromRequest.getClaimType().equalsIgnoreCase("Death") || claimsFromRequest.getClaimType().equalsIgnoreCase("Accident")
                    || claimsFromRequest.getClaimType().equalsIgnoreCase("TPD")) {
                lloAddress.setVisibility(View.VISIBLE);
                lloMedicine.setVisibility(View.GONE);
                isAddress = true;
            }

            lsMedicine = new ArrayList<>();
            adapter = new ClaimDrugTreatmentAdapter(this, lsMedicine);
            lvMedicine.setAdapter(adapter);

        } else {
            MyCustomDialog dialog = new MyCustomDialog.Builder(this)
                    .setMessage(getString(R.string.alert_claims_no_requester_info))
                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btnCapNhat.setActivated(false);
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }

    private void setListener() {

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreatmentHistory item = new TreatmentHistory();

                String tenthuoc = cedtMedicine.getText().toString();
                String cachdung = cedtMethodTreatment.getText().toString();
                if (TextUtils.isEmpty(tenthuoc)) {
                    cedtMedicine.setError(getString(R.string.error_field_required));
                    cedtMedicine.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(cachdung)) {
                    cedtMethodTreatment.setError(getString(R.string.error_field_required));
                    cedtMethodTreatment.requestFocus();
                    return;
                }

                item.setPatientType(Constant.CLAIMS_TREATMENT_DRUG);
                item.setTreatmentType(Constant.TreatmentType.IllnessTreatment.getStringValue());
                item.setDrugName(tenthuoc);
                item.setDiagnostic(cachdung);

                if (lsMedicine.add(item)) {
                    adapter.notifyDataSetChanged();

                    cedtMedicine.setText("");
                    cedtMethodTreatment.setText("");
                }
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lsMedicine != null) {
                    List<TreatmentHistory> ls = claimsFromRequest.getTreatmentHistorys();
                    if (ls != null)
                        ls.addAll(lsMedicine);
                    else
                        ls = lsMedicine;
                    claimsFromRequest.setTreatmentHistorys(ls);
                }

                claimsFromRequest.setPermanentAddress(cedtAddress.getText().toString());
                claimsFromRequest.setWorkPlace(cedtCompany.getText().toString());
                claimsFromRequest.setHomePhone(cedtPhone.getText().toString());

                if (!isAddress) {
                    lloAddress.setVisibility(View.VISIBLE);
                    lloMedicine.setVisibility(View.GONE);
                    isAddress = true;

                } else {
                    CustomPref.saveClaimsTemp(ClaimsDrugTreatmentActivity.this, claimsFromRequest);

                    Intent intent = new Intent(ClaimsDrugTreatmentActivity.this, ClaimsPaymentMethodActivity.class);
//                    intent.putExtra(Constant.CLAIMS_INTKEY_MAIN_REQUEST, claimsFromRequest);
                    startActivity(intent);
                }

            }
        });
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
