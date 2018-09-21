package com.dlvn.mcustomerportal.activity.prototype;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.WebNapasActivity;
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
import com.dlvn.mcustomerportal.utils.listerner.OnRegisterFragmentListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;
import com.dlvn.mcustomerportal.view.pinlock.IndicatorDots;
import com.dlvn.mcustomerportal.view.pinlock.PinLockListener;
import com.dlvn.mcustomerportal.view.pinlock.PinLockView;

import java.io.UnsupportedEncodingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements OnRegisterFragmentListener {

    private static final String TAG = "RegisterActivity";

    LinearLayout lloBack;
    Button btnTiepTuc;
    TextView tvStep, tvTitle, tvError;
    ClearableEditText cedtInput;
    CheckedTextView ctvShowPassword;
    AppCompatSeekBar sbStep;
    ToggleButton tbGender;

    ClientProfile user = null;
    int step = 1;
    int percent = 25;
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
        lloBack = (LinearLayout) findViewById(R.id.lloBack);
        btnTiepTuc = (Button) findViewById(R.id.btnTiepTuc);
        tvStep = (TextView) findViewById(R.id.tvStep);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvError = (TextView) findViewById(R.id.tvError);

        sbStep = (AppCompatSeekBar) findViewById(R.id.sbStep);
        sbStep.setThumb(null);
        sbStep.setEnabled(false);

        ctvShowPassword = (CheckedTextView) findViewById(R.id.ctvShowPassword);
        cedtInput = (ClearableEditText) findViewById(R.id.cedtInput);
        tbGender = (ToggleButton) findViewById(R.id.tbGender);
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

        ctvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ctvShowPassword.isChecked()) {
                    ctvShowPassword.setChecked(false);
                    cedtInput.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    ctvShowPassword.setChecked(true);
                    cedtInput.setTransformationMethod(null);
                }
            }
        });

        tbGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user.setGender("F");
                } else {
                    user.setGender("M");
                }
            }
        });
    }

    /**
     * setup view show/hide follow by step
     */
    private void setUpStepRegister() {
        myLog.E("setUpStepRegister Step = " + step);
        switch (step) {
            case 1:
                tvTitle.setText("Hãy tạo một mật khẩu để đăng nhập");
                cedtInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                cedtInput.setEnabled(true);
                cedtInput.setText("");
                ctvShowPassword.setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(user.getPassword())) {
                    cedtInput.setText(user.getPassword());
                }

                break;
            case 2:
                tvTitle.setText("Hãy cho chúng tôi biết tên của bạn");
                cedtInput.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                cedtInput.setEnabled(true);
                cedtInput.setText("");
                cedtInput.setVisibility(View.VISIBLE);
                ctvShowPassword.setVisibility(View.GONE);
                tbGender.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(user.getFullName())) {
                    cedtInput.setText(user.getFullName());
                }

                break;
            case 3:
                tvTitle.setText("Giới tính của bạn");
                cedtInput.setVisibility(View.GONE);
                cedtInput.setText("");
                tbGender.setVisibility(View.VISIBLE);

                if (user.getGender().equals("M"))
                    tbGender.setChecked(false);
                else if (user.getGender().equals("F"))
                    tbGender.setChecked(true);

                break;
            case 4:
                tvTitle.setText("Số điện thoại của bạn là gì");
                cedtInput.setVisibility(View.VISIBLE);
                cedtInput.setInputType(InputType.TYPE_CLASS_PHONE);
                tbGender.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(user.getCellPhone()))
                    cedtInput.setText(user.getCellPhone());
                else
                    cedtInput.setText("");
                break;
            default:
                break;
        }
        sbStep.setProgress(percent);
        tvStep.setText(step + "/4");
        tvError.setText(null);
    }

    /**
     * attemp data input follow by step
     */
    private void attempNextStep() {

        myLog.E("attempNextStep Step = " + step);
        cedtInput.setError(null);
        String strInput = cedtInput.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (step == 1 | step == 2 | step == 4) {
            // Check for a valid email address.
            if (TextUtils.isEmpty(strInput)) {
                cedtInput.setError(getString(R.string.error_field_required));
                tvError.setText("* " + getString(R.string.message_alert_input_password));
                focusView = cedtInput;
                cancel = true;
            }

            if (step == 2)
                if (strInput.length() >= 250) {
                    cedtInput.setError(getString(R.string.error_incorrect_fullname));
                    focusView = cedtInput;
                    cancel = true;
                }

            if (step == 4)
                if (!Utilities.isPhoneNumberValid(strInput)) {
                    cedtInput.setError(getString(R.string.error_incorrect_phone));
                    focusView = cedtInput;
                    cancel = true;
                }
        }

        if (cancel)
            focusView.requestFocus();
        else {

            if (tbGender.isChecked())
                user.setGender("F");
            else
                user.setGender("M");

            if (step == 1)
                user.setPassword(strInput);
            else if (step == 2)
                user.setFullName(strInput);
            else if (step == 3)
                user.setGender("");
            else if (step == 4) {
                user.setCellPhone(strInput);
                doCPRegister(RegisterActivity.this, user, Constant.LOGIN_ACTION_REGISTERACCOUNT, null);
            }

            step++;
            percent += 25;
            if (step > 4) {
                step = 4;
            }
            setUpStepRegister();

        }
    }

    public void doCPRegister(final Context context, final ClientProfile user, String Action, String otp) {

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

        data.setDeviceID(Utilities.getDeviceID(context));
        data.setOS(Utilities.getDeviceName() + "-" + Utilities.getVersion());
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

                                    if (result.getResult() != null && result.getResult().equals("false")) {

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

                                    } else if (result.getResult() != null && result.getResult().equals("true")) {

                                        if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CLIREGFAIL)) {


                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_CLIREGSUCC)) {

                                            showDialogInputOTP(context);
//                                            Intent intent = new Intent(c, DashboardActivity.class);
//                                            startActivity(intent);
                                        } else if (result.getErrLog().equals(Constant.ERR_CPLOGIN_SUCCESSFUL)) {
                                            Intent intent = new Intent(context, LoginMainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else if (result.getErrLog().equalsIgnoreCase(Constant.ERR_CPLOGIN_OTPINCORRECT)) {
                                            MyCustomDialog dialog = new MyCustomDialog.Builder(context)
                                                    .setTitle("Thông báo")
                                                    .setMessage("Bạn nhập sai mã OTP. Xin vui lòng thử lại")
                                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            showDialogInputOTP(context);
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
                myLog.E(t.getMessage());
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

        PinLockView mPinLockView;
        IndicatorDots mIndicatorDots;

        mPinLockView = dialog.findViewById(R.id.pin_lock_view);
        mIndicatorDots = dialog.findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);

        mPinLockView.setPinLength(Constant.OTP_LENGTH);

        mPinLockView.setTextColor(ContextCompat.getColor(context, R.color.white));
        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);

        mPinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                myLog.E(TAG, "OTP dialog pin = " + pin);
                if (pin.length() == Constant.OTP_LENGTH) {
                    doCPRegister(RegisterActivity.this, user, Constant.LOGIN_ACTION_CHECKOTP, pin);
                    dialog.dismiss();
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
                myLog.E(TAG, " OTP dialog empty");
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                myLog.E(TAG, " OTP dialog onPinChange length = " + pinLength + " ** inter : " + intermediatePin);
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (step > 1) {
            step--;
            setUpStepRegister();
        } else
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

    @Override
    public void onRegisterListener(int percent) {
        sbStep.setProgress(percent);
    }
}
