package com.pplus.prnumberbiz.apps.common.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.webkit.JavascriptInterface;

/**
 * Created by Administrator on 2016-08-30.
 */
public class WebViewInterface{

    public static final String WEBVIEW_JS_INTERFACE_NAME = "pplus";
    private Context mContext;
    private Handler mHandler = new Handler();

    public WebViewInterface(Context context) {
        mContext = context;
    }

    /**
     * Activity를 종료함.
     * sendBackKey
     */
    @JavascriptInterface
    public void sendFinishKey(){
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Activity act = (Activity)mContext;
                act.finish();
            }
        });
    }

    /**
     * WebViewActivity를 호출함
     * @param url
     * @param title
     */
    @JavascriptInterface
    public void internalUrl(final String url, final String title){
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("title", title);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 외부 브라우저를 호출함.
     * @param url
     */
    @JavascriptInterface
    public void externalUrl(final String url){
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }
}
