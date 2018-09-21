package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
 * Use the {@link LoginInputPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginInputPasswordFragment extends Fragment {

    public static final String USER = "user_login";

    private OnFragmentInteractionListener mListener;

    private View v;
    private LinearLayout lloBack;
    private EditText mPasswordView;
    private CheckedTextView chbShowPassword;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvForgotPassword;
    private ProgressDialog mProgressDialog;

    ClientProfile currentUser = null;
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

            mPasswordView = (EditText) v.findViewById(R.id.password);
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

            Button mEmailSignInButton = (Button) v.findViewById(R.id.email_sign_in_button);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

            mLoginFormView = v.findViewById(R.id.login_form);
            mProgressView = v.findViewById(R.id.login_progress);

            chbShowPassword = (CheckedTextView) v.findViewById(R.id.chbShowPassword);
            chbShowPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chbShowPassword.isChecked()) {
                        chbShowPassword.setChecked(false);
                        mPasswordView.setTransformationMethod(new PasswordTransformationMethod());
                    } else {
                        chbShowPassword.setChecked(true);
                        mPasswordView.setTransformationMethod(null);
                    }
                }
            });

            lloBack = (LinearLayout) v.findViewById(R.id.lloBack);
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
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) | !Utilities.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
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
                data.setPassword(user.getPassword());

                data.setDeviceID(Utilities.getDeviceID(getActivity()));
                data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
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

                                        if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                            currentUser.setaPIToken(result.getNewAPIToken());

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
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_FORGOTPASSWORD)) {

                                            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                            builder.setTitle(getString(R.string.forgotpassword_title)).setMessage(String.format(getString(R.string.forgotpassword_message), user.getEmail()))
                                                    .setPositiveButton(getString(R.string.forgotpassword_button), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            Utilities.actionOpenMailApp(getActivity());
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.create().show();

                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_WRONGPASS)) {

                                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                                            mPasswordView.requestFocus();
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CHANGEPASS)) {

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

                                            //save user profile
                                            CustomPref.saveUserLogin(getActivity(), user);

                                            Intent intent = new Intent(getActivity(), DashboardActivity.class);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        mListener = null;
    }

}
