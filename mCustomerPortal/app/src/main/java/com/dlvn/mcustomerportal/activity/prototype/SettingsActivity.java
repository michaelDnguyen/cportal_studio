package com.dlvn.mcustomerportal.activity.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.afragment.FundUnitPriceFragment;
import com.dlvn.mcustomerportal.afragment.InfoGeneralFragment;
import com.dlvn.mcustomerportal.afragment.ProductInfoFragment;
import com.dlvn.mcustomerportal.afragment.prototype.LienHeFragment;
import com.dlvn.mcustomerportal.afragment.prototype.SettingAccountFragment;
import com.dlvn.mcustomerportal.afragment.prototype.SettingLinkFragment;
import com.dlvn.mcustomerportal.afragment.prototype.SettingNotificationFragment;
import com.dlvn.mcustomerportal.afragment.prototype.SettingSecurityFragment;
import com.dlvn.mcustomerportal.afragment.prototype.SettingTransHistoryFragment;
import com.dlvn.mcustomerportal.afragment.prototype.SettingTutorialFragment;
import com.dlvn.mcustomerportal.afragment.prototype.UpdateAccountFragment;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.model.response.ClientProfile;
import com.dlvn.mcustomerportal.utils.myLog;

public class SettingsActivity extends BaseActivity {

    public static final String KEY_SETTINGS_TYPE = "Settings_Type";

    public static final int SETTING_ACCOUNT = 151;
    public static final int SETTING_SECURITY = 152;
    public static final int SETTING_LINK = 153;
    public static final int SETTING_NOTIFICATION = 154;
    public static final int SETTING_TUTORIAL = 155;
    public static final int SETTING_HISTORY_PAYMENT = 156;
    public static final int SETTING_CONTACT = 157;
    public static final int SETTING_INFO = 158;
    public static final int SETTING_LOGOUT = 159;
    public static final int SETTING_UNIT_PRICE = 160;

    public static final int SETTING_UPDATE_PROFILE = 113;

    int settings_type = 0;

    LinearLayout lloBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lloBack = findViewById(R.id.lloBack);
        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(KEY_SETTINGS_TYPE))
                settings_type = getIntent().getIntExtra(KEY_SETTINGS_TYPE, 0);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (settings_type) {

            case SETTING_ACCOUNT:
                SettingAccountFragment fragment = new SettingAccountFragment();
                fragmentTransaction.replace(R.id.main_container, fragment);
                fragmentTransaction.commit();
                break;

            case SETTING_SECURITY:
                SettingSecurityFragment fmsecurity = new SettingSecurityFragment();
                fragmentTransaction.replace(R.id.main_container, fmsecurity);
                fragmentTransaction.commit();
                break;

            case SETTING_LINK:
                SettingLinkFragment fmlink = new SettingLinkFragment();
                fragmentTransaction.replace(R.id.main_container, fmlink);
                fragmentTransaction.commit();
                break;

            case SETTING_NOTIFICATION:
                SettingNotificationFragment fmNotify = new SettingNotificationFragment();
                fragmentTransaction.replace(R.id.main_container, fmNotify);
                fragmentTransaction.commit();
                break;

            case SETTING_TUTORIAL:
                SettingTutorialFragment fmTutorial = new SettingTutorialFragment();
                fragmentTransaction.replace(R.id.main_container, fmTutorial);
                fragmentTransaction.commit();
                break;

            case SETTING_UNIT_PRICE:
                FundUnitPriceFragment fmPrice = new FundUnitPriceFragment();
                fragmentTransaction.replace(R.id.main_container, fmPrice);
                fragmentTransaction.commit();
                break;

            case SETTING_HISTORY_PAYMENT:
                SettingTransHistoryFragment fmTrans = new SettingTransHistoryFragment();
                fragmentTransaction.replace(R.id.main_container, fmTrans);
                fragmentTransaction.commit();
                break;

            case SETTING_CONTACT:
                LienHeFragment fmLienHe = new LienHeFragment();
                fragmentTransaction.replace(R.id.main_container, fmLienHe);
                fragmentTransaction.commit();
                break;

            case SETTING_INFO:

                break;

            case SETTING_LOGOUT:

                break;
            case SETTING_UPDATE_PROFILE:

                ClientProfile user = CustomPref.getUserLogin(SettingsActivity.this);
                UpdateAccountFragment updateAccountFragment = UpdateAccountFragment.newInstance(user, false);
                fragmentTransaction.replace(R.id.main_container, updateAccountFragment);
                fragmentTransaction.commit();
                break;

            default:
                Toast.makeText(SettingsActivity.this, "Không tìm thấy Settings tương ứng!", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
                myLog.e("onActivityResult", "ON RESULT CALLED");
            }
        } catch (Exception e) {
            myLog.e("onActivityResult", e.toString());
        }
    }
}
