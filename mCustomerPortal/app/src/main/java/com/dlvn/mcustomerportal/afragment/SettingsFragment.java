package com.dlvn.mcustomerportal.afragment;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.activity.ContactActivity;
import com.dlvn.mcustomerportal.activity.ListOfficeActivity;
import com.dlvn.mcustomerportal.activity.prototype.ClaimsHistoryActivity;
import com.dlvn.mcustomerportal.activity.prototype.DashboardActivity;
import com.dlvn.mcustomerportal.activity.prototype.SettingsActivity;
import com.dlvn.mcustomerportal.base.CPortalApplication;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.database.DataClient;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    View v;
    Button btnAccount, btnSecurity, btnLink, btnInfo, btnNotification, btnTutorial, btnHistory, btnContact, btnLogout, btnMap, btnPriceILP, btnClaims;
    TextView tvVersion;

    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_settings, container, false);

            btnAccount = v.findViewById(R.id.btnAccount);
            btnSecurity = v.findViewById(R.id.btnSecurity);
            btnLink = v.findViewById(R.id.btnLink);
            btnInfo = v.findViewById(R.id.btnInfo);
            btnNotification = v.findViewById(R.id.btnNotification);
            btnTutorial = v.findViewById(R.id.btnTutorial);
            btnHistory = v.findViewById(R.id.btnHistory);
            btnContact = v.findViewById(R.id.btnContact);
            btnLogout = v.findViewById(R.id.btnLogout);
            btnMap = v.findViewById(R.id.btnMap);
            btnPriceILP = v.findViewById(R.id.btnPriceILP);
            btnClaims = v.findViewById(R.id.btnClaims);

            tvVersion = v.findViewById(R.id.tvVersion);
            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                String version = pInfo.versionName;
                tvVersion.setText("Phiên bản " + version);
            } catch (PackageManager.NameNotFoundException e) {
                myLog.printTrace(e);
            }

            if (CustomPref.haveLogin(getActivity()))
                btnLogout.setVisibility(View.VISIBLE);
            else
                btnLogout.setVisibility(View.GONE);


            btnAccount.setOnClickListener(this);
            btnSecurity.setOnClickListener(this);
            btnLink.setOnClickListener(this);
            btnInfo.setOnClickListener(this);
            btnNotification.setOnClickListener(this);
            btnTutorial.setOnClickListener(this);
            btnHistory.setOnClickListener(this);
            btnContact.setOnClickListener(this);
            btnLogout.setOnClickListener(this);
            btnMap.setOnClickListener(this);
            btnPriceILP.setOnClickListener(this);
            btnClaims.setOnClickListener(this);
        }
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(getActivity(), SettingsActivity.class);

        switch (id) {
            case R.id.btnAccount:
                if (CustomPref.haveLogin(getActivity())) {
                    intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_ACCOUNT);
                    startActivity(intent);
                } else {
                    intent = null;
                    Utilities.showDialogAlertLoginNormal(getActivity());
                }
                break;
            case R.id.btnSecurity:
                if (CustomPref.haveLogin(getActivity())) {
                    intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_SECURITY);
                    startActivity(intent);
                } else {
                    intent = null;
                    Utilities.showDialogAlertLoginNormal(getActivity());
                }
                break;

            case R.id.btnLink:
                if (CustomPref.haveLogin(getActivity())) {
                    intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_LINK);
                    startActivity(intent);
                } else {
                    intent = null;
                    Utilities.showDialogAlertLoginNormal(getActivity());
                }
                break;

            case R.id.btnClaims:
                if (CustomPref.haveLogin(getActivity())) {
                    intent = new Intent(getActivity(), ClaimsHistoryActivity.class);
                    startActivity(intent);
                } else {
                    intent = null;
                    Utilities.showDialogAlertLoginNormal(getActivity());
                }
                break;

            case R.id.btnInfo:
                intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_INFO);
                startActivity(intent);
                break;

            case R.id.btnNotification:
                if (CustomPref.haveLogin(getActivity())) {
                    intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_NOTIFICATION);
                    startActivity(intent);
                } else {
                    intent = null;
                    Utilities.showDialogAlertLoginNormal(getActivity());
                }
                break;

            case R.id.btnTutorial:
                intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_TUTORIAL);
                startActivity(intent);
                break;

            case R.id.btnHistory:
                if (CustomPref.haveLogin(getActivity())) {
                    intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_HISTORY_PAYMENT);
                    startActivity(intent);
                } else {
                    intent = null;
                    Utilities.showDialogAlertLoginNormal(getActivity());
                }
                break;

            case R.id.btnMap:
                intent = null;
                if (CustomPref.haveLogin(getActivity())) {
                    Intent imap = new Intent(getActivity(), ListOfficeActivity.class);
                    startActivity(imap);
                } else
                    Utilities.showDialogAlertLoginNormal(getActivity());
                break;
            case R.id.btnPriceILP:
                intent.putExtra(SettingsActivity.KEY_SETTINGS_TYPE, SettingsActivity.SETTING_UNIT_PRICE);
                startActivity(intent);
                break;

            case R.id.btnContact:
                if (CustomPref.haveLogin(getActivity())) {
                    if (!TextUtils.isEmpty(CustomPref.getUserID(getActivity()))) {
                        Intent contactIntent = new Intent(getActivity(), ContactActivity.class);
                        startActivity(contactIntent);
                    } else {
                        Utilities.showDialogAlertLoginDLVNAcc(getActivity());
                        intent = null;
                    }
                } else {
                    Utilities.showDialogAlertLoginNormal(getActivity());
                    intent = null;
                }
                break;

            case R.id.btnLogout:
                intent = null;

                MyCustomDialog.Builder builder = new MyCustomDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.message_alert_logout))
                        .setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                CPortalApplication.getInstance().reLogin();
                                dialog.dismiss();

                            }
                        }).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;

            default:

                break;
        }
    }
}
