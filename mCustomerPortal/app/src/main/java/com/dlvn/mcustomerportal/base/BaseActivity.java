package com.dlvn.mcustomerportal.base;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.common.Constant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends AppCompatActivity  {
	
	ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		/*Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.setStatusBarColor(ContextCompat.getColor(this, R.color.red));
		}*/

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}

	protected void showProgressDialog(String message) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.setMessage(message);
		mProgressDialog.show();
	}

	protected void hideProgressDialog() {
		if (mProgressDialog == null)
			throw new RuntimeException();
		mProgressDialog.dismiss();
	}

	protected View view;

	private final String[] permissions = new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE };

	protected void checkPermission() {
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			permissionGranted();

		} else {
			ActivityCompat.requestPermissions(this, permissions, Constant.PERMISSION_REQUEST_CODE);
		}
	}

	private void requestPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
			showRequestPermissionRationale();

		} else {
			showAppPermissionSettings();
		}
	}

	private void showRequestPermissionRationale() {
		Snackbar snackbar = Snackbar.make(view, getString(R.string.permission_info), Snackbar.LENGTH_INDEFINITE)
				.setAction(getString(R.string.permission_ok), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ActivityCompat.requestPermissions(BaseActivity.this, permissions,
								Constant.PERMISSION_REQUEST_CODE);
					}
				});

		/*
		 * ((TextView) snackbar.getView()
		 * .findViewById(android.support.design.R.id.snackbar_text)).setMaxLines
		 * (maxLines);
		 */
		snackbar.show();
	}

	private void showAppPermissionSettings() {
		Snackbar snackbar = Snackbar.make(view, getString(R.string.permission_force), Snackbar.LENGTH_INDEFINITE)
				.setAction(getString(R.string.permission_settings), new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Uri uri = Uri.fromParts(getString(R.string.permission_package),
								BaseActivity.this.getPackageName(), null);

						Intent intent = new Intent();
						intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						intent.setData(uri);
						startActivityForResult(intent, Constant.PERMISSION_REQUEST_CODE);
					}
				});

		/*
		 * ((TextView) snackbar.getView()
		 * .findViewById(android.support.design.R.id.snackbar_text)).setMaxLines
		 * (maxLines);
		 */
		snackbar.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode != Constant.PERMISSION_REQUEST_CODE || grantResults.length == 0
				|| grantResults[0] == PackageManager.PERMISSION_DENIED) {
			permissionDenied();

		} else {
			permissionGranted();
		}
	}

	protected void permissionGranted() {
	}

	private void permissionDenied() {
		hideViews();
		requestPermission();
	}

	protected void hideViews() {
	}

	protected void setView(View view) {
		this.view = view;
	}

}
