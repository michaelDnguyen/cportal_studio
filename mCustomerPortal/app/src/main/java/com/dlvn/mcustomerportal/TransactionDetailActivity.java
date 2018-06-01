package com.dlvn.mcustomerportal;

import com.dlvn.mcustomerportal.adapter.TransactionDetailListAdapter;
import com.dlvn.mcustomerportal.adapter.model.TransactionModel;
import com.dlvn.mcustomerportal.base.BaseActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class TransactionDetailActivity extends BaseActivity {

	TextView tvMaGD, tvNgayGD;
	ListView lvData;
	
	TransactionDetailListAdapter adapter;
	TransactionModel transactionItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getViews();
		initData();
		setListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		lvData = (ListView) findViewById(R.id.lvData);
		
		tvNgayGD = (TextView) findViewById(R.id.tvNgayGD);
		tvMaGD = (TextView) findViewById(R.id.tvMaGD);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if(getIntent().getExtras().containsKey("transaction_item")){
			transactionItem = getIntent().getParcelableExtra("transaction_item");
		}
		
		if(transactionItem != null){
			tvMaGD.setText(transactionItem.getMaGiaoDich());
			tvNgayGD.setText(transactionItem.getNgayGD());
			
			if(transactionItem.getLstDetail() != null){
				adapter = new TransactionDetailListAdapter(this, transactionItem.getLstDetail(), transactionItem.getSoHD(), transactionItem.getNgayGD());
				lvData.setAdapter(adapter);
			}
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		
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
