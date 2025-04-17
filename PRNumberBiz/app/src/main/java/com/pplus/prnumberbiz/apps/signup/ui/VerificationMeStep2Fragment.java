package com.pplus.prnumberbiz.apps.signup.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.model.dto.User;
import com.pplus.prnumberbiz.core.network.model.dto.Verification;
import com.pplus.prnumberbiz.core.network.model.dto.VerificationMe;

import org.json.JSONObject;

import kr.co.bootpay.BootpayWebView;

public class VerificationMeStep2Fragment extends BaseFragment<VerificationMeActivity>{

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;


    public VerificationMeStep2Fragment(){

    }

    public static VerificationMeStep2Fragment newInstance(String param1){

        VerificationMeStep2Fragment fragment = new VerificationMeStep2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public int getLayoutResourceId(){

        return R.layout.fragment_verification_me_step2;
    }

    @Override
    public void initializeView(View container){

        BootpayWebView webView = container.findViewById(R.id.webview);
        // JavaScript 허용
//        webView.getSettings().setJavaScriptEnabled(true);
//        // JavaScript의 window.open 허용
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setAppCacheEnabled(false);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        // 두 번째 파라미터는 사용될 php에도 동일하게 사용해야함
        webView.addJavascriptInterface(new AndroidBridge(), "Auth");
        // web client 를 chrome 으로 설정
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new WebViewClient(){
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url){
//
//                view.loadUrl(url);
//                return false;
////                if(url.startsWith("intent:")) {
////                    try {
////                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
////                        Intent existPackage = getActivity().getPackageManager().getLaunchIntentForPackage(intent.getPackage());
////                        if(existPackage != null) {
////                            startActivity(intent);
////                        }
////
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////
////                    return true;
////                }else{
////                    return false;
////                }
//
//            }
//        });
        // webview url load
        webView.loadUrl(Const.API_URL + "jsp/auth");
    }

    private Handler handler = new Handler();

    private class AndroidBridge{

        @JavascriptInterface
        public void close(){
            getActivity().finish();
        }

        @JavascriptInterface
        public void sendMessage(final String msg){
            LogUtil.e(LOG_TAG, "msg1 : {}", msg);
            handler.post(new Runnable(){

                @Override
                public void run(){

                    LogUtil.e(LOG_TAG, "msg2 : {}", msg);
                    try {
                        JSONObject jsonObject = new JSONObject(msg);
                        VerificationMe verificationMe = new VerificationMe();
                        verificationMe.setOrder_id(jsonObject.optString("order_id"));
                        verificationMe.setUsername(jsonObject.optString("username"));
                        verificationMe.setPhone(jsonObject.optString("phone"));
                        verificationMe.setBirth(jsonObject.optString("birth"));
                        verificationMe.setGender(jsonObject.optString("gender"));
                        verificationMe.setUnique(jsonObject.optString("unique"));
                        verificationMe.setDi(jsonObject.optString("di"));
                        verificationMe.setToken(jsonObject.optString("token"));

                        Verification verification = new Verification();
                        verification.setMedia("external");
                        verification.setNumber(verificationMe.getOrder_id());
                        verification.setToken(verificationMe.getToken());

                        if(getParentActivity().getKey() == null || getParentActivity().getKey().equals(Const.JOIN)) {

                            User params = new User();
                            params.setMobile(verificationMe.getPhone());
                            params.setName(verificationMe.getUsername());
                            params.setBirthday(verificationMe.getBirth());
                            if(verificationMe.getGender().equals("0")){
                                params.setGender(EnumData.GenderType.female.name());
                            }else{
                                params.setGender(EnumData.GenderType.male.name());
                            }

                            params.setVerification(verification);
                            Intent data = new Intent();
                            data.putExtra(Const.USER, params);
                            getActivity().setResult(Activity.RESULT_OK, data);
                            getActivity().finish();

                        }else {
                            Intent data = new Intent();
                            data.putExtra(Const.VERIFICATION, verification);
                            data.putExtra(Const.MOBILE_NUMBER, verificationMe.getPhone());
                            getActivity().setResult(Activity.RESULT_OK, data);
                            getActivity().finish();
                        }


                    } catch (Exception e) {

                    }

                }
            });
        }
    }

    @Override
    public void init(){

    }

    @Override
    public String getPID(){

        return null;
    }

}
