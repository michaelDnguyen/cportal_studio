package com.dlvn.mcustomerportal.activity.prototype;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;
import com.dlvn.mcustomerportal.view.pinlock.IndicatorDots;
import com.dlvn.mcustomerportal.view.pinlock.PinLockListener;
import com.dlvn.mcustomerportal.view.pinlock.PinLockView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    LinearLayout lloBack;
    Button btnTiepTuc;
    TextView tvTitle, tvError, tvPolicy;
    RadioButton rdbMale, rdbFemale;
    TextInputLayout wrapperPassword, wrapperFullname, wrapperPhone;

    ClientProfile user = null;
    boolean isRePin = true;
    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        svRequester = ServicesGenerator.createService(ServicesRequest.class);

        initViews();
        setListener();

        user = new ClientProfile();
        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(Constant.INTENT_USER_DATA))
                user = getIntent().getParcelableExtra(Constant.INTENT_USER_DATA);
    }

    /**
     * initialize for views
     */
    private void initViews() {
        lloBack = findViewById(R.id.lloBack);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        tvTitle = findViewById(R.id.tvTitle);
        tvError = findViewById(R.id.tvError);

        wrapperPassword = findViewById(R.id.password);
        wrapperFullname = findViewById(R.id.wrapperFullName);
        wrapperPhone = findViewById(R.id.wrapperPhone);

        rdbMale = findViewById(R.id.rdbMale);
        rdbFemale = findViewById(R.id.rdbFemale);

        //setup conpany info & policy
        tvPolicy = findViewById(R.id.tvPolicy);
        tvPolicy.setText(Html.fromHtml(getString(R.string.txt_policy_term)));
        tvPolicy.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * set listener for view
     */
    private void setListener() {

        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempNextStep();
            }
        });

    }

    /**
     * attemp data input follow by step
     */
    private void attempNextStep() {

        myLog.e("attempNextStep Step = ");
        boolean cancel = false;
        View focusView = null;

        String password = wrapperPassword.getEditText().getText().toString();
        String fullname = wrapperFullname.getEditText().getText().toString();
        String phone = wrapperPhone.getEditText().getText().toString();

        if (TextUtils.isEmpty(password)) {
            wrapperPassword.setError(getString(R.string.error_field_required));
            tvError.setText("* " + getString(R.string.message_alert_input_password));
            focusView = wrapperPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(fullname)) {
            wrapperFullname.setError(getString(R.string.error_field_required));
            tvError.setText("* " + getString(R.string.message_alert_input_password));
            focusView = wrapperFullname;
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            wrapperPhone.setError(getString(R.string.error_field_required));
            tvError.setText("* " + getString(R.string.message_alert_input_password));
            focusView = wrapperPhone;
            cancel = true;
        }

        if (password.length() < 4) {
            wrapperPassword.setError(getString(R.string.error_invalid_password));
            tvError.setText("* " + getString(R.string.message_alert_correct_password));
            focusView = wrapperPassword;
            cancel = true;
        }

        if (!Utilities.isPasswordValid(password)) {
            wrapperPassword.setError(getString(R.string.error_invalid_password));
            tvError.setText("* " + getString(R.string.message_alert_correct_password));
            focusView = wrapperPassword;
            cancel = true;
        }

        if (fullname.length() >= 250) {
            wrapperFullname.setError(getString(R.string.error_incorrect_fullname));
            focusView = wrapperFullname;
            cancel = true;
        }

        if (!Utilities.isPhoneNumberValid(phone)) {
            wrapperPhone.setError(getString(R.string.error_incorrect_phone));
            focusView = wrapperPhone;
            cancel = true;
        }

        if (cancel)
            focusView.requestFocus();
        else {

            if (rdbFemale.isChecked())
                user.setGender("F");
            if (rdbMale.isChecked())
                user.setGender("M");

            user.setPassword(password);
            user.setFullName(fullname);
            user.setCellPhone(phone);

            doCPRegister(RegisterActivity.this, user, Constant.LOGIN_ACTION_REGISTERACCOUNT, null, null);
        }
    }

    public void doCPRegister(final Context context, final ClientProfile user, final String Action, String otp, final AlertDialog alertDialog) {

        final ProgressDialog process = new ProgressDialog(context);
        process.setMessage("Đăng kí tài khoản...");
        process.setCanceledOnTouchOutside(false);
        process.show();

        loginNewRequest data = new loginNewRequest();

        data.setUserLogin(user.getLoginName());
        data.setFullName(user.getFullName());
        data.setGender(user.getGender());
        data.setCellPhone(user.getCellPhone());
        data.setPassword(user.getPassword());
        data.setLinkGMail(user.getLinkGmail());
        data.setLinkFB(user.getLinkFaceBook());

        if (otp != null)
            data.setOtp(otp);

        if (Action.equalsIgnoreCase(Constant.LOGIN_ACTION_GET_CLIENTPROFILE))
            data.setApiToken(user.getaPIToken());

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
                try {
                    if (res.isSuccessful()) {
                        loginNewResponse response = res.body();
                        if (response != null)
                            if (response.getResponse() != null) {
                                loginNewResult result = response.getResponse();
                                if (result != null) {

                                    if (result.getResult() != null && result.getResult().equalsIgnoreCase("false")) {

                                        // If false -> show dialog
                                        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
                                        builder.setMessage(getString(R.string.message_error_username_login))
                                                .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        builder.create().show();

                                    } else if (result.getResult() != null && result.getResult().equalsIgnoreCase("true")) {

                                        if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_CLIREGFAIL)) {

                                            MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
                                            builder.setMessage("Tạo mới tài khoản thất bại, vui lòng thử lại.")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                            builder.create().show();

                                        } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_CLIREGSUCC)) {

                                            //update API token result when Register Success
                                            if (!TextUtils.isEmpty(result.getNewAPIToken())) {
                                                user.setaPIToken(result.getNewAPIToken());
                                                CustomPref.saveAPIToken(context, result.getNewAPIToken());
                                            }

                                            showDialogInputOTP(context);

                                        } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_OTPINCORRECT)) {

                                            MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                    .setMessage("Bạn nhập sai mã Pin. Xin vui lòng thử lại")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).create();
                                            dialog.show();
                                        } else {

                                            if (Action.equalsIgnoreCase(Constant.LOGIN_ACTION_CHECKOTP)) {
                                                alertDialog.dismiss();

                                                doCPRegister(context, user, Constant.LOGIN_ACTION_GET_CLIENTPROFILE, null, null);
                                            }

                                            if (Action.equalsIgnoreCase(Constant.LOGIN_ACTION_GET_CLIENTPROFILE)) {

                                                if (result.getClientProfile() != null) {

                                                    ClientProfile user = result.getClientProfile().get(0);
                                                    if (TextUtils.isEmpty(user.getaPIToken()))
                                                        user.setaPIToken(CustomPref.getAPIToken(context));

                                                    //Save Login Profile & Token
                                                    CustomPref.setLogin(context, true);
                                                    CustomPref.saveUserLogin(context, user);

                                                    Intent intent = new Intent(context, DashboardActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                            if (Action.equalsIgnoreCase(Constant.LOGIN_ACTION_GENERATEOTP)) {

                                                //OTP re-generate
                                                isRePin = false;
                                                MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
                                                builder.setMessage("Mã Pin đã được gửi lại vào địa chỉ email của quý khách.")
                                                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                builder.create().show();
                                            }
                                        }
                                    }
                                }
                            }
                    }
                } catch (Exception e) {
                    myLog.printTrace(e);
                }
                if (!isFinishing())
                    runOnUiThread(new Runnable() {
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
                if (!isFinishing())
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (process.isShowing())
                                process.dismiss();
                        }
                    });
            }
        });
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

        final CountDownTimer timer = new CountDownTimer(Constant.TIMER_COUNTDOWN_OTP, 1000) {

            public void onTick(long millisUntilFinished) {
                tvCountdown.setText("" + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                isRePin = true;
                tvCountdown.setText("0");
            }

        };
        timer.start();

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                myLog.e(TAG, "OTP dialog pin = " + pin);
                if (pin.length() == Constant.OTP_LENGTH) {
                    if (!tvCountdown.getText().toString().equalsIgnoreCase("0")) {
                        doCPRegister(RegisterActivity.this, user, Constant.LOGIN_ACTION_CHECKOTP, pin, dialog);

                    } else {
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
                mPinLockView.resetPinLockView();
                if (isRePin) {
                    doCPRegister(RegisterActivity.this, user, Constant.LOGIN_ACTION_GENERATEOTP, null, dialog);
                    try {
                        Thread.sleep(3000);
                        timer.start();
                    } catch (InterruptedException e) {
                        myLog.printTrace(e);
                    }
                } else {
//                    MyCustomDialog.Builder builder = new MyCustomDialog.Builder(context);
//                    builder.setMessage("Mã Pin đã được gửi vào địa chỉ Email của quý khách. Xin vui lòng kiểm tra lại.")
//                            .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.create().show();
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();
        if (v != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
                && v instanceof EditText) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                Utilities.hideSoftInputKeyboard(this, v);
        }

        return super.dispatchTouchEvent(ev);
    }
}
