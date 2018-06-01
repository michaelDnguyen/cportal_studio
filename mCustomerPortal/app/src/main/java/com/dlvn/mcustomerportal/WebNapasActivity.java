package com.dlvn.mcustomerportal;

import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;
import com.dlvn.mcustomerportal.utils.Utilities;
import com.dlvn.mcustomerportal.utils.myLog;
import com.dlvn.mcustomerportal.view.MyCustomDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class WebNapasActivity extends BaseActivity {

	WebView webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_napas);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		webview = (WebView) findViewById(R.id.webview);

		initData();
	}

	private void initData() {
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setAllowFileAccess(true);
		webview.getSettings().setPluginState(PluginState.ON);
		webview.getSettings().setDomStorageEnabled(true);

		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);

		webview.getSettings().setAllowContentAccess(true);
		webview.getSettings().setAllowFileAccessFromFileURLs(true);
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

		// webview.loadUrl("http://khuat.dai-ichi-life.com.vn:8090/mpayment.aspx");

		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().containsKey(PaymentInputActivity.KEY_NAPAS_PAYMENT_URL)) {
				String url = getIntent().getExtras().getString(PaymentInputActivity.KEY_NAPAS_PAYMENT_URL);
				webview.loadUrl(url);
			}

		}

		webview.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				myLog.E("shouldOverrideUrlLoading");
				myLog.E(url);
				view.loadUrl(url);

				if (url.contains(Constant.URL_PAYMENT_RES)) {

					String transactionID = "";

					String[] sarr = url.replace(Constant.URL_PAYMENT_RES, "").split("&");
					if (sarr.length > 0) {
						for (String string : sarr) {
							myLog.E(string);
							if (string.contains("vpc_TransactionNo")) {
								transactionID = string.substring("vpc_TransactionNo=".length());
							}
						}
					}

					// giao dịch thành công => gửi mail cho customer
					if (url.contains("vpc_ResponseCode=0")) {

						Intent data = new Intent();
						data.putExtra(PaymentInputActivity.KEY_NAPAS_PAYMENT_TRANSID, transactionID);
						setResult(PaymentInputActivity.RESULTCODE_PAYMENT_SUCCESS, data);
						finish();
					} else {
						Intent data = new Intent();
						data.putExtra(PaymentInputActivity.KEY_NAPAS_PAYMENT_TRANSID, transactionID);
						setResult(PaymentInputActivity.RESULTCODE_PAYMENT_FALSE, data);
						finish();
					}
				}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				myLog.E("onPageStarted");
				myLog.E(url);
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				myLog.E("onPageFinish");
				myLog.E(url);
				super.onPageFinished(view, url);
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
				myLog.E("shouldInterceptRequest");
				myLog.E("Url = " + request.getUrl().toString());
				return super.shouldInterceptRequest(view, request);
			}

			@Override
			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
				myLog.E("onReceivedError");
				myLog.E("Url = " + request.getUrl().toString());
				super.onReceivedError(view, request, error);
			}

			@Override
			public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
				myLog.E("onReceivedSslError = " + error.getUrl());
				myLog.E("onReceivedSslError code = " + error.getPrimaryError());

				MyCustomDialog.Builder builder = new MyCustomDialog.Builder(WebNapasActivity.this);
				builder.setMessage("Notification error ssl certificate invalid. Do you continue ?")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								handler.proceed();
							}
						}).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								handler.cancel();
							}
						});
				builder.create().show();
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
				Utilities.hideSoftInputKeyboard(WebNapasActivity.this, v);;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (webview.canGoBack()) {
					webview.goBack();
				} else {
					finish();
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
