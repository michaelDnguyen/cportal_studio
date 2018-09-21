package com.dlvn.mcustomerportal.afragment.prototype;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

/**
 * A placeholder fragment containing a simple view.
 */
public class SettingSecurityFragment extends Fragment {

    private static final String TAG = "SettingSecurityFragment";

    View v;
    TextView tvEmail, tvVerifyEmail, tvPhone, tvVerifyPhone, tvPassword;
    Switch swVerify2Layer, swFinger;

    public SettingSecurityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_settings_security, container, false);

            tvEmail = v.findViewById(R.id.tvEmail);
            tvVerifyEmail = v.findViewById(R.id.tvVerifyEmail);
            tvPhone = v.findViewById(R.id.tvPhone);
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
            if (!TextUtils.isEmpty(CustomPref.getEmail(getActivity()))) {
                tvEmail.setText(CustomPref.getEmail(getActivity()));
                tvVerifyEmail.setText("Đã xác thực");
                tvVerifyEmail.setTextColor(getResources().getColor(R.color.green));
            } else {
                tvVerifyEmail.setText("Xác thực ngay");
                tvVerifyEmail.setTextColor(getResources().getColor(R.color.red));
            }

            if (!TextUtils.isEmpty(CustomPref.getPhoneNumber(getActivity()))) {
                tvPhone.setText(CustomPref.getPhoneNumber(getActivity()));
                tvVerifyPhone.setText("Đã xác thực");
                tvVerifyPhone.setTextColor(getResources().getColor(R.color.green));
            } else {
                tvVerifyPhone.setText("Xác thực ngay");
                tvVerifyPhone.setTextColor(getResources().getColor(R.color.red));
            }

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
                                    .setTitle("Thông báo")
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
                                    .setTitle("Thông báo")
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
                                    .setTitle("Thông báo")
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
                            .setTitle("Thông báo")
                            .setMessage("Anh chị có muốn tắt chức năng xác thực bằng vân tay này không?")
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

                myLog.E(TAG, "ONclick status Finger switch = " + swFinger.isChecked());
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
