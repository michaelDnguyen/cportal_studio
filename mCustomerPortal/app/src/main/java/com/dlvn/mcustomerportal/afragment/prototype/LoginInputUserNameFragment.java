package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.activity.prototype.RegisterActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.User;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginInputUserNameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginInputUserNameFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, GoogleApiClient.OnConnectionFailedListener {

    private static final int FLAG_SOCIAL_FACEBOOK = 1;
    private static final int FLAG_SOCIAL_GMAIL = 2;

    // UI references.
    private ClearableEditText mEmailView;
    private EditText mPasswordView;
    private TextView tvPolicy, tvCopyRight;
    private View mProgressView;
    private View mLoginFormView;
    private Button btnFacebook, btnGoogle;
    private View v;

    private ProgressDialog mProgressDialog;

    int flag_social = 0;

    //facebook login
    LoginButton btnLoginFacebook;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    Profile facebookProfile = null;
    //String array permission
    private static String[] FACEBOOK_PERMISSION = {"public_profile", "email"};

    //google
    private SignInButton btnLoginGoogle;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount googleProfile = null;
    private static final int RC_GOOGLE_SIGN_IN = 007;

    User currentUser = null;
    ServicesRequest svRequester;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private OnFragmentInteractionListener mListener;

    public LoginInputUserNameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginInputUserNameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginInputUserNameFragment newInstance(String param1, String param2) {
        LoginInputUserNameFragment fragment = new LoginInputUserNameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());
        AppEventsLogger.activateApp(getActivity());
        currentUser = new User();
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_login_step1, container, false);

            mEmailView = (ClearableEditText) v.findViewById(R.id.email);
            populateAutoComplete();

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
                    flag_social = 0;
                    attemptLogin();
                }
            });

            mLoginFormView = v.findViewById(R.id.login_form);
            mProgressView = v.findViewById(R.id.login_progress);

            //FACEBOOK
            btnLoginFacebook = (LoginButton) v.findViewById(R.id.btnLoginFacebook);

            callbackManager = CallbackManager.Factory.create();
            btnLoginFacebook.setReadPermissions(Arrays.asList(FACEBOOK_PERMISSION));

            // Callback registration
            btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    myLog.E("registerCallback onSuccess");
                    myLog.E("Access token = " + loginResult.getAccessToken().getToken());
                    myLog.E("User ID = " + loginResult.getAccessToken().getUserId());
                    myLog.E("Permission = " + loginResult.getAccessToken().getPermissions());
                    myLog.E("Expires = " + loginResult.getAccessToken().getExpires());

                    currentUser.setLinkFacebook(loginResult.getAccessToken().getUserId());
                    flag_social = FLAG_SOCIAL_FACEBOOK;

                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {

                                    myLog.E("LoginActivity", response.toString());
                                    try {

//                                        try {
//                                            URL profile_pic = new URL(
//                                                    "http://graph.facebook.com/" + id + "/picture?type=large");
//                                            myLog.E("profile_pic", profile_pic + "");
//
//                                        } catch (MalformedURLException e) {
//                                            myLog.printTrace(e);
//                                        }

                                        if (object.has("name"))
                                            currentUser.setFullname(object.getString("name"));
                                        if (object.has("email")) {
                                            currentUser.setEmail(object.getString("email"));
                                            currentUser.setUserName(object.getString("email"));
                                        }
                                        if (object.has("gender"))
                                            currentUser.setGender(object.getString("gender"));

                                        //check account with services
                                        doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
//                                        String birthday = object.getString("birthday");

                                    } catch (JSONException e) {
                                        myLog.printTrace(e);
                                    }
                                }
                            });

                    Bundle parameters = new Bundle();
                    parameters.putString(GraphRequest.FIELDS_PARAM, "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    // App code
                    myLog.E("Cancel from login with facebook");
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                    myLog.E("Error from login with facebook: ");
                    myLog.printTrace(exception);
                }
            });

            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {

                    myLog.E("onCurrentAccessTokenChanged");
                    if (oldAccessToken != null)
                        myLog.E("old token" + oldAccessToken.getToken());
                    if (currentAccessToken != null) {
                        myLog.E("current token" + currentAccessToken.getToken());
                        accessToken = currentAccessToken;
                    }
                    // Set the access token using
                    // currentAccessToken when it's loaded or set.
                }
            };

            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(
                        Profile oldProfile,
                        Profile currentProfile) {

                    myLog.E("onCurrentProfileChanged");
                    // App code
                    facebookProfile = currentProfile;
                    if (facebookProfile != null) {
                        myLog.E("profile " + currentProfile.getFirstName() + currentProfile.getMiddleName() + currentProfile.getLastName());
                        myLog.E("id " + currentProfile.getId());
                        myLog.E("name " + currentProfile.getName());
                    }
                }
            };

            btnFacebook = (Button) v.findViewById(R.id.btnFacebook);
            btnFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (accessToken != null && !accessToken.isExpired()) {
//                        if (facebookProfile != null) {
//                            currentUser.setLinkFacebook(facebookProfile.getId());
//                            currentUser.setFullname(facebookProfile.getName());
//                            doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
//                        }
//                    } else {
//                        btnLoginFacebook.performClick();
//                    }
                    currentUser.setEmail("david_jlmbmfu_nokia@tfbnw.net");
                    currentUser.setUserName("david_jlmbmfu_nokia@tfbnw.net");
                    currentUser.setLinkFacebook("108360580075160");
                    currentUser.setFullname("David Nokia");
                    flag_social = FLAG_SOCIAL_FACEBOOK;
                    doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
                }
            });

            //GOOGLE
            btnGoogle = (Button) v.findViewById(R.id.btnGoogle);
            btnGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (googleProfile != null)
//                        doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
//                    else
//                        googleSignIn();

                    //for test
                    currentUser.setEmail("jonynguyen87@gmail.com");
                    currentUser.setUserName("jonynguyen87@gmail.com");
                    currentUser.setLinkGmail("109913173948764899767");
                    currentUser.setFullname("Nguyen Jony");
                    flag_social = FLAG_SOCIAL_GMAIL;

                    doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
                }
            });

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestProfile()
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity(), this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            //setup conpany info & policy
            tvPolicy = (TextView) v.findViewById(R.id.tvPolicy);
            tvPolicy.setText(Html.fromHtml(getString(R.string.txt_policy_term)));
            tvPolicy.setMovementMethod(LinkMovementMethod.getInstance());

            tvCopyRight = (TextView) v.findViewById(R.id.tvCopyRight);
            tvCopyRight.setText(Html.fromHtml(getString(R.string.txt_copyright_daiichi)));
            tvCopyRight.setMovementMethod(LinkMovementMethod.getInstance());

        }
        return v;
    }

    private void populateAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
//        else if (Utilities.validateSpecialCharacter(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_character));
//            focusView = mEmailView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            currentUser.setUserName(email);
            if (Utilities.validateEmail(email))
                currentUser.setEmail(email);

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
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

    /**
     * method google sigin
     */
    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    /**
     * method google sigout
     */
    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        btnGoogle.setText(R.string.signin_google);
                    }
                });
    }

    /**
     * Handle sign in by google
     *
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        myLog.E("handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            googleProfile = result.getSignInAccount();

            String id = googleProfile.getId();
            String personName = googleProfile.getDisplayName();
            String email = googleProfile.getEmail();
//            String personPhotoUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : null;

            myLog.E("Name: " + personName + ", email: " + email);
            flag_social = FLAG_SOCIAL_GMAIL;
            currentUser.setFullname(personName);
            currentUser.setLinkGmail(id);
            currentUser.setUserName(email);
            currentUser.setEmail(email);

            doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
        }
    }

    public void doCPLogin(final User user, String Acction) {

        final ProgressDialog process = new ProgressDialog(getActivity());
        process.setMessage("Kiểm tra thông tin...");
        process.setCanceledOnTouchOutside(false);
        process.show();

        loginNewRequest data = new loginNewRequest();

        data.setUserLogin(user.getUserName());
        data.setFullName(user.getFullname());
        data.setLinkFB(user.getLinkFacebook());
        data.setLinkGMail(user.getLinkGmail());

        data.setDeviceID(Utilities.getDeviceID(getActivity()));
        data.setDeviceName(Utilities.getDeviceName() + "-" + Utilities.getVersion());
        data.setProject(Constant.Project_ID);
        data.setAction(Acction);
        data.setAuthentication(Constant.Project_Authentication);

        final BaseRequest request = new BaseRequest();
        request.setJsonDataInput(data);

        Call<loginNewResponse> call = svRequester.CPRegisterAccount(request);
        call.enqueue(new Callback<loginNewResponse>() {

            @Override
            public void onResponse(Call<loginNewResponse> call, Response<loginNewResponse> res) {
                // TODO Auto-generated method stub
                try {
                    if (res.isSuccessful()) {
                        loginNewResponse response = res.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                loginNewResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {

                                        // If false -> show dialog
                                        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                                        builder.setMessage(getString(R.string.message_error_username_login))
                                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builder.create().show();

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CLIEXIST)) {

                                            if (flag_social == FLAG_SOCIAL_FACEBOOK || flag_social == FLAG_SOCIAL_GMAIL) {
                                                doCPLogin(user, Constant.LOGIN_ACTION_CPLOGIN);
                                            } else {
                                                LoginInputPasswordFragment fragment = LoginInputPasswordFragment.newInstance(currentUser);
                                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                transaction.add(R.id.main_container, fragment);
                                                transaction.addToBackStack(fragment.getClass().getName());
                                                transaction.commit();
                                            }
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CLINOEXIST)) {
                                            Intent intent = new Intent(getActivity(), RegisterActivity.class);
                                            intent.putExtra(Constant.INTENT_USER_DATA, user);
                                            startActivity(intent);
                                        } else if (result.getClientProfile() != null) {

                                            //Save info client profile
                                            CustomPref.setLogin(getActivity(), true);
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
                if (!getActivity().isFinishing())
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (process.isShowing())
                                process.dismiss();
                        }
                    });
            }

            @Override
            public void onFailure(Call<loginNewResponse> call, Throwable t) {
                // TODO Auto-generated method stub
                myLog.E(t.getMessage());
                if (!getActivity().isFinishing())
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (process.isShowing())
                                process.dismiss();
                        }
                    });
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

//        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        myLog.E(" Error Connecttion " + connectionResult);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            myLog.E("Fragment result: " + data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);

        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);

        myLog.E("FRAGMENT", "onResultCalled");
    }

    @Override
    public void onResume() {
        super.onResume();
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        facebookProfile = Profile.getCurrentProfile();
    }

    @Override
    public void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            myLog.E("Got cached sign-in");
            GoogleSignInResult result = opr.get();

            if (result.isSuccess()) {
                googleProfile = result.getSignInAccount();

                currentUser.setFullname(googleProfile.getDisplayName());
                currentUser.setLinkGmail(googleProfile.getId());
                currentUser.setUserName(googleProfile.getEmail());
                currentUser.setEmail(googleProfile.getEmail());
            }
            // handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
//          showProgress(true);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult result) {
                    hideProgressDialog();
                    if (result.isSuccess()) {
                        googleProfile = result.getSignInAccount();

                        currentUser.setFullname(googleProfile.getDisplayName());
                        currentUser.setLinkGmail(googleProfile.getId());
                        currentUser.setUserName(googleProfile.getEmail());
                        currentUser.setEmail(googleProfile.getEmail());
                    }
//                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
