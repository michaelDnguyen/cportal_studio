package com.dlvn.mcustomerportal.activity.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.User;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnRegisterFragmentListener;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements OnRegisterFragmentListener {

    LinearLayout lloBack;
    Button btnTiepTuc;
    TextView tvStep, tvTitle, tvError;
    ClearableEditText cedtInput;
    CheckedTextView ctvShowPassword;
    AppCompatSeekBar sbStep;
    ToggleButton tbGender;

    User user = null;
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

        user = new User();
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

                if (!TextUtils.isEmpty(user.getFullname())) {
                    cedtInput.setText(user.getFullname());
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
                user.setFullname(strInput);
            else if (step == 3)
                user.setGender("");
            else if (step == 4) {
                user.setPhone(strInput);
                doCPRegister(RegisterActivity.this, user, Constant.LOGIN_ACTION_REGISTERACCOUNT);
            }

            step++;
            percent += 25;
            if (step > 4) {
                step = 4;
            }
            setUpStepRegister();

        }
    }

    public void doCPRegister(final Context c, final User user, String Acction) {

        final ProgressDialog process = new ProgressDialog(c);
        process.setMessage("Đăng kí tài khoản...");
        process.setCanceledOnTouchOutside(false);
        process.show();

        loginNewRequest data = new loginNewRequest();

        data.setUserLogin(user.getUserName());
        data.setFullName(user.getFullname());
        data.setGender(user.getGender());
        data.setCellPhone(user.getPhone());
        data.setPassword(user.getPassword());
        data.setLinkGMail(user.getLinkGmail());
        data.setLinkFB(user.getLinkFacebook());

        data.setDeviceID(Utilities.getDeviceID(c));
        data.setDeviceName(Utilities.getDeviceName() + "-" + Utilities.getVersion());
        data.setProject(Constant.Project_ID);
        data.setAction(Acction);
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
                                        MyCustomDialog.Builder builder = new MyCustomDialog.Builder(c);
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
                                            Intent intent = new Intent(c, DashboardActivity.class);
                                            startActivity(intent);
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

    @Override
    public void onBackPressed() {
//        if (getFragmentManager().getBackStackEntryCount() > 0) {
//            getFragmentManager().popBackStackImmediate();
//        } else {
//            super.onBackPressed();
//        }
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
