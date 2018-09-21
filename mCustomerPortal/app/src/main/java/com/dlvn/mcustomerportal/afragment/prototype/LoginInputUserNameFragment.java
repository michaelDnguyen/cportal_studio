package com.dlvn.mcustomerportal.afragment.prototype;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.activity.prototype.PaymentNoLoginActivity;
import com.dlvn.mcustomerportal.activity.prototype.RegisterActivity;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

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
public class LoginInputUserNameFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginInputUserNameFragment";

    private static final int FLAG_SOCIAL_FACEBOOK = 1;
    private static final int FLAG_SOCIAL_GMAIL = 2;

    // UI references.
    private ClearableEditText mEmailView;
    private EditText mPasswordView;
    private TextView tvPolicy, tvCopyRight;
    private Button btnFacebook, btnGoogle, btnPayment;
    private View v;

    private ProgressDialog mProgressDialog;

    int flag_social = 0;

    //facebook login
    LoginButton btnLoginFacebook;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    AccessToken accessToken;
    Profile facebookProfile = null;
    //String array permission
    private static String[] FACEBOOK_PERMISSION = {"public_profile", "email"};

    //google
    private SignInButton btnLoginGoogle;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount googleProfile = null;
    private static final int RC_GOOGLE_SIGN_IN = 007;

    ClientProfile currentUser = null;
    ServicesRequest svRequester;

    /**
     * @modify 11/09/2018
     * For Finger Print Authentication
     */
    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String SECRET_MESSAGE = "Very secret message";
    private static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
    public static final String DEFAULT_KEY_NAME = "default_key";

    LinearLayout lloFingerPrintButton;
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;

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
//        FacebookSdk.sdkInitialize(getActivity());
//        AppEventsLogger.activateApp(getActivity());

        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
            myLog.printTrace(e);
        }

        currentUser = new ClientProfile();
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_login_step1, container, false);

            mEmailView = (ClearableEditText) v.findViewById(R.id.email);
//            populateAutoComplete();

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

                    currentUser.setLinkFaceBook(loginResult.getAccessToken().getUserId());
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
                                            currentUser.setFullName(object.getString("name"));
                                        if (object.has("email")) {
                                            currentUser.setEmail(object.getString("email"));
                                            currentUser.setLoginName(object.getString("email"));
                                        }
                                        if (object.has("gender"))
                                            currentUser.setGender(object.getString("gender"));

                                        //check account with services
                                        doCPLogin(currentUser, Constant.LOGIN_ACTION_CPLOGIN);
//                                        String birthday = object.getString("birthday");

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

                    if (accessToken != null && !accessToken.isExpired()) {
                        if (facebookProfile != null) {
                            currentUser.setLinkFaceBook(facebookProfile.getId());
                            currentUser.setFullName(facebookProfile.getName());
                            doCPLogin(currentUser, Constant.LOGIN_ACTION_CPLOGIN);
                        }
                    } else {
                        btnLoginFacebook.performClick();
                    }

                    //for test
//                    currentUser.setEmail("david_jlmbmfu_nokia@tfbnw.net");
//                    currentUser.setLoginName("david_jlmbmfu_nokia@tfbnw.net");
//                    currentUser.setLinkFaceBook("108360580075160");
//                    currentUser.setFullName("David Nokia");
//                    flag_social = FLAG_SOCIAL_FACEBOOK;
//                    doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
                }
            });

            //GOOGLE
            btnGoogle = (Button) v.findViewById(R.id.btnGoogle);
            btnGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (googleProfile != null)
                        doCPLogin(currentUser, Constant.LOGIN_ACTION_CPLOGIN);
                    else
                        googleSignIn();

                    //for test
//                    currentUser.setEmail("jonynguyen87@gmail.com");
//                    currentUser.setLoginName("jonynguyen87@gmail.com");
//                    currentUser.setLinkGmail("109913173948764899767");
//                    currentUser.setFullName("Nguyen Jony");
//                    flag_social = FLAG_SOCIAL_GMAIL;
//                    doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
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

            btnPayment = v.findViewById(R.id.payment_button);
            btnPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent payment = new Intent(getActivity(), PaymentNoLoginActivity.class);
                    startActivity(payment);
                }
            });

            /**
             * Actions for Finger Print Authentication
             */
            lloFingerPrintButton = v.findViewById(R.id.lloFingerPrintButton);
            initFingerPrint();
        }
        return v;
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

        if (cancel) {
            focusView.requestFocus();
        } else {
            currentUser.setLoginName(email);
            if (Utilities.validateEmail(email))
                currentUser.setEmail(email);

            if (NetworkUtils.isConnectedHaveDialog(getActivity()))
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
            currentUser.setFullName(personName);
            currentUser.setLinkGmail(id);
            currentUser.setLoginName(email);
            currentUser.setEmail(email);

            doCPLogin(currentUser, Constant.LOGIN_ACTION_CHECKACCOUNT);
//            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
//            updateUI(false);
            Toast.makeText(getActivity(), "Đăng nhập google thất bại!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @modify 11/09/2018
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void initFingerPrint() {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
        try {
            mKeyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
        }
        final Cipher defaultCipher;
        Cipher cipherNotInvalidated;
        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipherNotInvalidated = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }

        lloFingerPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyguardManager keyguardManager = getActivity().getSystemService(KeyguardManager.class);
                FingerprintManager fingerprintManager = getActivity().getSystemService(FingerprintManager.class);

                if (CustomPref.haveAuthFinger(getActivity())) {

                    if (!keyguardManager.isKeyguardSecure()) {
                        // Show a message that the user hasn't set up a fingerprint or lock screen.
                        Toast.makeText(getActivity(),
                                "Màn hình bảo vệ chưa được cài đặt.\n"
                                        + "Đi đến 'Settings -> Security -> Fingerprint' để cài đặt bảo vệ bằng vân tay.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (!fingerprintManager.hasEnrolledFingerprints()) {
                        // This happens when no fingerprints are registered.
                        Toast.makeText(getActivity(),
                                "Đi đến 'Settings -> Security -> Fingerprint' và cài đặt ít nhất một vân tay.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    createKey(DEFAULT_KEY_NAME, true);
                    createKey(KEY_NAME_NOT_INVALIDATED, false);
                    showFingerPrintFragmentDialog(defaultCipher, DEFAULT_KEY_NAME);
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                            .setTitle("Thông báo")
                            .setMessage("Anh/chị cần đăng nhập vào ứng dụng và cài đặt vân tay trong mục Bảo mật.")
                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
            }
        });
    }

    /**
     * Initialize the {@link Cipher} instance with the created key in the
     * {@link #createKey(String, boolean)} method.
     *
     * @param keyName the key name to init the cipher
     * @return {@code true} if initialization is successful, {@code false} if the lock screen has
     * been disabled or reset after the key was generated, or if a fingerprint got enrolled after
     * the key was generated.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    /**
     * Proceed the purchase operation
     *
     * @param withFingerprint {@code true} if the purchase was made by using a fingerprint
     * @param cryptoObject    the Crypto object
     */
    @SuppressLint("NewApi")
    public void onPurchased(boolean withFingerprint,
                            @Nullable FingerprintManager.CryptoObject cryptoObject) {
        if (withFingerprint) {
            // If the user has authenticated with fingerprint, verify that using cryptography and
            // then show the confirmation message.
            assert cryptoObject != null;
            tryEncrypt(cryptoObject.getCipher());
        } else {
            // Authentication happened with backup password. Just show the confirmation message.
            showConfirmation(null);
        }
    }

    private void showConfirmation(byte[] encrypted) {
        if (encrypted != null) {
            Toast.makeText(getActivity(), Base64.encodeToString(encrypted, 0 /* flags */), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Tries to encrypt some data with the generated key in {@link #createKey} which is
     * only works if the user has just authenticated via fingerprint.
     */
    private void tryEncrypt(Cipher cipher) {
        try {
            byte[] encrypted = cipher.doFinal(SECRET_MESSAGE.getBytes());
//            showConfirmation(encrypted);

            //Goto Dashboard
            myLog.E("Login throught FingerPrint");
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            CustomPref.setLogin(getActivity(), true);
            startActivity(intent);
            getActivity().finish();

        } catch (BadPaddingException | IllegalBlockSizeException e) {
            Toast.makeText(getActivity(), "Failed to encrypt the data with the generated key. "
                    + "Retry the purchase", Toast.LENGTH_LONG).show();
            myLog.E(TAG, "Failed to encrypt the data with the generated key." + e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore.load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            // This is a workaround to avoid crashes on devices whose API level is < 24
            // because KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment is only
            // visible on API level +24.
            // Ideally there should be a compat library for KeyGenParameterSpec.Builder but
            // which isn't available yet.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showFingerPrintFragmentDialog(Cipher mCipher, String mKeyName) {
        if (initCipher(mCipher, mKeyName)) {

            // Show the fingerprint dialog. The user has the option to use the fingerprint with
            // crypto, or you can fall back to using a server-side verified password.
            FingerPrintAuthenDialogFragment fragment
                    = new FingerPrintAuthenDialogFragment(LoginInputUserNameFragment.this);
            fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));

            fragment.setStage(
                    FingerPrintAuthenDialogFragment.Stage.FINGERPRINT);
            fragment.show(getActivity().getFragmentManager(), DIALOG_FRAGMENT_TAG);
        } else {
            // This happens if the lock screen has been disabled or or a fingerprint got
            // enrolled. Thus show the dialog to authenticate with their password first
            // and ask the user if they want to authenticate with fingerprints in the
            // future
            FingerPrintAuthenDialogFragment fragment
                    = new FingerPrintAuthenDialogFragment(LoginInputUserNameFragment.this);
            fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
            fragment.setStage(
                    FingerPrintAuthenDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
            fragment.show(getActivity().getFragmentManager(), DIALOG_FRAGMENT_TAG);
        }
    }

    /**
     * Check user exist or Login if Social
     *
     * @param user
     * @param Acction
     */
    public void doCPLogin(final ClientProfile user, String Acction) {

        final ProgressDialog process = new ProgressDialog(getActivity());
        process.setMessage("Kiểm tra thông tin...");
        process.setCanceledOnTouchOutside(false);
        process.show();

        loginNewRequest data = new loginNewRequest();

        data.setUserLogin(user.getLoginName());
        data.setFullName(user.getFullName());
        data.setLinkFB(user.getLinkFaceBook());
        data.setLinkGMail(user.getLinkGmail());

        data.setApiToken(CustomPref.getAPIToken(getActivity()));
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
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_LINKNOEXIST)) {
                                            Intent intent = new Intent(getActivity(), RegisterActivity.class);
                                            intent.putExtra(Constant.INTENT_USER_DATA, user);
                                            startActivity(intent);
                                        } else if (result.getClientProfile() != null) {

                                            ClientProfile user = result.getClientProfile().get(0);

                                            if (TextUtils.isEmpty(user.getaPIToken()))
                                                user.setaPIToken(result.getNewAPIToken());

                                            //Save Login Profile & Token
                                            CustomPref.setLogin(getActivity(), true);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        myLog.E(" Error Connecttion " + connectionResult);
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


        //sigin silent
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            myLog.E("Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//
//            if (result.isSuccess()) {
//                googleProfile = result.getSignInAccount();
//
//                currentUser.setFullName(googleProfile.getDisplayName());
//                currentUser.setLinkGmail(googleProfile.getId());
//                currentUser.setLoginName(googleProfile.getEmail());
//                currentUser.setEmail(googleProfile.getEmail());
//            }
//            // handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
////          showProgress(true);
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult result) {
//                    hideProgressDialog();
//                    if (result.isSuccess()) {
//                        googleProfile = result.getSignInAccount();
//
//                        currentUser.setFullName(googleProfile.getDisplayName());
//                        currentUser.setLinkGmail(googleProfile.getId());
//                        currentUser.setLoginName(googleProfile.getEmail());
//                        currentUser.setEmail(googleProfile.getEmail());
//                    }
////                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
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
