package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.activity.prototype.LoginMainActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.NetworkUtils;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.pinlock.IndicatorDots;
import com.dlvn.mcustomerportal.view.pinlock.PinLockListener;
import com.dlvn.mcustomerportal.view.pinlock.PinLockView;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginInputPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginInputPasswordFragment extends Fragment {

    private static final String TAG = "LoginInputPasswordFragment";
    public static final String USER = "user_login";

    private View v;
    private LinearLayout lloBack;
    private EditText edtPassword;
    private CheckedTextView chbShowPassword;
    private TextView tvForgotPassword;
    private ProgressDialog mProgressDialog;

    ClientProfile currentUser = null;
    boolean isRetryPassword = false;
    ServicesRequest svRequester;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    public LoginInputPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginInputPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginInputPasswordFragment newInstance(ClientProfile user) {
        LoginInputPasswordFragment fragment = new LoginInputPasswordFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUser = getArguments().getParcelable(USER);
        }
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_login_step2, container, false);

            edtPassword = v.findViewById(R.id.password);
            edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = (Button) v.findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            chbShowPassword = (CheckedTextView) v.findViewById(R.id.chbShowPassword);
            chbShowPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chbShowPassword.isChecked()) {
                        chbShowPassword.setChecked(false);
                        edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    } else {
                        chbShowPassword.setChecked(true);
                        edtPassword.setTransformationMethod(null);
                    }
                }
            });

            lloBack = v.findViewById(R.id.lloBack);
            lloBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });

            tvForgotPassword = (TextView) v.findViewById(R.id.tvForgotPassword);
            tvForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAuthTask != null)
                        return;


                    if (NetworkUtils.isConnectedHaveDialog(getActivity())) {
                        mAuthTask = new UserLoginTask(currentUser, Constant.LOGIN_ACTION_FORGOTPASSWORD);
                        mAuthTask.execute((Void) null);
                    }
                }
            });
        }
        return v;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        edtPassword.setError(null);

        // Store values at the time of the login attempt.
        String password = edtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (isRetryPassword) {
            if (TextUtils.isEmpty(password)) {
                edtPassword.setError(getString(R.string.error_invalid_password));
                focusView = edtPassword;
                cancel = true;
            }
        } else {
            if (TextUtils.isEmpty(password)) {
                edtPassword.setError(getString(R.string.error_invalid_password));
                focusView = edtPassword;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            currentUser.setPassword(password);
            mAuthTask = new UserLoginTask(currentUser);

            if (NetworkUtils.isConnectedHaveDialog(getActivity()))
                mAuthTask.execute((Void) null);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Response<loginNewResponse>> {

        ClientProfile user;
        String acction;
        String pin = null;
        AlertDialog alertDialog = null;

        UserLoginTask(ClientProfile u) {
            user = u;
            acction = Constant.LOGIN_ACTION_CPLOGIN;
        }

        UserLoginTask(ClientProfile u, String Acc) {
            user = u;
            acction = Acc;
        }

        UserLoginTask(ClientProfile u, String Acc, String pin, AlertDialog alert) {
            user = u;
            acction = Acc;
            this.pin = pin;
            alertDialog = alert;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Response<loginNewResponse> doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Response<loginNewResponse> result = null;
            try {

                loginNewRequest data = new loginNewRequest();
//                if (!TextUtils.isEmpty(user.getClientID()))
//                    data.setUserLogin(user.getClientID());
//                else
                data.setClientID(user.getClientID());
                data.setUserLogin(user.getLoginName());

                data.setPassword(user.getPassword());

                data.setDeviceID(Utilities.getDeviceID(getActivity()));
                data.setOS(Utilities.getDeviceOS());
                data.setDeviceToken(CustomPref.getFirebaseToken(getActivity()));
                data.setProject(Constant.Project_ID);
                data.setAction(acction);
                data.setAuthentication(Constant.Project_Authentication);

                if (pin != null)
                    data.setOtp(pin);

                BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<loginNewResponse> call = svRequester.CPRegisterAccount(request);
                result = call.execute();

            } catch (Exception e) {
                myLog.printTrace(e);
                return null;
            }

            // TODO: register the new account here.
            return result;
        }

        @Override
        protected void onPostExecute(final Response<loginNewResponse> success) {
            mAuthTask = null;
            hideProgressDialog();

            if (success != null) {
                try {
                    if (success.isSuccessful()) {
                        loginNewResponse response = success.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                loginNewResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {
                                        //If account not exits --> link to register
                                        Toast.makeText(getActivity(), "Đăng nhập lỗi: " + result.getErrLog(), Toast.LENGTH_LONG).show();

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CLINOEXIST)) {

                                            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                            builder.setMessage(getString(R.string.message_error_username_login))
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.create().show();
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CLIEXISTNOACTIVE)) {
                                            showDialogInputOTP(getActivity());

                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_OTPEXPIRY)) {

                                            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                            builder.setMessage("Mã Pin đã hết hạn, xin vui lòng nhấn nút Gửi lại mã Pin và thử lại.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.create().show();

                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_SUCCESSFUL)) {

                                            alertDialog.dismiss();
                                            Intent intent = new Intent(getActivity(), LoginMainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            getActivity().finish();
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
                                        } else if (result.getErrLog().equalsIgnoreCase("SUCC")) {
                                            //OTP re-generate
                                            Toast.makeText(getActivity(), "Mã Pin đã được gửi lại vào địa chỉ email của quý khách.", Toast.LENGTH_LONG).show();

                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_FORGOTPASSWORD)) {

                                            isRetryPassword = true;
                                            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                            builder.setTitle(getString(R.string.forgotpassword_title)).setMessage(getString(R.string.forgotpassword_message))
                                                    .setPositiveButton(getString(R.string.forgotpassword_button), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            Utilities.actionOpenMailApp(getActivity());
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.create().show();

                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_WRONGPASS)) {

                                            edtPassword.setError(getString(R.string.error_incorrect_password));
                                            edtPassword.requestFocus();
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CHANGEPASS)) {

                                            if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                                CustomPref.saveAPIToken(getActivity(), result.getNewAPIToken());

                                            //If exist profile, save profile
                                            if (result.getClientProfile() != null) {
                                                //get user profile
                                                ClientProfile user = result.getClientProfile().get(0);
                                                if (TextUtils.isEmpty(user.getaPIToken()))
                                                    user.setaPIToken(result.getNewAPIToken());

                                                //save user profile
                                                CustomPref.saveUserLogin(getActivity(), user);
                                                currentUser = user;
                                            }

                                            LoginChangePasswordFragment fragment = LoginChangePasswordFragment.newInstance(currentUser);
                                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                            transaction.add(R.id.main_container, fragment);
                                            transaction.addToBackStack(fragment.getClass().getName());
                                            transaction.commit();

                                        } else if (result.getClientProfile() != null) {

                                            //Save info client profile
                                            CustomPref.setLogin(getActivity(), true);

                                            //get user profile
                                            ClientProfile user = result.getClientProfile().get(0);
                                            if (TextUtils.isEmpty(user.getaPIToken()))
                                                user.setaPIToken(result.getNewAPIToken());

                                            //check xem user login này có phải là user login trước đó ko
                                            if (CustomPref.getUserLogin(getActivity()) != null) {
                                                if (!user.getClientID().equalsIgnoreCase(CustomPref.getUserLogin(getActivity()).getClientID())) {
                                                    myLog.e(TAG, "User Other login: old = " + CustomPref.getUserLogin(getActivity()).getClientID() + " ** new = " + user.getClientID());
                                                    CustomPref.clearUserLogin(getActivity());
                                                    CustomPref.setAuthFinger(getActivity(), false);
                                                    Utilities.deleteAllFileInFolder(getActivity().getExternalFilesDir(null).getAbsolutePath());
                                                }
                                            }

                                            //save user profile
                                            CustomPref.saveUserLogin(getActivity(), user);

                                            Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            } else {
                edtPassword.setError(getString(R.string.error_incorrect_password));
                edtPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            hideProgressDialog();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Đang kiểm tra...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        if (!getActivity().isFinishing())
            mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (!getActivity().isFinishing())
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
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

        PinLockView mPinLockView;
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

        final CountDownTimer timer = new CountDownTimer(Constant.TIMER_COUNTDOWN_OTP, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountdown.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                tvCountdown.setText("0");
            }

        };
        timer.start();

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                myLog.e(TAG, "OTP dialog pin = " + pin);
                if (pin.length() == Constant.OTP_LENGTH) {
                    if (!tvCountdown.getText().toString().equalsIgnoreCase("0"))
//                        doVerifyOTP(RegisterActivity.this, user, Constant.LOGIN_ACTION_CHECKOTP, pin, dialog);
                        new UserLoginTask(currentUser, Constant.LOGIN_ACTION_CHECKOTP, pin, dialog).execute();
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
                } else {
                    MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
                    builder.setMessage("Mã OTP phải bao gồm 6 số.")
                            .setPositiveButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                }
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
//                doVerifyOTP(RegisterActivity.this, user, Constant.LOGIN_ACTION_GENERATEOTP, null, dialog);
                new UserLoginTask(currentUser, Constant.LOGIN_ACTION_GENERATEOTP, null, dialog).execute();
                try {
                    Thread.sleep(3000);
                    timer.start();
                } catch (InterruptedException e) {
                    myLog.printTrace(e);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
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
