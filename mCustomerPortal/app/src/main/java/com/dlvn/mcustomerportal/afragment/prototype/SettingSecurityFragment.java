package com.dlvn.mcustomerportal.afragment.prototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.CPSaveLogTask;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.dlvn.mcustomerportal.view.clearable_edittext.ClearableEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingSecurityFragment extends Fragment {

    private static final String TAG = "SettingSecurityFragment";

    View v;
    TextView tvVerifyEmail, tvVerifyPhone, tvPassword;
    Switch swVerify2Layer, swFinger;
    ClearableEditText cedtEmail, cedtPhone;

    ClientProfile currentUser = null;
    ServicesRequest svRequester;

    public SettingSecurityFragment() {
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
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_settings_security, container, false);

            cedtEmail = v.findViewById(R.id.cedtEmail);
            cedtEmail.setEnabled(false);
            tvVerifyEmail = v.findViewById(R.id.tvVerifyEmail);
            cedtPhone = v.findViewById(R.id.cedtPhone);
            cedtPhone.setEnabled(false);
            tvVerifyPhone = v.findViewById(R.id.tvVerifyPhone);
            tvPassword = v.findViewById(R.id.tvPassword);

            swVerify2Layer = v.findViewById(R.id.swVerify2Layer);
            swFinger = v.findViewById(R.id.swFinger);

            initDatas();
            setListener();
        }

        return v;
    }

    private void initDatas() {
        if (CustomPref.haveLogin(getActivity())) {

            cedtEmail.setText(CustomPref.getEmail(getActivity()));
            if (CustomPref.haveVerifyEmail(getActivity())) {
                tvVerifyEmail.setText("Đã xác thực");
                tvVerifyEmail.setTextColor(getResources().getColor(R.color.green));
                tvVerifyEmail.setEnabled(false);
            } else {
                tvVerifyEmail.setText("Xác thực ngay");
                tvVerifyEmail.setTextColor(getResources().getColor(R.color.red));
                tvVerifyEmail.setEnabled(true);
                cedtEmail.setFocusable(false);
            }

            currentUser = CustomPref.getUserLogin(getActivity());

            cedtPhone.setText(CustomPref.getPhoneNumber(getActivity()));

            if (CustomPref.haveVerifyCellphone(getActivity())) {
                tvVerifyPhone.setText("Đã xác thực");
                tvVerifyPhone.setTextColor(getResources().getColor(R.color.green));
                tvVerifyPhone.setEnabled(false);
            } else {
                tvVerifyPhone.setText("Xác thực ngay");
                tvVerifyPhone.setTextColor(getResources().getColor(R.color.red));
                tvVerifyPhone.setEnabled(true);
                cedtPhone.setFocusable(false);
            }

            if (CustomPref.haveVerifyTwoFA(getActivity()))
                swVerify2Layer.setChecked(true);
            else
                swVerify2Layer.setChecked(false);

            if (CustomPref.haveAuthFinger(getActivity()))
                swFinger.setChecked(true);
            else
                swFinger.setChecked(false);
        }
    }

    private void setListener() {
        swFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swFinger.isChecked()) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //Fingerprint API only available on from Android 6.0 (M)
                        FingerprintManager fingerprintManager = (FingerprintManager) getActivity().getSystemService(Context.FINGERPRINT_SERVICE);
                        if (!fingerprintManager.isHardwareDetected()) {
                            // Device doesn't support fingerprint authentication
                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                    .setMessage("Thiết bị không hỗ trợ chức năng này!")
                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            swFinger.setChecked(false);
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                            // User hasn't enrolled any fingerprints to authenticate with
                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                    .setMessage("Anh chị chưa tạo vân tay cho thiết bị! Anh chị cần tạo vân tay cho thiết bị trước khi bật chức năng này")
                                    .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            swFinger.setChecked(false);
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        } else {
                            // Everything is ready for fingerprint authentication
                            MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                    .setMessage("Anh chị có muốn dùng vân tay để đăng nhập vào ứng dụng này không?")
                                    .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            swFinger.setChecked(true);
                                            CustomPref.setAuthFinger(getActivity(), true);
                                            dialog.dismiss();
                                        }
                                    }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            swFinger.setChecked(false);
                                            dialog.dismiss();
                                        }
                                    }).create();
                            dialog.show();
                        }
                    }
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                            .setMessage("Bạn có chắc chắn muốn tắt chức năng gia tăng bảo mật này không?")
                            .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    swFinger.setChecked(false);
                                    CustomPref.setAuthFinger(getActivity(), false);
                                    dialog.dismiss();
                                }
                            }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    swFinger.setChecked(true);
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
                myLog.e(TAG, "OnClick status Finger switch = " + swFinger.isChecked());
            }
        });

        swVerify2Layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swVerify2Layer.isChecked()) {
                    if (!CustomPref.haveVerifyCellphone(getActivity())) {

                        MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                                .setMessage("Bạn chưa xác thực số điện thoại. Vui lòng thực hiện xác thực số điện thoại để sử dụng tính năng này.")
                                .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        swVerify2Layer.setChecked(false);
                                        //Todo: process activity for 2FA action
                                        dialog.dismiss();
                                    }
                                }).create();
                        dialog.show();
                    } else
                        new doUpdate2FATask(getActivity(), currentUser, Constant.ACTION_ACTIVE).execute();
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                            .setMessage("Bạn có chắc chắn muốn tắt chức năng gia tăng bảo mật này không?")
                            .setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new doUpdate2FATask(getActivity(), currentUser, Constant.ACTION_NON_ACTIVE).execute();
                                    //Todo: off this function
                                    dialog.dismiss();
                                }
                            }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    swVerify2Layer.setChecked(true);
                                    dialog.dismiss();
                                }
                            }).create();
                    dialog.show();
                }
            }
        });

        tvVerifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = cedtPhone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    InputOTPFragment fragment = InputOTPFragment.newInstance(InputOTPFragment.VALID_OTP_PHONE, null);
                    fragmentTransaction.add(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(fragment.getClass().getName());
                    fragmentTransaction.commit();
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                            .setMessage("Vui lòng nhập số điện thoại cần xác thực.")
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

        tvVerifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = cedtEmail.getText().toString();
                if (!TextUtils.isEmpty(email)) {
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    InputOTPFragment fragment = InputOTPFragment.newInstance(InputOTPFragment.VALID_OTP_EMAIL, null);
                    fragmentTransaction.add(R.id.main_container, fragment);
                    fragmentTransaction.addToBackStack(fragment.getClass().getName());
                    fragmentTransaction.commit();
                } else {
                    MyCustomDialog dialog = new MyCustomDialog.Builder(getActivity())
                            .setMessage("Vui lòng nhập địa chỉ email cần xác thực.")
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

    @Override
    public void onStop() {
        super.onStop();
        String email = cedtEmail.getText().toString();
        String phone = cedtPhone.getText().toString();
        if (Utilities.validateEmail(email))
            CustomPref.setEmail(getActivity(), email);
        if (Utilities.isPhoneNumberValid(phone))
            CustomPref.setPhoneNumber(getActivity(), phone);

        new CPSaveLogTask(getActivity(), Constant.CPLOG_SETTINGSECURITY_CLOSE).execute();
    }

    private class doUpdate2FATask extends AsyncTask<Void, Void, Response<loginNewResponse>> {

        ProgressDialog process;
        ClientProfile user;
        Context context;
        String active;

        public doUpdate2FATask(Context c, ClientProfile user, String active) {
            context = c;
            this.user = user;
            this.active = active;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            process = new ProgressDialog(getActivity());
            process.setMessage("Cập nhật...");
            process.setCanceledOnTouchOutside(false);
            process.show();
        }

        @Override
        protected Response<loginNewResponse> doInBackground(Void... voids) {

            Response<loginNewResponse> res = null;

            try {
                loginNewRequest data = new loginNewRequest();

                if (!TextUtils.isEmpty(user.getLoginName()))
                    data.setUserLogin(user.getLoginName());
                else
                    data.setUserLogin(user.getClientID());
                data.setClientID(user.getClientID());

                data.setFullName(user.getFullName());
                data.setLinkFB(user.getLinkFaceBook());
                data.setLinkGMail(user.getLinkGmail());

                data.setTwoFA(active);

                data.setApiToken(user.getaPIToken());
                data.setDeviceID(Utilities.getDeviceID(getActivity()));
                data.setOS(Utilities.getDeviceOS());
                data.setProject(Constant.Project_ID);
                data.setAction(Constant.LOGIN_ACTION_UPDATE2FA);
                data.setAuthentication(Constant.Project_Authentication);

                final BaseRequest request = new BaseRequest();
                request.setJsonDataInput(data);

                Call<loginNewResponse> call = svRequester.CPRegisterAccount(request);
                res = call.execute();
            } catch (IOException e) {
                myLog.printTrace(e);
            }
            return res;
        }

        @Override
        protected void onPostExecute(Response<loginNewResponse> res) {
            super.onPostExecute(res);
            process.dismiss();

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

                                    if (result.getErrLog().equals(Constant.ERR_CPLOGIN_SUCCESSFUL)) {
                                        CustomPref.setLogin(getActivity(), true);
                                        CustomPref.saveUserLogin(getActivity(), user);
                                    }

                                    if (active.equalsIgnoreCase(Constant.ACTION_ACTIVE)) {
                                        swVerify2Layer.setChecked(true);
                                        CustomPref.setVerifyTwoFA(context, Constant.ACTION_ACTIVE);
                                    } else {
                                        swVerify2Layer.setChecked(false);
                                        CustomPref.setVerifyTwoFA(context, Constant.ACTION_NON_ACTIVE);
                                    }

                                    if (result.getClientProfile() != null) {

                                        ClientProfile user = result.getClientProfile().get(0);

                                        if (TextUtils.isEmpty(user.getaPIToken()))
                                            user.setaPIToken(result.getNewAPIToken());

                                        //Save Login Profile & Token
                                        CustomPref.setLogin(getActivity(), true);
                                        CustomPref.saveUserLogin(getActivity(), user);
                                    }
                                }
                            }
                        }
                } else
                    swVerify2Layer.setChecked(false);
            } catch (Exception e) {
                myLog.printTrace(e);
                swVerify2Layer.setChecked(false);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        new CPSaveLogTask(getActivity(), Constant.CPLOG_SETTINGSECURITY_OPEN).execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
