package com.dlvn.mcustomerportal.activity;

import java.util.Random;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.CustomPref;
import com.dlvn.mcustomerportal.services.ServicesGenerator;
import com.dlvn.mcustomerportal.services.ServicesRequest;
import com.dlvn.mcustomerportal.services.model.BaseRequest;
import com.dlvn.mcustomerportal.services.model.User;
import com.dlvn.mcustomerportal.services.model.request.loginRequest;
import com.dlvn.mcustomerportal.services.model.response.loginResponse;
import com.dlvn.mcustomerportal.services.model.response.loginResult;
import com.dlvn.mcustomerportal.utils.DialogUtils;
import com.dlvn.mcustomerportal.utils.Utilities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @arthor nn.tai
 * @date Sep 30, 2016
 */
public class LoginActivity extends BaseActivity {

	private static final String TAG = "LoginActivity";

	Button btnLogin;
	AutoCompleteTextView actAccount;
	EditText edtPassword;

	boolean isLogin = false;
	ServicesRequest svRequester;
	String deviceid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		svRequester = ServicesGenerator.createService(ServicesRequest.class);
		deviceid = Utilities.getDeviceID(LoginActivity.this);

		actAccount = (AutoCompleteTextView) findViewById(R.id.actUsername);
		edtPassword = (EditText) findViewById(R.id.edtPassword);

		if (!TextUtils.isEmpty(CustomPref.getUserID(LoginActivity.this)))
			actAccount.setText(CustomPref.getUserID(LoginActivity.this));

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String loginName = actAccount.getText().toString();
				String password = edtPassword.getText().toString();

				if (validateLogin(loginName, password)) {
					doApprovalLogin(loginName, password);
				}
			}
		});
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

	private boolean validateLogin(String userName, String password) {
		if (TextUtils.isEmpty(userName)) {
			DialogUtils.showAlertDialog(LoginActivity.this, getString(R.string.message_error_username_login));
			return false;
		}

		if (TextUtils.isEmpty(password)) {
			DialogUtils.showAlertDialog(LoginActivity.this, getString(R.string.message_error_username_login));
			return false;
		}

		return true;
	}

	private void doApprovalLogin(final String userID, final String password) {
		// showProgressDialog("Login...");

		loginRequest data = new loginRequest();
		data.setAgentId(userID);
		data.setPassword(password);
		data.setDeviceId(Utilities.getDeviceID(LoginActivity.this));
		data.setDeviceToken("");
		data.setProject("mAGP");

		BaseRequest request = new BaseRequest();
		request.setJsonDataInput(data);

		Call<loginResponse> call = svRequester.PDLPadAuthLogin(request);
		call.enqueue(new Callback<loginResponse>() {

			@Override
			public void onResponse(Call<loginResponse> call, Response<loginResponse> res) {
				// TODO Auto-generated method stub
				if (res.isSuccessful()) {
					loginResponse response = res.body();
					if (response != null)
						if (response.getPDLPadAuthLoginResult() != null) {
							loginResult result = response.getPDLPadAuthLoginResult().get(0);
							if (result != null) {
//								User user = new User();
//								user.setUserID(userID);
//								user.setPassword(password);
//								user.setUserName(result.getUserName());
//								user.setAPIToken(result.getAPIToken());
//
//								// random value demo
//								int nhd = 0, tgt = 0, point = 0;
//								Random rand = new Random();
//								user.setNumberContract(rand.nextInt(10) % 10);
//								user.setAmountContract((rand.nextInt(1000) % 1000) * 1000000);
//								user.setPoint(rand.nextInt(10000));
//								user.setProposalNo("000" + rand.nextInt(99999));
//
//								CustomPref.saveUserLogin(LoginActivity.this, user);
//								CustomPref.setLogin(LoginActivity.this, true);
//								CustomPref.setRefreshing(LoginActivity.this, true);
//
//								Intent i = new Intent(getBaseContext(), HomeActivity.class);
//								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//								startActivity(i);
//								finish();
							}
						}
				}
			}

			@Override
			public void onFailure(Call<loginResponse> call, Throwable t) {
				// TODO Auto-generated method stub

			}
		});

	}
}
