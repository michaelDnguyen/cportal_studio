package com.dlvn.mcustomerportal;

import java.util.ArrayList;
import java.util.List;

import com.dlvn.mcustomerportal.adapter.model.ContractModel;
import com.dlvn.mcustomerportal.base.BaseActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ContractDetailActivity extends BaseActivity {

	public static final String KEY_PROPOSAL = "";

	Spinner spnHopDong;
	
	ContractModel itemHopDong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contract_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getViews();
		initData();
		setListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		spnHopDong = (Spinner) findViewById(R.id.spnHopDong);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (getIntent().getExtras().containsKey("CONTRACT_DETAIL"))
			itemHopDong = getIntent().getParcelableExtra("CONTRACT_DETAIL");

		if(itemHopDong != null)
			setTitle("Chi tiết HĐ " + itemHopDong.getSoHopDong());
		
		List<String> list = new ArrayList<String>();
		list.add("00658947");
		list.add("00864521");
		list.add("00247846");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnHopDong.setAdapter(dataAdapter);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		spnHopDong.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String itemSelected = (String) parent.getAdapter().getItem(position);
				if(!TextUtils.isEmpty(itemSelected))
					setTitle("Chi tiết HĐ " + itemSelected);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
