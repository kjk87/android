//package com.pplus.prnumberuser.core.outgoing;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.WindowManager;
//
//import com.pplus.utils.part.logs.LogUtil;
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.LauncherScreenActivity;
//import com.pplus.prnumberuser.apps.common.mgmt.AppInfoManager;
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberuser.apps.common.mgmt.LoginResultManager2;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.main.ui.AppMainActivity2;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.AppVersion;
//import com.pplus.prnumberuser.core.network.model.dto.Device;
//import com.pplus.prnumberuser.core.network.model.dto.InstalledApp;
//import com.pplus.prnumberuser.core.network.model.dto.User;
//import com.pplus.prnumberuser.core.network.model.request.params.ParamsRegDevice;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberuser.core.util.DebugConfig;
//import com.pplus.prnumberuser.core.util.PplusCommonUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class OutGoingActivity extends BaseActivity{
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
//        return R.layout.activity_launcher_screen;
//    }
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        setActionbarColor(Color.TRANSPARENT);
//
//        if(LoginInfoManager.getInstance().isMember()) {
//
//            LogUtil.e(LOG_TAG, "OutGoingActivity");
//
//            DebugConfig.setDebugMode(true);
//            DebugConfig.init(this);
//
////            getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks(){
////
////                @Override
////                public void onActivityCreated(Activity activity, Bundle savedInstanceState){
////
////                    if(activity instanceof AlertActivity) {
////                        return;
////                    }
////
////                    if(PRNumberApplication.getActivityList().contains(activity)) {
////                        PRNumberApplication.getActivityList().remove(activity);
////                    }
////                    PRNumberApplication.getActivityList().add(activity);
////
////                }
////
////                @Override
////                public void onActivityDestroyed(Activity activity){
////
////                    PRNumberApplication.getActivityList().remove(activity);
////                }
////
////                /**
////                 * Unused implementation
////                 **/
////                @Override
////                public void onActivityStarted(Activity activity){
////
////                }
////
////                @Override
////                public void onActivityResumed(Activity activity){
////
////                    if(activity instanceof BaseActivity) {
//////                        PplusCommonUtil.requestScreenLog(((BaseActivity) activity).getSID());
////                    }
////                }
////
////                @Override
////                public void onActivityPaused(Activity activity){
////
////                    //                        LogUtil.e(LOG_TAG, "onActivityPaused {}", activity.getClass().getSimpleName());
////                }
////
////                @Override
////                public void onActivityStopped(Activity activity){
////
////                    //                        LogUtil.e(LOG_TAG, "onActivityStopped {}", activity.getClass().getSimpleName());
////                }
////
////                @Override
////                public void onActivitySaveInstanceState(Activity activity, Bundle outState){
////
////                }
////            });
//
//            checkAppVersion();
//        } else {
//            Intent intent = new Intent(OutGoingActivity.this, LauncherScreenActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
//
//    private void checkAppVersion(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("appKey", getPackageName());
//        params.put("version", AppInfoManager.getInstance().getAppVersion());
//
//        ApiBuilder.create().appVersion(params).setCallback(new PplusCallback<NewResultResponse<AppVersion>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<AppVersion>> call, NewResultResponse<AppVersion> response){
//
//                AppVersion appVersion = response.getData();
//
//                if(appVersion.getVersionProp() == null) {
//                    if(LoginInfoManager.getInstance().isMember()) {
//                        if(AppInfoManager.getInstance().isAutoSingIn()) {
//                            login();
//                        } else {
//                            goLauncher();
//                        }
//                    }
//                    return;
//                }
//
//                String newVersion = appVersion.getVersionProp().getLastVersion();
//                int isUpdate;
//                isUpdate = AppInfoManager.getInstance().isVersionUpdate(newVersion);
//
//                // 서버의 값이 강제업데이트인 경우에는 1로 변경
//                if(isUpdate != -1 && appVersion.getVersionProp().isMustUpdate()) {
//                    isUpdate = 1;
//                }
//
//                // 선택 업데이트인 경우
//                if(isUpdate == 0) {
//                    login();
//                }
//                // 강제 업데이트
//                else if(isUpdate == 1) {
//                    goLauncher();
//                } else {
//                    login();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<AppVersion>> call, Throwable t, NewResultResponse<AppVersion> response){
//
//            }
//
//        }).build().call();
//    }
//
//    private void login(){
//
//        final Map<String, String> params = new HashMap<>();
//
//        if(StringUtils.isEmpty(LoginInfoManager.getInstance().getUser().getLoginId()) || StringUtils.isEmpty(LoginInfoManager.getInstance().getUser().getPassword())) {
//            goLauncher();
//            return;
//        }
//
//        params.put("loginId", LoginInfoManager.getInstance().getUser().getLoginId());
//        params.put("password", PplusCommonUtil.Companion.decryption(LoginInfoManager.getInstance().getUser().getPassword()));
//        ApiBuilder.create().login(params).setCallback(new PplusCallback<NewResultResponse<User>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){
//
//                final User data = response.getData();
//                data.setPassword(PplusCommonUtil.Companion.encryption(params.get("password")));
//                LoginInfoManager.getInstance().setUser(data);
//                LoginInfoManager.getInstance().save();
//
//                existsDevice(data.getNo());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//                goLauncher();
//
//            }
//        }).build().call();
//    }
//
//    private void existsDevice(final Long no){
//
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
//                saveDevice(response.getData());
//                if(response.getData() == null || response.getData().getDevice() == null || response.getData().getDevice().getInstalledApp() == null || StringUtils.isEmpty(response.getData().getDevice().getInstalledApp().getPushKey()) || !AppInfoManager.getInstance().getPushToken().equals(response.getData().getDevice().getInstalledApp().getPushKey())) {
//                    registDevice(no, LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().isPushActivate(), LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().getPushMask());
//                } else {
//                    loginCheck(LoginInfoManager.getInstance().getUser());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
//                PplusCommonUtil.Companion.alertAlarmAgree(new PplusCommonUtil.Companion.AlarmAgreeListener(){
//
//                    @Override
//                    public void result(boolean pushActivate, String pushMask){
//                        registDevice(no, pushActivate, pushMask);
//                    }
//                });
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
//        ApiBuilder.create().registDevice(params).setCallback(new PplusCallback<NewResultResponse<User>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){
//
//                saveDevice(response.getData());
//                loginCheck(LoginInfoManager.getInstance().getUser());
//                AppInfoManager.getInstance().setAlimAgree(true);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
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
//        LoginResultManager2.getInstance().success(data, new LoginResultManager2.LoginResultListener(){
//
//            @Override
//            public void loginResult(LoginResultManager2.loginState state){
//
//                switch (state) {
//                    case Success:
//                        getPageNumber();
//                        break;
//                    default:
//                        goLauncher();
//                        break;
//                }
//            }
//        });
//    }
//
//    private void goLauncher(){
//        Intent intent = new Intent(OutGoingActivity.this, LauncherScreenActivity.class);
//        startActivity(intent);
//        finish();
//    }
//
//    private void getPageNumber(){
//
//        final String strNumber = getIntent().getStringExtra(Const.NUMBER);
//        Intent intent = new Intent(this, AppMainActivity2.class);
//        intent.putExtra(Const.KEY, Const.OUT_GOING);
//        intent.putExtra(Const.NUMBER, strNumber);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
//        finish();
//
////        Map<String, String> params = new HashMap<>();
////        params.put("search", strNumber);
////        ApiBuilder.create().getPageByNumber(params).setCallback(new PplusCallback<NewResultResponse<Page>>(){
////
////            @Override
////            public void onResponse(Call<NewResultResponse<Page>> call, NewResultResponse<Page> response){
////
////                if(response.getData() == null) {
////                    goLauncher();
////                    return;
////                }
////                PplusCommonUtil.Companion.goPage(OutGoingActivity.this, response.getData());
////                finish();
////
////            }
////
////            @Override
////            public void onFailure(Call<NewResultResponse<Page>> call, Throwable t, NewResultResponse<Page> response){
////                goLauncher();
////            }
////        }).build().call();
//
//    }
//
//    //    @Override
//    //    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//    //
//    //        super.onActivityResult(requestCode, resultCode, data);
//    //        if(resultCode == RESULT_OK) {
//    //            switch (requestCode) {
//    //                case Const.REQ_REG_NUMBER:
//    //                    Intent intent = new Intent(this, LauncherScreenActivity.class);
//    //                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//    //                    startActivity(intent);
//    //                    break;
//    //            }
//    //        }
//    //        finish();
//    //    }
//}
