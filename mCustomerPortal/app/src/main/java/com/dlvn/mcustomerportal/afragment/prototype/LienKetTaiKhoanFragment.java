package com.dlvn.mcustomerportal.afragment.prototype;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.activity.prototype.RegisterActivity;
import com.dlvn.mcustomerportal.adapter.SingleListAdapter;
import com.dlvn.mcustomerportal.adapter.model.SingleChoiceModel;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.MasterData_City;
import com.dlvn.mcustomerportal.services.model.response.MasterData_District;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
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
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LienKetTaiKhoanFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LienKetTaiKhoanFragment";

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

    public LienKetTaiKhoanFragment() {
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
            view = inflater.inflate(R.layout.fragment_lien_ket_tai_khoan, container, false);

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
                        showDialogValidDLVNAccount(getActivity());
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
                    myLog.E(TAG, "Switch button UnLinked DLVN Account!");
                }
            }
        });

        swGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swGoogle.isChecked())
                    btnLoginGoogle.performClick();
                else {
                    googleSignOut();
                    mGoogleApiClient.disconnect();
                }
            }
        });

        swFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swFacebook.isChecked())
                    btnLoginFacebook.performClick();
                else {
                    LoginManager.getInstance().logOut();
//                    accessTokenTracker.stopTracking();
//                    accessToken = null;
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
                myLog.E("registerCallback onSuccess");
                myLog.E("Access token = " + loginResult.getAccessToken().getToken());
                myLog.E("User ID = " + loginResult.getAccessToken().getUserId());
                myLog.E("Permission = " + loginResult.getAccessToken().getPermissions());
                myLog.E("Expires = " + loginResult.getAccessToken().getExpires());

                currentUser.setLinkFaceBook(loginResult.getAccessToken().getUserId());
                flag_social = FLAG_SOCIAL_FACEBOOK;

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                myLog.E("LoginActivity", response.toString());
                                try {

                                    if (object.has("name"))
                                        currentUser.setFullName(object.getString("name"));
                                    if (object.has("email")) {
                                        currentUser.setEmail(object.getString("email"));
                                    }
                                    if (object.has("gender"))
                                        currentUser.setGender(object.getString("gender"));

                                    //check account with services
                                    doCPLogin(currentUser, Constant.LOGIN_ACTION_LINKACCOUNTSOCIAL, swFacebook);

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
                myLog.E(TAG, "Cancel from login with facebook");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                myLog.E(TAG, "Error from login with facebook: ");
                myLog.printTrace(exception);
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                myLog.E(TAG, "onCurrentAccessTokenChanged");
                if (oldAccessToken != null)
                    myLog.E(TAG, "old token" + oldAccessToken.getToken());
                if (currentAccessToken != null) {
                    myLog.E(TAG, "current token" + currentAccessToken.getToken());
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

                myLog.E(TAG, "onCurrentProfileChanged");
                // App code
                facebookProfile = currentProfile;
                if (facebookProfile != null) {
                    myLog.E(TAG, "profile " + currentProfile.getFirstName() + currentProfile.getMiddleName() + currentProfile.getLastName());
                    myLog.E(TAG, "id " + currentProfile.getId());
                    myLog.E(TAG, "name " + currentProfile.getName());
                }
            }
        };
    }

    private void initActionGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleProfile != null) {
                    flag_social = FLAG_SOCIAL_GMAIL;
                    currentUser.setFullName(googleProfile.getDisplayName());
                    currentUser.setLinkGmail(googleProfile.getId());
                    currentUser.setEmail(googleProfile.getEmail());
                    doCPLogin(currentUser, Constant.LOGIN_ACTION_CPLOGIN, swGoogle);
                } else
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
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            myLog.E(TAG, "Gooogle has sign Out!!!");
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
            currentUser.setFullName(personName);
            currentUser.setLinkGmail(id);
            currentUser.setEmail(email);

            doCPLogin(currentUser, Constant.LOGIN_ACTION_LINKACCOUNTSOCIAL, swGoogle);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(getActivity(), "Đăng nhập google thất bại!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Check user exist or Login if Social
     *
     * @param user
     * @param Acction
     */
    public void doCPLogin(final ClientProfile user, String Acction, final SwitchCompat swButton) {

        loginNewRequest data = new loginNewRequest();

        if (!TextUtils.isEmpty(user.getClientID()))
            data.setUserLogin(user.getClientID());
        else
            data.setUserLogin(user.getLoginName());

        data.setFullName(user.getFullName());
        data.setLinkFB(user.getLinkFaceBook());
        data.setLinkGMail(user.getLinkGmail());

        data.setPolicyNo(user.getPolicyNo());
        data.setPOID(user.getPOID());
        data.setPODOB(user.getPoDOB());

        data.setApiToken(user.getaPIToken());
        data.setDeviceID(Utilities.getDeviceID(getActivity()));
        data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
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
                                        swButton.setChecked(false);

                                        if (flag_social == FLAG_SOCIAL_FACEBOOK) {
                                            swFacebook.setChecked(false);
                                        }

                                        if (flag_social == FLAG_SOCIAL_GMAIL) {
                                            swGoogle.setChecked(false);
                                        }

                                        if (flag_social == FLAG_SOCIAL_DLVN) {
                                            swDLVN.setChecked(false);
                                        }

                                        if (result.getNewAPIToken().equalsIgnoreCase("invalidtoken")) {
                                            Utilities.processLoginAgain(getActivity(), getString(R.string.message_alert_relogin));
                                        }

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getErrLog().equals(Constant.ERR_CPLOGIN_LINKACCFAIL)) {

                                            swButton.setChecked(false);
                                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity()).setMessage("Thông tin xác nhận không chính xác. Xin vui lòng thử lại.").setTitle("Thông báo").setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
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

                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_LINKACCSUCC)) {

                                            swButton.setChecked(true);

                                            if (!TextUtils.isEmpty(result.getNewAPIToken()))
                                                user.setaPIToken(result.getNewAPIToken());

                                            ClientProfile userNew = result.getClientProfile().get(0);
                                            if (userNew != null) {
                                                user.setClientID(userNew.getClientID());
                                                user.setDOB(userNew.getDOB());
                                                user.setPOID(userNew.getPOID());
                                                user.setFullName(userNew.getFullName());
                                                user.setAddress(userNew.getAddress());

                                                //Save Login Profile & Token
                                                CustomPref.setLogin(getActivity(), true);
                                                CustomPref.saveUserLogin(getActivity(), user);
                                            }

                                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity()).setMessage("Tài khoản đã được liên kết thành công.").setTitle("Thông báo").setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
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
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
            }

            @Override
            public void onFailure(Call<loginNewResponse> call, Throwable t) {
                // TODO Auto-generated method stub
                myLog.E(t.getMessage());
            }
        });
    }

    /**
     * Show dialog cho user chon single choice
     *
     * @param context
     */
    public void showDialogValidDLVNAccount(final Context context) {

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
                dialog.dismiss();
            }
        });

        final EditText edtProposalID, edtCMND;
        final TextView tvDoB;
        Button btnXacNhan;

        edtProposalID = dialog.findViewById(R.id.edtPolicyID);
        edtCMND = dialog.findViewById(R.id.edtCMND);

        tvDoB = dialog.findViewById(R.id.tvDoB);
        tvDoB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        android.R.style.Theme_Material_Light_Dialog_Alert, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        cal.set(year, month, dayOfMonth);
                        tvDoB.setText(dateFormat.format(cal.getTime()));
                    }
                }, year, month, day);
                dialog.show();
            }
        });


        btnXacNhan = dialog.findViewById(R.id.btnXacNhan);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtProposalID.setError(null);
                edtCMND.setError(null);
                tvDoB.setError(null);

                String policy = edtProposalID.getText().toString();
                String cmnd = edtCMND.getText().toString();
                String dob = tvDoB.getText().toString();

                if (TextUtils.isEmpty(policy)) {
                    edtProposalID.setError(getString(R.string.error_field_required));
                    return;
                }

                if (TextUtils.isEmpty(cmnd)) {
                    edtCMND.setError(getString(R.string.error_field_required));
                    return;
                }

                if (TextUtils.isEmpty(dob)) {
                    tvDoB.setError(getString(R.string.error_field_required));
                    return;
                }

                currentUser.setPolicyNo(policy);
                currentUser.setPOID(cmnd);
                currentUser.setPoDOB(dob);
                flag_social = FLAG_SOCIAL_DLVN;

                doCPLogin(currentUser, Constant.LOGIN_ACTION_LINKCLIENTID, swDLVN);
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        myLog.E(TAG, " Google connection failed!!!");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            myLog.E(TAG, "Fragment result: " + data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);

        myLog.E(TAG, "onActivityResult Called");
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
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

}
