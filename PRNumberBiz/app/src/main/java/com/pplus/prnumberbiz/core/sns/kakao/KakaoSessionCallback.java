package com.pplus.prnumberbiz.core.sns.kakao;//package kr.co.j2n.pplus.core.sns.kakao;
//
//import android.util.Log;
//
//import com.kakao.auth.ISessionCallback;
//import com.kakao.auth.Session;
//import com.kakao.util.exception.KakaoException;
//import com.kakao.util.helper.log.Logger;
//
///**
// * Created by 김종경 on 2016-07-25.
// */
//public class KakaoSessionCallback implements ISessionCallback{
//
//    @Override
//    public void onSessionOpened(){
//
//        Log.e("onSessionOpened", "onSessionOpened");
//        Log.e("AccessToken", Session.getCurrentSession().getAccessToken());
//        KakaoUtil.requestMe();
//    }
//
//    @Override
//    public void onSessionOpenFailed(KakaoException exception){
//
//        Log.e("onSessionOpenFailed", "onSessionOpenFailed");
//        if(exception != null) {
//            Logger.e(exception);
//        }
//    }
//}
