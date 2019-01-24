package com.dlvn.mcustomerportal.activity.prototype;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.afragment.prototype.LoginInputUserNameFragment;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;


/**
 * A login screen that offers login via email/password.
 */
public class LoginMainActivity extends BaseActivity {

    // Request code for READ_CONTACTS. It can be any number > 0.
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLog.e("Oncreate LoginMainActivity");

        setContentView(R.layout.activity_login_main);
        Fabric.with(this, new Crashlytics());

        if (TextUtils.isEmpty(CustomPref.getFirebaseToken(this)))
            if (FirebaseInstanceId.getInstance().getInstanceId() != null) {
                Task<InstanceIdResult> resultTask = FirebaseInstanceId.getInstance().getInstanceId();
                resultTask.addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        myLog.e("onSuccess Firebase token " + instanceIdResult.getToken());
                        CustomPref.setFirebaseToken(LoginMainActivity.this, instanceIdResult.getToken());
                    }
                });
            }

//check permission for read contact
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
//            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
//        } else {
        startInitScreen();
//        }

        /**
         * Get Hash key for Facebook
         */
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.dlvn.mcustomerportal",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                myLog.e("KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void startInitScreen() {
        // Set up the login form.
        if (!CustomPref.haveLogin(this)) {
            myLog.e("LoginMain not login");
            LoginInputUserNameFragment fragment = new LoginInputUserNameFragment();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_container, fragment);
            ft.commit();
        } else {
            myLog.e("LoginMain has login");
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        myLog.e("dispatchTouchEvent");
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
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                startInitScreen();
            } else {
                MyCustomDialog dialog = new MyCustomDialog.Builder(this)
                        .setTitle(getString(R.string.title_alert))
                        .setMessage(getString(R.string.message_alert_permission_contact))
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
