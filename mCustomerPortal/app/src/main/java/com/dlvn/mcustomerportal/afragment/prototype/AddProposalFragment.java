package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.afragment.InfoContractFragment;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.GoogleServiceGenerator;
import com.dlvn.mcustomerportal.services.GoogleServiceRequest;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.SiteVerifyResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.dlvn.mcustomerportal.utils.listerner.OnRegisterFragmentListener} interface
 * to handle interaction events.
 * Use the {@link AddProposalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProposalFragment extends Fragment {

    private static final String TAG = "AddProposalFragment";

    View v;
    TextView tvError;
    ClearableEditText cedtPolicyNo, cedtPOID, cedtDOB;

    LinearLayout lloBack;
    SwipeRefreshLayout swRefresh;
    Button btnTiepTuc;
    ImageButton ibtnReCapcha;

    boolean isCaptcha = false;
    ServicesRequest svRequester;

    OnFragmentInteractionListener onAddFragment;

    public AddProposalFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddProposalFragment newInstance(String param1, String param2) {
        AddProposalFragment fragment = new AddProposalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_add_proposal, container, false);

            initViews(v);
            setListener();
        }

        return v;
    }

    private void initViews(View v) {
        lloBack = v.findViewById(R.id.lloBack);
        swRefresh = v.findViewById(R.id.swRefresh);
        /**
         * TODO: Hide Header of Dashboard Activity
         */
        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
        if (lloHeader != null)
            lloHeader.setVisibility(View.GONE);

        btnTiepTuc = v.findViewById(R.id.btnTiepTuc);
        ibtnReCapcha = v.findViewById(R.id.ibtnReCapcha);

        tvError = v.findViewById(R.id.tvError);
        tvError.setText(null);

        cedtPolicyNo = v.findViewById(R.id.cedtPolicy);
        cedtPOID = v.findViewById(R.id.cedtPOID);
        cedtDOB = v.findViewById(R.id.cedtDOB);
    }

    private void setListener() {
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        cedtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR) - 18;
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        cal.set(year, month, dayOfMonth);
                        cedtDOB.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean cancel = false;
                View focusView = null;

                String strPolicyNo = cedtPolicyNo.getText().toString();
                String strPOID = cedtPOID.getText().toString();
                String strDoB = cedtDOB.getText().toString();

                if (TextUtils.isEmpty(strPolicyNo)) {
                    cedtPolicyNo.setError(getString(R.string.error_field_required));
                    focusView = cedtPolicyNo;
                    cancel = true;
                }

                if (TextUtils.isEmpty(strPOID)) {
                    cedtPOID.setError(getString(R.string.error_field_required));
                    focusView = cedtPOID;
                    cancel = true;
                }

                if (TextUtils.isEmpty(strDoB)) {
                    cedtDOB.setError(getString(R.string.error_field_required));
                    focusView = cedtDOB;
                    cancel = true;
                }

                if (cancel)
                    focusView.requestFocus();
                else {
                    if (NetworkUtils.isConnectedHaveDialog(getActivity()))
                        verifyWithReCaptcha(strPolicyNo, strPOID, strDoB);
                }
            }
        });

        ibtnReCapcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                verifyWithReCaptcha();
            }
        });
    }

    private void verifyWithReCaptcha(final String Policy, final String PoID, final String DoB) {
        myLog.e(TAG, "verifyWithReCaptcha");
        SafetyNet.getClient(getActivity()).verifyWithRecaptcha(getString(R.string.api_sitekey_recaptcha))
                .addOnSuccessListener(getActivity(), new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            myLog.e(TAG, "Token result " + response.getTokenResult());
                            getSiteVerifyReCaptCha(getActivity(), response.getTokenResult(), Policy, PoID, DoB);
                        }
                    }
                })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            myLog.e(TAG, "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            myLog.e(TAG, "Unknown type of error: " + e.getMessage());
                        }
                    }
                });
    }

    private void getSiteVerifyReCaptCha(final Context c, final String responseCaptcha, final String Policy, final String PoID, final String DoB) {
        new AsyncTask<Void, Void, Response<SiteVerifyResponse>>() {

            @Override
            protected Response<SiteVerifyResponse> doInBackground(Void... voids) {

                GoogleServiceGenerator svgenerate = new GoogleServiceGenerator();
                GoogleServiceRequest service = svgenerate.createService(c, GoogleServiceRequest.class);

                Call<SiteVerifyResponse> call = service.getSiteVerifyReCaptCha2(getString(R.string.api_secrectkey_recaptcha), responseCaptcha);
                Response<SiteVerifyResponse> response = null;

                try {
                    response = call.execute();
                } catch (IOException e) {
                    myLog.printTrace(e);
                }

                return response;
            }

            @Override
            protected void onPostExecute(Response<SiteVerifyResponse> result) {
                super.onPostExecute(result);
                if (result != null) {
                    if (result.isSuccessful()) {
                        SiteVerifyResponse rs = result.body();
                        if (rs != null) {
                            if (rs.getSuccess()) {
                                new LinkPolicyTask(getActivity(), Policy, PoID, DoB).execute();
                            } else {
                                MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                        .setMessage(getString(R.string.alert_message_verify_captcha))
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
        }.execute();
    }

    /**
     * Task link to Policy exist
     */
    public class LinkPolicyTask extends AsyncTask<Void, Void, Response<loginNewResponse>> {

        String policyNo, PoID, DoB;
        Context context;
        ClientProfile user;

        public LinkPolicyTask(Context c, String pol, String poid, String dob) {
            this.context = c;
            this.policyNo = pol;
            this.PoID = poid;
            this.DoB = dob;
            user = CustomPref.getUserLogin(getActivity());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swRefresh.setRefreshing(true);
        }

        @Override
        protected Response<loginNewResponse> doInBackground(Void... voids) {
            Response<loginNewResponse> res = null;

            try {
                loginNewRequest data = new loginNewRequest();

                if (!TextUtils.isEmpty(user.getClientID()))
                    data.setUserLogin(user.getClientID());
                else
                    data.setUserLogin(user.getLoginName());

                data.setFullName(user.getFullName());
                data.setLinkFB(user.getLinkFaceBook());
                data.setLinkGMail(user.getLinkGmail());

                if (policyNo.contains("UL") || policyNo.contains("VE") || policyNo.contains("IL"))
                    data.setProposalNo(policyNo);
                else
                    data.setPolicyNo(policyNo);
                data.setPOID(PoID);

                try {
                    SimpleDateFormat formattoDate = new SimpleDateFormat("dd/mm/yyyy");
                    Date date = formattoDate.parse(DoB);

                    SimpleDateFormat formattoStr = new SimpleDateFormat("yyyy-mm-dd");
                    String toString = formattoStr.format(date);

                    data.setPODOB(toString);
                } catch (ParseException e) {
                    myLog.printTrace(e);
                    data.setPODOB(DoB);
                }

                data.setApiToken(user.getaPIToken());
                data.setDeviceID(Utilities.getDeviceID(getActivity()));
                data.setOS(Utilities.getDeviceOS());
                data.setDeviceToken(CustomPref.getFirebaseToken(getActivity()));
                data.setProject(Constant.Project_ID);
                data.setAction(Constant.LOGIN_ACTION_LINKCLIENTID);
                data.setAuthentication(Constant.Project_Authentication);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<loginNewResponse> call = svRequester.CPRegisterAccount(request);
                res = call.execute();
            } catch (Exception e) {
                myLog.printTrace(e);
            }
            return res;
        }

        @Override
        protected void onPostExecute(Response<loginNewResponse> res) {
            super.onPostExecute(res);
            swRefresh.setRefreshing(false);

            try {
                if (res.isSuccessful()) {
                    loginNewResponse response = res.body();
                    if (response != null)
                        if (response.getResponse() != null) {
                            loginNewResult result = response.getResponse();
                            if (result != null) {

                                if (result.getResult() != null && result.getResult().equals("false")) {

                                    if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                        Utilities.processLoginAgain(getActivity(), getString(R.string.message_alert_relogin));
                                    }

                                } else if (result.getResult() != null && result.getResult().equals("true")) {

                                    if (result.getErrLog().equals(Constant.ERR_CPLOGIN_LINKACCFAIL)) {

                                        tvError.setText("Số yêu cầu bảo hiểm chưa chính xác, xin vui lòng kiểm tra lại.");

                                    } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CLINOEXIST)) {

                                        tvError.setText("Tài khoản này không tồn tại, xin vui lòng nhập lại chính xác số yêu cầu bảo hiểm, mã khách hàng và ngày sinh.");

                                    } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_LINKACCSUCC)) {

                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            user.setaPIToken(result.getNewAPIToken());

                                        ClientProfile userNew = result.getClientProfile().get(0);
                                        if (userNew != null) {
                                            user.setClientID(userNew.getClientID());

                                            user.setDOB(userNew.getDOB());
                                            user.setPoDOB(userNew.getDOB());

                                            user.setPOID(userNew.getPOID());
                                            user.setFullName(userNew.getFullName());
                                            user.setGender(userNew.getGender());
                                            user.setAddress(userNew.getAddress());

                                            //Save Login Profile & Token
                                            CustomPref.setLogin(getActivity(), true);
                                            CustomPref.saveUserLogin(getActivity(), user);
                                        }

                                        MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                .setMessage("Hợp đồng đã được liên kết thành công.")
                                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                        InfoContractFragment fragment = new InfoContractFragment();
                                                        onAddFragment.onFragmentAddstackListener(DashboardActivity.TAB_CONTRACT, fragment, false);
                                                    }
                                                }).create();
                                        dialog.show();
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
