package com.dlvn.mcustomerportal.activity;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ExpParentLevelAdapter;
import com.dlvn.mcustomerportal.adapter.model.ContractDetailModel;
import com.dlvn.mcustomerportal.adapter.model.PolicyItemDetailModel;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPGetPolicyInfoByPOLIDRequest;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyInfoByPOLIDResult;
import com.dlvn.mcustomerportal.services.model.response.CPPolicy;
import com.dlvn.mcustomerportal.services.model.response.CPPolicyInfo;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import retrofit2.Call;
import retrofit2.Response;

public class ContractDetailActivity extends BaseActivity {

    Spinner spnHopDong;
    ExpandableListView lvPolicyDetail;
    ExpParentLevelAdapter adapter;
    Button btnPayment;

    //Nút "Quay Lại"
    LinearLayout lloBack;

    CPPolicy itemPolicy;
    ServicesRequest svRequester;
    getPolicyDetailTask policyDetailTask = null;

    List<ContractDetailModel> lstDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_detail);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        // TODO Auto-generated method stub
        lloBack = (LinearLayout) findViewById(R.id.lloBack);
        spnHopDong = (Spinner) findViewById(R.id.spnHopDong);
        lvPolicyDetail = (ExpandableListView) findViewById(R.id.lvPolicyDetail);
        btnPayment = (Button) findViewById(R.id.btnPayment);
    }

    private void initData() {
        // TODO Auto-generated method stub
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        lstDetail = new ArrayList<>();

        if (getIntent().getExtras().containsKey("CONTRACT_DETAIL"))
            itemPolicy = getIntent().getParcelableExtra("CONTRACT_DETAIL");

        if (itemPolicy != null)
            setTitle("Chi tiết HĐ " + itemPolicy.getPolicyID());

        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYDETAIL);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYPRODUCT);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYANN);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYPAYMENT);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYBENE);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYLIFEINSURED);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYAGENT);

        List<String> list = new ArrayList<String>();
        list.add("00658947");
        list.add("00864521");
        list.add("00247846");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnHopDong.setAdapter(dataAdapter);
    }

    private void initListDetail() {
        if (adapter == null) {
            adapter = new ExpParentLevelAdapter(ContractDetailActivity.this, lstDetail);
            lvPolicyDetail.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();

        lvPolicyDetail.setGroupIndicator(null);
        lvPolicyDetail.expandGroup(0);
    }

    private void setListener() {

        /**
         * Set action for the Back LinearLayout (Quay lai)
         */
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // TODO Auto-generated method stub
        spnHopDong.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                String itemSelected = (String) parent.getAdapter().getItem(position);
                if (!TextUtils.isEmpty(itemSelected))
                    setTitle("Chi tiết HĐ " + itemSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void attempTaskGetPolicyInfo(String act) {
//        if (policyDetailTask != null)
//            policyDetailTask.setAction(act);
//        else {
//            policyDetailTask = new getPolicyIDInquiry(ContractDetailActivity.this, act);
//
//        }
//        policyDetailTask.execute();
        new getPolicyDetailTask(ContractDetailActivity.this, act).execute();
    }

    /**
     * Task call API get List Policy Bu CLient ID
     */
    public class getPolicyDetailTask extends AsyncTask<Void, Void, Response<CPGetPolicyInfoByPOLIDResponse>> {

        Context context;
        String Action;

        public getPolicyDetailTask(Context c, String act) {
            context = c;
            Action = act;
        }

        public void setAction(String act) {
            Action = act;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Response<CPGetPolicyInfoByPOLIDResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<CPGetPolicyInfoByPOLIDResponse> result = null;
            try {

                CPGetPolicyInfoByPOLIDRequest data = new CPGetPolicyInfoByPOLIDRequest();
                data.setUserID(CustomPref.getUserID(context));
                data.setPassword(CustomPref.getPassword(context));
                data.setAPIToken(CustomPref.getAPIToken(context));

                data.setAction(Action);
                data.setPolID(itemPolicy.getPolicyID());

                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<CPGetPolicyInfoByPOLIDResponse> call = svRequester.CPGetPolicyInfoByPOLID(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<CPGetPolicyInfoByPOLIDResponse> success) {
            policyDetailTask = null;
//			swipeContainer.setRefreshing(false);

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        CPGetPolicyInfoByPOLIDResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                CPGetPolicyInfoByPOLIDResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        myLog.e("ContactDetailActivity","Get Point: " + result.getErrLog());
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

                                        if (result.getCpPolicyInfos() != null) {

                                            if (Action.equals(Constant.POINFO_ACTION_POLICYDETAIL)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPolicyID()));
                                                    lst.add(parseStringValue(mo.getFrequency()));
                                                    lst.add(parseStringValue(mo.getPolicyStatus()));
                                                    lst.add(parseStringValue(mo.getPolIssEffDate()));
                                                    lst.add(parseStringValue(mo.getPolMPremAmt()));
                                                    lst.add(parseStringValue(mo.getPolSndryAmt()));
                                                }
                                                lstDetail.add(new ContractDetailModel("Chi tiết hợp đồng", lst));
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYANN)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPolAccountValue()));
                                                    lst.add(parseStringValue(mo.getAllocateMPrem()));
                                                    lst.add(parseStringValue(mo.getACFIP()));
                                                    lst.add(parseStringValue(mo.getLoyaltyBonus()));
                                                    lst.add(parseStringValue(mo.getTotalDeduct()));
                                                    lst.add(parseStringValue(mo.getNetWithDrawal()));
                                                    lst.add(parseStringValue(mo.getWdChargeAmt()));
                                                    lst.add(parseStringValue(mo.getPrincIntAmt()));
                                                }
                                                lstDetail.add(new ContractDetailModel("Thông tin các giá trị hợp đồng", lst));
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYLIFEINSURED)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getDOB()));
                                                    lst.add(parseStringValue(mo.getPOID()));
                                                    lst.add(parseStringValue(mo.getGender()));
                                                    lst.add(parseStringValue(mo.getFullName()));
                                                    lst.add(parseStringValue(mo.getAddress()));
                                                    lst.add(parseStringValue(mo.getNote()));
                                                }
                                                lstDetail.add(new ContractDetailModel("Thông tin người được bảo hiểm chính", lst));
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYBENE)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getDOB()));
                                                    lst.add(parseStringValue(mo.getPOID()));
                                                    lst.add(parseStringValue(mo.getGender()));
                                                    lst.add(parseStringValue(mo.getFullName()));
                                                    lst.add(parseStringValue(mo.getAddress()));
                                                    lst.add(parseStringValue(mo.getNote()));
                                                }
                                                lstDetail.add(new ContractDetailModel("Thông tin người thụ hưởng", lst));
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYPAYMENT)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPolicyID()));
                                                    lst.add(parseStringValue(mo.getPolMPremAmt()));
                                                    lst.add(parseStringValue(mo.getPolSndryAmt()));
                                                    lst.add(parseStringValue(mo.getTotalDeposit()));
                                                    lst.add(parseStringValue(mo.getBasicPrem()));
                                                    lst.add(parseStringValue(mo.getExcessPrem()));
                                                }
                                                lstDetail.add(new ContractDetailModel("Thông tin người mua bảo hiểm", lst));
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYPRODUCT)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPolicyLIName()));
                                                    lst.add(parseStringValue(mo.getProductName()));
                                                    lst.add(parseStringValue(mo.getFaceAmount()));
                                                    lst.add(parseStringValue(mo.getPolExpiryDate()));
                                                    lst.add(parseStringValue(mo.getPolIssEffDate()));
                                                }
                                                lstDetail.add(new ContractDetailModel("Thông tin sản phẩm", lst));
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYAGENT)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPrimaryAgent()));
                                                    lst.add(parseStringValue(mo.getServAgent()));
                                                    lst.add(parseStringValue(mo.getContactEmail()));
                                                }
                                                lstDetail.add(new ContractDetailModel("Thông tin tư vấn", lst));
                                            }

                                            initListDetail();
                                        }
                                    } else {
                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            } else {

            }
        }

        @Override
        protected void onCancelled() {
            policyDetailTask = null;
        }
    }

    /**
     * Parse from string to split by ";"
     *
     * @param str
     * @return
     */
    private PolicyItemDetailModel parseStringValue(String str) {
        if (TextUtils.isEmpty(str))
            return null;
        else if (str.contains(";")) {
            String[] arr = str.split(";");
            return new PolicyItemDetailModel(arr[0], arr[1], "");
        }
        return new PolicyItemDetailModel(str, "", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
