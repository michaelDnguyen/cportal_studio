package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.GoogleServiceGenerator;
import com.dlvn.mcustomerportal.services.GoogleServiceRequest;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.CPSubmitFormRequest;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResponse;
import com.dlvn.mcustomerportal.services.model.response.CPSubmitFormResult;
import com.dlvn.mcustomerportal.services.model.response.PaymentDetailModel;
import com.dlvn.mcustomerportal.services.model.response.SiteVerifyResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.DateUtils;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;
import com.dlvn.mcustomerportal.view.pinlock.IndicatorDots;
import com.dlvn.mcustomerportal.view.pinlock.PinLockListener;
import com.dlvn.mcustomerportal.view.pinlock.PinLockView;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlterationPOInfoFragment extends Fragment {

    private static final String TAG = "AlterationPOInfoFragment";

    private static final String KEY_ALTER_LIST = "KEY_ALTER_LIST";

    private static final String KEY_POINFO = "KEY_POINFO";
    private static final String KEY_LIINFO = "KEY_LIINFO";
    private static final String KEY_BENEINFO = "KEY_BENEINFO";
    private static final String KEY_ALTERATION_PO_CHANGE = "ALTERATION_PO_CHANGE";
    private static final String KEY_ALTERATION_LI_CHANGE = "ALTERATION_LI_CHANGE";
    private static final String KEY_ALTERATION_BENE_CHANGE = "ALTERATION_BENE_CHANGE";

    View view;
    LinearLayout lloBack;
    SwipeRefreshLayout swRefresh;
    Button btnCapNhat;
    TextView tvDoB, tvGender, tvTitle;
    ClearableEditText cedtFullName, cedtEmail, cedtPhone, cedtHomePhone, cedtWorkPhone, cedtHomeAddress, cedtWorkAddress;
    TextView tvStep;
    AppCompatSeekBar sbStep;

    boolean isCaptCha = false;
    boolean isRePin = true;
    int iStep = 0;
    ArrayList<PaymentDetailModel> lsAlter;
    ServicesRequest svRequester;

    OnFragmentInteractionListener onAddFragment;

    public static AlterationPOInfoFragment newInstance(ArrayList<PaymentDetailModel> lsArray) {
        AlterationPOInfoFragment fragment = new AlterationPOInfoFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_ALTER_LIST, lsArray);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lsAlter = getArguments().getParcelableArrayList(KEY_ALTER_LIST);
        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_alteration_poinfo, container, false);

            //init views
            getViews();
            initData();
            setListener();
        }
        return view;
    }

    private void getViews() {
        lloBack = view.findViewById(R.id.lloBack);
        btnCapNhat = view.findViewById(R.id.btnCapNhat);

        swRefresh = view.findViewById(R.id.swRefresh);
        cedtFullName = view.findViewById(R.id.cedtFullName);
        tvDoB = view.findViewById(R.id.tvDoB);
        tvGender = view.findViewById(R.id.tvGender);
        tvTitle = view.findViewById(R.id.tvTitle);

        cedtEmail = view.findViewById(R.id.cedtEmail);
        cedtPhone = view.findViewById(R.id.cedtPhone);
        cedtHomeAddress = view.findViewById(R.id.cedtHomeAddress);
        cedtWorkAddress = view.findViewById(R.id.cedtWorkAddress);
        cedtHomePhone = view.findViewById(R.id.cedtHomePhone);
        cedtWorkPhone = view.findViewById(R.id.cedtWorkPhone);

        tvStep = view.findViewById(R.id.tvStep);
        sbStep = view.findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);
        sbStep.setMax(lsAlter.size());
        iStep = 1;
    }

    private void initData() {
        //initData
        if (lsAlter != null)
            if (lsAlter.size() > 0) {

                tvStep.setText(iStep + "/" + lsAlter.size());
                sbStep.setProgress(iStep);

                cedtFullName.setText(lsAlter.get(iStep - 1).getTenKhachHang());
                tvGender.setText(lsAlter.get(iStep - 1).getGender());
                if (!TextUtils.isEmpty(lsAlter.get(iStep - 1).getDob()))
                    tvDoB.setText(DateUtils.parseDateForMCP(lsAlter.get(iStep - 1).getDob().trim()));

                cedtEmail.setText(lsAlter.get(iStep - 1).getEmail());
                if (!TextUtils.isEmpty(lsAlter.get(iStep - 1).getPhone()))
                    cedtPhone.setText(lsAlter.get(iStep - 1).getPhone().trim());

                if (!TextUtils.isEmpty(lsAlter.get(iStep - 1).getPhoneHome()))
                    cedtHomePhone.setText(lsAlter.get(iStep - 1).getPhoneHome().trim());

                if (!TextUtils.isEmpty(lsAlter.get(iStep - 1).getPhoneWork()))
                    cedtWorkPhone.setText(lsAlter.get(iStep - 1).getPhoneWork().trim());

                if (!TextUtils.isEmpty(lsAlter.get(iStep - 1).getAddressHome()))
                    cedtHomeAddress.setText(lsAlter.get(iStep - 1).getAddressHome().trim());

                if (!TextUtils.isEmpty(lsAlter.get(iStep - 1).getAddressWork()))
                    cedtWorkAddress.setText(lsAlter.get(iStep - 1).getAddressWork().trim());
            }
    }

    private void setListener() {
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iStep > 1) {
                    attempActionData(false);
                } else
                    getActivity().onBackPressed();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempActionData(true);
            }
        });

        swRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swRefresh.setRefreshing(false);
            }
        });
    }

    private void attempActionData(boolean isNext) {
        View focusView = null;
        boolean invalid = true;

        cedtFullName.setError(null);
        cedtEmail.setError(null);
        cedtPhone.setError(null);
        cedtHomeAddress.setError(null);
        cedtWorkAddress.setError(null);

        String fullname = cedtFullName.getText().toString();
        String email = cedtEmail.getText().toString();
        String phone = cedtPhone.getText().toString();
        String phoneHome = cedtHomePhone.getText().toString();
        String phoneWork = cedtWorkPhone.getText().toString();
        String homeAdd = cedtHomeAddress.getText().toString();
        String busicessAdd = cedtWorkAddress.getText().toString();

        if (TextUtils.isEmpty(fullname)) {
            cedtFullName.setError(getString(R.string.error_field_required));
            focusView = cedtFullName;
            invalid = false;
        }

        if (!TextUtils.isEmpty(email))
            if (!Utilities.validateEmail(email)) {
                cedtEmail.setError(getString(R.string.error_invalid_email));
                focusView = cedtEmail;
                invalid = false;
            }

        if (!TextUtils.isEmpty(phone))
            if (!Utilities.isPhoneNumberValid(phone)) {
                cedtPhone.setError(getString(R.string.error_incorrect_phone));
                focusView = cedtPhone;
                invalid = false;
            }

        if (!invalid) {
            focusView.requestFocus();
        } else {

            lsAlter.get(iStep - 1).setTenKhachHang(fullname);
            lsAlter.get(iStep - 1).setEmail(email);
            lsAlter.get(iStep - 1).setPhone(phone);
            lsAlter.get(iStep - 1).setAddressHome(homeAdd);
            lsAlter.get(iStep - 1).setAddressWork(busicessAdd);
            lsAlter.get(iStep - 1).setPhoneHome(phoneHome);
            lsAlter.get(iStep - 1).setPhoneWork(phoneWork);

            if (!isNext)
                iStep--;

            if (NetworkUtils.isConnectedHaveDialog(getActivity())) {
                if (iStep == lsAlter.size()) {
                    if (isCaptCha)
                        verifyWithReCaptcha(getActivity());
                    else {
                        showDialogInputOTP(getActivity());

                        if (CustomPref.getTimeGenerateOTP(getActivity()) > 0) {
                            long timeOld = CustomPref.getTimeGenerateOTP(getActivity());
                            long timeCurrent = System.currentTimeMillis();
                            myLog.e(TAG, "distance Time " + (timeCurrent - timeOld));

                            if ((timeCurrent - timeOld) > Constant.TIMER_COUNTDOWN_OTP)
                                doVerifyOTPTask(getActivity(), Constant.LOGIN_ACTION_GENERATEOTP, null, null);

                        } else
                            doVerifyOTPTask(getActivity(), Constant.LOGIN_ACTION_GENERATEOTP, null, null);
                    }
                } else {
                    if (isNext)
                        iStep++;
                    initData();
                }
            }
        }
    }

    private void showDialogInputOTP(final Context context) {
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_input_otp, null);
        alerDialog.setView(view);
        final AlertDialog dialog = alerDialog.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        final PinLockView mPinLockView;
        IndicatorDots mIndicatorDots;
        final TextView tvRefeshPin, tvCountdown;
        Button btnOpenMail;

        mPinLockView = dialog.findViewById(R.id.pin_lock_view);
        mIndicatorDots = dialog.findViewById(R.id.indicator_dots);
        tvRefeshPin = dialog.findViewById(R.id.tvRefeshPin);
        tvCountdown = dialog.findViewById(R.id.tvCountdown);
        btnOpenMail = dialog.findViewById(R.id.btnOpenMail);

        mPinLockView.attachIndicatorDots(mIndicatorDots);

        mPinLockView.setPinLength(Constant.OTP_LENGTH);

        mPinLockView.setTextColor(ContextCompat.getColor(context, R.color.grey_dark));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

        long timeToCount = Constant.TIMER_COUNTDOWN_OTP;
        if (CustomPref.getTimeGenerateOTP(getActivity()) > 0) {
            long timeOld = CustomPref.getTimeGenerateOTP(getActivity());
            long timeCurrent = System.currentTimeMillis();
            myLog.e(TAG, "distance Time " + (timeCurrent - timeOld));

            if ((timeCurrent - timeOld) < Constant.TIMER_COUNTDOWN_OTP)
                timeToCount -= (timeCurrent - timeOld);
        }

        final CountDownTimer timer = new CountDownTimer(timeToCount, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountdown.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvCountdown.setText("0");
                isRePin = true;
            }
        };
        timer.start();

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                myLog.e(TAG, "OTP dialog pin = " + pin);
                if (pin.length() == Constant.OTP_LENGTH) {
                    if (!tvCountdown.getText().toString().equalsIgnoreCase("0"))
                        doVerifyOTPTask(getActivity(), Constant.LOGIN_ACTION_CHECKOTP, pin, dialog);
                    else {
                        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
                        builder.setMessage("Mã Pin đã hết hạn, xin vui lòng nhấn nút Gửi lại mã Pin và thử lại.")
                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                }
                mPinLockView.resetPinLockView();
            }

            @Override
            public void onEmpty() {
                myLog.e(TAG, " OTP dialog empty");
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                myLog.e(TAG, " OTP dialog onPinChange length = " + pinLength + " ** inter : " + intermediatePin);
            }
        });

        tvRefeshPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRePin) {
                    doVerifyOTPTask(getActivity(), Constant.LOGIN_ACTION_GENERATEOTP, null, dialog);
                    try {
                        Thread.sleep(3000);
                        timer.start();
                    } catch (InterruptedException e) {
                        myLog.printTrace(e);
                    }
                } else {
//                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
//                            .setMessage("Mã Pin đã được gửi lại vào địa chỉ email của quý khách. Xin vui lòng kiểm tra lại.")
//                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).create();
//                    dialog.show();
                }
            }
        });

        btnOpenMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.actionOpenMailApp(context);
            }
        });

        dialog.show();
    }

    private void verifyWithReCaptcha(final Context context) {
        myLog.e(TAG, "verifyWithReCaptcha");
        SafetyNet.getClient(getActivity()).verifyWithRecaptcha(getString(R.string.api_sitekey_recaptcha))
                .addOnSuccessListener(getActivity(), new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            myLog.e(TAG, "Token result " + response.getTokenResult());
                            getSiteVerifyReCaptCha(context, response.getTokenResult());
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

    private void getSiteVerifyReCaptCha(final Context context, final String responseCaptcha) {
        new AsyncTask<Void, Void, Response<SiteVerifyResponse>>() {

            @Override
            protected Response<SiteVerifyResponse> doInBackground(Void... voids) {

                GoogleServiceGenerator svgenerate = new GoogleServiceGenerator();
                GoogleServiceRequest service = svgenerate.createService(context, GoogleServiceRequest.class);

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
                                showDialogInputOTP(context);
                            } else {
                                MyCustomDialog dialog = new MyCustomDialog.Builder(context)
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

    private class doSubmitForm extends AsyncTask<Void, Void, Response<CPSubmitFormResponse>> {

        Context context;

        public doSubmitForm(Context c) {
            this.context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swRefresh.setRefreshing(true);
        }

        @Override
        protected Response<CPSubmitFormResponse> doInBackground(Void... voids) {
            Response<CPSubmitFormResponse> response = null;

            try {
                CPSubmitFormRequest data = new CPSubmitFormRequest();

                if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
                    data.setUserLogin(CustomPref.getUserName(context));
                else
                    data.setUserLogin(CustomPref.getUserID(context));
                data.setClientID(CustomPref.getUserID(context));

                data.setAPIToken(CustomPref.getAPIToken(context));
                data.setDeviceId(Utilities.getDeviceID(context));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAuthentication(Constant.Project_Authentication);

                data.setLstClient(lsAlter);

                data.setAction(Constant.ACTION_ALTERATIONFORM);

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
            swRefresh.setRefreshing(false);

            if (response != null)
                if (response.isSuccessful()) {
                    CPSubmitFormResponse rp = response.body();
                    if (rp != null) {
                        CPSubmitFormResult result = rp.getResponse();
                        if (result != null) {
                            if (result.getResult().equalsIgnoreCase("true")) {
                                MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                        .setMessage("Yêu cầu đã được gửi thành công!")
                                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).create();
                                dialog.show();

                            } else {

                                if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                    Utilities.processLoginAgain(context, getString(R.string.message_alert_relogin));
                                } else {
                                    MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                            .setMessage("Yêu cầu đã được gửi không thành công! Xin vui lòng thử lại.")
                                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    isCaptCha = true;
                                                    dialog.dismiss();
                                                }
                                            }).create();
                                    dialog.show();
                                }
                            }
                        }
                    }
                } else {
                    Utilities.showDialogErrorRequest(getActivity());
                }
        }
    }

    public void doVerifyOTPTask(final Context context, final String Action, String otp, final AlertDialog alertDialog) {

        final ProgressDialog process = new ProgressDialog(context);
        process.setMessage("Đang gửi lại pin...");
        process.setCanceledOnTouchOutside(false);
        process.show();

        loginNewRequest data = new loginNewRequest();

        if (!TextUtils.isEmpty(CustomPref.getUserName(context)))
            data.setUserLogin(CustomPref.getUserName(context));
        else
            data.setUserLogin(CustomPref.getUserID(context));
        data.setClientID(CustomPref.getUserID(context));

        data.setPassword(CustomPref.getPassword(context));
        data.setLinkGMail(CustomPref.getGoogleID(context));
        data.setLinkFB(CustomPref.getFacebookID(context));

        if (otp != null)
            data.setOtp(otp);

        data.setApiToken(CustomPref.getAPIToken(context));
        data.setDeviceID(Utilities.getDeviceID(context));
        data.setOS(Utilities.getDeviceOS());
        data.setProject(Constant.Project_ID);
        data.setAction(Action);
        data.setAuthentication(Constant.Project_Authentication);

        BaseRequest request = new BaseRequest();
        request.setJsonDataInput(data);

        Call<loginNewResponse> call = svRequester.CPRegisterAccount(request);
        call.enqueue(new Callback<loginNewResponse>() {

            @Override
            public void onResponse(Call<loginNewResponse> call, Response<loginNewResponse> res) {
                // TODO Auto-generated method stub
                process.dismiss();

                try {
                    if (res.isSuccessful()) {
                        loginNewResponse response = res.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                loginNewResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equalsIgnoreCase("false")) {

                                        // If false -> show dialog

                                    } else if (result.getResult() != null && result.getResult().equalsIgnoreCase("true")) {

                                        if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_OTPINCORRECT)) {

                                            MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                    .setMessage("Bạn nhập sai mã Pin. Xin vui lòng thử lại")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();
                                        } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_OTPEXPIRY)) {

                                            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                            builder.setMessage("Mã Pin đã hết hạn, xin vui lòng nhấn nút Gửi lại mã Pin và thử lại.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.create().show();

                                        } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_SUCCESSFUL)) {

                                            if (Action.equalsIgnoreCase(Constant.LOGIN_ACTION_CHECKOTP)) {
                                                alertDialog.dismiss();

                                                new doSubmitForm(context).execute();
                                            }

                                            if (Action.equalsIgnoreCase(Constant.LOGIN_ACTION_GENERATEOTP)) {

                                                CustomPref.setTimeGenerateOTP(context, System.currentTimeMillis());
                                                //OTP re-generate
                                                isRePin = false;
                                                MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                        .setMessage("Mã Pin đã được gửi lại vào địa chỉ email của quý khách.")
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
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }

            @Override
            public void onFailure(Call<loginNewResponse> call, Throwable t) {
                // TODO Auto-generated method stub
                myLog.e(t.getMessage());
                process.dismiss();

            }
        });
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
}
