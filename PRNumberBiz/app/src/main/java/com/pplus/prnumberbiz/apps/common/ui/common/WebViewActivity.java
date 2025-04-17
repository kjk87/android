package com.pplus.prnumberbiz.apps.common.ui.common;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pple.pplus.utils.part.info.OsUtil;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.network.model.dto.Terms;

import java.util.ArrayList;

/**
 * Created by ksh on 2016-08-30.
 */
public class WebViewActivity extends BaseActivity {

    private static final String TAG = WebViewActivity.class.getSimpleName();

    private WebView mWebView;
    private TextView text_terms_title;
    private String mUrl = null;
    private ScrollView sv_terms;
    private LinearLayout ll_terms;

    private boolean isRIghtMenuClick = false; // Right Menu를 클릭 여부
    private int mCurrentIndex = 0;          // RIght Menu를 위한 Index

    private ArrayList<Terms> mTermsList = null;

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_webview;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        mWebView = (WebView) findViewById(R.id.webview_layout);

        mUrl = getIntent().getStringExtra(Const.WEBVIEW_URL);

        sv_terms = (ScrollView) findViewById(R.id.sv_terms);
        ll_terms = (LinearLayout) findViewById(R.id.ll_terms);
        sv_terms.setVisibility(View.GONE);

        mTermsList = getIntent().getParcelableArrayListExtra(Const.TERMS_LIST);
        ll_terms.removeAllViews();
        if(mTermsList != null && mTermsList.size() > 0) {
            for(int i = 0; i < mTermsList.size(); i++) {
                if(i != mCurrentIndex) {

                    View view = getLayoutInflater().inflate(R.layout.item_dropbox_category, null);
                    TextView tv = (TextView) view.findViewById(R.id.text_dropbox_category_name);
                    tv.setText(mTermsList.get(i).getSubject());
                    tv.setTag(i);
                    tv.setTag(tv.getId(), mTermsList.get(i).getUrl());
                    tv.setOnClickListener(menuClickListener);
                    ll_terms.addView(view);
                }
            }
        }

        findViewById(R.id.image_terms_back).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        text_terms_title = (TextView)findViewById(R.id.text_terms_title);
        text_terms_title.setText(getIntent().getStringExtra(Const.TITLE));

        text_terms_title.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if(!isRIghtMenuClick) {
                    sv_terms.setVisibility(View.VISIBLE);
                    text_terms_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_setting_up, 0);
                } else {
                    sv_terms.setVisibility(View.GONE);
                    text_terms_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_setting_down, 0);
                }
                isRIghtMenuClick = !isRIghtMenuClick;
            }
        });

        if(!TextUtils.isEmpty(mUrl)) {

            mWebView.setWebViewClient(new WebClient());
            WebSettings set = mWebView.getSettings();
            if(OsUtil.isLollipop()) {
                mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                CookieManager.getInstance().setAcceptCookie(true);
                CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
            }
            set.setJavaScriptEnabled(true);
            set.setLoadWithOverviewMode(true);
            //set.setUseWideViewPort(true);

            webChromeClient chromeClient = new webChromeClient();
            mWebView.setWebChromeClient(chromeClient);
            /**
             * 화면 포트에 맞도록 웹 설정을 변경합니다.
             * v 1.1.1 버전 웹 퍼블리셔 이슈로 인하여 해당 오류 사항을 수정하지않습니다.

             mWebView.getSettings().setUseWideViewPort(true);
             mWebView.getSettings().setLoadWithOverviewMode(true);
             mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

             * */

            WebViewInterface webViewInterface = new WebViewInterface(this);
            mWebView.addJavascriptInterface(webViewInterface, WebViewInterface.WEBVIEW_JS_INTERFACE_NAME);

            loadUrl(mUrl);

        }

    }

    public void loadUrl(String url){

        mWebView.loadUrl(mUrl);
    }

    public View.OnClickListener menuClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view){

            isRIghtMenuClick = !isRIghtMenuClick;
            sv_terms.setVisibility(View.GONE);
            // Webview 처리
            int i = (int) view.getTag();
            mCurrentIndex = i;
            sv_terms.setVisibility(View.GONE);

            // Text 변경
            if(mTermsList != null && mTermsList.size() > 0 && mTermsList.size() > i) {
                text_terms_title.setText(mTermsList.get(i).getSubject());
            }
            text_terms_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_setting_down, 0);
            mUrl = (String) view.getTag(view.getId());
            loadUrl(mUrl);
        }
    };

    class WebClient extends WebViewClient{

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){

            LogUtil.d(TAG, "view = " + view);
            //            if(url.startsWith(SchemaManager.SCHEMA_PPLUS)) {
            //                SchemaManager.getInstance(WebViewActivity.this).moveToSchme(url);
            //                return true;
            //            } else
            if(url != null && url.startsWith("http")) {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            } else {
                return false;
            }
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){

            String url = request.getUrl().toString();
            return shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            LogUtil.d(TAG, "onPageStarted url = {}", url);
            showProgress("");
        }

        @Override
        public void onPageFinished(WebView view, String url){
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            LogUtil.d(TAG, "onPageFinished url = {}", url);
            hideProgress();
        }

    }

    public class webChromeClient extends WebChromeClient{

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg){
            // TODO Auto-generated method stub
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result){

            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.setContents(message);
            builder.setRightText(getString(R.string.word_confirm));
            builder.setOnAlertResultListener(new OnAlertResultListener(){

                @Override
                public void onCancel(){

                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                    switch (event_alert) {
                        case RIGHT:
                            result.confirm();
                            break;
                        case LEFT:
                            break;
                    }
                }
            }).builder().show(WebViewActivity.this);
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result){

            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.setContents(getString(R.string.msg_question_logout));
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
            builder.setOnAlertResultListener(new OnAlertResultListener(){

                @Override
                public void onCancel(){

                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                    switch (event_alert) {
                        case RIGHT:
                            result.confirm();
                            break;
                        case LEFT:
                            result.cancel();
                            break;
                    }
                }
            }).builder().show(WebViewActivity.this);
            return true;
        }

    }

}


