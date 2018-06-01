package com.dlvn.mcustomerportal;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dlvn.mcustomerportal.base.BaseActivity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ProductDetailActivity extends BaseActivity {

	TextView tvKhauHieu, tvNoiDung;
	ImageView imvSanPham;
	
	String title = "";
	String content = "";
	String pathImage = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		getViews();
		initData();
		setListener();
	}
	
	private void getViews() {
		// TODO Auto-generated method stub
		imvSanPham = (ImageView) findViewById(R.id.imvSanPham);
		tvKhauHieu = (TextView) findViewById(R.id.tvKhauHieu);
		tvNoiDung = (TextView) findViewById(R.id.tvNoiDung);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if(getIntent().getExtras().containsKey("title"))
			title = getIntent().getStringExtra("title");
		
		if(getIntent().getExtras().containsKey("content"))
			content = getIntent().getStringExtra("content");
		
		if(getIntent().getExtras().containsKey("pathImage"))
			pathImage = getIntent().getStringExtra("pathImage");
		
		//get current size screen
		final Point sizeScreen;
		
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		sizeScreen = new Point();
		display.getSize(sizeScreen);
		
		imvSanPham.post(new Runnable() {

			@Override
			public void run() {

				int width = sizeScreen.x;

				LinearLayout.LayoutParams param = new LayoutParams((int) width, (int) (width * 0.7));
				imvSanPham.setLayoutParams(param);
				imvSanPham.getParent().requestLayout();
			}
		});
		
		Glide.with(this).load(pathImage).apply(new RequestOptions().fitCenter()).into(imvSanPham);
		setTitle(title);
		tvNoiDung.setText(content);
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
