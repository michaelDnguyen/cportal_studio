package com.dlvn.mcustomerportal.activity.prototype;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.afragment.prototype.LoginStep1Fragment;
import com.dlvn.mcustomerportal.utils.listerner.OnRegisterFragmentListener;

public class RegisterActivity extends AppCompatActivity implements OnRegisterFragmentListener {

    LinearLayout lloBack;
    Button btnTiepTuc;
    TextView tvStep;
    AppCompatSeekBar sbStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();

        LoginStep1Fragment fragment = new LoginStep1Fragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.main_container, fragment);
        ft.addToBackStack(fragment.getClass().getName());
        ft.commit();

        setListener();

    }

    private void initViews() {

        lloBack = (LinearLayout) findViewById(R.id.lloBack);
        btnTiepTuc = (Button) findViewById(R.id.btnTiepTuc);
        tvStep = (TextView) findViewById(R.id.tvStep);
        sbStep = (AppCompatSeekBar) findViewById(R.id.sbStep);
    }

    private void setListener() {

        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

        sbStep.setProgress(percent);
    }
}
