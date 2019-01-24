package com.dlvn.mcustomerportal.activity.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.ScrollingTabContainerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.SingleSpinnerAdapter;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.request.CPGetPolicyListByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.request.CPSubmitFormRequest;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyListByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyListByCLIIDResult;
import com.dlvn.mcustomerportal.services.model.response.CPPolicy;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ClaimsPaymentMethodActivity extends BaseActivity {

    private static final String TAG = "ClaimsPaymentMethodActivity";

    LinearLayout lloBack;
    TextView tvPolicyPayment;
    RadioButton rdbNhanQuaDongPhi, rdbNhanTienMat, rdbNhanTienMatPacific, rdbNhanQuaCMND, rdbNhanQuaNH;
    LinearLayout lloNhanQuaDongPhi, lloNhanTienMat, lloNhanTienMatPacific, lloNhanQuaCMND, lloNhanQuaNH;

    ClearableEditText cedtBankName, cedtBankBranch, cedtAccName, cedtAccNumber;
    ClearableEditText cedtCMNDBankName, cedtCMNDBankBranch, cedtCMNDNumber, cedtCMNDDate, cedtCMNDPlace;
    Spinner spnPolicy;

    AppCompatSeekBar sbStep;
    Button btnCapNhat;
    TextView tvStep;

    ServicesRequest svRequester;
    ClaimsFromRequest claimsFromRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_payment_method);
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        lloBack = findViewById(R.id.lloBack);

        rdbNhanQuaCMND = findViewById(R.id.rdbNhanQuaCMND);
        rdbNhanQuaDongPhi = findViewById(R.id.rdbNhanQuaDongPhi);
        rdbNhanTienMat = findViewById(R.id.rdbNhanTienMat);
        rdbNhanTienMatPacific = findViewById(R.id.rdbNhanTienMatPacific);
        rdbNhanQuaNH = findViewById(R.id.rdbNhanQuaNH);

        lloNhanQuaCMND = findViewById(R.id.lloNhanQuaCMND);
        lloNhanQuaCMND.setVisibility(View.GONE);
        lloNhanQuaDongPhi = findViewById(R.id.lloNhanQuaDongPhi);
        lloNhanQuaDongPhi.setVisibility(View.GONE);
        lloNhanTienMat = findViewById(R.id.lloNhanTienMat);
        lloNhanTienMat.setVisibility(View.GONE);
        lloNhanTienMatPacific = findViewById(R.id.lloNhanTienMatPacific);
        lloNhanTienMatPacific.setVisibility(View.GONE);
        lloNhanQuaNH = findViewById(R.id.lloNhanQuaNH);
        lloNhanQuaNH.setVisibility(View.GONE);

        spnPolicy = findViewById(R.id.spnPolicy);

        tvStep = findViewById(R.id.tvStep);
        sbStep = findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);
        sbStep.setMax(4);
        sbStep.setProgress(4);

        btnCapNhat = findViewById(R.id.btnCapNhat);

        tvPolicyPayment = findViewById(R.id.tvPolicyPayment);
        tvPolicyPayment.setText(Html.fromHtml(getString(R.string.txt_payment_term)));
        tvPolicyPayment.setMovementMethod(LinkMovementMethod.getInstance());

        cedtBankName = findViewById(R.id.cedtBankName);
        cedtBankBranch = findViewById(R.id.cedtBankBranch);
        cedtAccName = findViewById(R.id.cedtAccountName);
        cedtAccNumber = findViewById(R.id.cedtAccountNumber);

        cedtCMNDBankName = findViewById(R.id.cedtCMNDBankName);
        cedtCMNDBankBranch = findViewById(R.id.cedtCMNDBankBranch);
        cedtCMNDNumber = findViewById(R.id.cedtCMNDNumber);
        cedtCMNDDate = findViewById(R.id.cedtCMNDDate);
        cedtCMNDPlace = findViewById(R.id.cedtCMNDPlace);
    }

    private void initData() {

        claimsFromRequest = CustomPref.getClaimsTemp(this);

        if (claimsFromRequest == null) {
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
        } else {
            new getPolicyListTask(this).execute();
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

                if (rdbNhanQuaNH.isChecked()) {
                    claimsFromRequest.setBankName(cedtBankName.getText().toString());
                    claimsFromRequest.setBankBranch(cedtBankBranch.getText().toString());
                    claimsFromRequest.setAccountName(cedtAccName.getText().toString());
                    claimsFromRequest.setAccountNumber(cedtAccNumber.getText().toString());
                    claimsFromRequest.setPaymentMethod("Nhận qua chuyển khoản ngân hàng");
                } else if (rdbNhanQuaCMND.isChecked()) {
                    claimsFromRequest.setBankName(cedtCMNDBankName.getText().toString());
                    claimsFromRequest.setBankBranch(cedtCMNDBankName.getText().toString());
                    claimsFromRequest.setPayIssueID(cedtCMNDNumber.getText().toString());
                    claimsFromRequest.setPayDateIssue(cedtCMNDDate.getText().toString());
                    claimsFromRequest.setPayPlaceIsssue(cedtCMNDPlace.getText().toString());
                    claimsFromRequest.setPaymentMethod("Nhận bằng CMND tại ngân hàng");
                } else if (rdbNhanQuaDongPhi.isChecked()) {

                    SingleChoiceModel item = (SingleChoiceModel) spnPolicy.getSelectedItem();
                    if (item != null) {
                        claimsFromRequest.setPaymentMethod("Chuyển đóng phí cho hợp đồng");
                        claimsFromRequest.setPaymentPolicy(item.getCode());
                    }
                } else if (rdbNhanTienMat.isChecked()) {
                    claimsFromRequest.setPaymentMethod("Nhận tiền mặt tại văn phòng chi nhánh của Dai-Ichi Life");
                } else if (rdbNhanTienMatPacific.isChecked()) {
                    claimsFromRequest.setPaymentMethod("Nhận tiền mặt tại văn phòng Pacific Cross VN");
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(ClaimsPaymentMethodActivity.this)
                            .setMessage("Anh/chị cần chọn một phương thức thanh toán Quyền lợi Bảo hiểm.")
                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }

                CustomPref.saveClaimsTemp(ClaimsPaymentMethodActivity.this, claimsFromRequest);

                Intent intent = new Intent(ClaimsPaymentMethodActivity.this, ClaimsAddPhotoActivity.class);
                startActivity(intent);
            }
        });

        rdbNhanQuaDongPhi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    lloNhanQuaDongPhi.setVisibility(View.VISIBLE);
                else
                    lloNhanQuaDongPhi.setVisibility(View.GONE);
            }
        });

        rdbNhanTienMat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    lloNhanTienMat.setVisibility(View.VISIBLE);
                else
                    lloNhanTienMat.setVisibility(View.GONE);
            }
        });

        rdbNhanTienMatPacific.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    lloNhanTienMatPacific.setVisibility(View.VISIBLE);
                else
                    lloNhanTienMatPacific.setVisibility(View.GONE);
            }
        });

        rdbNhanQuaCMND.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    lloNhanQuaCMND.setVisibility(View.VISIBLE);
                else
                    lloNhanQuaCMND.setVisibility(View.GONE);
            }
        });

        rdbNhanQuaNH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    lloNhanQuaNH.setVisibility(View.VISIBLE);
                else
                    lloNhanQuaNH.setVisibility(View.GONE);
            }
        });

        spnPolicy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * get Policy List of client
     */
    public class getPolicyListTask extends AsyncTask<Void, Void, Response<CPGetPolicyListByCLIIDResponse>> {

        Context context;

        public getPolicyListTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Response<CPGetPolicyListByCLIIDResponse> doInBackground(Void... params) {

            Response<CPGetPolicyListByCLIIDResponse> result = null;
            try {

                CPGetPolicyListByCLIIDRequest data = new CPGetPolicyListByCLIIDRequest();
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setPassword(CustomPref.getPassword(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setClientID(CustomPref.getUserID(context));
                data.setLinkGmail(CustomPref.getGoogleID(context));
                data.setLinkFacebook(CustomPref.getFacebookID(context));

                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<CPGetPolicyListByCLIIDResponse> call = svRequester.CPGetPolicyListByCLIID(request);
                result = call.execute();

            } catch (Exception e) {
                return null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(final Response<CPGetPolicyListByCLIIDResponse> success) {

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        CPGetPolicyListByCLIIDResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                CPGetPolicyListByCLIIDResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            //Save Token
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

                                        if (result.getPolicyList() != null) {

                                            List<CPPolicy> lstPolicy = result.getPolicyList();
                                            if (lstPolicy.size() > 0) {
                                                List<SingleChoiceModel> categories = new ArrayList<>();
                                                for (CPPolicy mo : lstPolicy)
                                                    categories.add(new SingleChoiceModel(0, mo.getPolicyID(), mo.getPolicyID()));

                                                SingleSpinnerAdapter dataAdapter = new SingleSpinnerAdapter(context, categories);
                                                spnPolicy.setAdapter(dataAdapter);
                                            }
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
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
