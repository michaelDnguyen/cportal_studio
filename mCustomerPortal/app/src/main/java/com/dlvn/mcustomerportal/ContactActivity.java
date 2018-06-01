package com.dlvn.mcustomerportal;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.utils.Utilities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class ContactActivity extends BaseActivity {

	LinearLayout lloCall;
	
	Spinner spnLoaiLienHe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getViews();
		initDatas();
		setListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		lloCall = (LinearLayout) findViewById(R.id.lloCall);
		spnLoaiLienHe = (Spinner) findViewById(R.id.spnLoaiLienHe);
	}

	private void initDatas() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		list.add("Yêu cầu thu phí");
		list.add("Yêu cầu thay đổi địa chỉ");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnLoaiLienHe.setAdapter(dataAdapter);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		lloCall.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Utilities.actionCallPhoneNumber(ContactActivity.this, Constant.PHONE_CUSTOMER_SERVICE);
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
