package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.adapter.ExpParentLevelAdapter;
import com.dlvn.mcustomerportal.adapter.model.ContractDetailModel;
import com.dlvn.mcustomerportal.adapter.model.PolicyItemDetailModel;
import com.dlvn.mcustomerportal.afragment.ElectricBillFragment;
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
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Fragment cho chi tiet cua Hop Dong
 *
 * @date July 17, 2018
 */
public class ContractDetailFragment extends Fragment {

    public static final String titlePOLICYDETAIL = "Chi tiết hợp đồng";
    public static final String titlePOLICYPRODUCT = "Thông tin sản phẩm";
    public static final String titlePOLICYANN = "Thông tin các giá trị hợp đồng";
    public static final String titlePOLICYPAYMENT = "Thông tin Thanh Toán";
    public static final String titlePOLICYCLIENT = "Thông tin người mua bảo hiểm";
    public static final String titlePOLICYBENE = "Thông tin người thụ hưởng";
    public static final String titlePOLICYLIFEINSURED = "Thông tin người được bảo hiểm chính";
    public static final String titlePOLICYAGENT = "Thông tin tư vấn";
    public static final String titlePOLICYPENDING = "Thông tin cần bổ sung";

    Bundle bundle;
    Bundle bundleForPayment = new Bundle();

    View view;
    Spinner spnHopDong;
    ExpandableListView lvPolicyDetail;
    ExpParentLevelAdapter adapter;
    Button btnPayment, btnTaxInvoice;

    OnFragmentInteractionListener onAddFragment;

    //Nút "Quay Lại"
    LinearLayout lloBack;
    TextView tvCapNhat;

    CPPolicy itemPolicy;
    ServicesRequest svRequester;
    getPolicyDetailTask policyDetailTask = null;

    //Flag cho btnPayment can click or not
    boolean isPaymentClick = false;

    List<ContractDetailModel> lstDetail;
    PaymentDetailModel paymentModel = null, LIProfile = null, BeneProfile = null;

    public ContractDetailFragment() {
        // Required empty public constructor
    }

    public static ContractDetailFragment newInstance(String param1, String param2) {
        ContractDetailFragment fragment = new ContractDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        bundle = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.activity_contract_detail, container, false);
            getViews(view);
            initData();
            setListener();
        }
        return view;
    }

    private void getViews(View view) {
        lloBack = view.findViewById(R.id.lloBack);
        tvCapNhat = view.findViewById(R.id.tvCapNhat);
        tvCapNhat.setEnabled(false);
        tvCapNhat.setTextColor(Color.GRAY);

        spnHopDong = view.findViewById(R.id.spnHopDong);
        lvPolicyDetail = view.findViewById(R.id.lvPolicyDetail);

        btnPayment = view.findViewById(R.id.btnPayment);
        btnPayment.setEnabled(false);
        btnPayment.setBackground(getResources().getDrawable(R.drawable.state_btn_grey_rectangle));

        btnTaxInvoice = view.findViewById(R.id.btnTaxInvoice);

        /**
         * TODO: Hide Header of Dashboard Activity
         */
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
    }

    private void initData() {
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        lstDetail = new ArrayList<>();

        if (bundle.containsKey("CONTRACT_DETAIL"))
            itemPolicy = bundle.getParcelable("CONTRACT_DETAIL");

        //init title for parent list
        lstDetail.add(new ContractDetailModel(titlePOLICYDETAIL, new ArrayList<PolicyItemDetailModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYCLIENT, new ArrayList<PolicyItemDetailModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYANN, new ArrayList<PolicyItemDetailModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYLIFEINSURED, new ArrayList<PolicyItemDetailModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYBENE, new ArrayList<PolicyItemDetailModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYPRODUCT, new ArrayList<PolicyItemDetailModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYAGENT, new ArrayList<PolicyItemDetailModel>()));

        if (!itemPolicy.getPolicyStatus().contains("Đang hiệu lực")) {
            isPaymentClick = false;
            lstDetail.add(new ContractDetailModel(titlePOLICYPENDING, new ArrayList<PolicyItemDetailModel>()));
            tvCapNhat.setVisibility(View.INVISIBLE);
        } else
            isPaymentClick = true;

        initListDetail();

        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYDETAIL);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICY_CLIENT);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYPAYMENT);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYPRODUCT);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYANN);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYBENE);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYLIFEINSURED);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYAGENT);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICY_PENDING);
    }

    private void initListDetail() {
        if (adapter == null) {
            adapter = new ExpParentLevelAdapter(getActivity(), lstDetail);
            lvPolicyDetail.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();

    }

    private void setListener() {
        /**
         * Set action for the Back LinearLayout (Quay lai)
         */
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        tvCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlterationFragment fragment = AlterationFragment.newInstance(paymentModel, LIProfile, BeneProfile);
                onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_CONTRACT, fragment, true);

            }
        });

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PolicyPaymentFragment fragment = new PolicyPaymentFragment();
                if (itemPolicy != null) {
                    if (itemPolicy.getPolicyID() != null)
                        bundleForPayment.putString(Constant.INT_KEY_POLICY_NO, itemPolicy.getPolicyID().trim());
                }
                if (paymentModel != null)
                    bundleForPayment.putParcelable(Constant.INT_KEY_PAYMENT_MODEL, paymentModel);

                fragment.setArguments(bundleForPayment);
                onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_CONTRACT, fragment, true);
            }
        });

        btnTaxInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElectricBillFragment fragment = ElectricBillFragment.newInstance(itemPolicy);
                onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_CONTRACT, fragment, true);
//                startActivity(new Intent(getActivity(), SampleActivity.class));
            }
        });

        lvPolicyDetail.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //Show dialog detail for Policy Pending
                if (lstDetail.get(groupPosition).getLstValue().get(childPosition).getActionPolicy() != null)
                    if (lstDetail.get(groupPosition).getLstValue().get(childPosition).getActionPolicy().equalsIgnoreCase(Constant.POINFO_ACTION_POLICY_PENDING))
                        showDialogPendingDetail(getActivity(), lstDetail.get(groupPosition).getLstValue().get(childPosition).get_title(), "", lstDetail.get(groupPosition).getLstValue().get(childPosition).getIssueCreDt(), lstDetail.get(groupPosition).getLstValue().get(childPosition).getIssueUpDt());

                return false;
            }
        });
    }

    private void replaceSubListOfContract(String title, List<PolicyItemDetailModel> lst) {
        for (int i = 0; i < lstDetail.size(); i++) {
            if (lstDetail.get(i).getTitle().equalsIgnoreCase(title)) {
                lstDetail.get(i).setLstValue(lst);
            }
        }
    }

    private void attempTaskGetPolicyInfo(String act) {
        new getPolicyDetailTask(getActivity(), act).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            Response<CPGetPolicyInfoByPOLIDResponse> result = null;
            try {

                CPGetPolicyInfoByPOLIDRequest data = new CPGetPolicyInfoByPOLIDRequest();
                data.setUserID(CustomPref.getUserID(context));
                data.setPassword(CustomPref.getPassword(context));
                data.setAPIToken(CustomPref.getAPIToken(context));

                data.setAction(Action);
                data.setPolID(itemPolicy.getPolicyID().trim());

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

                                        myLog.e("ContractDetailFrag", "Get Policy Detail: " + result.getErrLog());

                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }
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
                                                replaceSubListOfContract(titlePOLICYDETAIL, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICY_CLIENT)) {

                                                List<PolicyItemDetailModel> lst = new ArrayList<>();
                                                if (result.getCpPolicyInfos() != null) {

                                                    paymentModel = new PaymentDetailModel();
                                                    paymentModel.setSoHopDong(itemPolicy.getPolicyID());
                                                    paymentModel.setDob("");
                                                    paymentModel.setCmnd("");
                                                    paymentModel.setEmail(CustomPref.getEmail(context));

                                                    //Alteration: set type & title
                                                    paymentModel.setClientType(Constant.AlterationType.PO.toValue());
                                                    paymentModel.setTitleType(Constant.AlterationType.PO.toString());

                                                    for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                        lst.add(new PolicyItemDetailModel("Tên khách hàng", mo.getFullName(), ""));
                                                        lst.add(new PolicyItemDetailModel("Giới tính", mo.getGender(), ""));
                                                        lst.add(new PolicyItemDetailModel("Chứng minh nhân dân", mo.getPOID(), ""));
                                                        lst.add(new PolicyItemDetailModel("Ngày tháng năm sinh", mo.getDOB(), ""));
                                                        lst.add(new PolicyItemDetailModel("Địa chỉ", mo.getAddress(), ""));
                                                        lst.add(new PolicyItemDetailModel("Điện thoại", mo.getCellPhone(), ""));
                                                        lst.add(new PolicyItemDetailModel("Email", mo.getContactEmail(), ""));

                                                        paymentModel.setMaKhachHang(mo.getClientID());
                                                        paymentModel.setTenKhachHang(mo.getFullName());
                                                        paymentModel.setTenNguoiNop("");
                                                        paymentModel.setCmnd(mo.getPOID());
                                                        paymentModel.setDob(mo.getDOB());
                                                        paymentModel.setPhone(mo.getCellPhone());
                                                        paymentModel.setEmail(mo.getContactEmail());
                                                        paymentModel.setGender(mo.getGender());
                                                        paymentModel.setAddressHome(mo.getAddress());
                                                    }

                                                    if (result.getPolicyInfo() != null) {
                                                        paymentModel.setPhiBHGoc(result.getPolicyInfo().get(0).getPolSndryAmt());
                                                        paymentModel.setPhiAplGoc(result.getPolicyInfo().get(0).getAPL());
                                                        paymentModel.setPhiOplGoc(result.getPolicyInfo().get(0).getOPL());
                                                    }
                                                }

                                                if (isPaymentClick) {
                                                    btnPayment.setEnabled(true);
                                                    btnPayment.setBackgroundResource(R.drawable.state_btn_red_radius_gradien);
                                                    tvCapNhat.setEnabled(true);
                                                    tvCapNhat.setTextColor(Color.RED);
                                                }

                                                replaceSubListOfContract(titlePOLICYCLIENT, lst);

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
                                                replaceSubListOfContract(titlePOLICYANN, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYLIFEINSURED)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getFullName()));
                                                    lst.add(parseStringValue(mo.getDOB()));

                                                    PolicyItemDetailModel gender = parseStringValue(mo.getGender());
                                                    if (gender.get_content().equalsIgnoreCase("F"))
                                                        gender.set_content("Nữ");
                                                    else
                                                        gender.set_content("Nam");
                                                    lst.add(gender);

                                                    lst.add(parseStringValue(mo.getPOID()));
                                                    lst.add(parseStringValue(mo.getAddress()));

                                                    LIProfile = new PaymentDetailModel();
                                                    PolicyItemDetailModel temp = parseStringValue(mo.getClientID());
                                                    if (temp != null)
                                                        LIProfile.setMaKhachHang(temp.get_content());
                                                    temp = parseStringValue(mo.getFullName());
                                                    if (temp != null)
                                                        LIProfile.setTenKhachHang(temp.get_content());
                                                    temp = parseStringValue(mo.getPOID());
                                                    if (temp != null)
                                                        LIProfile.setCmnd(temp.get_content());
                                                    temp = parseStringValue(mo.getDOB());
                                                    if (temp != null)
                                                        LIProfile.setDob(temp.get_content());
                                                    temp = parseStringValue(mo.getGender());
                                                    if (temp != null)
                                                        LIProfile.setGender(temp.get_content());
                                                    temp = parseStringValue(mo.getAddress());
                                                    if (temp != null)
                                                        LIProfile.setAddressHome(temp.get_content());

                                                    //Alteration: set type & title
                                                    LIProfile.setClientType(Constant.AlterationType.LI.toValue());
                                                    LIProfile.setTitleType(Constant.AlterationType.LI.toString());
                                                }

                                                replaceSubListOfContract(titlePOLICYLIFEINSURED, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYBENE)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getFullName()));
                                                    lst.add(parseStringValue(mo.getGender()));
                                                    lst.add(parseStringValue(mo.getDOB()));
                                                    lst.add(parseStringValue(mo.getPOID()));
                                                    lst.add(parseStringValue(mo.getAddress()));
                                                    lst.add(parseStringValue(mo.getNote()));

                                                    BeneProfile = new PaymentDetailModel();
                                                    PolicyItemDetailModel temp = parseStringValue(mo.getClientID());
                                                    if (temp != null)
                                                        BeneProfile.setMaKhachHang(temp.get_content());
                                                    temp = parseStringValue(mo.getFullName());
                                                    if (temp != null)
                                                        BeneProfile.setTenKhachHang(temp.get_content());
                                                    temp = parseStringValue(mo.getPOID());
                                                    if (temp != null)
                                                        BeneProfile.setCmnd(temp.get_content());
                                                    temp = parseStringValue(mo.getDOB());
                                                    if (temp != null)
                                                        BeneProfile.setDob(temp.get_content());
                                                    temp = parseStringValue(mo.getGender());
                                                    if (temp != null)
                                                        BeneProfile.setGender(temp.get_content());
                                                    temp = parseStringValue(mo.getAddress());
                                                    if (temp != null)
                                                        BeneProfile.setAddressHome(temp.get_content());

                                                    //Alteration: set type & title
                                                    BeneProfile.setClientType(Constant.AlterationType.BENE.toValue());
                                                    BeneProfile.setTitleType(Constant.AlterationType.BENE.toString());
                                                }
                                                replaceSubListOfContract(titlePOLICYBENE, lst);
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
                                                replaceSubListOfContract(titlePOLICYPAYMENT, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYPRODUCT)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getProductName()));
//                                                    lst.add(parseStringValue(mo.getPolicyLIName()));
                                                    lst.add(parseStringValue(mo.getFaceAmount()));
                                                    lst.add(parseStringValue(mo.getPolExpiryDate()));
                                                    lst.add(parseStringValue(mo.getPolIssEffDate()));
                                                }
                                                replaceSubListOfContract(titlePOLICYPRODUCT, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYAGENT)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPrimaryAgent()));
                                                    lst.add(parseStringValue(mo.getServAgent()));
                                                    lst.add(parseStringValue(mo.getContactEmail()));
                                                    lst.add(parseStringValue(mo.getCellPhone()));
//                                                    lst.add(new PolicyItemDetailModel("Điện thoại", mo.getCellPhone(), ""));
                                                }
                                                replaceSubListOfContract(titlePOLICYAGENT, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICY_PENDING)) {
                                                List<PolicyItemDetailModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(new PolicyItemDetailModel(mo.getIssueReqDesc(), "", Constant.POINFO_ACTION_POLICY_PENDING, mo.getIssueCreatedDate(), mo.getIssueFolwupDate()));
                                                }
                                                replaceSubListOfContract(titlePOLICYPENDING, lst);
                                            }

                                            initListDetail();
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
            String[] arr = str.split(";", -1);
            return new PolicyItemDetailModel(arr[0] + "", arr[1] + "", "");
        }
        return new PolicyItemDetailModel(str, "", "");
    }

    /**
     * @param context
     */
    private void showDialogPendingDetail(Context context, String title, String content, String createDt, String updateDt) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_policy_pending);
        dialog.setCanceledOnTouchOutside(true);

        TextView tvTitle, tvContent, tvCreateDt, tvUpdateDT;
        tvTitle = dialog.findViewById(R.id.tvTitle);
        tvContent = dialog.findViewById(R.id.tvContent);
        tvCreateDt = dialog.findViewById(R.id.tvCreateDT);
        tvUpdateDT = dialog.findViewById(R.id.tvUpdateDT);

        if (title != null)
            tvTitle.setText(title);
        if (content != null)
            tvContent.setText(content);
        if (createDt != null)
            tvCreateDt.setText(createDt);
        if (updateDt != null)
            tvUpdateDT.setText(updateDt);

        Button btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);

        new CPSaveLogTask(getActivity(), Constant.CPLOG_POLICYDETAIL_OPEN).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_POLICYDETAIL_CLOSE).execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onAddFragment = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onAddFragment = null;
    }
}
