package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
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
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingLinkFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SettingLinkFragment";

    View view;
    TextView tvAccountDLVN, tvAccountGoogle, tvAccountFacebook;
    SwitchCompat swDLVN, swGoogle, swFacebook;

    //facebook login
    LoginButton btnLoginFacebook;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    AccessToken accessToken;
    Profile facebookProfile = null;
    //String array permission
    private static String[] FACEBOOK_PERMISSION = {"public_profile", "email"};

    //google login
    private Button btnLoginGoogle;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount googleProfile = null;
    private static final int RC_GOOGLE_SIGN_IN = 007;

    ClientProfile currentUser = null;
    ServicesRequest svRequester;

    int flag_social = 0;

    private static final int FLAG_SOCIAL_FACEBOOK = 1;
    private static final int FLAG_SOCIAL_GMAIL = 2;
    private static final int FLAG_SOCIAL_DLVN = 3;

    public SettingLinkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = new ClientProfile();
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_settings_link, container, false);

            tvAccountDLVN = view.findViewById(R.id.tvAcountDLVN);
            tvAccountGoogle = view.findViewById(R.id.tvAcountGoogle);
            tvAccountFacebook = view.findViewById(R.id.tvAcountFacebook);

            swDLVN = view.findViewById(R.id.swAcountDLVN);
            swGoogle = view.findViewById(R.id.swAcountGoogle);
            swFacebook = view.findViewById(R.id.swAcountFacebook);

            //FACEBOOK
            btnLoginFacebook = view.findViewById(R.id.btnLoginFacebook);

            //GOOGLE
            btnLoginGoogle = view.findViewById(R.id.btnLoginGoogle);

            initDatas();
            setListener();
        }

        return view;
    }

    private void initDatas() {
        currentUser = CustomPref.getUserLogin(getActivity());

        if (!TextUtils.isEmpty(CustomPref.getUserID(getActivity()))) {
            tvAccountDLVN.setText(CustomPref.getUserID(getActivity()));
            swDLVN.setChecked(true);
        } else
            swDLVN.setChecked(false);

        if (!TextUtils.isEmpty(CustomPref.getGoogleID(getActivity())))
            swGoogle.setChecked(true);
        else
            swGoogle.setChecked(false);

        if (!TextUtils.isEmpty(CustomPref.getFacebookID(getActivity())))
            swFacebook.setChecked(true);
        else
            swFacebook.setChecked(false);


        initActionFaceBook();
        initActionGoogle();
    }

    private void setListener() {
        swDLVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (swDLVN.isChecked()) {
                    if (TextUtils.isEmpty(currentUser.getClientID()))
                        showDialogValidDLVNAccount(getActivity(), swDLVN);
                    else {

                        MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity()).setMessage("Tài khoản đã được liên kết.").setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        dialog.show();
                    }
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                            .setMessage("Bạn có chắc chắn muốn gỡ bỏ liên kết tài khoản Dai-Ichi Life không? Thông tin liên kết tài khoản vui lòng xem tại đây.")
                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doCPLogin(currentUser, Constant.LOGIN_ACTION_UnLinkAccountSocial, swDLVN, null);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    swDLVN.setChecked(true);
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                    myLog.e(TAG, "Switch button UnLinked DLVN Account!");
                }
            }
        });

        swGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkUtils.isConnectedHaveDialog(getActivity()))
                    if (swGoogle.isChecked())
                        btnLoginGoogle.performClick();
                    else {

                        MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                .setMessage("Bạn có chắc chắn muốn gỡ bỏ liên kết tài khoản Google không? Thông tin liên kết tài khoản vui lòng xem tại đây.")
                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        googleSignOut();
                                        mGoogleApiClient.disconnect();
                                        doCPLogin(currentUser, Constant.LOGIN_ACTION_UnLinkAccountSocial, swGoogle, null);
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        swGoogle.setChecked(true);
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                else {
                    if (swGoogle.isChecked())
                        swGoogle.setChecked(false);
                    else
                        swGoogle.setChecked(true);
                }
            }
        });

        swFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isConnectedHaveDialog(getActivity()))
                    if (swFacebook.isChecked())
                        btnLoginFacebook.performClick();
                    else {

                        MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                .setMessage("Bạn có chắc chắn muốn gỡ bỏ liên kết tài khoản Facebook không? Thông tin liên kết tài khoản vui lòng xem tại đây.")
                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LoginManager.getInstance().logOut();
                                        doCPLogin(currentUser, Constant.LOGIN_ACTION_UnLinkAccountSocial, swFacebook, null);
//                                    accessTokenTracker.stopTracking();
//                                    accessToken = null;
                                        dialog.dismiss();
                                    }
                                }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        swFacebook.setChecked(true);
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    }
                else {
                    if (swFacebook.isChecked())
                        swFacebook.setChecked(false);
                    else
                        swFacebook.setChecked(true);
                }
            }
        });
    }

    private void initActionFaceBook() {

        callbackManager = CallbackManager.Factory.create();
        btnLoginFacebook.setReadPermissions(Arrays.asList(FACEBOOK_PERMISSION));

        // Callback registration
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                myLog.e("registerCallback onSuccess");
                myLog.e("Access token = " + loginResult.getAccessToken().getToken());
                myLog.e("User ID = " + loginResult.getAccessToken().getUserId());
                myLog.e("Permission = " + loginResult.getAccessToken().getPermissions());
                myLog.e("Expires = " + loginResult.getAccessToken().getExpires());

                currentUser.setLinkFaceBook(loginResult.getAccessToken().getUserId());
                final String facebookID = loginResult.getAccessToken().getUserId();
                flag_social = FLAG_SOCIAL_FACEBOOK;

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                myLog.e("LoginActivity", response.toString());
                                try {

                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + facebookID + "/picture?type=large");
                                        myLog.e("profile_pic Facebook ", profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        myLog.printTrace(e);
                                    }

                                    if (object.has("name"))
                                        currentUser.setFullName(object.getString("name"));
                                    if (object.has("email")) {
                                        currentUser.setEmail(object.getString("email"));
                                    }
                                    if (object.has("gender"))
                                        currentUser.setGender(object.getString("gender"));

                                    //check account with services
                                    doCPLogin(currentUser, Constant.LOGIN_ACTION_LINKACCOUNTSOCIAL, swFacebook, null);

                                } catch (JSONException e) {
                                    myLog.printTrace(e);
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString(GraphRequest.FIELDS_PARAM, "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                myLog.e(TAG, "Cancel from login with facebook");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                myLog.e(TAG, "Error from login with facebook: ");
                myLog.printTrace(exception);
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                myLog.e(TAG, "onCurrentAccessTokenChanged");
                if (oldAccessToken != null)
                    myLog.e(TAG, "old token" + oldAccessToken.getToken());
                if (currentAccessToken != null) {
                    myLog.e(TAG, "current token" + currentAccessToken.getToken());
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

                myLog.e(TAG, "onCurrentProfileChanged");
                // App code
                facebookProfile = currentProfile;
                if (facebookProfile != null) {
                    myLog.e(TAG, "profile " + currentProfile.getFirstName() + currentProfile.getMiddleName() + currentProfile.getLastName());
                    myLog.e(TAG, "id " + currentProfile.getId());
                    myLog.e(TAG, "name " + currentProfile.getName());
                }
            }
        };
    }

    private void initActionGoogle() {

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (googleProfile != null) {
//                    flag_social = FLAG_SOCIAL_GMAIL;
//                    currentUser.setFullName(googleProfile.getDisplayName());
//                    currentUser.setLinkGmail(googleProfile.getId());
//                    currentUser.setEmail(googleProfile.getEmail());
//                    doCPLogin(currentUser, Constant.LOGIN_ACTION_LINKACCOUNTSOCIAL, swGoogle, null);
//                } else
                googleSignIn();

            }
        });
    }

    /**
     * method google sigin
     */
    private void googleSignIn() {
        if (mGoogleApiClient != null) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
        }
    }

    /**
     * method google sigout
     */
    private void googleSignOut() {
        if (mGoogleApiClient != null)
            if (mGoogleApiClient.isConnected())
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                myLog.e(TAG, "Gooogle has sign Out!!!");
                            }
                        });
    }

    /**
     * Handle sign in by google
     *
     * @param result
     */
    private void handleSignInResult(GoogleSignInResult result) {
        myLog.e("handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            googleProfile = result.getSignInAccount();

            String id = googleProfile.getId();
            String personName = googleProfile.getDisplayName();
            String email = googleProfile.getEmail();
//            String personPhotoUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : null;

            myLog.e("Name: " + personName + ", email: " + email);
            flag_social = FLAG_SOCIAL_GMAIL;
            currentUser.setFullName(personName);
            currentUser.setLinkGmail(id);
            currentUser.setEmail(email);

            doCPLogin(currentUser, Constant.LOGIN_ACTION_LINKACCOUNTSOCIAL, swGoogle, null);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getActivity(), "Đăng nhập google thất bại!", Toast.LENGTH_SHORT).show();
            swGoogle.setChecked(false);
        }
    }


    /**
     * Check user exist or Login if Social
     *
     * @param user
     * @param Acction
     */
    public void doCPLogin(final ClientProfile user, String Acction, final SwitchCompat swButton, final AlertDialog alertDialog) {

        final ProgressDialog process = new ProgressDialog(getActivity());
        process.setMessage("Kiểm tra...");
        process.setCanceledOnTouchOutside(false);
        process.show();

        loginNewRequest data = new loginNewRequest();

        if (!TextUtils.isEmpty(user.getClientID()))
            data.setUserLogin(user.getClientID());
        else
            data.setUserLogin(user.getLoginName());

        data.setFullName(user.getFullName());
        if (Acction.equalsIgnoreCase(Constant.LOGIN_ACTION_UnLinkAccountSocial)) {
            if (swButton.equals(swFacebook))
                data.setLinkFB(Constant.LOGIN_Constant_UnLinkSocial);
            if (swButton.equals(swGoogle))
                data.setLinkGMail(Constant.LOGIN_Constant_UnLinkSocial);
        } else {
            data.setLinkFB(user.getLinkFaceBook());
            data.setLinkGMail(user.getLinkGmail());
        }

//        data.setPolicyNo(user.getPolicyNo());
//        data.setPOID(user.getPOID());
//        data.setPODOB(user.getPoDOB());
        data.setUserName(user.getUserName());
        data.setPassword(user.getPassword());

        data.setApiToken(user.getaPIToken());
        data.setDeviceID(Utilities.getDeviceID(getActivity()));
        data.setOS(Utilities.getDeviceOS());
        data.setDeviceToken(CustomPref.getFirebaseToken(getActivity()));
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
                    swButton.setChecked(false);

                    if (res.isSuccessful()) {
                        loginNewResponse response = res.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                loginNewResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equals("false")) {

                                        if (alertDialog != null)
                                            alertDialog.dismiss();

                                        if (flag_social == FLAG_SOCIAL_FACEBOOK) {
                                            swFacebook.setChecked(false);
                                        }

                                        if (flag_social == FLAG_SOCIAL_GMAIL) {
                                            swGoogle.setChecked(false);
                                        }

                                        if (flag_social == FLAG_SOCIAL_DLVN) {
                                            swDLVN.setChecked(false);
                                        }

                                        if (result.getNewAPIToken().equalsIgnoreCase(Constant.ERROR_TOKENINVALID)) {
                                            Utilities.processLoginAgain(getActivity(), getString(R.string.message_alert_relogin));
                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_LINKACCFAIL)) {

                                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                    .setMessage("Thông tin xác nhận không chính xác. Xin vui lòng thử lại.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            if (flag_social == FLAG_SOCIAL_FACEBOOK) {
                                                                swFacebook.setChecked(false);
                                                            }

                                                            if (flag_social == FLAG_SOCIAL_GMAIL) {
                                                                swGoogle.setChecked(false);
                                                            }

                                                            if (flag_social == FLAG_SOCIAL_DLVN) {
                                                                swDLVN.setChecked(false);
                                                            }

                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();

                                        } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_CLINOEXIST)) {
                                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                    .setMessage("Tài khoản này không tồn tại, xin vui lòng nhập lại chính xác số hợp đồng, mã khách hàng và ngày sinh.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_LINKACCSUCC)) {

                                            swButton.setChecked(true);

                                            if (alertDialog != null)
                                                alertDialog.dismiss();

                                            if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                                user.setaPIToken(result.getNewAPIToken());

                                            if (result.getClientProfile() != null) {
                                                ClientProfile userNew = result.getClientProfile().get(0);
                                                if (userNew != null) {
                                                    user.setClientID(userNew.getClientID());
                                                    user.setDOB(userNew.getDOB());
                                                    user.setPOID(userNew.getPOID());
                                                    user.setFullName(userNew.getFullName());
                                                    user.setAddress(userNew.getAddress());
                                                }
                                            }

                                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                    .setMessage("Tài khoản đã được liên kết thành công.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            if (TextUtils.isEmpty(CustomPref.getUserName(getActivity()))) {
                                                                android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                                UpdateAccountFragment fragment = new UpdateAccountFragment();

                                                                Bundle bundle = new Bundle();
                                                                bundle.putParcelable(Constant.INTENT_USER_DATA, user);
                                                                fragment.setArguments(bundle);

                                                                ft.replace(R.id.main_container, fragment);
                                                                ft.commit();
                                                            }
                                                            //Save Login Profile & Token
                                                            CustomPref.setLogin(getActivity(), true);
                                                            CustomPref.saveUserLogin(getActivity(), user);

                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();

                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_UNLINKACCSUCC)) {
                                            if (swButton.equals(swGoogle)) {
                                                CustomPref.setGoogleID(getActivity(), "");
                                                swButton.setChecked(false);
                                            }

                                            if (swButton.equals(swFacebook)) {
                                                CustomPref.setFacebookID(getActivity(), "");
                                                swButton.setChecked(false);
                                            }

                                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                                    .setMessage("Tài khoản đã được gỡ bỏ liên kết thành công.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_UNLINKACCFAIL)) {
                                            swButton.setChecked(true);
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }

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
                myLog.e(t.getMessage());
            }
        });
    }

    /**
     * Show dialog cho user chon single choice
     *
     * @param context
     */
    public void showDialogValidDLVNAccount(final Context context, final SwitchCompat swButton) {

        AlertDialog.Builder alerDialog = new AlertDialog.Builder(context);
        LayoutInflater li = LayoutInflater.from(context);
        View view = li.inflate(R.layout.dialog_valid_dlvn_account, null);
        alerDialog.setView(view);
        final AlertDialog dialog = alerDialog.create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));

        dialog.setCanceledOnTouchOutside(true);

        ImageButton btnClose = dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (swButton.isChecked())
                    swButton.setChecked(false);
                else
                    swButton.setChecked(true);
                dialog.dismiss();
            }
        });

        final ClearableEditText edtUserName, edtPassword;
        Button btnXacNhan;
        ImageButton ibtnReCapcha;

        edtUserName = dialog.findViewById(R.id.cedtUserName);
        edtPassword = dialog.findViewById(R.id.cedtPassword);

//        edtDoB = dialog.findViewById(R.id.cedtDOB);
//        edtDoB.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                final Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//                final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
//                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        // TODO Auto-generated method stub
//                        cal.set(year, month, dayOfMonth);
//                        edtDoB.setText(dateFormat.format(cal.getTime()));
//                    }
//                }, year, month, day);
//                dialog.show();
//            }
//        });

        ibtnReCapcha = dialog.findViewById(R.id.ibtnReCapcha);
        ibtnReCapcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtUserName.setError(null);
                edtPassword.setError(null);

                String username = edtUserName.getText().toString();
                String password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    edtUserName.setError(getString(R.string.error_field_required));
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError(getString(R.string.error_field_required));
                    return;
                }
                currentUser.setUserName(username);
                currentUser.setPassword(password);

                flag_social = FLAG_SOCIAL_DLVN;

                if (NetworkUtils.isConnectedHaveDialog(context))
                    verifyWithReCaptcha(dialog);
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (swButton.isChecked())
                        swButton.setChecked(false);
                    else
                        swButton.setChecked(true);
                    dialog.dismiss();

                    return true;
                }
                return false;
            }
        });
    }

    private void verifyWithReCaptcha(final AlertDialog dialog) {
        myLog.e(TAG, "verifyWithReCaptcha");
        SafetyNet.getClient(getActivity()).verifyWithRecaptcha(getString(R.string.api_sitekey_recaptcha))
                .addOnSuccessListener(getActivity(), new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        if (!response.getTokenResult().isEmpty()) {
                            myLog.e(TAG, "Token result " + response.getTokenResult());
                            getSiteVerifyReCaptCha(getActivity(), response.getTokenResult(), dialog);
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

    private void getSiteVerifyReCaptCha(final Context c, final String responseCaptcha, final AlertDialog dialog) {
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
                                doCPLogin(currentUser, Constant.LOGIN_ACTION_LINKCLIENTID, swDLVN, dialog);
                            } else {
                                MyCustomDialog dialog = new MyCustomDialog.Builder(c)
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        myLog.e(TAG, " Google connection failed!!!");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            myLog.e(TAG, "Fragment result: " + data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);

        myLog.e(TAG, "onActivityResult Called");
    }

    @Override
    public void onResume() {
        super.onResume();
        myLog.e(TAG, "onResume");
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        facebookProfile = Profile.getCurrentProfile();

        try {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestProfile()
                    .requestEmail()
                    .build();

            if (mGoogleApiClient == null)
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .enableAutoManage(getActivity(), this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
            googleSignOut();
        } catch (Exception e) {
            myLog.printTrace(e);
        }

        new CPSaveLogTask(getActivity(), Constant.CPLOG_SETTINGLINKED_OPEN).execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_SETTINGLINKED_CLOSE).execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
}
