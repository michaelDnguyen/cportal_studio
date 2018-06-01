package com.dlvn.mcustomerportal;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.cPortalPref;
import com.dlvn.mcustomerportal.utils.Utilities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Bonus_NopPhiBH_Step01_Activity extends BaseActivity {

	Spinner spnHopDong;

	TextView tvTenKhachHang, tvMaKhachHang, tvDiem;
	EditText edtPhiDinhKy, edtKhoanTamUng, edtGiaTriHoanLai;
	Button btnTiep;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bonus__nop_phi_bh);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getViews();
		initDatas();
		setListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		spnHopDong = (Spinner) findViewById(R.id.spnHopDong);

		tvMaKhachHang = (TextView) findViewById(R.id.tvMaKhachHang);
		tvTenKhachHang = (TextView) findViewById(R.id.tvTenKhachHang);
		tvDiem = (TextView) findViewById(R.id.tvDiem);

		edtPhiDinhKy = (EditText) findViewById(R.id.edtPhiBaoHiem);
		edtKhoanTamUng = (EditText) findViewById(R.id.edtKhoanTamUng);
		edtGiaTriHoanLai = (EditText) findViewById(R.id.edtGiaTriHoanLai);

		btnTiep = (Button) findViewById(R.id.btnTiep);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		list.add("00658947");
		list.add("00864521");
		list.add("00247846");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnHopDong.setAdapter(dataAdapter);

		if (cPortalPref.haveLogin(this)) {
			tvTenKhachHang.setText(cPortalPref.getUserName(this));
			tvMaKhachHang.setText(cPortalPref.getUserProposal(this));
			tvDiem.setText(cPortalPref.getUserPoint(this) + "");
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		edtPhiDinhKy.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().equals(current) && !TextUtils.isEmpty(s.toString())) {
					edtPhiDinhKy.removeTextChangedListener(this);

					String cleanString = s.toString().replaceAll("[$,.]", "");
					double parsed = Double.parseDouble(cleanString);
					String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
					current = formatted;
					edtPhiDinhKy.setText(formatted);
					edtPhiDinhKy.setSelection(formatted.length());

					edtPhiDinhKy.addTextChangedListener(this);
				}
			}
		});

		edtKhoanTamUng.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().equals(current) && !TextUtils.isEmpty(s.toString())) {
					edtKhoanTamUng.removeTextChangedListener(this);

					String cleanString = s.toString().replaceAll("[$,.]", "");
					double parsed = Double.parseDouble(cleanString);
					String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
					current = formatted;
					edtKhoanTamUng.setText(formatted);
					edtKhoanTamUng.setSelection(formatted.length());

					edtKhoanTamUng.addTextChangedListener(this);
				}
			}
		});

		edtGiaTriHoanLai.addTextChangedListener(new TextWatcher() {
			private String current = "";

			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (!s.toString().equals(current) && !TextUtils.isEmpty(s.toString())) {
					edtGiaTriHoanLai.removeTextChangedListener(this);

					String cleanString = s.toString().replaceAll("[$,.]", "");
					double parsed = Double.parseDouble(cleanString);
					String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
					current = formatted;
					edtGiaTriHoanLai.setText(formatted);
					edtGiaTriHoanLai.setSelection(formatted.length());

					edtGiaTriHoanLai.addTextChangedListener(this);
				}
			}
		});
		
		btnTiep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String shd = (String)spnHopDong.getSelectedItem();
				String phibh = edtPhiDinhKy.getText().toString();
				String tamungdongphi = edtKhoanTamUng.getText().toString();
				String tamung = edtGiaTriHoanLai.getText().toString();
				
				Intent intent = new Intent(Bonus_NopPhiBH_Step01_Activity.this, Bonus_NopPhiBH_Step02_Activity.class);
				intent.putExtra(Constant.NOPPHI_SOHOPDONG, shd);
				intent.putExtra(Constant.NOPPHI_PHIBAOHIEM, phibh);
				intent.putExtra(Constant.NOPPHI_TAMUNG_DONGPHI, tamungdongphi);
				intent.putExtra(Constant.NOPPHI_TAMUNG, tamung);
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
