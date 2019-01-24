package com.dlvn.mcustomerportal.activity.prototype;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Thông tin điều trị/ tổng số tiền bồi thường (với Health Care)
 */
public class ClaimsTreatmentActivity extends BaseActivity {

    private static final String TAG = "ClaimsTreatmentActivity";

    LinearLayout lloBack, lloClaimTreatment, lloClaimAmount;
    TextView tvFromDate, tvToDate;
    ClearableEditText cedtHospital, cedtDiagnostic, cedtClaimAmount, cedtClaimAmountChar;
    RadioButton rdbNoitru, rdbNgoaitru;

    ListView lvTreatment;
    ClaimTreatmentListAdapter adapter;
    List<TreatmentHistory> lsTreatment;

    AppCompatSeekBar sbStep;
    Button btnCapNhat, btnThem;
    TextView tvStep;

    ServicesRequest svRequester;
    ClaimsFromRequest claimsFromRequest;
    boolean isHealthCare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_treatment);

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

        lloClaimTreatment = findViewById(R.id.lloClaimTreatment);
        lloClaimAmount = findViewById(R.id.lloClaimAmount);

        cedtHospital = findViewById(R.id.cedtHospital);
        cedtDiagnostic = findViewById(R.id.cedtDiagnostic);
        cedtClaimAmount = findViewById(R.id.cedtClaimAmount);
        cedtClaimAmountChar = findViewById(R.id.cedtClaimAmountChar);

        rdbNoitru = findViewById(R.id.rdbNoitru);
        rdbNgoaitru = findViewById(R.id.rdbNgoaitru);

        tvToDate = findViewById(R.id.tvToDate);
        tvFromDate = findViewById(R.id.tvFromDate);

        lvTreatment = findViewById(R.id.lvTreatment);

        tvStep = findViewById(R.id.tvStep);
        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);
        sbStep.setMax(4);
        sbStep.setProgress(1);

        btnCapNhat = findViewById(R.id.btnCapNhat);
        btnThem = findViewById(R.id.btnAdd);
    }

    private void initData() {

        //get temp claims
        claimsFromRequest = CustomPref.getClaimsTemp(this);

        if (claimsFromRequest != null) {

            lsTreatment = new ArrayList<>();
            adapter = new ClaimTreatmentListAdapter(this, lsTreatment);
            lvTreatment.setAdapter(adapter);

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

        cedtClaimAmount.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current) && !TextUtils.isEmpty(s.toString())) {
                    cedtClaimAmount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");
                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
                    current = formatted;
                    cedtClaimAmount.setText(formatted);
                    cedtClaimAmount.setSelection(formatted.length());

                    cedtClaimAmount.addTextChangedListener(this);
                }
            }
        });

        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog dialog = new DatePickerDialog(ClaimsTreatmentActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        tvFromDate.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        tvToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog dialog = new DatePickerDialog(ClaimsTreatmentActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        tvToDate.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TreatmentHistory item = new TreatmentHistory();
                String hospital = cedtHospital.getText().toString();
                String chuandoan = cedtDiagnostic.getText().toString();

                if (TextUtils.isEmpty(hospital)) {
                    cedtHospital.setError(getString(R.string.error_field_required));
                    cedtHospital.requestFocus();
                    return;
                }

                if (!rdbNgoaitru.isChecked() && !rdbNoitru.isChecked()) {
                    rdbNoitru.setError("Cần chọn loại nội - ngoại trú");
                    rdbNoitru.requestFocus();
                    return;
                }

                item.setTreatmentHospital(hospital);
                item.setDiagnostic(chuandoan);
                if (rdbNoitru.isChecked())
                    item.setPatientType(Constant.CLAIMS_TREATMENT_INPATIENT);
                else if (rdbNgoaitru.isChecked())
                    item.setPatientType(Constant.CLAIMS_TREATMENT_OUTPATIENT);
                item.setTreatmentDateFrom(tvFromDate.getText().toString());
                item.setTreatmentDateTo(tvToDate.getText().toString());

                if (claimsFromRequest != null) {
                    if (claimsFromRequest.getClaimDeath().size() > 0)
                        if (claimsFromRequest.getClaimDeath().get(0).getClaimReason().equalsIgnoreCase(Constant.CLAIMS_REASON_ACCIDENT))
                            item.setTreatmentType(Constant.TreatmentType.Accident.getStringValue());
                        else
                            item.setTreatmentType(Constant.TreatmentType.IllnessProgression.getStringValue());
                }

                if (lsTreatment.add(item)) {
                    adapter.notifyDataSetChanged();

                    //reset form
                    cedtHospital.setText("");
                    cedtDiagnostic.setText("");
                    tvFromDate.setText("");
                    tvToDate.setText("");
                    rdbNgoaitru.setChecked(false);
                    rdbNoitru.setChecked(false);
                }
//                adapter.setData(lsTreatment);
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (claimsFromRequest != null) {
                    if (!isHealthCare)//new false thì add list treatment to form request,
                        if (lsTreatment != null) {
                            List<TreatmentHistory> ls = claimsFromRequest.getTreatmentHistorys();
                            if (ls != null)
                                ls.addAll(lsTreatment);
                            else
                                ls = lsTreatment;
                            claimsFromRequest.setTreatmentHistorys(ls);
                        }

                    CustomPref.saveClaimsTemp(ClaimsTreatmentActivity.this, claimsFromRequest);

                    if (claimsFromRequest.getClaimType().contains("HealthCare")) {
                        lloClaimTreatment.setVisibility(View.GONE);
                        lloClaimAmount.setVisibility(View.VISIBLE);
                        isHealthCare = true;
                    } else {
                        // nếu ko healthcare thì đi đến mục điều trị
                        Intent intent = new Intent(ClaimsTreatmentActivity.this, ClaimsDrugTreatmentActivity.class);
                        startActivity(intent);
                    }

                    //if healthcare đi thẳng đến mục thông tin thanh toán
                    if (isHealthCare) {
                        claimsFromRequest.setHCClaimAmt(cedtClaimAmount.getText().toString().replace(",", ""));
                        claimsFromRequest.setHCClaimAmtChar(cedtClaimAmountChar.getText().toString());

                        Intent intent = new Intent(ClaimsTreatmentActivity.this, ClaimsPaymentMethodActivity.class);
                        startActivity(intent);
                    }
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
