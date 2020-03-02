package com.symbo.insurance.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.symbo.insurance.R;
import com.symbo.insurance.constants.AppConstants;
import com.symbo.insurance.databinding.ActivityWebViewBinding;
import com.symbo.insurance.utils.DialogUtils;

public class WebViewActivity extends AppCompatActivity implements AppConstants {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    private String url;
    private String name;
    private ActivityWebViewBinding binding;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_web_view);
        if(getIntent() != null && getIntent().hasExtra(WEB_URL)){
            url = getIntent().getStringExtra(WEB_URL);
            if(getIntent().hasExtra(NAME)){
                name = getIntent().getStringExtra(NAME);
                binding.incToolbar.tvMainText.setText(name);
            }
            setupWebView();
        }
        binding.incToolbar.tvBack.setOnClickListener((View)->{
            finish();
        });
    }

    private void setupWebView() {
        dialog = DialogUtils.showLoadingDialog(this);
        binding.webview.getSettings().setLoadsImagesAutomatically(true);
        binding.webview.setPadding(10, 0, 0, 0);
        binding.webview.setInitialScale(getScale());
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.getSettings().setLoadWithOverviewMode(true);
        binding.webview.getSettings().setUseWideViewPort(true); // for responsivness
        binding.webview.setScrollbarFadingEnabled(false);
        binding.webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Add progressbar if needed
                super.onPageStarted(view, url, favicon);
                dialog = DialogUtils.showLoadingDialog(WebViewActivity.this);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                //dismiss here
                super.onPageCommitVisible(view, url);
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(dialog != null && dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });

        binding.webview.loadUrl(url);
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(480);
        val = val * 100d;
        return val.intValue();
    }
}
