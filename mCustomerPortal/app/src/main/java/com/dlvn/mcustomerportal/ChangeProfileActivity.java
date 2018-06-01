package com.dlvn.mcustomerportal;

import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.cPortalPref;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeProfileActivity extends BaseActivity {

	TextView tvTenKH, tvMaKH;
	EditText edtDTNha, edtDTMobile, edtDTCoQuan;
	Button btnCapNhat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_profile);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getViews();
		initData();
		setListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		tvMaKH = (TextView) findViewById(R.id.tvMaKhachHang);
		tvTenKH = (TextView) findViewById(R.id.tvTenKhachHang);

		edtDTNha = (EditText) findViewById(R.id.edtDTNha);
		edtDTMobile = (EditText) findViewById(R.id.edtDTMobile);
		edtDTCoQuan = (EditText) findViewById(R.id.edtDTCoQuan);

		btnCapNhat = (Button) findViewById(R.id.btnCapNhat);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (cPortalPref.haveLogin(this)) {
			tvTenKH.setText(cPortalPref.getUserName(this));
			tvMaKH.setText(cPortalPref.getUserProposal(this));
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnCapNhat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

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
