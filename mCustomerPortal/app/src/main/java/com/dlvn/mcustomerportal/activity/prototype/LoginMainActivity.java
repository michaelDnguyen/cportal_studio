package com.dlvn.mcustomerportal.activity.prototype;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.afragment.prototype.LoginStep1Fragment;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;


/**
 * A login screen that offers login via email/password.
 */
public class LoginMainActivity extends BaseActivity implements OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        // Set up the login form.

        LoginStep1Fragment fragment = new LoginStep1Fragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.main_container, fragment);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
