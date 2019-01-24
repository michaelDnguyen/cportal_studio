package com.dlvn.mcustomerportal.afragment;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.ElectricBillListAdapter;
import com.dlvn.mcustomerportal.adapter.model.ElectricBillModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.GetTaxInvoiceRequest;
import com.dlvn.mcustomerportal.services.model.response.CPPolicy;
import com.dlvn.mcustomerportal.services.model.response.GetTaxInvoiceResponse;
import com.dlvn.mcustomerportal.services.model.response.GetTaxInvoiceResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Response;

public class ElectricBillFragment extends Fragment {

    private static final String TAG = "ElectricBillFragment";

    public static final String KEY_POLICY_ITEM = "KEY_POLICY_ITEM";

    View view;

    ListView lvData;
    TextView tvNoData;
    SwipeRefreshLayout swipeContainer;

    ElectricBillListAdapter adapter;
    List<ElectricBillModel> lstData;

    CPPolicy policyItem = null;

    OnFragmentInteractionListener onAddFragment;
    ServicesRequest svRequester;

    public ElectricBillFragment() {
        // Required empty public constructor
    }

    public static ElectricBillFragment newInstance(CPPolicy policy) {
        ElectricBillFragment fragment = new ElectricBillFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_POLICY_ITEM, policy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            policyItem = getArguments().getParcelable(KEY_POLICY_ITEM);
        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_electricbill, container, false);

            getView(view);
            initDatas();
            setListener();
        }

        return view;
    }

    private void getView(View v) {
        // TODO Auto-generated method stub
        lvData = v.findViewById(R.id.lvData);
        lvData.setDividerHeight(10);

        tvNoData = v.findViewById(R.id.tvNoData);
        tvNoData.setVisibility(View.GONE);
        swipeContainer = v.findViewById(R.id.swipeContainer);
    }

    private void initDatas() {
        if (lstData == null)
            lstData = new ArrayList<>();

        if (policyItem != null)
            new doGetTaxInvoiceTask(getActivity(), Constant.ACTION_TAXINVOICE_LIST).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void setListener() {
        // TODO Auto-generated method stub
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new doGetTaxInvoiceTask(getActivity(), Constant.ACTION_TAXINVOICE_LIST).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    /**
     *
     */
    private class doGetTaxInvoiceTask extends AsyncTask<Void, Void, Response<GetTaxInvoiceResponse>> {

        Context context;
        String action;

        public doGetTaxInvoiceTask(Context c, String action) {
            this.context = c;
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected Response<GetTaxInvoiceResponse> doInBackground(Void... voids) {
            Response<GetTaxInvoiceResponse> res = null;

            try {
                GetTaxInvoiceRequest data = new GetTaxInvoiceRequest();

                data.setDeviceToken(CustomPref.getFirebaseToken(context));
                data.setDeviceID(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);
                data.setAPIToken(CustomPref.getAPIToken(context));

                data.setClientID(CustomPref.getUserID(context));
                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));

                data.setPolicyNo(policyItem.getPolicyID().trim());
                data.setAction(action);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<GetTaxInvoiceResponse> call = svRequester.GetTaxInvoice(request);
                res = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            return res;
        }

        @Override
        protected void onPostExecute(Response<GetTaxInvoiceResponse> success) {
            super.onPostExecute(success);
            swipeContainer.setRefreshing(false);

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        GetTaxInvoiceResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                GetTaxInvoiceResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        myLog.e(TAG, "Get Tax Invoice: " + result.getErrLog());

                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                        }
                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        //Save Token
                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            CustomPref.saveAPIToken(context, result.getNewAPIToken());

                                        if (result.getElectricBill() != null) {

                                            lstData = result.getElectricBill();

                                            if (adapter == null) {
                                                adapter = new ElectricBillListAdapter(getActivity(), lstData, svRequester);
                                                lvData.setAdapter(adapter);
                                            } else
                                                adapter.reFreshData(lstData);

                                            if (lstData.size() > 0)
                                                tvNoData.setVisibility(View.GONE);
                                            else
                                                tvNoData.setVisibility(View.VISIBLE);
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
    public void onResume() {
        super.onResume();
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);
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