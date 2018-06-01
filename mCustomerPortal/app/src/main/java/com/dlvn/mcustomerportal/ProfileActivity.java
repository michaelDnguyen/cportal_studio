package com.dlvn.mcustomerportal;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.cPortalPref;
import com.dlvn.mcustomerportal.utils.myLog;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Profile screen
 * 
 * @author nn.tai
 * @date Dec 11, 2017
 */
public class ProfileActivity extends BaseActivity {

	ImageView imvProfile;
	TextView tvName, tvThayDoiThongTin, tvLichSuGiaoDich, tvThayDoiPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getViews();
		initData();
		setListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		imvProfile = (ImageView) findViewById(R.id.imvProfile);
		tvName = (TextView) findViewById(R.id.tvName);
		tvThayDoiThongTin = (TextView) findViewById(R.id.tvThayDoiThongTin);
		tvThayDoiPassword = (TextView) findViewById(R.id.tvThayDoiPassword);
		tvLichSuGiaoDich = (TextView) findViewById(R.id.tvLichSuGiaoDich);
	}

	private void initData() {
		// TODO Auto-generated method stub
		// Loading profile image
		Glide.with(this).load(R.drawable.avatar_user).thumbnail(0.5f).apply(RequestOptions.circleCropTransform())
				.listener(new RequestListener<Drawable>() {

					@Override
					public boolean onLoadFailed(GlideException arg0, Object arg1, Target<Drawable> arg2, boolean arg3) {
						// progress.setVisibility(View.GONE);
						myLog.E("Load Image Failed!");
						return false;
					}

					@Override
					public boolean onResourceReady(Drawable arg0, Object arg1, Target<Drawable> arg2, DataSource arg3,
							boolean arg4) {
						// progress.setVisibility(View.GONE);
						myLog.E("Load Image Ready!");
						return false;
					}
				}).into(imvProfile);

		tvName.setText(cPortalPref.getUserName(this));
	}

	private void setListener() {
		
		tvThayDoiThongTin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ProfileActivity.this, ChangeProfileActivity.class));
			}
		});

		tvThayDoiPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
			}
		});

		tvLichSuGiaoDich.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(ProfileActivity.this, TransactionHistoryActivity.class));
			}
		});
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
