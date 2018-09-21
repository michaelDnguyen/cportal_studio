package com.dlvn.mcustomerportal.activity;

import com.dlvn.mcustomerportal.R;
import com.dlvn.mcustomerportal.base.BaseActivity;
import com.dlvn.mcustomerportal.common.Constant;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebTutorialActivity extends BaseActivity {

    WebView webview;
    String url, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_tutorial);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webview = (WebView) findViewById(R.id.webview);

        // Config webview
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setPluginState(PluginState.ON);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDisplayZoomControls(true);
        webview.getSettings().setSupportZoom(true);

        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);

        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(Constant.INT_KEY_WEB_URL))
                url = getIntent().getExtras().getString(Constant.INT_KEY_WEB_URL);

            if (getIntent().getExtras().containsKey(Constant.INT_KEY_WEB_URL_TITLE))
                title = getIntent().getExtras().getString(Constant.INT_KEY_WEB_URL_TITLE);
        }

        if (!TextUtils.isEmpty(title))
            setTitle(title);

        if (!TextUtils.isEmpty(url))
            webview.loadUrl(url);
//		webview.loadUrl("file:///android_asset/Huong_dan_su_dung.htm");

        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view, request);
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
