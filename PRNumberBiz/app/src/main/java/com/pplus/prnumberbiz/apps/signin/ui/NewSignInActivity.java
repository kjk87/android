//package com.pplus.prnumberbiz.apps.signin.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.InputType;
//import android.view.View;
//import android.widget.EditText;
//
//import com.facebook.AccessToken;
//import com.facebook.FacebookException;
//import com.facebook.GraphResponse;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.kakao.auth.AuthType;
//import com.kakao.auth.ErrorCode;
//import com.kakao.auth.ISessionCallback;
//import com.kakao.auth.Session;
//import com.kakao.network.ErrorResult;
//import com.kakao.usermgmt.callback.MeResponseCallback;
//import com.kakao.usermgmt.response.model.UserProfile;
//import com.kakao.util.exception.KakaoException;
//import com.pple.pplus.utils.part.apps.permission.Permission;
//import com.pple.pplus.utils.part.apps.permission.PermissionListener;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.PRNumberBizApplication;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.PPlusPermission;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.mgmt.AppInfoManager;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginResultManager2;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.pages.ui.PageConfigFirstActivity;
//import com.pplus.prnumberbiz.apps.signup.ui.RegPrNumberActivity;
//import com.pplus.prnumberbiz.apps.signup.ui.SignUpPreActivity;
//import com.pplus.prnumberbiz.apps.signup.ui.VerificationMeActivity;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.code.common.SnsTypeCode;
//import com.pplus.prnumberbiz.core.contact.LoadContactTask;
//import com.pplus.prnumberbiz.core.database.DBManager;
//import com.pplus.prnumberbiz.core.database.dao.ContactDao;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.InstalledApp;
//import com.pplus.prnumberbiz.core.network.model.dto.Page;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.network.model.dto.Verification;
//import com.pplus.prnumberbiz.core.network.model.request.params.ParamsRegDevice;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.network.model.dto.Device;
//import com.pplus.prnumberbiz.core.sns.facebook.FaceBookUtil;
//import com.pplus.prnumberbiz.core.sns.google.GoogleUtil;
//import com.pplus.prnumberbiz.core.sns.kakao.KakaoUtil;
//import com.pplus.prnumberbiz.core.sns.naver.NaverUtil;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import org.json.JSONObject;
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class NewSignInActivity extends BaseActivity implements View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_new_sign_in;
//    }
//
//    private EditText mEditId, mEditPw;
//    private View text_sign_in;
//
//    private boolean mIsAutoSigIn = true;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        if(StringUtils.isNotEmpty(refreshedToken)) {
//            AppInfoManager.getInstance().setPushToken(refreshedToken);
//        }
//
//        mEditId = (EditText) findViewById(R.id.edit_sign_in_id);
//        mEditId.setSingleLine();
//        mEditPw = (EditText) findViewById(R.id.edit_sign_in_pw);
//        mEditPw.setSingleLine();
//        mEditPw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//
//        text_sign_in = findViewById(R.id.text_sigin_in);
//        text_sign_in.setOnClickListener(this);
//
//        View text_auto = findViewById(R.id.text_sign_in_auto);
//        mIsAutoSigIn = true;
//        text_auto.setOnClickListener(this);
//        text_auto.setSelected(mIsAutoSigIn);
//
//        findViewById(R.id.text_sign_in_find_id).setOnClickListener(this);
//        findViewById(R.id.text_sign_in_find_password).setOnClickListener(this);
//        findViewById(R.id.text_sign_in_signUp).setOnClickListener(this);
//        findViewById(R.id.layout_new_sign_in_facebook).setOnClickListener(snsClickListener);
//        findViewById(R.id.layout_new_sign_in_kakao).setOnClickListener(snsClickListener);
//        findViewById(R.id.layout_new_sign_in_google).setOnClickListener(snsClickListener);
//        findViewById(R.id.layout_new_sign_in_naver).setOnClickListener(snsClickListener);
//    }
//
//
//    private View.OnClickListener snsClickListener = new View.OnClickListener(){
//
//        @Override
//        public void onClick(View view){
//
//            switch (view.getId()) {
//                case R.id.layout_new_sign_in_facebook:
//                    FaceBookUtil.registerCallback(new FaceBookUtil.FaceBookCallbackListener(){
//
//                        @Override
//                        public void onSuccess(com.facebook.login.LoginResult loginResult){
//                            //로그인 성공후 프로필 정보 요청
//                            FaceBookUtil.requestProfile(loginResult.getAccessToken());
//                        }
//
//                        @Override
//                        public void onCancel(){
//
//                            hideProgress();
//                        }
//
//                        @Override
//                        public void onError(FacebookException exception){
//
//                            hideProgress();
//                        }
//
//                        @Override
//                        public void onCompleted(JSONObject object, GraphResponse response){
//
//                            hideProgress();
//                            //프로필 정보 요청 성공
//                            String id = object.optString("id");
//                            String token = AccessToken.getCurrentAccessToken().getToken();
//
//                            LogUtil.e("id", "" + id);
//                            LogUtil.e("token", "" + token);
//                            mIsAutoSigIn = true;
//                            Map<String, String> params = new HashMap<>();
//                            params.put("loginId", id);
//                            params.put("password", token);
//                            params.put("accountType", SnsTypeCode.facebook.name());
//                            login(params);
//                        }
//                    });
//                    FaceBookUtil.logIn(NewSignInActivity.this);
//                    showProgress("");
//                    break;
//                case R.id.layout_new_sign_in_kakao:
//                    mKakaoSessionCallback = new KakaoSessionCallback();
//                    KakaoUtil.addCallback(mKakaoSessionCallback);
//                    Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, NewSignInActivity.this);
//                    break;
//                case R.id.layout_new_sign_in_google:
//                    showProgress("");
//                    GoogleUtil.init(NewSignInActivity.this, new GoogleUtil.GoogleSignListener(){
//
//                        @Override
//                        public void handleSignInResult(GoogleSignInResult result){
//
//                            hideProgress();
//                            LogUtil.e(LOG_TAG, result.getStatus().toString());
//                            if(result.isSuccess()) {
//                                GoogleSignInAccount acct = result.getSignInAccount();
//                                String id = acct.getId();
//                                String token = acct.getIdToken();
//
//                                LogUtil.e("id", "" + id);
//                                LogUtil.e("token", "" + token);
//                                mIsAutoSigIn = true;
//                                Map<String, String> params = new HashMap<>();
//                                params.put("loginId", id);
//                                params.put("password", token);
//                                params.put("accountType", SnsTypeCode.google.name());
//                                login(params);
//                            }
//                        }
//                    });
//                    break;
//                case R.id.layout_new_sign_in_naver:
//                    NaverUtil.click(NewSignInActivity.this, new NaverUtil.NaverCallbackListener(){
//
//                        @Override
//                        public void onSuccess(String token, String content){
//
//                            hideProgress();
//                            LogUtil.e(LOG_TAG, "token : {}, content : {}", token, content);
//                            String id = null;
//                            try {
//                                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//                                factory.setNamespaceAware(true);
//                                XmlPullParser xpp = factory.newPullParser();
//
//                                xpp.setInput(new StringReader(content));
//                                int eventType = xpp.getEventType();
//                                boolean isId = false;
//                                while (eventType != XmlPullParser.END_DOCUMENT) {
//                                    if(eventType == XmlPullParser.START_TAG) {
//                                        if(xpp.getName().equals("email")) {
//                                            isId = true;
//                                        }
//                                    } else if(eventType == XmlPullParser.TEXT) {
//                                        if(isId) {
//                                            LogUtil.e(LOG_TAG, "{} ", xpp.getText());
//                                            id = xpp.getText();
//                                            isId = false;
//                                            break;
//                                        }
//
//                                    }
//                                    eventType = xpp.next();
//                                }
//                            } catch (Exception e) {
//
//                            }
//
//                            mIsAutoSigIn = true;
//
//                            Map<String, String> params = new HashMap<>();
//                            params.put("loginId", id);
//                            params.put("password", token);
//                            params.put("accountType", SnsTypeCode.naver.name());
//                            login(params);
//                        }
//
//                        @Override
//                        public void onError(){
//
//                            LogUtil.e(LOG_TAG, "naver onError");
//                            hideProgress();
//                        }
//                    });
//                    showProgress("");
//
//                    break;
//
//            }
//        }
//    };
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
//                    mIsAutoSigIn = true;
//                    Map<String, String> params = new HashMap<>();
//                    params.put("loginId", "" + id);
//                    params.put("password", token);
//                    params.put("accountType", SnsTypeCode.kakao.name());
//                    login(params);
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
//        }
//    }
//
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.text_sigin_in:
//                String id = mEditId.getText().toString().trim();
//
//                if(StringUtils.isEmpty(id)) {
//                    showAlert(R.string.msg_input_id);
//                    return;
//                }
//
//                String password = mEditPw.getText().toString().trim();
//
//                if(StringUtils.isEmpty(password)) {
//                    showAlert(R.string.msg_input_password);
//                    return;
//                }
//
//                Map<String, String> params = new HashMap<>();
//                params.put("loginId", id);
//                params.put("password", password);
//                params.put("accountType", SnsTypeCode.pplus.name());
//                login(params);
//                break;
//            case R.id.text_sign_in_auto://자동로그인 체크
//                if(mIsAutoSigIn) {
//                    mIsAutoSigIn = false;
//                } else {
//                    mIsAutoSigIn = true;
//                }
//                view.setSelected(mIsAutoSigIn);
//                break;
//            case R.id.text_sign_in_find_id://아이디 찾기 이동
//                Intent intent = new Intent(this, FindIdActivity.class);
//                intent.putExtra(Const.KEY, Const.FIND_ID);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_FIND_ID);
//                break;
//            case R.id.text_sign_in_find_password://비밀번호 찾기 이동
//                findPw();
//                break;
//            case R.id.text_sign_in_signUp://회원가입 이동
//                goSignUp();
//                break;
//        }
//    }
//
//    private void findPw(){
//
//        Intent intent = new Intent(this, FindPWActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivityForResult(intent, Const.REQ_FIND_PW);
//    }
//
//    private void goSignUp(){
//
//        if(PplusCommonUtil.Companion.getCountryCode().equalsIgnoreCase("kr")) {
//
//            Intent intent = new Intent(this, SignUpPreActivity.class);
//            intent.putExtra(Const.KEY, Const.JOIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivityForResult(intent, Const.REQ_JOIN);
//
//            //            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//            //            builder.setTitle(getString(R.string.word_notice_alert));
//            //            builder.addContents(new AlertData.MessageData(getString(R.string.msg_verification_me_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//            //            builder.addContents(new AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//            //            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//            //            builder.setOnAlertResultListener(new OnAlertResultListener(){
//            //
//            //                @Override
//            //                public void onCancel(){
//            //
//            //                }
//            //
//            //                @Override
//            //                public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//            //
//            //                    switch (event_alert) {
//            //                        case RIGHT:
//            //                            Intent intent = new Intent(NewSignInActivity.this, VerificationMeActivity.class);
//            //                            intent.putExtra(Const.KEY, Const.JOIN);
//            //                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            //                            startActivityForResult(intent, Const.REQ_JOIN);
//            //                            break;
//            //                    }
//            //                }
//            //            }).builder().show(this);
//
//        } else {
//            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//            builder.setTitle(getString(R.string.word_notice_alert));
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_do_not_service_country_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_do_not_service_country_alert2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//            builder.setLeftText(getString(R.string.word_confirm));
//            builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                @Override
//                public void onCancel(){
//
//                }
//
//                @Override
//                public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                }
//            }).builder().show(this);
//        }
//    }
//
//    private void login(final Map<String, String> params){
//
//        showProgress("");
//        ApiBuilder.create().login(params).setCallback(new PplusCallback<NewResultResponse<User>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){
//
//                final User data = response.getData();
//                data.setPassword(PplusCommonUtil.Companion.encryption(params.get("password")));
//                hideProgress();
//                LoginInfoManager.getInstance().setUser(data);
//                LoginInfoManager.getInstance().save();
//
////                if(data.getCertificationLevel() < 11) {
////                    AlertBuilder.Builder builder = new AlertBuilder.Builder();
////                    builder.setTitle(getString(R.string.word_notice_alert));
////                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_verification_for_use_master), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
////                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_leave_cancel_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
////                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
////                    builder.setOnAlertResultListener(new OnAlertResultListener(){
////
////                        @Override
////                        public void onCancel(){
////
////                        }
////
////                        @Override
////                        public void onResult(AlertBuilder.EVENT_ALERT event_alert){
////
////                            switch (event_alert) {
////                                case RIGHT:
////                                    Intent intent = new Intent(NewSignInActivity.this, VerificationMeActivity.class);
////                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////                                    intent.putExtra(Const.KEY, Const.VERIFICATION_MASTER);
////                                    startActivityForResult(intent, Const.REQ_VERIFICATION_MASTER);
////                                    break;
////                            }
////
////                        }
////                    }).builder().show(NewSignInActivity.this);
////                } else {
////
////
////                }
//
//                Page page = LoginInfoManager.getInstance().getUser().getPage();
//                if(page != null && page.getProperties() != null && StringUtils.isNotEmpty(page.getProperties().getEmptyNumberCause()) && page.getProperties().getEmptyNumberCause().equals("removeByAdmin")) {
//                    Intent intent = new Intent(NewSignInActivity.this, RegPrNumberActivity.class);
//                    intent.putExtra(Const.KEY, Const.RE_REG);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivityForResult(intent, Const.REQ_RE_REG_NUMBER);
//                } else {
//                    existsDevice(data.getNo());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
//                hideProgress();
//                if(response != null) {
//                    if(response.getResultCode() == 503) {
//                        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                        builder.setTitle(getString(R.string.word_notice_alert));
//                        builder.addContents(new AlertData.MessageData(getString(R.string.msg_not_user_go_sign_up), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//                        builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                            @Override
//                            public void onCancel(){
//
//                            }
//
//                            @Override
//                            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                                switch (event_alert) {
//                                    case RIGHT:
//                                        goSignUp();
//                                        break;
//                                }
//                            }
//                        }).builder().show(NewSignInActivity.this);
//                    } else {
//                        LoginResultManager2.getInstance().fail(response.getData(), new LoginResultManager2.LoginResultListener(){
//
//                            @Override
//                            public void loginResult(LoginResultManager2.loginState state){
//
//                                switch (state) {
//
//                                    case AuthActivity:
//                                        findPw();
//                                        break;
//                                }
//                            }
//                        });
//                    }
//                } else {
//                    showAlert(R.string.login_fail_general);
//                }
//
//            }
//        }).build().call();
//    }
//
//    private void existsDevice(final Long no){
//
//        showProgress("");
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + no);
//        params.put("device.deviceId", PplusCommonUtil.Companion.getDeviceID());
//        params.put("device.installedApp.appKey", getPackageName());
//        params.put("device.installedApp.version", AppInfoManager.getInstance().getAppVersion());
//        ApiBuilder.create().existsDevice(params).setCallback(new PplusCallback<NewResultResponse<User>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){
//
//                hideProgress();
//                LogUtil.e(LOG_TAG, "onResponse");
//                saveDevice(response.getData());
//                if(StringUtils.isNotEmpty(AppInfoManager.getInstance().getPushToken()) && (response.getData() == null || response.getData().getDevice() == null || response.getData().getDevice().getInstalledApp() == null || StringUtils.isEmpty(response.getData().getDevice().getInstalledApp().getPushKey()) || !AppInfoManager.getInstance().getPushToken().equals(response.getData().getDevice().getInstalledApp().getPushKey()))) {
//                    registDevice(no, LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().isPushActivate(), LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().getPushMask());
//                } else {
//                    loginCheck(LoginInfoManager.getInstance().getUser());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
//                hideProgress();
//                PplusCommonUtil.Companion.alertAlarmAgree(new PplusCommonUtil.Companion.AlarmAgreeListener(){
//
//                    @Override
//                    public void result(boolean pushActivate, String pushMask){
//                        registDevice(no, pushActivate, pushMask);
//                    }
//                });
//
//            }
//        }).build().call();
//    }
//
//    private void registDevice(Long no, boolean pushActivate, String pushMask){
//
//        ParamsRegDevice params = new ParamsRegDevice();
//        params.setNo(no);
//
//        InstalledApp installedApp = new InstalledApp();
//        installedApp.setAppKey(getPackageName());
//        installedApp.setVersion(AppInfoManager.getInstance().getAppVersion());
//        installedApp.setPushActivate(pushActivate);
//        if(StringUtils.isNotEmpty(pushMask)) {
//            installedApp.setPushMask(pushMask);
//        }
//
//        installedApp.setPushKey(AppInfoManager.getInstance().getPushToken());
//        Device device = new Device();
//        device.setDeviceId(PplusCommonUtil.Companion.getDeviceID());
//        device.setPlatform("aos");
//        device.setInstalledApp(installedApp);
//        params.setDevice(device);
//        showProgress("");
//        ApiBuilder.create().registDevice(params).setCallback(new PplusCallback<NewResultResponse<User>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){
//
//                hideProgress();
//                saveDevice(response.getData());
//                AppInfoManager.getInstance().setAlimAgree(true);
//                loginCheck(LoginInfoManager.getInstance().getUser());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    private void saveDevice(User data){
//
//        LoginInfoManager.getInstance().getUser().setDevice(data.getDevice());
//        LoginInfoManager.getInstance().getUser().setTalkDenyDay(data.getTalkDenyDay());
//        LoginInfoManager.getInstance().getUser().setTalkDenyStartTime(data.getTalkDenyStartTime());
//        LoginInfoManager.getInstance().getUser().setTalkDenyEndTime(data.getTalkDenyEndTime());
//        LoginInfoManager.getInstance().getUser().setModDate(data.getModDate());
//        LoginInfoManager.getInstance().getUser().setSessionKey(data.getSessionKey());
//        LoginInfoManager.getInstance().save();
//    }
//
//    private void loginCheck(User data){
//
//        if(data.getCertificationLevel() != null && data.getCertificationLevel() < 11) {
//            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//            builder.setTitle(getString(R.string.word_notice_alert));
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_verification_for_use_master), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_leave_cancel_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//            builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                @Override
//                public void onCancel(){
//
//                }
//
//                @Override
//                public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                    switch (event_alert) {
//                        case RIGHT:
//                            Intent intent = new Intent(NewSignInActivity.this, VerificationMeActivity.class);
//                            intent.putExtra(Const.MOBILE_NUMBER, LoginInfoManager.getInstance().getUser().getMobile());
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            intent.putExtra(Const.KEY, Const.VERIFICATION_MASTER);
//                            startActivityForResult(intent, Const.REQ_VERIFICATION_MASTER);
//                            break;
//                    }
//
//                }
//            }).builder().show(NewSignInActivity.this);
//        }else{
//            Page page = LoginInfoManager.getInstance().getUser().getPage();
//
//            if(page != null) {
//                AppInfoManager.getInstance().setAutoSignIn(mIsAutoSigIn);
//                if(page.getStatus().equals(EnumData.PageStatus.normal.name())) {
//                    LoginResultManager2.getInstance().success(data, new LoginResultManager2.LoginResultListener(){
//
//                        @Override
//                        public void loginResult(LoginResultManager2.loginState state){
//
//                            switch (state) {
//                                case Success:
//                                    LogUtil.e(LOG_TAG, "loginCheck Success");
//                                    successLogin();
//                                    break;
//                                case SecessionCancelActivity:
//
//                                    Intent intent = new Intent(NewSignInActivity.this, VerificationMeActivity.class);
//                                    intent.putExtra(Const.KEY, Const.VERIFICATION);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    startActivityForResult(intent, Const.REQ_LEAVE_CANCEL);
//                                    break;
//                                case Exile: // 영구정지
//                                    setResult(RESULT_CANCELED);
//                                    finish();
//                                    break;
//                                case AuthActivity:
//                                    findPw();
//                                    break;
//                            }
//                        }
//                    });
//
//                } else if(page.getStatus().equals(EnumData.PageStatus.pending.name())) {
//
//                    Intent intent = new Intent(this, PageConfigFirstActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    intent.putExtra(Const.FIRST, false);
//                    startActivityForResult(intent, Const.REQ_SET_PAGE);
//                } else if(page.getStatus().equals(EnumData.PageStatus.reqApproval.name()) || page.getStatus().equals(EnumData.PageStatus.denyApproval.name()) || page.getStatus().equals(EnumData.PageStatus.ready.name())) {
//
//                    Intent intent = new Intent(this, PageApprovalActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivityForResult(intent, Const.REQ_SET_PAGE);
//                }
//            }
//        }
//
//    }
//
//    private void successLogin(){
//        // 자동로그인여부 저장
//        AppInfoManager.getInstance().setAutoSignIn(mIsAutoSigIn);
//
//        PPlusPermission pPlusPermission = new PPlusPermission(NewSignInActivity.this);
//        pPlusPermission.addPermission(Permission.PERMISSION_KEY.CONTACTS);
//        pPlusPermission.setPermissionListener(new PermissionListener(){
//
//            @Override
//            public void onPermissionGranted(){
//
//                LogUtil.e(LOG_TAG, "onPermissionGranted");
//                showProgress(getString(R.string.msg_contact_loading));
//                LoadContactTask task = new LoadContactTask(NewSignInActivity.this, new LoadContactTask.OnResultListener(){
//
//                    @Override
//                    public void onResult(){
//
//                        hideProgress();
//                        setResult(RESULT_OK);
//                        finish();
//                    }
//                });
//                task.execute();
//
//            }
//
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions){
//
//                LogUtil.e(LOG_TAG, "onPermissionDenied");
//                setResult(RESULT_OK);
//                finish();
//            }
//        });
//        pPlusPermission.checkPermission();
//
//
//    }
//
//    @Override
//    protected void onPause(){
//
//        super.onPause();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        KakaoUtil.handleActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        FaceBookUtil.onActivityResult(requestCode, resultCode, data);
//        GoogleUtil.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Const.REQ_FIND_ID:
//                    if(data != null && data.getStringExtra(Const.KEY).equals(Const.FIND_PW)) {
//                        findPw();
//                    }
//                    break;
//                case Const.REQ_FIND_PW:
//                    break;
//                case Const.REQ_SET_PAGE:
//                    loginCheck(LoginInfoManager.getInstance().getUser());
//                    break;
//                case Const.REQ_LEAVE_CANCEL:
//
//                    if(data != null) {
//                        Verification verification = data.getParcelableExtra(Const.VERIFICATION);
//                        if(verification == null) {
//                            return;
//                        }
//                        Map<String, String> params = new HashMap<>();
//                        params.put("number", verification.getNumber());
//                        params.put("token", verification.getToken());
//                        params.put("loginId", LoginInfoManager.getInstance().getUser().getLoginId());
//                        showProgress("");
//                        ApiBuilder.create().cancelLeave(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                            @Override
//                            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                                hideProgress();
//                                Map<String, String> params = new HashMap<String, String>();
//                                params.put("loginId", LoginInfoManager.getInstance().getUser().getLoginId());
//                                params.put("password", PplusCommonUtil.Companion.decryption(LoginInfoManager.getInstance().getUser().getPassword()));
//                                params.put("accountType", LoginInfoManager.getInstance().getUser().getAccountType());
//                                login(params);
//                            }
//
//                            @Override
//                            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                                ContactDao contactDao = DBManager.getInstance(PRNumberBizApplication.getContext()).getSession().getContactDao();
//                                contactDao.deleteAll();
//                                LoginInfoManager.getInstance().clear(); // 초기화
//                                AppInfoManager.getInstance().setAutoSignIn(false);
//                                hideProgress();
//                            }
//                        }).build().call();
//                    }
//                    break;
//                case Const.REQ_VERIFICATION_MASTER:
////                    if(data != null) {
////                        Verification verification = data.getParcelableExtra(Const.VERIFICATION);
////                        if(verification == null) {
////                            return;
////                        }
////                        Map<String, String> params = new HashMap<>();
////                        params.put("number", verification.getNumber());
////                        params.put("token", verification.getToken());
////                        params.put("type", "levelup");
////                        params.put("loginId", LoginInfoManager.getInstance().getUser().getLoginId());
////                        showProgress("");
////                        ApiBuilder.create().confirmVerification(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
////
////                            @Override
////                            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
////
////                                hideProgress();
////                                Map<String, String> params = new HashMap<String, String>();
////                                params.put("loginId", LoginInfoManager.getInstance().getUser().getLoginId());
////                                params.put("password", PplusCommonUtil.decryption(LoginInfoManager.getInstance().getUser().getPassword()));
////                                params.put("accountType", LoginInfoManager.getInstance().getUser().getAccountType());
////                                login(params);
////                            }
////
////                            @Override
////                            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
////
////                                ContactDao contactDao = DBManager.getInstance(PRNumberBizApplication.getContext()).getSession().getContactDao();
////                                contactDao.deleteAll();
////                                LoginInfoManager.getInstance().clear(); // 초기화
////                                AppInfoManager.getInstance().setAutoSignIn(false);
////                                hideProgress();
////                            }
////                        }).build().call();
////                    }
//                    break;
//                case Const.REQ_RE_REG_NUMBER:
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("loginId", LoginInfoManager.getInstance().getUser().getLoginId());
//                    params.put("password", PplusCommonUtil.Companion.decryption(LoginInfoManager.getInstance().getUser().getPassword()));
//                    params.put("accountType", LoginInfoManager.getInstance().getUser().getAccountType());
//                    login(params);
//                    break;
//            }
//        } else {
//            switch (requestCode) {
//                case Const.REQ_RE_REG_NUMBER:
//                case Const.REQ_LEAVE_CANCEL:
//                    PplusCommonUtil.Companion.logOutAndRestart();
//                    break;
//                case Const.REQ_SET_PAGE:
//                    LoginInfoManager.getInstance().clear();
//                    break;
//            }
//        }
//
//    }
//
//    private void verificationLogin(){
//
//    }
//}
