package com.pplus.prnumberuser.core.sns.instagram;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.pplus.prnumberuser.R;


/**
 * Created by 김종경 on 2016-06-14.
 */

public class InstagramDialog extends Dialog{

    static final float[] DIMENSIONS_LANDSCAPE = {460, 260};
    static final float[] DIMENSIONS_PORTRAIT = {280, 420};
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;
    private String mUrl;
    private OAuthDialogListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private static final String TAG = "Instagram-WebView";

    public InstagramDialog(Context context, String url, OAuthDialogListener listener){

        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        //        getWindow().setWindowAnimations(R.style.DialogAnimation);
        mUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_instagram);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
        setUpWebView();
        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(null);
        } else {
            cookieManager.removeAllCookie();
        }

    }

    private void setUpWebView(){

        mWebView = (WebView) findViewById(R.id.instagram_web_view);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(true);
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
    }

    private class OAuthWebViewClient extends WebViewClient{

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request){

            return super.shouldInterceptRequest(view, request);
        }



//        @TargetApi(24)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request){
//
//            if(request.getUrl().toString().startsWith(InstagramUtil.CALLBACK_URL)) {
//                String urls[] = request.getUrl().toString().split("=");
//                mListener.onComplete(urls[1]);
//                dismiss();
//                return true;
//            }
//            return super.shouldOverrideUrlLoading(view, request);
//        }


        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){

            Log.d(TAG, "Redirecting URL " + url);
            if(url.startsWith(InstagramUtil.CALLBACK_URL)) {
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                InstagramDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){

            super.onReceivedError(view, request, error);
            mListener.onError(error.toString());
            InstagramDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){

            Log.d(TAG, "Loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url){

            super.onPageFinished(view, url);
            String title = mWebView.getTitle();
            if(title != null && title.length() > 0) {
                //                mTitle.setText(title);
            }
            Log.d(TAG, "onPageFinished URL: " + url);
            mSpinner.dismiss();
        }
    }

    public interface OAuthDialogListener{

        public abstract void onComplete(String accessToken);

        public abstract void onError(String error);
    }
}

