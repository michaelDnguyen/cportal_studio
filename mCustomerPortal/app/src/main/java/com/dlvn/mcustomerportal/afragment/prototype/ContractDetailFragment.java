package com.dlvn.mcustomerportal.afragment.prototype;

import android.content.Context;
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
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.ContractDetailActivity;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.adapter.ExpParentLevelAdapter;
import com.dlvn.mcustomerportal.adapter.model.ContractDetailModel;
import com.dlvn.mcustomerportal.adapter.model.HomeItemModel;
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
    public static final String titlePOLICYPAYMENT = "Thông tin người mua bảo hiểm";
    public static final String titlePOLICYBENE = "Thông tin người thụ hưởng";
    public static final String titlePOLICYLIFEINSURED = "Thông tin người được bảo hiểm chính";
    public static final String titlePOLICYAGENT = "Thông tin tư vấn";

    Bundle bundle;
    Bundle bundleForPayment = new Bundle();
    View view;
    Spinner spnHopDong;
    ExpandableListView lvPolicyDetail;
    ExpParentLevelAdapter adapter;
    Button btnPayment;

    OnFragmentInteractionListener onAddFragment;

    //Nút "Quay Lại"
    LinearLayout lloBack;

    CPPolicy itemPolicy;
    ServicesRequest svRequester;
    getPolicyDetailTask policyDetailTask = null;

    List<ContractDetailModel> lstDetail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContractDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContractDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContractDetailFragment newInstance(String param1, String param2) {
        ContractDetailFragment fragment = new ContractDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bundle = getArguments();
//        String parram = bundle.getString("CONTRACT_DETAIL");
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
        // TODO Auto-generated method stub
        lloBack = (LinearLayout) view.findViewById(R.id.lloBack);
        spnHopDong = (Spinner) view.findViewById(R.id.spnHopDong);
        lvPolicyDetail = (ExpandableListView) view.findViewById(R.id.lvPolicyDetail);
        btnPayment = (Button) view.findViewById(R.id.btnPayment);

        /**
         * TODO: Hide Header of Dashboard Activity
         */
        LinearLayout lloHeader = (LinearLayout) getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
    }

    private void initData() {
        // TODO Auto-generated method stub
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        lstDetail = new ArrayList<>();

        if (bundle.containsKey("CONTRACT_DETAIL"))
            itemPolicy = bundle.getParcelable("CONTRACT_DETAIL");

        //init title for parent list
        lstDetail.add(new ContractDetailModel(titlePOLICYDETAIL, new ArrayList<HomeItemModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYANN, new ArrayList<HomeItemModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYLIFEINSURED, new ArrayList<HomeItemModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYBENE, new ArrayList<HomeItemModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYPAYMENT, new ArrayList<HomeItemModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYPRODUCT, new ArrayList<HomeItemModel>()));
        lstDetail.add(new ContractDetailModel(titlePOLICYAGENT, new ArrayList<HomeItemModel>()));
        initListDetail();

        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYDETAIL);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYPRODUCT);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYANN);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYPAYMENT);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYBENE);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYLIFEINSURED);
        attempTaskGetPolicyInfo(Constant.POINFO_ACTION_POLICYAGENT);
    }

    private void initListDetail() {
        if (adapter == null) {
            adapter = new ExpParentLevelAdapter(getActivity(), lstDetail);
            lvPolicyDetail.setAdapter(adapter);
        } else
            adapter.notifyDataSetChanged();

//        lvPolicyDetail.expandGroup(0,true);
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

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PolicyPaymentFragment fragment = new PolicyPaymentFragment();
                fragment.setArguments(bundleForPayment);
                onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_CONTRACT, fragment, true);
            }
        });
    }

    private void replaceSubListOfContract(String title, List<HomeItemModel> lst) {
        for (int i = 0; i < lstDetail.size(); i++) {
            if (lstDetail.get(i).getTitle().equalsIgnoreCase(title)) {
                lstDetail.get(i).setLstValue(lst);
            }
        }
    }

    private void attempTaskGetPolicyInfo(String act) {
        new getPolicyDetailTask(getActivity(), act).execute();
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
                data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
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
                                        myLog.E("ContractDetailFrag", "Get Point: " + result.getErrLog());
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveToken(context, result.getNewAPIToken());

                                        if (result.getCpPolicyInfos() != null) {

                                            if (Action.equals(Constant.POINFO_ACTION_POLICYDETAIL)) {
                                                List<HomeItemModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPolicyID()));
                                                    /**
                                                     * TODO: Lay so HD
                                                     */
                                                    bundleForPayment.putString(Constant.INT_KEY_POLICY_NO, parseStringValue(mo.getPolicyID()).get_content());
                                                    //
                                                    lst.add(parseStringValue(mo.getFrequency()));
                                                    lst.add(parseStringValue(mo.getPolicyStatus()));
                                                    lst.add(parseStringValue(mo.getPolIssEffDate()));
                                                    lst.add(parseStringValue(mo.getPolMPremAmt()));
                                                    lst.add(parseStringValue(mo.getPolSndryAmt()));
                                                }
//                                                lstDetail.add(new ContractDetailModel(titlePOLICYDETAIL, lst));
                                                replaceSubListOfContract(titlePOLICYDETAIL, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYANN)) {
                                                List<HomeItemModel> lst = new ArrayList<>();

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
//                                                lstDetail.add(new ContractDetailModel(titlePOLICYANN, lst));
                                                replaceSubListOfContract(titlePOLICYANN, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYLIFEINSURED)) {
                                                List<HomeItemModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getDOB()));
                                                    lst.add(parseStringValue(mo.getPOID()));
                                                    lst.add(parseStringValue(mo.getGender()));
                                                    lst.add(parseStringValue(mo.getFullName()));
                                                    lst.add(parseStringValue(mo.getAddress()));
                                                    lst.add(parseStringValue(mo.getNote()));
                                                }
//                                                lstDetail.add(new ContractDetailModel(titlePOLICYLIFEINSURED, lst));
                                                replaceSubListOfContract(titlePOLICYLIFEINSURED, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYBENE)) {
                                                List<HomeItemModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getDOB()));
                                                    lst.add(parseStringValue(mo.getPOID()));
                                                    lst.add(parseStringValue(mo.getGender()));
                                                    lst.add(parseStringValue(mo.getFullName()));
                                                    lst.add(parseStringValue(mo.getAddress()));
                                                    lst.add(parseStringValue(mo.getNote()));
                                                }
//                                                lstDetail.add(new ContractDetailModel(titlePOLICYBENE, lst));
                                                replaceSubListOfContract(titlePOLICYBENE, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYPAYMENT)) {
                                                List<HomeItemModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPolicyID()));
                                                    lst.add(parseStringValue(mo.getPolMPremAmt()));
                                                    /**
                                                     * TODO: Lay Phi bao hiem dinh ky
                                                     */
                                                    bundleForPayment.putString("phiBaoHiemDinhKy", parseStringValue(mo.getPolMPremAmt()).get_content());
                                                    //
                                                    lst.add(parseStringValue(mo.getPolSndryAmt()));
                                                    lst.add(parseStringValue(mo.getTotalDeposit()));
                                                    lst.add(parseStringValue(mo.getBasicPrem()));
                                                    lst.add(parseStringValue(mo.getExcessPrem()));
                                                }
//                                                lstDetail.add(new ContractDetailModel(titlePOLICYPAYMENT, lst));
                                                replaceSubListOfContract(titlePOLICYPAYMENT, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYPRODUCT)) {
                                                List<HomeItemModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPolicyLIName()));
                                                    lst.add(parseStringValue(mo.getProductName()));
                                                    lst.add(parseStringValue(mo.getFaceAmount()));
                                                    lst.add(parseStringValue(mo.getPolExpiryDate()));
                                                    lst.add(parseStringValue(mo.getPolIssEffDate()));
                                                }
//                                                lstDetail.add(new ContractDetailModel(titlePOLICYPRODUCT, lst));
                                                replaceSubListOfContract(titlePOLICYPRODUCT, lst);
                                            } else if (Action.equals(Constant.POINFO_ACTION_POLICYAGENT)) {
                                                List<HomeItemModel> lst = new ArrayList<>();

                                                for (CPPolicyInfo mo : result.getCpPolicyInfos()) {
                                                    lst.add(parseStringValue(mo.getPrimaryAgent()));
                                                    lst.add(parseStringValue(mo.getServAgent()));
                                                    lst.add(parseStringValue(mo.getContactEmail()));
                                                }
//                                                lstDetail.add(new ContractDetailModel(titlePOLICYAGENT, lst));
                                                replaceSubListOfContract(titlePOLICYAGENT, lst);
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
    private HomeItemModel parseStringValue(String str) {
        if (TextUtils.isEmpty(str))
            return null;
        else if (str.contains(";")) {
            String[] arr = str.split(";");
            return new HomeItemModel(arr[0], arr[1], "");
        }
        return new HomeItemModel(str, "", "");
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
    }
}
