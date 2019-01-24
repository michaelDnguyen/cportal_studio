package com.dlvn.mcustomerportal.afragment.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import retrofit2.Call;
import retrofit2.Response;

public class InputOTPFragment extends Fragment {

    private static final String TAG = "InputOTPFragment";

    private static final String KEY_VALID = "KEY-VALID";
    public static final int VALID_OTP_EMAIL = 88;
    public static final int VALID_OTP_PHONE = 90;

    View v;
    SwipeRefreshLayout swRefresh;

    PinLockView mPinLockView;
    IndicatorDots mIndicatorDots;
    TextView tvRefeshPin, tvCountdown;
    Button btnOpenMail;

    CountDownTimer timer;
    ServicesRequest svRequester;
    int validCode = 0;
    boolean isCallRepin = false;
    ClientProfile currentUser;

    public InputOTPFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InputOTPFragment newInstance(int valid, ClientProfile user) {

        InputOTPFragment fragment = new InputOTPFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_VALID, valid);
        bundle.putParcelable(Constant.INTENT_USER_DATA, user);
        fragment.setArguments(bundle);
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
            validCode = bundle.getInt(KEY_VALID);
        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_input_otp, container, false);

            initViews(v);
            setListener();
        }
        return v;
    }

    private void initViews(View v) {
        swRefresh = v.findViewById(R.id.swRefresh);

        mPinLockView = v.findViewById(R.id.pin_lock_view);
        mIndicatorDots = v.findViewById(R.id.indicator_dots);
        tvRefeshPin = v.findViewById(R.id.tvRefeshPin);
        tvCountdown = v.findViewById(R.id.tvCountdown);
        btnOpenMail = v.findViewById(R.id.btnOpenMail);

        mPinLockView.attachIndicatorDots(mIndicatorDots);

        mPinLockView.setPinLength(Constant.OTP_LENGTH);

        mPinLockView.setTextColor(ContextCompat.getColor(getActivity(), R.color.grey_dark));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

        timer = new CountDownTimer(Constant.TIMER_COUNTDOWN_OTP, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountdown.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvCountdown.setText("0");
                isCallRepin = true;
            }
        };

        if (currentUser == null)
            currentUser = CustomPref.getUserLogin(getActivity());

        if (validCode != CustomPref.getTypeGenerateOTP(getActivity()))
            CustomPref.setTimeGenerateOTP(getActivity(), 0);

        if (CustomPref.getTimeGenerateOTP(getActivity()) > 0) {
            long timeOld = CustomPref.getTimeGenerateOTP(getActivity());
            long timeCurrent = System.currentTimeMillis();
            myLog.e(TAG, "distance Time " + (timeCurrent - timeOld));

            if ((timeCurrent - timeOld) > Constant.TIMER_COUNTDOWN_OTP)
                new CheckOTPTask(getActivity(), currentUser, Constant.LOGIN_ACTION_GENERATEOTP, null).execute();
            else {
                long timeCon = Constant.TIMER_COUNTDOWN_OTP - (timeCurrent - timeOld);
                timer = new CountDownTimer(timeCon, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tvCountdown.setText("" + millisUntilFinished / 1000);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        tvCountdown.setText("0");
                        isCallRepin = true;
                    }
                };
                timer.start();
            }
        } else
            new CheckOTPTask(getActivity(), currentUser, Constant.LOGIN_ACTION_GENERATEOTP, null).execute();
    }

    private void setListener() {

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                myLog.e(TAG, "OTP dialog pin = " + pin);
                if (pin.length() == Constant.OTP_LENGTH) {
                    if (!tvCountdown.getText().toString().equalsIgnoreCase("0"))
                        new CheckOTPTask(getActivity(), currentUser, Constant.LOGIN_ACTION_CHECKOTP, pin).execute();
                    else {
                        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
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
                if (isCallRepin)
                    new CheckOTPTask(getActivity(), currentUser, Constant.LOGIN_ACTION_GENERATEOTP, null).execute();
                else {
//                    MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
//                            .setMessage("Mã Pin đã được gửi lại vào số điện thoại của quý khách. Xin vui lòng kiểm tra lại.")
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
                Utilities.actionOpenMailApp(getActivity());
            }
        });

    }

    /**
     * Task link to Policy exist
     */
    public class CheckOTPTask extends AsyncTask<Void, Void, Response<loginNewResponse>> {

        Context context;
        ClientProfile user;
        String otp, action;

        public CheckOTPTask(Context c, ClientProfile user, String act, String pin) {
            this.context = c;
            this.user = user;
            otp = pin;
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

                data.setClientID(user.getClientID());
                if (!TextUtils.isEmpty(user.getLoginName()))
                    data.setUserLogin(user.getLoginName());
                else
                    data.setUserLogin(user.getClientID());

                data.setFullName(user.getFullName());
                data.setLinkFB(user.getLinkFaceBook());
                data.setLinkGMail(user.getLinkGmail());

                data.setOtp(otp);
                if (action.equalsIgnoreCase(Constant.LOGIN_ACTION_VERYFYCELLPHONE))
                    data.setCellPhone(CustomPref.getPhoneNumber(getActivity()));
                else if (action.equalsIgnoreCase(Constant.LOGIN_ACTION_VERYFYEMAIL))
                    if (!TextUtils.isEmpty(CustomPref.getEmail(getActivity())))
                        data.setEmail(CustomPref.getEmail(getActivity()).trim());
                    else if (action.equalsIgnoreCase(Constant.LOGIN_ACTION_GENERATEOTP)) {
                        if (validCode == VALID_OTP_EMAIL) {
                            if (!TextUtils.isEmpty(CustomPref.getEmail(getActivity())))
                                data.setEmail(CustomPref.getEmail(getActivity()).trim());
                        } else if (validCode == VALID_OTP_PHONE)
                            data.setCellPhone(CustomPref.getPhoneNumber(getActivity()));
                    }

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

                                    if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_OTPEXPIRY)) {

                                        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                        builder.setMessage("Mã Pin đã hết hạn, xin vui lòng nhấn nút Gửi lại mã Pin và thử lại.")
                                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builder.create().show();

                                    } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_OTPINCORRECT)) {

                                        MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                .setMessage("Bạn nhập sai mã Pin. Xin vui lòng thử lại")
                                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).create();
                                        dialog.show();
                                    } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_SUCCESSFUL)) {

                                        if (action.equalsIgnoreCase(Constant.LOGIN_ACTION_GENERATEOTP)) {

                                            try {
                                                isCallRepin = false;
                                                CustomPref.setTimeGenerateOTP(context, System.currentTimeMillis());
                                                CustomPref.setTypeGenerateOTP(context, validCode);
                                                timer.start();
                                            } catch (Exception e) {
                                                myLog.printTrace(e);
                                            }
                                            //OTP re-generate
                                            if (validCode == VALID_OTP_PHONE) {
                                                MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                        .setMessage("Mã Pin đã được gửi lại vào số điện thoại của quý khách.")
                                                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        }).create();
                                                dialog.show();
                                            } else if (validCode == VALID_OTP_EMAIL) {
                                                MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                        .setMessage("Mã Pin đã được gửi lại vào email của quý khách.")
                                                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        }).create();
                                                dialog.show();
                                            }

                                        } else if (action.equalsIgnoreCase(Constant.LOGIN_ACTION_CHECKOTP)) {

                                            //OTP successful
                                            if (validCode == VALID_OTP_PHONE) {
                                                CustomPref.setVerifyCellphone(getActivity(), "1");
                                                new CheckOTPTask(getActivity(), currentUser, Constant.LOGIN_ACTION_VERYFYCELLPHONE, null).execute();
                                            } else if (validCode == VALID_OTP_EMAIL) {
                                                CustomPref.setVerifyEmail(getActivity(), "1");
                                                new CheckOTPTask(getActivity(), currentUser, Constant.LOGIN_ACTION_VERYFYEMAIL, null).execute();
                                            }

                                        } else if (action.equalsIgnoreCase(Constant.LOGIN_ACTION_VERYFYCELLPHONE)) {

                                            if (result.getClientProfile() != null) {

                                                ClientProfile user = result.getClientProfile().get(0);

                                                if (TextUtils.isEmpty(user.getaPIToken()))
                                                    user.setaPIToken(result.getNewAPIToken());

                                                //Save Login Profile & Token
                                                CustomPref.saveUserLogin(getActivity(), user);
                                            }
                                        }
                                    } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_CHANGESSUCC)) {

                                        if (validCode == VALID_OTP_PHONE) {
                                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                    .setMessage("Anh/chị đã xác thực số điện thoại thành công.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                            SettingSecurityFragment fragment = new SettingSecurityFragment();
                                                            fragmentTransaction.replace(R.id.main_container, fragment);
                                                            fragmentTransaction.commit();

                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();
                                        } else if (validCode == VALID_OTP_EMAIL) {
                                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                    .setMessage("Anh/chị đã xác thực email thành công.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                            SettingSecurityFragment fragment = new SettingSecurityFragment();
                                                            fragmentTransaction.replace(R.id.main_container, fragment);
                                                            fragmentTransaction.commit();

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

                            } else {

                            }
                        }
                    }
                }
            }
        }.execute();
    }

}
