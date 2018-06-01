package com.dlvn.mcustomerportal;

import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.cPortalPref;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class Bonus_NopPhiBH_Step03_Activity extends BaseActivity {

	Button btnQuayLai;

	TextView tvTenKhachHang, tvMaKhachHang, tvDiem;
	TextView tvHopDong, tvDiemSuDung, tvDiemConLai, tvPhiBaoHiem, tvTamUngDongPhi, tvTamUng;
	
	float diemTichLuy = 0, diemSuDung = 0;
	String soHopDong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bonus__nop_phi_bh__step03);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	
		getViews();
		initDatas();
		setListener();
	}
	
	private void getViews() {
		// TODO Auto-generated method stub
		tvMaKhachHang = (TextView) findViewById(R.id.tvMaKhachHang);
		tvTenKhachHang = (TextView) findViewById(R.id.tvTenKhachHang);
		tvDiem = (TextView) findViewById(R.id.tvDiem);

		tvHopDong = (TextView) findViewById(R.id.tvSoHopDong);
		tvDiemSuDung = (TextView) findViewById(R.id.tvDiemSuDung);
		tvDiemConLai = (TextView) findViewById(R.id.tvDiemConLai);

		btnQuayLai = (Button) findViewById(R.id.btnQuayLai);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		if (cPortalPref.haveLogin(this)) {
			tvTenKhachHang.setText(cPortalPref.getUserName(this));
			tvMaKhachHang.setText(cPortalPref.getUserProposal(this));
			diemTichLuy = (float) cPortalPref.getUserPoint(this);
			tvDiem.setText(diemTichLuy + "");
		}
		
		if (getIntent().getExtras().containsKey(Constant.NOPPHI_SOHOPDONG)) {
			soHopDong = getIntent().getStringExtra(Constant.NOPPHI_SOHOPDONG);
			diemSuDung = getIntent().getFloatExtra(Constant.NOPPHI_DIEMSUDUNG, 0);

			// init data
			tvHopDong.setText(soHopDong);
			tvDiemSuDung.setText(diemSuDung+"");
			tvDiemConLai.setText((diemTichLuy - diemSuDung) + "");
			tvDiem.setText((diemTichLuy - diemSuDung) + "");

		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == android.R.id.home) {
			// finish the activity
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
