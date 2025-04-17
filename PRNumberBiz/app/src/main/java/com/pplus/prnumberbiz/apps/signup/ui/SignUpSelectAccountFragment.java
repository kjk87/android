//package com.pplus.prnumberbiz.apps.signup.ui;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import com.facebook.AccessToken;
//import com.facebook.FacebookException;
//import com.facebook.GraphResponse;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.kakao.auth.AuthType;
//import com.kakao.auth.ErrorCode;
//import com.kakao.auth.ISessionCallback;
//import com.kakao.auth.Session;
//import com.kakao.network.ErrorResult;
//import com.kakao.usermgmt.callback.MeResponseCallback;
//import com.kakao.usermgmt.response.model.UserProfile;
//import com.kakao.util.exception.KakaoException;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.core.code.common.SnsTypeCode;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.sns.facebook.FaceBookUtil;
//import com.pplus.prnumberbiz.core.sns.google.GoogleUtil;
//import com.pplus.prnumberbiz.core.sns.kakao.KakaoUtil;
//import com.pplus.prnumberbiz.core.sns.naver.NaverUtil;
//
//import org.json.JSONObject;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import java.io.StringReader;
//
///**
// * 회원가입 완료
// */
//public class SignUpSelectAccountFragment extends BaseFragment<SignUpActivity>{
//
//    private User paramsJoin;
//
//    public static SignUpSelectAccountFragment newInstance(User params){
//
//        SignUpSelectAccountFragment fragment = new SignUpSelectAccountFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(Const.JOIN, params);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            paramsJoin = getArguments().getParcelable(Const.JOIN);
//        }
//    }
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    public SignUpSelectAccountFragment(){
//        // Required empty public constructor
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_sign_up_select_account;
//    }
//
//    @Override
//    public void initializeView(View container){
//
//        container.findViewById(R.id.text_select_account_prnumber).setOnClickListener(this);
//        container.findViewById(R.id.layout_select_account_facebook).setOnClickListener(this);
//        container.findViewById(R.id.layout_select_account_kakao).setOnClickListener(this);
//        container.findViewById(R.id.layout_select_account_google).setOnClickListener(this);
//        container.findViewById(R.id.layout_select_account_naver).setOnClickListener(this);
//    }
//    @Override
//    public void init(){
//
//    }
//    @Override
//    public void onClick(View v){
//
//        switch (v.getId()) {
//            case R.id.text_select_account_prnumber:
//                paramsJoin.setAccountType(SnsTypeCode.pplus.name());
//                getParentActivity().signUpInput(paramsJoin);
//                break;
//            case R.id.layout_select_account_facebook:
//                FaceBookUtil.registerCallback(new FaceBookUtil.FaceBookCallbackListener(){
//
//                    @Override
//                    public void onSuccess(com.facebook.login.LoginResult loginResult){
//                        //로그인 성공후 프로필 정보 요청
//                        FaceBookUtil.requestProfile(loginResult.getAccessToken());
//                    }
//
//                    @Override
//                    public void onCancel(){
//
//                        hideProgress();
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception){
//
//                        hideProgress();
//                    }
//
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response){
//
//                        hideProgress();
//                        //프로필 정보 요청 성공
//                        String id = object.optString("id");
//                        String token = AccessToken.getCurrentAccessToken().getToken();
//
//                        LogUtil.e("id", "" + id);
//                        LogUtil.e("token", "" + token);
//                        paramsJoin.setLoginId(id);
//                        paramsJoin.setPassword(token);
//                        paramsJoin.setAccountType(SnsTypeCode.facebook.name());
//                        getParentActivity().signUpInput(paramsJoin);
//                    }
//                });
//                FaceBookUtil.logIn(getActivity());
//                showProgress("");
//                break;
//            case R.id.layout_select_account_kakao:
//                mKakaoSessionCallback = new KakaoSessionCallback();
//                KakaoUtil.addCallback(mKakaoSessionCallback);
//                Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, getActivity());
//                break;
//            case R.id.layout_select_account_google:
//                showProgress("");
//                GoogleUtil.init(getActivity(), new GoogleUtil.GoogleSignListener(){
//
//                    @Override
//                    public void handleSignInResult(GoogleSignInResult result){
//
//                        hideProgress();
//                        if(result.isSuccess()) {
//
//                            GoogleSignInAccount acct = result.getSignInAccount();
//                            String id = acct.getId();
//                            String token = acct.getIdToken();
//
//                            LogUtil.e("id", "" + id);
//                            LogUtil.e("token", "" + token);
//                            paramsJoin.setLoginId(id);
//                            paramsJoin.setPassword(token);
//                            paramsJoin.setAccountType(SnsTypeCode.google.name());
//                            getParentActivity().signUpInput(paramsJoin);
//                        }
//                    }
//                });
//                break;
//            case R.id.layout_select_account_naver:
//                NaverUtil.click(getActivity(), new NaverUtil.NaverCallbackListener(){
//
//                    @Override
//                    public void onSuccess(String token, String content){
//
//                        hideProgress();
//                        LogUtil.e(LOG_TAG, "token : {}, content : {}", token, content);
//                        String id = null;
//                        try {
//                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                            factory.setNamespaceAware(true);
//                            XmlPullParser xpp = factory.newPullParser();
//
//                            xpp.setInput(new StringReader(content));
//                            int eventType = xpp.getEventType();
//                            boolean isId = false;
//                            while (eventType != XmlPullParser.END_DOCUMENT) {
//                                if(eventType == XmlPullParser.START_TAG) {
//                                    if(xpp.getName().equals("email")) {
//                                        isId = true;
//                                    }
//                                } else if(eventType == XmlPullParser.TEXT) {
//                                    if(isId) {
//                                        LogUtil.e(LOG_TAG, "{} ", xpp.getText());
//                                        id = xpp.getText();
//                                        isId = false;
//                                        break;
//                                    }
//
//                                }
//                                eventType = xpp.next();
//                            }
//                        } catch (Exception e) {
//
//                        }
//
//                        paramsJoin.setLoginId(id);
//                        paramsJoin.setPassword(token);
//                        paramsJoin.setAccountType(SnsTypeCode.naver.name());
//                        getParentActivity().signUpInput(paramsJoin);
//                    }
//
//                    @Override
//                    public void onError(){
//
//                        hideProgress();
//                    }
//                });
//                showProgress("");
//
//                break;
//        }
//    }
//
//    private KakaoSessionCallback mKakaoSessionCallback;
//
//    //카카오톡 콜백class
//    class KakaoSessionCallback implements ISessionCallback{
//
//        @Override
//        public void onSessionOpened(){
//
//            KakaoUtil.requestMe(new MeResponseCallback(){
//
//                @Override
//                public void onFailure(ErrorResult errorResult){
//
//                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
//                    if(result == ErrorCode.CLIENT_ERROR_CODE) {
//
//                    } else {
//
//                    }
//                }
//
//                @Override
//                public void onSessionClosed(ErrorResult errorResult){
//
//                }
//
//                @Override
//                public void onSuccess(UserProfile userProfile){
//
//                    long id = userProfile.getId();
//                    String token = Session.getCurrentSession().getAccessToken();
//
//                    LogUtil.e("id", "" + id);
//                    LogUtil.e("token", "" + token);
//                    paramsJoin.setLoginId("" +id);
//                    paramsJoin.setPassword(token);
//                    paramsJoin.setAccountType(SnsTypeCode.kakao.name());
//                    getParentActivity().signUpInput(paramsJoin);
//                }
//
//                @Override
//                public void onNotSignedUp(){
//
//                }
//            });
//        }
//
//        @Override
//        public void onSessionOpenFailed(KakaoException exception){
//
//            LogUtil.e(LOG_TAG, exception.toString());
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        KakaoUtil.handleActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        FaceBookUtil.onActivityResult(requestCode, resultCode, data);
//        GoogleUtil.onActivityResult(requestCode, resultCode, data);
//    }
//}
