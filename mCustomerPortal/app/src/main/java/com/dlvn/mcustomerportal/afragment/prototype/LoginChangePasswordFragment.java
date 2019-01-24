package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
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

import retrofit2.Call;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginChangePasswordFragment extends Fragment {

    public static final String USER = "user_login";

    private View v;
    private LinearLayout lloBack;
    private EditText mPasswordView, mConfirmPassword;
    private CheckedTextView chbShowPassword;
    private ProgressDialog mProgressDialog;

    ClientProfile currentUser = null;
    ServicesRequest svRequester;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    public LoginChangePasswordFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginChangePasswordFragment newInstance(ClientProfile user) {
        LoginChangePasswordFragment fragment = new LoginChangePasswordFragment();
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
            v = inflater.inflate(R.layout.fragment_login_step3, container, false);

            mPasswordView = v.findViewById(R.id.password);
            mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });
            mConfirmPassword = v.findViewById(R.id.confirm_password);
            mConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        attemptLogin();
                        return true;
                    }
                    return false;
                }
            });

            Button mEmailSignInButton = v.findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });


            chbShowPassword = v.findViewById(R.id.chbShowPassword);
            chbShowPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chbShowPassword.isChecked()) {
                        chbShowPassword.setChecked(false);
                        mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
                        mConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    } else {
                        chbShowPassword.setChecked(true);
                        mPasswordView.setTransformationMethod(null);
                        mConfirmPassword.setTransformationMethod(null);
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
        mPasswordView.setError(null);
        mConfirmPassword.setError(null);

        // Store values at the time of the login attempt.
        String password = mPasswordView.getText().toString();
        String confirmpassword = mConfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !Utilities.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!TextUtils.isEmpty(confirmpassword) && !Utilities.isPasswordValid(confirmpassword)) {
            mConfirmPassword.setError(getString(R.string.error_invalid_password));
            focusView = mConfirmPassword;
            cancel = true;
        }

        if (!password.equals(confirmpassword)) {
            mConfirmPassword.setError(getString(R.string.error_invalid_password_change));
            focusView = mConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            currentUser.setPassword(password);

            if (NetworkUtils.isConnectedHaveDialog(getActivity())) {
                mAuthTask = new UserLoginTask(currentUser, Constant.LOGIN_ACTION_CHANGEPASSWORD);
                mAuthTask.execute((Void) null);
            }
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Response<loginNewResponse>> {

        ClientProfile user;
        String acction;

        UserLoginTask(ClientProfile u) {
            user = u;
            acction = Constant.LOGIN_ACTION_CPLOGIN;
        }

        UserLoginTask(ClientProfile u, String Acc) {
            user = u;
            acction = Acc;
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

                data.setUserLogin(user.getLoginName());
                data.setClientID(user.getClientID());
                data.setPassword(user.getPassword());

                data.setApiToken(CustomPref.getAPIToken(getActivity()));
                data.setDeviceID(Utilities.getDeviceID(getActivity()));
                data.setOS(Utilities.getDeviceOS());
                data.setDeviceToken(CustomPref.getFirebaseToken(getActivity()));
                data.setProject(Constant.Project_ID);
                data.setAction(acction);
                data.setAuthentication(Constant.Project_Authentication);

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
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CHANGEPASSFAIL)) {

                                            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                            builder.setMessage(getString(R.string.error_changepass_fail))
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.create().show();

                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CHANGEPASSSUCC)) {

                                            //Save Login Profile & Token
                                            CustomPref.setLogin(getActivity(), true);

                                            try {
                                                if (result.getClientProfile() != null) {
                                                    ClientProfile user = result.getClientProfile().get(0);

                                                    if (TextUtils.isEmpty(user.getaPIToken()))
                                                        user.setaPIToken(result.getNewAPIToken());
                                                    CustomPref.saveUserLogin(getActivity(), user);
                                                }
                                            } catch (Exception e) {
                                                myLog.printTrace(e);
                                            }

                                            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                            builder.setMessage("Anh/chị đã khôi phục mật khẩu thành công.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();

                                                            Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            getActivity().finish();
                                                        }
                                                    });
                                            builder.create().show();
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
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
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
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
