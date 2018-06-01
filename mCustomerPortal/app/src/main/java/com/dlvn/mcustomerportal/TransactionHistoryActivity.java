package com.dlvn.mcustomerportal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dlvn.mcustomerportal.adapter.TransactionListAdapter;
import com.dlvn.mcustomerportal.adapter.model.TransactionDetailModel;
import com.dlvn.mcustomerportal.adapter.model.TransactionModel;
import com.dlvn.mcustomerportal.base.BaseActivity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Activity history transaction
 * 
 * @author nn.tai
 * @date Jan 4, 2018
 */
public class TransactionHistoryActivity extends BaseActivity {

	SwipeRefreshLayout swipeLayout;
	ListView lvTransaction;
	Spinner spnTinhTrang;
	Button btnTimKiem;
	TextView tvTuNgay, tvDenNgay;

	TransactionListAdapter adapter;
	List<TransactionModel> lstData;

	SimpleDateFormat dateFormat;
	Calendar fromDate, toDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_history);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getViews();
		initData();
		setListener();

	}

	private void getViews() {
		// TODO Auto-generated method stub
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		lvTransaction = (ListView) findViewById(R.id.lvTransaction);
		spnTinhTrang = (Spinner) findViewById(R.id.spnTinhTrang);

		btnTimKiem = (Button) findViewById(R.id.btnTimkiem);
		tvTuNgay = (TextView) findViewById(R.id.tvTuNgay);
		tvDenNgay = (TextView) findViewById(R.id.tvDenNgay);
	}

	private void initData() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		list.add("Tất cả");
		list.add("Hoàn tất");
		list.add("Không thành công");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnTinhTrang.setAdapter(dataAdapter);

		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		fromDate = Calendar.getInstance();
		toDate = Calendar.getInstance();
	}

	private void initDataSearch() {
		List<TransactionDetailModel> lstDetail = new ArrayList<>();
		lstDetail.add(new TransactionDetailModel("Phí bảo hiểm định kỳ", "6,500,000", "Hoàn tất"));
		lstDetail.add(new TransactionDetailModel("Khoản tạm ứng từ giá trị hoàn lại", "3,120,000", "Hoàn tất"));

		List<TransactionDetailModel> lstDetail2 = new ArrayList<>();
		lstDetail2.add(new TransactionDetailModel("Phí bảo hiểm định kỳ", "14,684,000", "Hoàn tất"));

		List<TransactionDetailModel> lstDetail3 = new ArrayList<>();
		lstDetail3.add(new TransactionDetailModel("Phí bảo hiểm định kỳ", "7,500,000", "Hoàn tất"));
		lstDetail3.add(new TransactionDetailModel("Khoản tạm ứng từ giá trị hoàn lại", "2,420,000", "Hoàn tất"));

		List<TransactionDetailModel> lstDetail4 = new ArrayList<>();
		lstDetail4.add(new TransactionDetailModel("Khoản tạm ứng từ giá trị hoàn lại", "5,720,000", "Hoàn tất"));

		lstData = new ArrayList<>();
		lstData.add(new TransactionModel("125895", "00886596", "9,620,000", "Hoàn tất", "14/12/2017", lstDetail));
		lstData.add(new TransactionModel("12468", "00956324", "860,000", "Hoàn tất", "14/12/2017", null));
		lstData.add(new TransactionModel("12145", "00886596", "14,684,000", "Hoàn tất", "14/12/2017", lstDetail2));
		lstData.add(new TransactionModel("11864", "00956324", "10,920,000", "Hoàn tất", "14/12/2017", lstDetail3));
		lstData.add(new TransactionModel("10586", "00369544", "5,720,000", "Hoàn tất", "14/12/2017", lstDetail2));

		adapter = new TransactionListAdapter(this, lstData);
		lvTransaction.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	private void setListener() {
		// TODO Auto-generated method stub
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				swipeLayout.setRefreshing(false);
			}
		});

		btnTimKiem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initDataSearch();
			}
		});

		tvTuNgay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int year = fromDate.get(Calendar.YEAR);
				int month = fromDate.get(Calendar.MONTH);
				int day = fromDate.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dialog = new DatePickerDialog(TransactionHistoryActivity.this,
						android.R.style.Theme_Material_Light_Dialog_Alert, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
								// TODO Auto-generated method stub
								fromDate.set(year, month, dayOfMonth);
								tvTuNgay.setText(dateFormat.format(fromDate.getTime()));
							}
						}, year, month, day);
				dialog.show();
			}
		});

		tvDenNgay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int year = toDate.get(Calendar.YEAR);
				int month = toDate.get(Calendar.MONTH);
				int day = toDate.get(Calendar.DAY_OF_MONTH);

				DatePickerDialog dialog = new DatePickerDialog(TransactionHistoryActivity.this,
						android.R.style.Theme_Material_Light_Dialog_Alert, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
								// TODO Auto-generated method stub
								toDate.set(year, month, dayOfMonth);
								tvDenNgay.setText(dateFormat.format(toDate.getTime()));
							}
						}, year, month, day);
				dialog.show();
			}
		});
		
		lvTransaction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TransactionModel item = (TransactionModel) parent.getAdapter().getItem(position);
				if(item != null){
					Intent intent = new Intent(TransactionHistoryActivity.this, TransactionDetailActivity.class);
					intent.putExtra("transaction_item", item);
					startActivity(intent);
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
