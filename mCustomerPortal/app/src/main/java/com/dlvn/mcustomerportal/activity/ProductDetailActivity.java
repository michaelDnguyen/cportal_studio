package com.dlvn.mcustomerportal.activity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.adapter.model.ProductDetailModel;
import com.dlvn.mcustomerportal.base.BaseActivity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ProductDetailActivity extends BaseActivity {

    private static final String TAG = "ProductDetailActivity";

    public static final String INT_PRODUCT_DETAIL = "Product-Detail";

    LinearLayout lloBack;
    TextView tvKhauHieu, tvNoiDung;
    ImageView imvSanPham;
    WebView webView;

    ProductDetailModel detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getViews();
        initData();
        setListener();
    }

    private void getViews() {
        // TODO Auto-generated method stub
        lloBack = findViewById(R.id.lloBack);

        imvSanPham = findViewById(R.id.imvSanPham);
        tvKhauHieu = findViewById(R.id.tvKhauHieu);
        tvNoiDung = findViewById(R.id.tvNoiDung);

        webView = findViewById(R.id.webview);
        // Config webview
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setSupportZoom(true);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

    }

    @TargetApi(Build.VERSION_CODES.N)
    private void initData() {
        // TODO Auto-generated method stub

        if (getIntent().getExtras() != null)
            if (getIntent().getExtras().containsKey(INT_PRODUCT_DETAIL))
                detail = getIntent().getExtras().getParcelable(INT_PRODUCT_DETAIL);

        //get current size screen
        final Point sizeScreen;

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        sizeScreen = new Point();
        display.getSize(sizeScreen);

//        Glide.with(this).load(drawable).apply(new RequestOptions().centerCrop()).into(new SimpleTarget<Drawable>() {
//            @Override
//            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                imvSanPham.setImageDrawable(resource);
//                imvSanPham.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        int width = sizeScreen.x;
//                        LinearLayout.LayoutParams param = new LayoutParams((int) width, LayoutParams.WRAP_CONTENT);
//                        imvSanPham.setLayoutParams(param);
//                        imvSanPham.getParent().requestLayout();
//                    }
//                });
//            }
//        });

        if (detail != null) {
            Glide.with(this).asBitmap().load(detail.getThumbnailURL()).into(imvSanPham);
            tvKhauHieu.setText(detail.getTitle());
            tvNoiDung.setText(detail.getDescription());
            webView.loadUrl(detail.getContentURL());
        }
    }

    private void setListener() {

        lloBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
