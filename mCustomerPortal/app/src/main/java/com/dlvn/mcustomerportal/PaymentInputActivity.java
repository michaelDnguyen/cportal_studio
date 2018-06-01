package com.dlvn.mcustomerportal;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.common.Constant.FeeFrequency;
import com.dlvn.mcustomerportal.common.cPortalPref;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentInputActivity extends BaseActivity {

	public static final int REQUEST_NAPAS_PAYMENT = 3886;
	public static final String KEY_NAPAS_PAYMENT = "KEY_NAPAS_PAYMENT_SUCCESS";
	public static final String KEY_NAPAS_PAYMENT_AMOUNT = "KEY_NAPAS_PAYMENT_AMOUNT";
	public static final String KEY_NAPAS_PAYMENT_TRANSID = "KEY_NAPAS_PAYMENT_TRANSID";

	public static final String KEY_NAPAS_PAYMENT_URL = "NAPAS_PAYMENT_URL";

	public static final int RESULTCODE_PAYMENT_FALSE = 7777;
	public static final int RESULTCODE_PAYMENT_SUCCESS = 8386;

	EditText edtHotenNguoiNop, edtPhoneNumber, edtEmail, edtBMBH;
	TextView tvProposalNo, tvHinhThuc, tvSoTienThanhToan;

	Spinner spnDinhKyDP;
	Button btnThanhToan;

	String uniqueValue = "";
	String Amount = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_input);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getView();
		initDatas();
		setListener();
	}

	private void getView() {
		edtHotenNguoiNop = (EditText) findViewById(R.id.edtHotenNguoiNop);
		edtPhoneNumber = (EditText) findViewById(R.id.edtPhoneNumber);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtBMBH = (EditText) findViewById(R.id.edtBMBH);

		tvProposalNo = (TextView) findViewById(R.id.tvProposalNo);
		tvSoTienThanhToan = (TextView) findViewById(R.id.tvSoTienThanhToan);
		tvHinhThuc = (TextView) findViewById(R.id.tvHinhThuc);

		spnDinhKyDP = (Spinner) findViewById(R.id.spnDinhKyDongPhi);

		btnThanhToan = (Button) findViewById(R.id.btnThanhToan);
	}

	private void initDatas() {
		if (getIntent().getExtras().containsKey(KEY_NAPAS_PAYMENT_AMOUNT))
			Amount = getIntent().getExtras().getString(KEY_NAPAS_PAYMENT_AMOUNT);

		if (!TextUtils.isEmpty(Amount))
			tvSoTienThanhToan.setText(Amount);

		tvProposalNo.setText(cPortalPref.getUserProposal(this));
		edtHotenNguoiNop.setText(cPortalPref.getUserName(this));
		edtPhoneNumber.setText("0987654123");

		List<String> list = new ArrayList<String>();
		list.add(FeeFrequency.YEARLY.toString());
		list.add(FeeFrequency.HALF_YEARLY.toString());
		list.add(FeeFrequency.QUARTERLY.toString());
		list.add(FeeFrequency.MONTHLY.toString());
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDinhKyDP.setAdapter(dataAdapter);
		spnDinhKyDP.setPrompt("Chọn");
	}

	private void setListener() {
		btnThanhToan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					if (validatePayment()) {

						String feeFrequency = FeeFrequency.fromName((String) spnDinhKyDP.getSelectedItem())
								.getStringValue();
						myLog.E("FeeFrequency = " + feeFrequency);

						String url;
						url = initPaymentRequest(tvProposalNo.getText().toString(),
								edtHotenNguoiNop.getText().toString(), edtPhoneNumber.getText().toString(),
								edtBMBH.getText().toString(), tvSoTienThanhToan.getText().toString().replace(",", ""),
								feeFrequency);

						Intent napas = new Intent(PaymentInputActivity.this, WebNapasActivity.class);
						napas.putExtra(KEY_NAPAS_PAYMENT_URL, url);

						startActivityForResult(napas, REQUEST_NAPAS_PAYMENT);
					} else
						Toast.makeText(PaymentInputActivity.this, "Vui lòng nhập số tiền thanh toán", 3000).show();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

		tvSoTienThanhToan.addTextChangedListener(new TextWatcher() {
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
					tvSoTienThanhToan.removeTextChangedListener(this);

					String cleanString = s.toString().replaceAll("[$,.]", "");
					double parsed = Double.parseDouble(cleanString);
					String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
					current = formatted;
					tvSoTienThanhToan.setText(formatted);

					tvSoTienThanhToan.addTextChangedListener(this);
				}
			}
		});
	}

	private String initPaymentRequest(String proposalNo, String payer, String phone, String poName, String amount,
			String feeFrequency) throws UnsupportedEncodingException {

		StringBuffer buf = new StringBuffer();
		buf.append(Constant.URL_PAYMENT);
		buf.append("?");

		buf.append("sProposalNo=" + Base64.encodeToString(proposalNo.getBytes("UTF-8"), Base64.DEFAULT));
		buf.append("&");
		buf.append("sPayer=" + Base64.encodeToString(payer.getBytes("UTF-8"), Base64.DEFAULT));
		buf.append("&");
		buf.append("sPhoneNumber=" + Base64.encodeToString(phone.getBytes("UTF-8"), Base64.DEFAULT));
		buf.append("&");
		buf.append("sPOName=" + Base64.encodeToString(poName.getBytes("UTF-8"), Base64.DEFAULT));
		buf.append("&");
		buf.append("sAmount=" + Base64.encodeToString(amount.getBytes("UTF-8"), Base64.DEFAULT));

		buf.append("&");
		buf.append("sFCCode=" + Base64.encodeToString("110555".getBytes("UTF-8"), Base64.DEFAULT));
		buf.append("&");
		buf.append("sAgentName=" + Base64.encodeToString("Nguyen Van Teo".getBytes("UTF-8"), Base64.DEFAULT));
		buf.append("&");
		buf.append("sAgentType=" + Base64.encodeToString("BM".getBytes("UTF-8"), Base64.DEFAULT));
		// Add fee frequency
		buf.append("&");
		buf.append("sFeeFrequency=" + Base64.encodeToString(feeFrequency.getBytes("UTF-8"), Base64.DEFAULT));
		return buf.toString();
	}

	private boolean validatePayment() {

		if (TextUtils.isEmpty(edtHotenNguoiNop.getText().toString())) {
			MyCustomDialog.Builder builder = new MyCustomDialog.Builder(PaymentInputActivity.this);
			builder.setMessage("Anh/chị vui lòng nhập tên người nộp tiền").setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			return false;
		}

		if (TextUtils.isEmpty(edtBMBH.getText().toString())) {
			MyCustomDialog.Builder builder = new MyCustomDialog.Builder(PaymentInputActivity.this);
			builder.setMessage("Anh/chị vui lòng nhập tên người mua bảo hiểm").setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}

					});
			builder.create().show();
			return false;
		}
		return true;
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
