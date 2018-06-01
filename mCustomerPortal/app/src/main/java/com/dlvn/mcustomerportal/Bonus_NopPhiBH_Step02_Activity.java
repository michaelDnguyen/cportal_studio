package com.dlvn.mcustomerportal;

import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.cPortalPref;
import com.dlvn.mcustomerportal.utils.Utilities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Bonus_NopPhiBH_Step02_Activity extends BaseActivity {

	Button btnXacNhan;

	TextView tvTenKhachHang, tvMaKhachHang, tvDiem;
	TextView tvHopDong, tvDiemSuDung, tvDiemConLai, tvPhiBaoHiem, tvTamUngDongPhi, tvTamUng;

	EditText edtEmail, edtPhone;

	String soHopDong, phiBaoHiem, tamUngDP, tamUng;
	float diemTichLuy = 0, diemSuDung = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bonus__nop_phi_bh__step02);
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

		tvHopDong = (TextView) findViewById(R.id.tvHopDong);
		tvDiemSuDung = (TextView) findViewById(R.id.tvDiemSuDung);
		tvDiemConLai = (TextView) findViewById(R.id.tvDiemConLai);
		tvPhiBaoHiem = (TextView) findViewById(R.id.tvPhiBaoHiem);
		tvTamUngDongPhi = (TextView) findViewById(R.id.tvTamUngDongPhi);
		tvTamUng = (TextView) findViewById(R.id.tvTamUng);

		btnXacNhan = (Button) findViewById(R.id.btnTiep);
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
			phiBaoHiem = getIntent().getStringExtra(Constant.NOPPHI_PHIBAOHIEM);
			tamUngDP = getIntent().getStringExtra(Constant.NOPPHI_TAMUNG_DONGPHI);
			tamUng = getIntent().getStringExtra(Constant.NOPPHI_TAMUNG);

			// init data
			tvHopDong.setText(soHopDong);
			tvPhiBaoHiem.setText(phiBaoHiem);
			tvTamUngDongPhi.setText(tamUngDP);
			tvTamUng.setText(tamUng);

			if (!TextUtils.isEmpty(phiBaoHiem))
				diemSuDung += Float.parseFloat(phiBaoHiem.replace(",", "")) / 1000;
			if (!TextUtils.isEmpty(tamUngDP))
				diemSuDung += Float.parseFloat(tamUngDP.replace(",", "")) / 1000;
			if (!TextUtils.isEmpty(tamUng))
				diemSuDung += Float.parseFloat(tamUng.replace(",", "")) / 1000;

			tvDiemSuDung.setText(diemSuDung + "");
			tvDiemConLai.setText((diemTichLuy - diemSuDung) + "");

		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		btnXacNhan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Bonus_NopPhiBH_Step02_Activity.this, Bonus_NopPhiBH_Step03_Activity.class);
				intent.putExtra(Constant.NOPPHI_SOHOPDONG, soHopDong);
				intent.putExtra(Constant.NOPPHI_DIEMSUDUNG, diemSuDung);
				startActivity(intent);

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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == android.R.id.home) {
			// finish the activity
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
