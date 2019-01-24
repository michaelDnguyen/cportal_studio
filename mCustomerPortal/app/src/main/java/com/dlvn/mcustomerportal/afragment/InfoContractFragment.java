package com.dlvn.mcustomerportal.afragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.adapter.ContractListAdapter;
import com.dlvn.mcustomerportal.afragment.prototype.AddProposalFragment;
import com.dlvn.mcustomerportal.afragment.prototype.ContractDetailFragment;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPGetPolicyListByCLIIDRequest;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyListByCLIIDResponse;
import com.dlvn.mcustomerportal.services.model.response.CPGetPolicyListByCLIIDResult;
import com.dlvn.mcustomerportal.services.model.response.CPPolicy;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Thông tin hợp đồng bảo hiểm: gồm hợp đồng nhóm và hợp đồng cá nhân
 *
 * @author nn.tai
 * @date Nov 7, 2017
 */
public class InfoContractFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    Button btnHopDongBH, btnYeuCauBH, btnAddPolicy;
    ListView lvData;
    TextView tvNoData;
    SwipeRefreshLayout swipeContainer;
    LinearLayout lloData;

    ContractListAdapter hdbhAdapter, ycbhAdapter;
    boolean isHopDong = true;
    List<CPPolicy> lstPolicy;

    ServicesRequest svRequester;
    getPolicyListTask getPolicyTask = null;

    OnFragmentInteractionListener onAddFragment;

    public InfoContractFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoContractFragment newInstance(String param1, String param2) {
        InfoContractFragment fragment = new InfoContractFragment();
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
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        lstPolicy = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * TODO: Hide Header of Dashboard Activity
         */
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.VISIBLE);

        new CPSaveLogTask(getActivity(), Constant.CPLOG_POLICY_OPEN).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_POLICY_CLOSE).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_info_contract, container, false);
            getViews(view);
            initDatas();
            setListener();

        }
        return view;
    }

    private void getViews(View v) {
        btnHopDongBH = v.findViewById(R.id.btnHDCaNhan);
        btnYeuCauBH = v.findViewById(R.id.btnHDNhom);
        btnAddPolicy = v.findViewById(R.id.btnAddPolicy);

        tvNoData = v.findViewById(R.id.tvNoData);
        tvNoData.setVisibility(View.GONE);

        swipeContainer = v.findViewById(R.id.swipeContainer);

        lvData = v.findViewById(R.id.lvDataCaNhan);
        lvData.setDividerHeight(10);

        lloData = v.findViewById(R.id.lloData);
    }

    private void initDatas() {

        if (TextUtils.isEmpty(CustomPref.getUserID(getActivity()))) {
            lloData.setVisibility(View.GONE);
            btnAddPolicy.setVisibility(View.VISIBLE);
        } else {
            lloData.setVisibility(View.VISIBLE);
            btnAddPolicy.setVisibility(View.GONE);
            btnHopDongBH.setSelected(true);

            initHopDongBH();

            lvData.setAdapter(hdbhAdapter);
            attempPolicyByCLIID();
        }
    }

    private void initHopDongBH() {
        if (hdbhAdapter == null)
            hdbhAdapter = new ContractListAdapter(getActivity(), lstPolicy);
        else {
            hdbhAdapter.reFreshData(lstPolicy);
        }
    }

    private void initYeuCauBH() {
        List<CPPolicy> lst2 = new ArrayList<>();

        ycbhAdapter = new ContractListAdapter(getActivity(), lst2);
        if (hdbhAdapter == null)
            hdbhAdapter = new ContractListAdapter(getActivity(), lst2);
        else {
            hdbhAdapter.reFreshData(lst2);
//            hdbhAdapter.setData(lst2);
        }
    }

    private void attempPolicyByCLIID() {
        if (getPolicyTask != null)
            return;
        else {
            getPolicyTask = new getPolicyListTask();
            getPolicyTask.execute();
            swipeContainer.setRefreshing(true);
        }
    }

    private void setListener() {
        btnHopDongBH.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.setSelected(true);
                btnYeuCauBH.setSelected(false);
                if (!isHopDong) {
                    initHopDongBH();
                    isHopDong = true;
                }
            }
        });

        btnYeuCauBH.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                v.setSelected(true);
                btnHopDongBH.setSelected(false);
                if (isHopDong) {
                    initYeuCauBH();
                    isHopDong = false;
                }
            }
        });

        swipeContainer.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
//                swipeContainer.setRefreshing(false);
                attempPolicyByCLIID();
            }
        });

        lvData.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CPPolicy item = (CPPolicy) parent.getAdapter().getItem(position);

                Bundle bundle = new Bundle();
                bundle.putParcelable("CONTRACT_DETAIL", item);
                ContractDetailFragment fragment = new ContractDetailFragment();
                fragment.setArguments(bundle);

                onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_CONTRACT, fragment, true);
            }
        });

        btnAddPolicy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProposalFragment fragment = new AddProposalFragment();
                onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_CONTRACT, fragment, true);
            }
        });
    }

    /**
     * Task call API get List Policy By CLient ID
     */
    public class getPolicyListTask extends AsyncTask<Void, Void, Response<CPGetPolicyListByCLIIDResponse>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Response<CPGetPolicyListByCLIIDResponse> doInBackground(Void... params) {

            Response<CPGetPolicyListByCLIIDResponse> result = null;
            try {

                CPGetPolicyListByCLIIDRequest data = new CPGetPolicyListByCLIIDRequest();
                if (!TextUtils.isEmpty(CustomPref.getUserName(getActivity())))
                    data.setUserLogin(CustomPref.getUserName(getActivity()));
                else
                    data.setUserLogin(CustomPref.getUserID(getActivity()));
                data.setPassword(CustomPref.getPassword(getActivity()));
                data.setAPIToken(CustomPref.getAPIToken(getActivity()));
                data.setClientID(CustomPref.getUserID(getActivity()));
                data.setLinkGmail(CustomPref.getGoogleID(getActivity()));
                data.setLinkFacebook(CustomPref.getFacebookID(getActivity()));

                data.setDeviceId(Utilities.getDeviceID(getActivity()));
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
            getPolicyTask = null;
            swipeContainer.setRefreshing(false);

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        CPGetPolicyListByCLIIDResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                CPGetPolicyListByCLIIDResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        myLog.e("InfoContractFragment", "Get Point: " + result.getErrLog());

                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(getActivity(), getString(R.string.message_alert_relogin));
                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            //Save Token
                                            CustomPref.saveAPIToken(getActivity(), result.getNewAPIToken());

                                        if (result.getPolicyList() != null) {

                                            lstPolicy = result.getPolicyList();
                                            if (lstPolicy.size() > 0) {
                                                initHopDongBH();
                                            }
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
            getPolicyTask = null;
        }
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
