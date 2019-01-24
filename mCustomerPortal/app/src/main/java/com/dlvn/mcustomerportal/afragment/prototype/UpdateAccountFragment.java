package com.dlvn.mcustomerportal.afragment.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.GoogleServiceGenerator;
import com.dlvn.mcustomerportal.services.GoogleServiceRequest;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.SiteVerifyResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.Utilities;
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

import retrofit2.Call;
import retrofit2.Response;

public class UpdateAccountFragment extends Fragment {

    private static final String TAG = "UpdateAccountFragment";

    private static final String USER_DATA = "User-Data";
    private static final String SHOW_SETTINGLINK = "Show-SettingLink";

    View v;
    SwipeRefreshLayout swRefresh;
    Button btnCapNhat;
    ClearableEditText cedtFullName, cedtEmail, cedtPhone, cedtAddress;
    RadioButton rdbNam, rdbNu;

    ServicesRequest svRequester;
    ClientProfile currentUser;
    boolean isShowSettingLink = false;

    public UpdateAccountFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UpdateAccountFragment newInstance(ClientProfile profile, boolean showLink) {
        UpdateAccountFragment fragment = new UpdateAccountFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_DATA, profile);
        args.putBoolean(SHOW_SETTINGLINK, showLink);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(Constant.INTENT_USER_DATA)) {
                currentUser = bundle.getParcelable(Constant.INTENT_USER_DATA);
                myLog.e(TAG, "have User Data");
            }
            if (bundle.containsKey(SHOW_SETTINGLINK)) {
                isShowSettingLink = bundle.getBoolean(SHOW_SETTINGLINK);
                myLog.e(TAG, "Dont show setting Link");
            }
        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_update_account, container, false);

            initViews(v);
            initData();
            setListener();
        }
        return v;
    }

    private void initViews(View v) {
//        lloBack = v.findViewById(R.id.lloBack);
//        LinearLayout lloHeader = getActivity().findViewById(R.id.lloHeader);
//        if (lloHeader != null) {
//            lloHeader.setVisibility(View.GONE);
//        }

        swRefresh = v.findViewById(R.id.swRefresh);
        btnCapNhat = v.findViewById(R.id.btnUpdate);

        cedtFullName = v.findViewById(R.id.cedtFullName);
        cedtEmail = v.findViewById(R.id.cedtEmail);
        cedtPhone = v.findViewById(R.id.cedtPhone);
        cedtAddress = v.findViewById(R.id.cedtAddress);

        rdbNam = v.findViewById(R.id.rdbNam);
        rdbNu = v.findViewById(R.id.rdbNu);

        //hide edit button
        TextView tvEdit = getActivity().findViewById(R.id.tvEdit);
        if (tvEdit != null)
            tvEdit.setVisibility(View.GONE);

        if (currentUser == null)
            currentUser = CustomPref.getUserLogin(getActivity());
    }

    private void initData() {
        if (currentUser != null) {
            cedtFullName.setText(currentUser.getFullName());
            cedtEmail.setText(currentUser.getEmail());
            cedtPhone.setText(currentUser.getCellPhone());
            cedtAddress.setText(currentUser.getAddress());

            if (currentUser.getGender().equals("F"))
                rdbNu.setChecked(true);
            else
                rdbNam.setChecked(true);
        }
    }

    private void setListener() {
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                builder.setMessage("Anh/chị có đồng ý cập nhật thông tin tài khoản là thông tin đã hiển thị hay không.")
                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                currentUser.setFullName(cedtFullName.getText().toString());
                                currentUser.setEmail(cedtEmail.getText().toString());
                                currentUser.setCellPhone(cedtPhone.getText().toString());
                                currentUser.setAddress(cedtAddress.getText().toString());
                                if (rdbNam.isChecked())
                                    currentUser.setGender("M");
                                else
                                    currentUser.setGender("F");

                                new UpdateAccountTask(getActivity(), currentUser, Constant.LOGIN_ACTION_UPDATEACCOUNT).execute();

                                dialog.dismiss();
                            }
                        }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

    /**
     * Task link to Policy exist
     */
    public class UpdateAccountTask extends AsyncTask<Void, Void, Response<loginNewResponse>> {

        Context context;
        ClientProfile user;
        String otp = null, action;

        public UpdateAccountTask(Context c, ClientProfile user, String act) {
            this.context = c;
            this.user = user;
            action = act;
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
                data.setGender(user.getGender());
                data.setEmail(user.getEmail());
                data.setCellPhone(user.getCellPhone());
                data.setAddress(user.getAddress());

                data.setLinkFB(user.getLinkFaceBook());
                data.setLinkGMail(user.getLinkGmail());

                data.setOtp(otp);
                if (action.equalsIgnoreCase(Constant.LOGIN_ACTION_VERYFYCELLPHONE))
                    data.setCellPhone(CustomPref.getPhoneNumber(getActivity()));

                data.setApiToken(user.getaPIToken());

                data.setDeviceID(Utilities.getDeviceID(getActivity()));
                data.setOS(Utilities.getDeviceOS());
                data.setDeviceToken(CustomPref.getFirebaseToken(getActivity()));
                data.setProject(Constant.Project_ID);
                data.setAction(action);
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

                                } else if (result.getResult() != null && result.getResult().equalsIgnoreCase("true")) {

                                    if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_CHANGESSUCC)) {

                                        //save info just update
                                        CustomPref.saveUserLogin(getActivity(), user);

                                        if (result.getClientProfile() != null) {
                                            ClientProfile user = result.getClientProfile().get(0);

                                            if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                                user.setaPIToken(result.getNewAPIToken());

                                            //Save Login Profile & Token
                                            CustomPref.saveUserLogin(getActivity(), user);
                                        }

                                        MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                .setMessage("Anh/chị đã cập nhật thông tin thành công!")
                                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        if (isShowSettingLink) {
                                                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                            SettingLinkFragment fragment = new SettingLinkFragment();
                                                            ft.replace(R.id.main_container, fragment);
                                                            ft.commit();
                                                        } else {
                                                            getActivity().onBackPressed();
                                                        }
                                                        dialog.dismiss();

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

    private void verifyWithReCaptcha() {
        myLog.e(TAG, "verifyWithReCaptcha");
        SafetyNet.getClient(getActivity()).verifyWithRecaptcha(getString(R.string.api_sitekey_recaptcha))
                .addOnSuccessListener(getActivity(), new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            myLog.e(TAG, "Token result " + response.getTokenResult());
                            getSiteVerifyReCaptCha(getActivity(), response.getTokenResult());
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

    private void getSiteVerifyReCaptCha(final Context c, final String responseCaptcha) {
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
//                                isCaptcha = true;
//                                ibtnReCapcha.setImageResource(R.drawable.img_captch_success);
                            } else {

                            }
                        }
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            onAddFragment = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
