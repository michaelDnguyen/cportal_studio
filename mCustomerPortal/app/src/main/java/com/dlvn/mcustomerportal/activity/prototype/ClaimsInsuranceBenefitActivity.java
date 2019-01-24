package com.dlvn.mcustomerportal.activity.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.SingleChoiceListAdapter;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.claims.ClaimsFromRequest;
import com.dlvn.mcustomerportal.services.model.request.GetClientProfileByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.GetClientProfileByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.GetClientProfileByCLIIDResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ClaimsInsuranceBenefitActivity extends BaseActivity {

    private static final String TAG = "ClaimsInsuranceBenefitActivity";

    LinearLayout lloBack;
    TextView tvLifeInsuredName;
    AppCompatSeekBar sbStep;
    Button btnCapNhat;
    TextView tvStep;

    ListView lvData, lvHealthCare;
    SingleChoiceListAdapter adapter, adapterHealthCare;
    List<SingleChoiceModel> lsData, lsHealthCare;
    boolean isHealthCare = false;

    ClaimsFromRequest claimsFromRequest;
    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims_insurance_benefit);

        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        lloBack = findViewById(R.id.lloBack);
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isHealthCare) {
//                    lvHealthCare.setVisibility(View.GONE);
//                    lvData.setVisibility(View.VISIBLE);
//                } else
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
        sbStep.setProgress(2);

        tvLifeInsuredName = findViewById(R.id.tvLifeInsuredName);

        btnCapNhat = findViewById(R.id.btnCapNhat);

        lvData = findViewById(R.id.lvData);
        lvHealthCare = findViewById(R.id.lvHealthCare);
    }

    private void initData() {
        claimsFromRequest = CustomPref.getClaimsTemp(this);
        if (claimsFromRequest != null) {
            tvLifeInsuredName.setText(claimsFromRequest.getLIFullName());
            new GetBenifitsListTask(this, claimsFromRequest.getLiClientID()).execute();
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
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (isHealthCare) {
//                    lvData.setVisibility(View.GONE);
//                    lvHealthCare.setVisibility(View.VISIBLE);
//
//                } else {
                SingleChoiceModel claimType = null, claimHealthCare = null;
                claimType = adapter.getSelectionItem();
//                if (adapterHealthCare != null)
//                    claimHealthCare = adapterHealthCare.getSelectionItem();

                //save temp claim
                CustomPref.saveClaimsTemp(ClaimsInsuranceBenefitActivity.this, claimsFromRequest);

                if (!TextUtils.isEmpty(claimType.getCode())) {
                    myLog.e(TAG, "claimType = " + claimType.getCode() + " -- policyNo = " + claimType.getSubname());

                    if (claimType.getCode().equalsIgnoreCase("Healthcare1")) {
//                        if (claimHealthCare.getCode().equalsIgnoreCase(Constant.CLAIMS_TREATMENT_DENTAL)) {
                        Intent intent = new Intent(ClaimsInsuranceBenefitActivity.this, ClaimsTreatmentActivity.class);
//                        intent.putExtra(Constant.CLAIMS_INTKEY_MAIN_REQUEST, claimsFromRequest);
                        startActivity(intent);
//                        } else {
//                            Intent intent = new Intent(ClaimsInsuranceBenefitActivity.this, ClaimsReasonActivity.class);
//                            intent.putExtra(Constant.CLAIMS_INTKEY_MAIN_REQUEST, claimsFromRequest);
//                            startActivity(intent);
//                        }
                    } else {
                        Intent intent = new Intent(ClaimsInsuranceBenefitActivity.this, ClaimsReasonActivity.class);
//                        intent.putExtra(Constant.CLAIMS_INTKEY_MAIN_REQUEST, claimsFromRequest);
                        startActivity(intent);
                    }
                }
//                }
            }
        });

        //init for HealthCare

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                adapter.setSelectedAtPosition(position);

                SingleChoiceModel claimType = adapter.getItem(position);

                claimsFromRequest.setClaimType(claimType.getCode());
                claimsFromRequest.setClaimName(claimType.getName());
                claimsFromRequest.setPolicyNo(claimType.getSubname());

//                if (claimType.getCode().equalsIgnoreCase("Healthcare")) {
//                    //init health care benifits
//                    lsHealthCare = new ArrayList<>();
//                    lsData.add(new SingleChoiceModel(0, Constant.CLAIMS_TREATMENT_DENTAL, "Chăm sóc răng", claimType.getSubname(), false));
//                    lsData.add(new SingleChoiceModel(0, Constant.CLAIMS_TREATMENT_OUTPATIENT, "Điều trị ngoại trú", claimType.getSubname(), false));
//                    lsData.add(new SingleChoiceModel(0, Constant.CLAIMS_TREATMENT_INPATIENT, "Điều trị nội trú", claimType.getSubname(), false));
//
//                    adapterHealthCare = new SingleChoiceListAdapter(ClaimsInsuranceBenefitActivity.this, 0, lsHealthCare);
//                    lvHealthCare.setAdapter(adapterHealthCare);
//                    isHealthCare = true;
//                }
            }
        });


        lvHealthCare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterHealthCare.setSelectedAtPosition(position);
                isHealthCare = false;
            }
        });
    }

    /**
     * Task get LifeInsuredList
     */
    private class GetBenifitsListTask extends AsyncTask<Void, Void, Response<GetClientProfileByCLIIDResponse>> {

        Context context;
        String clientID;
        ProgressDialog process;

        public GetBenifitsListTask(Context c, String clientid) {
            this.context = c;
            clientID = clientid;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (process == null)
                process = new ProgressDialog(context);
            process.setIndeterminate(true);
            process.setIndeterminateDrawable(getResources().getDrawable(R.drawable.dialog_progress_cercle));
            process.show();
        }

        @Override
        protected Response<GetClientProfileByCLIIDResponse> doInBackground(Void... voids) {
            Response<GetClientProfileByCLIIDResponse> response = null;

            try {
                GetClientProfileByCLIIDRequest data = new GetClientProfileByCLIIDRequest();
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setAction(Constant.CLAIMS_ACTION_LIFEINSURED_BENEFIT);
                data.setClientID(clientID);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetClientProfileByCLIIDResponse> call = svRequester.GetClientProfileByCLIID(request);
                response = call.execute();

            } catch (IOException e) {
                myLog.printTrace(e);
            }

            return response;
        }

        @Override
        protected void onPostExecute(Response<GetClientProfileByCLIIDResponse> response) {
            super.onPostExecute(response);
            process.dismiss();

            if (response != null)
                if (response.isSuccessful()) {
                    GetClientProfileByCLIIDResponse rp = response.body();
                    if (rp != null) {
                        GetClientProfileByCLIIDResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                if (result.getClientProfile() != null) {
                                    List<ClientProfile> lst = result.getClientProfile();
                                    if (lst != null) {
                                        lsData = new ArrayList<>();
                                        for (int i = 0; i < lst.size(); i++) {

                                            //get code Claims: vd: Death
                                            String[] scode = lst.get(i).getClaimsType().split(";");
                                            //get Claims Name: vd: Tử Vong
                                            String[] sname = lst.get(i).getClaimsName().split(";");

                                            if (sname.length > 0) {
                                                for (int a = 0; a < sname.length; a++) {
                                                    if (!TextUtils.isEmpty(sname[a]))
                                                        lsData.add(new SingleChoiceModel(0, scode[a], sname[a], lst.get(i).getPolicyID(), false));
                                                }
                                            }
                                        }

                                        adapter = new SingleChoiceListAdapter(context, 0, lsData);
                                        lvData.setAdapter(adapter);

                                        //check if claims exist & selected, set selected in list
                                        if (!TextUtils.isEmpty(claimsFromRequest.getClaimType())) {
                                            for (int i = 0; i < lsData.size(); i++)
                                                if (lsData.get(i).getCode().equalsIgnoreCase(claimsFromRequest.getClaimType())
                                                        && lsData.get(i).getSubname().equalsIgnoreCase(claimsFromRequest.getPolicyNo()))
                                                    adapter.setSelectedAtPosition(i);
                                        }
                                    }
                                }
                            } else {

                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                } else {
                                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                            .setMessage("Yêu cầu đã được gửi không thành công! Xin vui lòng thử lại.")
                                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    dialog.show();
                                }
                            }
                        }
                    }
                }
        }
    }

    @Override
    public void onBackPressed() {
//        if (isHealthCare) {
//            lvHealthCare.setVisibility(View.GONE);
//            lvData.setVisibility(View.VISIBLE);
//        } else
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
