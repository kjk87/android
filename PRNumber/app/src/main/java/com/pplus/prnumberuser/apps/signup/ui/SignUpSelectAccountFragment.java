//package com.pplus.prnumberuser.apps.signup.ui;
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
//import com.pplus.utils.part.logs.LogUtil;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberuser.core.code.common.SnsTypeCode;
//import com.pplus.prnumberuser.core.network.model.dto.User;
//import com.pplus.prnumberuser.core.sns.facebook.FaceBookUtil;
//import com.pplus.prnumberuser.core.sns.google.GoogleUtil;
//
//import org.json.JSONObject;
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
//        return "Sign_upchoice";
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
//    public void init(){
//
//    }
//
//    @Override
//    public void initializeView(View container){
//
//        container.findViewById(R.id.text_select_account_prnumber).setOnClickListener(this);
//        container.findViewById(R.id.layout_select_account_facebook).setOnClickListener(this);
//        container.findViewById(R.id.layout_select_account_google).setOnClickListener(this);
//    }
//
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
////                        PplusCommonUtil.requestScreenLog("facebook 로그인");
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
//                FaceBookUtil.logIn(this);
//                showProgress("");
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
////                            PplusCommonUtil.requestScreenLog("google 로그인");
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
//        }
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        FaceBookUtil.onActivityResult(requestCode, resultCode, data);
//        GoogleUtil.onActivityResult(requestCode, resultCode, data);
//    }
//
//}
