package com.dlvn.mcustomerportal.activity.prototype;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.afragment.prototype.LoginStep1Fragment;
import com.dlvn.mcustomerportal.utils.listerner.onRegisterFragmentListener;

public class RegisterActivity extends AppCompatActivity implements onRegisterFragmentListener{

    LinearLayout lloBack;
    Button btnTiepTuc;
    TextView tvStep;
    AppCompatSeekBar sbStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        LoginStep1Fragment fragment = new LoginStep1Fragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.main_container, fragment);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();


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
    public void onRegisterListener(int percent) {

    }
}
