package com.dlvn.mcustomerportal;

import java.text.NumberFormat;
import java.util.Locale;

import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant.PayChannel;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaymentMethodActivity extends BaseActivity {

	EditText edtAmount;
	TextView tvError;
	Button btnNapas, btnCash, btnBank;
	Button btnMPos;

	PayChannel dlgPaymentMethod = null;
	String Amount = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_method);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getView();
		setListener();
	}

	private void getView() {
		tvError = (TextView) findViewById(R.id.tvError);

		edtAmount = (EditText) findViewById(R.id.edtAmount);
		btnNapas = (Button) findViewById(R.id.btnNapas);
		btnCash = (Button) findViewById(R.id.btnCash);
		btnBank = (Button) findViewById(R.id.btnBank);

		btnMPos = (Button) findViewById(R.id.btnMPos);
	}

	private void setListener() {
		edtAmount.addTextChangedListener(new TextWatcher() {
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
					edtAmount.removeTextChangedListener(this);

					String cleanString = s.toString().replaceAll("[$,.]", "");
					double parsed = Double.parseDouble(cleanString);
					String formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed);
					current = formatted;
					edtAmount.setText(formatted);
					edtAmount.setSelection(formatted.length());

					edtAmount.addTextChangedListener(this);
				}
			}
		});

		btnNapas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edtAmount.getText().length() > 4) {
					dlgPaymentMethod = PayChannel.NAPAS;
					btnCash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_cash, 0, 0, 0);
					btnBank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_bank, 0, 0, 0);
					btnNapas.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_napas, 0, R.drawable.ico_checked,
							0);
				} else {
					MyCustomDialog.Builder builder = new MyCustomDialog.Builder(PaymentMethodActivity.this);
					builder.setMessage(getString(R.string.alert_input_payment)).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
			}
		});

		btnCash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edtAmount.getText().length() > 4) {
					dlgPaymentMethod = PayChannel.CASH;
					btnNapas.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_napas, 0, 0, 0);
					btnBank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_bank, 0, 0, 0);
					btnCash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_cash, 0, R.drawable.ico_checked, 0);
				} else {
					MyCustomDialog.Builder builder = new MyCustomDialog.Builder(PaymentMethodActivity.this);
					builder.setMessage(getString(R.string.alert_input_payment)).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
			}
		});

		btnBank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (edtAmount.getText().length() > 4) {
					dlgPaymentMethod = PayChannel.BANK;
					btnNapas.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_napas, 0, 0, 0);
					btnCash.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_cash, 0, 0, 0);
					btnBank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ico_bank, 0, R.drawable.ico_checked, 0);
				} else {
					MyCustomDialog.Builder builder = new MyCustomDialog.Builder(PaymentMethodActivity.this);
					builder.setMessage(getString(R.string.alert_input_payment)).setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							});
					builder.create().show();
				}
			}
		});

		btnMPos.setEnabled(false);
		btnMPos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dlgPaymentMethod = PayChannel.MPOS;
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.payment, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_tiep) {
			String amount = edtAmount.getText().toString();

			if (!TextUtils.isEmpty(amount) && dlgPaymentMethod != null) {
				if (dlgPaymentMethod.equals(PayChannel.CASH) || dlgPaymentMethod.equals(PayChannel.BANK)) {

				} else if (dlgPaymentMethod.equals(PayChannel.NAPAS)) {

					Intent intent = new Intent(PaymentMethodActivity.this, PaymentInputActivity.class);
					intent.putExtra(PaymentInputActivity.KEY_NAPAS_PAYMENT_AMOUNT, edtAmount.getText().toString());
					startActivity(intent);

				}
			} else {

				MyCustomDialog.Builder builder = new MyCustomDialog.Builder(PaymentMethodActivity.this);
				builder.setMessage(getString(R.string.alert_input_payment)).setPositiveButton("Đồng ý",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				builder.create().show();
			}
		} else if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
