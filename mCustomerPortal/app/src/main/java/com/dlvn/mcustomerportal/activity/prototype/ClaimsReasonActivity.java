package com.dlvn.mcustomerportal.activity.prototype;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsItem;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Lý do Claim/ Mô tả chi tiết/ tiến triển
 */
public class ClaimsReasonActivity extends BaseActivity {

    private static final String TAG = "ClaimsReasonActivity";

    LinearLayout lloBack, lloAccident, lloIllness;
    RadioButton rdbAccident, rdbIllness;
    ClearableEditText cedtAccidentHoanCanh, cedtAccidentThuongTat, cedtAccidentCauser, cedtAccidentPhoneCauser, cedtAccidentStatusCauser, cedtIllnessMoTa, cedtIllnessDienTien;
    TextView tvAccidentThoiGian, tvIllnessThoiGian;

    AppCompatSeekBar sbStep;
    Button btnCapNhat;
    TextView tvStep;

    ServicesRequest svRequester;
    ClaimsFromRequest claimsFromRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_reason);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        lloBack = findViewById(R.id.lloBack);
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initViews();
        initData();
        setListener();

    }

    private void initViews() {
        tvStep = findViewById(R.id.tvStep);
        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);
        sbStep.setMax(4);
        sbStep.setProgress(1);

        btnCapNhat = findViewById(R.id.btnCapNhat);

        lloAccident = findViewById(R.id.lloAccident);
        lloAccident.setVisibility(View.GONE);
        lloIllness = findViewById(R.id.lloIllness);
        lloIllness.setVisibility(View.GONE);

        rdbAccident = findViewById(R.id.rdbAccident);
        rdbIllness = findViewById(R.id.rdbIllness);

        tvAccidentThoiGian = findViewById(R.id.tvAccidentThoiGian);
        tvIllnessThoiGian = findViewById(R.id.tvIllnessThoiGian);

        cedtAccidentHoanCanh = findViewById(R.id.cedtAccidentHoanCanh);
        cedtAccidentThuongTat = findViewById(R.id.cedtAccidentThuongTat);
        cedtAccidentCauser = findViewById(R.id.cedtAccidentCauser);
        cedtAccidentPhoneCauser = findViewById(R.id.cedtAccidentPhoneCauser);
        cedtAccidentStatusCauser = findViewById(R.id.cedtAccidentStatusCauser);

        cedtIllnessMoTa = findViewById(R.id.cedtIllnessMoTa);
        cedtIllnessDienTien = findViewById(R.id.cedtIllnessDienTien);
    }

    private void initData() {

        claimsFromRequest = CustomPref.getClaimsTemp(this);
        if (claimsFromRequest != null) {

            //ClaimsType la Accident OR Sickness
            if (claimsFromRequest.getClaimType().equalsIgnoreCase("Accident")) {
                rdbIllness.setVisibility(View.GONE);
            } else if (claimsFromRequest.getClaimType().equalsIgnoreCase("Sickness")) {
                rdbAccident.setVisibility(View.GONE);
            }

            //if Reason Claims exist
            if (claimsFromRequest.getClaimDeath().size() > 0) {
                ClaimsItem item = claimsFromRequest.getClaimDeath().get(0);
                if (item != null) {
                    //if reason is Accident
                    if (item.getClaimReason().equalsIgnoreCase(Constant.CLAIMS_REASON_ACCIDENT)) {
                        rdbAccident.setChecked(true);
                        lloAccident.setVisibility(View.VISIBLE);

                        if (claimsFromRequest.getClaimType().contains("Healthcare")) {
                            ((View) cedtAccidentCauser.getParent()).setVisibility(View.GONE);
                            ((View) cedtAccidentPhoneCauser.getParent()).setVisibility(View.GONE);
                            ((View) cedtAccidentStatusCauser.getParent()).setVisibility(View.GONE);
                        }

                        tvAccidentThoiGian.setText(item.getAccidentDate());
                        cedtAccidentCauser.setText(item.getAccidentCausers());
                        cedtAccidentThuongTat.setText(item.getAccidentIllnessDesc());
                        cedtAccidentHoanCanh.setText(item.getAccidentPlace());
                        cedtAccidentPhoneCauser.setText(item.getCausersPhone());
                        cedtAccidentStatusCauser.setText(item.getStimulantStatus());
                    } else if (item.getClaimReason().equalsIgnoreCase(Constant.CLAIMS_REASON_ILLNESS)) {
                        //if reason is Illness
                        rdbIllness.setChecked(true);
                        lloIllness.setVisibility(View.VISIBLE);

                        if (claimsFromRequest.getClaimType().contains("Healthcare")) {
                            ((View) cedtIllnessDienTien.getParent()).setVisibility(View.GONE);
                        }

                        cedtIllnessMoTa.setText(item.getAccidentIllnessDesc());
                        cedtIllnessDienTien.setText(item.getIllnessProgression());
                        tvIllnessThoiGian.setText(item.getLossDate());
                        cedtIllnessMoTa.setText(item.getStimulantStatus());
                    }
                }
            }

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

        tvAccidentThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog dialog = new DatePickerDialog(ClaimsReasonActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        tvAccidentThoiGian.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        tvIllnessThoiGian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog dialog = new DatePickerDialog(ClaimsReasonActivity.this,
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(year, month, dayOfMonth);
                        tvIllnessThoiGian.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClaimsItem claimsItem = new ClaimsItem();
                if (rdbAccident.isChecked()) {
                    claimsItem.setAccidentDate(tvAccidentThoiGian.getText().toString());
                    claimsItem.setAccidentCausers(cedtAccidentCauser.getText().toString());
                    claimsItem.setAccidentIllnessDesc(cedtAccidentThuongTat.getText().toString());
                    claimsItem.setAccidentPlace(cedtAccidentHoanCanh.getText().toString());
                    claimsItem.setCausersPhone(cedtAccidentPhoneCauser.getText().toString());
                    claimsItem.setAccidentReason(claimsFromRequest.getClaimType());
                    claimsItem.setLossDate(tvAccidentThoiGian.getText().toString());
                    claimsItem.setLossPlace("");
                    claimsItem.setStimulantStatus(cedtAccidentStatusCauser.getText().toString());

                    claimsItem.setClaimReason(Constant.CLAIMS_REASON_ACCIDENT);

                } else if (rdbIllness.isChecked()) {
                    claimsItem.setAccidentIllnessDesc(cedtIllnessMoTa.getText().toString());
                    claimsItem.setIllnessProgression(cedtIllnessDienTien.getText().toString());
                    claimsItem.setAccidentReason(claimsFromRequest.getClaimType());
                    claimsItem.setLossDate(tvIllnessThoiGian.getText().toString());
                    claimsItem.setLossPlace("");
                    claimsItem.setStimulantStatus(cedtIllnessMoTa.getText().toString());

                    claimsItem.setClaimReason(Constant.CLAIMS_REASON_ILLNESS);
                }

                if (claimsItem != null) {
                    List<ClaimsItem> lst = new ArrayList<ClaimsItem>();
                    lst.add(claimsItem);
                    claimsFromRequest.setClaimDeath(lst);
                }

                CustomPref.saveClaimsTemp(ClaimsReasonActivity.this, claimsFromRequest);

                new checkClaimDuplicateTask(ClaimsReasonActivity.this, claimsFromRequest.getClaimType(),
                        claimsFromRequest.getPolicyNo(), claimsItem.getLossDate()).execute();
//                Intent intent = new Intent(ClaimsReasonActivity.this, ClaimsTreatmentActivity.class);
//                startActivity(intent);
            }
        });

        rdbAccident.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lloAccident.setVisibility(View.VISIBLE);

                    if (claimsFromRequest.getClaimType().contains("Healthcare")) {
                        ((View) cedtAccidentCauser.getParent()).setVisibility(View.GONE);
                        ((View) cedtAccidentPhoneCauser.getParent()).setVisibility(View.GONE);
                        ((View) cedtAccidentStatusCauser.getParent()).setVisibility(View.GONE);
                    }
                } else
                    lloAccident.setVisibility(View.GONE);
            }
        });

        rdbIllness.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lloIllness.setVisibility(View.VISIBLE);

                    if (claimsFromRequest.getClaimType().contains("Healthcare")) {
                        ((View) cedtIllnessDienTien.getParent()).setVisibility(View.GONE);
                    }
                } else
                    lloIllness.setVisibility(View.GONE);
            }
        });
    }

    private class checkClaimDuplicateTask extends AsyncTask<Void, Void, Response<CPSubmitFormResponse>> {

        Context context;
        ProgressBar processBar;
        String claimsType, policyNo, dateLoss;

        public checkClaimDuplicateTask(Context c, String claimsType, String policyNo, String dateLoss) {
            this.context = c;
            this.claimsType = claimsType;
            this.policyNo = policyNo;
            this.dateLoss = dateLoss;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (processBar == null)
                processBar = new ProgressBar(context, null, android.R.attr.progressBarStyleSmall);
            processBar.setIndeterminate(true);
            processBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Response<CPSubmitFormResponse> doInBackground(Void... voids) {
            Response<CPSubmitFormResponse> response = null;

            ClaimsFromRequest data = new ClaimsFromRequest();
            try {
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOs(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setAction(Constant.CLAIMS_ACTION_CHECK_CLAIM_DUPLICATE);
                data.setPolicyNo(policyNo);
                data.setClaimSubType(claimsType);
                if (claimsType.contains("Healthcare"))
                    data.setClaimType("Healthcare");
                else
                    data.setClaimType(claimsType);
                data.setDateLoss(dateLoss);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<CPSubmitFormResponse> call = svRequester.SubmitFormContact(request);
                response = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<CPSubmitFormResponse> response) {
            super.onPostExecute(response);
            processBar.setVisibility(View.GONE);

            if (response != null)
                if (response.isSuccessful()) {
                    CPSubmitFormResponse rp = response.body();
                    if (rp != null) {
                        CPSubmitFormResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                if (!TextUtils.isEmpty(result.getErrLog())) {

                                    //check với server xem claims đã dc submiss lần nào chưa
                                    if (result.getErrLog().equalsIgnoreCase("EXIST")) {
                                        MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                .setTitle(result.getErrLog())
                                                .setMessage("Yêu cầu Quyền lợi bảo hiểm này đã được nộp. Anh/chị có thể yêu cầu Quyền lợi bảo hiểm khác.")
                                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).create();
                                        dialog.show();
                                    } else {
                                        Intent intent = new Intent(ClaimsReasonActivity.this, ClaimsTreatmentActivity.class);
                                        startActivity(intent);
                                    }
                                }

                            } else if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                            }
                        }
                    }
                }
        }
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
