package com.dlvn.mcustomerportal;

import com.dlvn.mcustomerportal.base.BaseActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePasswordActivity extends BaseActivity {

	EditText edtOldPass, edtNewPass, edtNewpassAgain;
	TextView tvError;
	Button btnCapNhat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_password);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getViews();
		initData();
		setListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		btnCapNhat = (Button) findViewById(R.id.btnCapNhat);

		tvError = (TextView) findViewById(R.id.tvError);
		
		edtOldPass = (EditText) findViewById(R.id.edtOldPassword);
		edtNewPass = (EditText) findViewById(R.id.edtNewPassword);
		edtNewpassAgain = (EditText) findViewById(R.id.edtNewPasswordAgain);
	}

	private void initData() {
		// TODO Auto-generated method stub
		
	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnCapNhat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String oldPass = edtOldPass.getText().toString();
				String newPass = edtNewPass.getText().toString();
				String newPassCon = edtNewpassAgain.getText().toString();

				if (!newPass.equals(newPassCon)) {
					tvError.setText("Mật khẩu mới không khớp , vui lòng nhập lại mật khẩu mới");
					tvError.setVisibility(View.VISIBLE);
				} else if (newPass.equals(oldPass)) {
					tvError.setText("Mật khẩu mới đã được sử dụng , vui lòng nhập lại mật khẩu mới");
					tvError.setVisibility(View.VISIBLE);
				} else if (newPass.length() < 8 || newPass.length() > 512) {
					tvError.setText("Mật khẩu mới quá ngắn(hoặc quá dài) , vui lòng chắc chắn rằng mật khẩu mới của bạn có từ 8 đến 512 ký tự");
					tvError.setVisibility(View.VISIBLE);
				} else {
					tvError.setVisibility(View.GONE);
					//TODO: process update password
				}
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
