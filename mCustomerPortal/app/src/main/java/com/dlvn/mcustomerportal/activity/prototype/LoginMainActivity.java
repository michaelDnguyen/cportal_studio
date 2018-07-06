package com.dlvn.mcustomerportal.activity.prototype;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.crashlytics.android.Crashlytics;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.afragment.prototype.LoginInputUserNameFragment;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.User;
import com.dlvn.mcustomerportal.services.model.request.loginNewRequest;
import com.dlvn.mcustomerportal.services.model.response.loginNewResponse;
import com.dlvn.mcustomerportal.services.model.response.loginNewResult;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.listerner.OnFragmentInteractionListener;
import com.dlvn.mcustomerportal.utils.myLog;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginMainActivity extends BaseActivity implements OnFragmentInteractionListener {

    ServicesRequest svRequester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        svRequester = ServicesGenerator.createService(ServicesRequest.class);
        Fabric.with(this, new Crashlytics());
        // Set up the login form.

        if (!CustomPref.haveLogin(this)) {
            LoginInputUserNameFragment fragment = new LoginInputUserNameFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.main_container, fragment);
            ft.commit();
        } else {
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
                myLog.E("onActivityResult", "ON RESULT CALLED");
            }
        } catch (Exception e) {
            myLog.E("onActivityResult", e.toString());
        }
    }

    public void doCPLogin(User user, String Acction) {
        showProgressDialog("Login...");

        loginNewRequest data = new loginNewRequest();
        data.setUserLogin(user.getUserName());
        data.setDeviceID(Utilities.getDeviceID(LoginMainActivity.this));
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
                if (res.isSuccessful()) {
                    loginNewResponse response = res.body();
                    if (response != null)
                        if (response.getResponse() != null) {
                            loginNewResult result = response.getResponse();
                            if (result != null) {


                            }
                        }
                }
            }

            @Override
            public void onFailure(Call<loginNewResponse> call, Throwable t) {
                // TODO Auto-generated method stub
                myLog.E(t.getMessage());
            }
        });
    }
}
