//package com.pplus.prnumberbiz.apps.signup.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentTransaction;
//import android.view.View;
//
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.PRNumberBizApplication;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.LauncherScreenActivity;
//import com.pplus.prnumberbiz.apps.common.mgmt.AppInfoManager;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.pages.ui.PageConfigFirstActivity;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Device;
//import com.pplus.prnumberbiz.core.network.model.dto.InstalledApp;
//import com.pplus.prnumberbiz.core.network.model.dto.Page;
//import com.pplus.prnumberbiz.core.network.model.dto.PageProperties;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.network.model.request.params.ParamsRegDevice;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.network.model.dto.PRNumber;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class RegPrNumberActivity extends BaseActivity implements ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_reg_pr_number;
//    }
//
//    private User paramsJoin;
//    private String mKey;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mKey = getIntent().getStringExtra(Const.KEY);
//
//        if(mKey.equals(Const.JOIN) || mKey.equals(Const.VERIFICATION_MASTER)){
//            paramsJoin = getIntent().getParcelableExtra(Const.JOIN);
//
//            BaseFragment fragment = SelectNumberFragment.newInstance();
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.reg_prnumber_container, fragment, fragment.getClass().getSimpleName());
//            ft.addToBackStack(fragment.getClass().getSimpleName());
//            ft.commit();
//        }else{
//            inputPrNumber(EnumData.PageTypeCode.valueOf(LoginInfoManager.getInstance().getUser().getPage().getType()));
//        }
//
//    }
//
//    public String getKey(){
//
//        return mKey;
//    }
//
//    public User getParamsJoin(){
//
//        return paramsJoin;
//    }
//
//    public void inputPrNumber(EnumData.PageTypeCode pageTypeCode){
//
//        BaseFragment fragment = InputPrNumberFragment.newInstance(pageTypeCode);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.reg_prnumber_container, fragment, fragment.getClass().getSimpleName());
//        ft.addToBackStack(fragment.getClass().getSimpleName());
//        ft.commit();
//    }
//
//    public void allocateNumber(final String number){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("number", number);
//        params.put("pageNo", ""+LoginInfoManager.getInstance().getUser().getPage().getNo());
//        showProgress("");
//        ApiBuilder.create().allocateVirtualNumberToPage(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//                Page page = LoginInfoManager.getInstance().getUser().getPage();
//
//                if(page.getProperties() == null){
//                    page.setProperties(new PageProperties());
//                }else{
//                    page.getProperties().setEmptyNumberCause("");
//                }
//                ApiBuilder.create().updatePagePropertiesAll(page).setCallback(new PplusCallback<NewResultResponse<Page>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Page>> call, NewResultResponse<Page> response){
//                        hideProgress();
//                        setResult(RESULT_OK);
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Page>> call, Throwable t, NewResultResponse<Page> response){
//                        hideProgress();
//                    }
//                }).build().call();
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//                hideProgress();
//                switch (response.getResultCode()) {
//                    case 504:
//                        showAlert(getString(R.string.msg_using_number));
//                        break;
//                    case 610:
//                    case 611:
//                        showAlert(getString(R.string.msg_can_not_use_number));
//                        break;
//                }
//            }
//        }).build().call();
//    }
//
//    public void levelUp(final String prNumber){
//        PRNumber number = new PRNumber();
//        number.setNumber(prNumber);
//        paramsJoin.setNumber(number);
//        paramsJoin.setNo(LoginInfoManager.getInstance().getUser().getNo());
//        showProgress("");
//
//        ApiBuilder.create().levelup(paramsJoin).setCallback(new PplusCallback<NewResultResponse<User>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){
//
//                hideProgress();
//                User user = LoginInfoManager.getInstance().getUser();
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("loginId", user.getLoginId());
//                params.put("password", PplusCommonUtil.Companion.decryption(user.getPassword()));
//                params.put("accountType", user.getAccountType());
//                login(params);
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
//                hideProgress();
//                switch (response.getResultCode()) {
//                    case 504:
//                        showAlert(getString(R.string.msg_using_number));
//                        break;
//                    case 587:
//                        showAlert(getString(R.string.msg_need_agree_terms));
//                        break;
//                    case 610:
//                    case 611:
//                        showAlert(getString(R.string.msg_can_not_use_number));
//                        break;
//                    default:
//                        showAlert(getString(R.string.msg_failed_levelup));
//                        break;
//                }
//            }
//        }).build().call();
//    }
//
//    public void signUp(final String prNumber){
//
//        PRNumber number = new PRNumber();
//        number.setNumber(prNumber);
//        paramsJoin.setNumber(number);
//        showProgress("");
//
//        ApiBuilder.create().join(paramsJoin).setCallback(new PplusCallback<NewResultResponse<User>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){
//
//                hideProgress();
//                LoginInfoManager.getInstance().setUser(response.getData());
//                LoginInfoManager.getInstance().getUser().setPassword(PplusCommonUtil.Companion.encryption(paramsJoin.getPassword()));
//                LoginInfoManager.getInstance().save();
//
//                User user = LoginInfoManager.getInstance().getUser();
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("loginId", user.getLoginId());
//                params.put("password", PplusCommonUtil.Companion.decryption(user.getPassword()));
//                params.put("accountType", user.getAccountType());
//                login(params);
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
//                hideProgress();
//                switch (response.getResultCode()) {
//                    case 504:
//                        showAlert(getString(R.string.msg_using_number));
//                        break;
//                    case 587:
//                        showAlert(getString(R.string.msg_need_agree_terms));
//                        break;
//                    case 583:
//                        showAlert(getString(R.string.msg_duplicate_id));
//                        break;
//                    case 610:
//                    case 611:
//                        showAlert(getString(R.string.msg_can_not_use_number));
//                        break;
//                    default:
//                        showAlert(getString(R.string.msg_failed_join));
//                        break;
//                }
//            }
//        }).build().call();
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
//                try {
//                    data.setPassword(PplusCommonUtil.Companion.encryption(params.get("password")));
//                }catch (Exception e){
//
//                }
//
//                hideProgress();
//                LoginInfoManager.getInstance().setUser(data);
//                LoginInfoManager.getInstance().save();
//
//                existsDevice(data.getNo());
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){
//
//                hideProgress();
//                showAlert(R.string.login_fail_general);
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
//                    AppInfoManager.getInstance().setAlimAgree(true);
//                    setPage();
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
//                setPage();
//
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
//    private void setPage(){
//        Intent intent = new Intent(this, PageConfigFirstActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivityForResult(intent, Const.REQ_SET_PAGE);
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
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case Const.REQ_SET_PAGE:
//                if(resultCode == RESULT_OK) {
//                    successLogin();
//                } else {
//                    setPage();
//                }
//                break;
//        }
//    }
//
//    private void successLogin(){
//
//        // 자동로그인여부 저장
//        AppInfoManager.getInstance().setAutoSignIn(true);
//        for(Activity activity : PRNumberBizApplication.getActivityList()) {
//            activity.finish();
//        }
//        Intent intent = new Intent(RegPrNumberActivity.this, LauncherScreenActivity.class);
//        startActivity(intent);
//
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reg_prnumber), ToolbarOption.ToolbarMenu.RIGHT);
//        return toolbarOption;
//    }
//
//    @Override
//    public OnToolbarListener getOnToolbarClickListener(){
//
//        return new OnToolbarListener(){
//
//            @Override
//            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){
//
//                switch (toolbarMenu) {
//                    case RIGHT:
//                        if(tag.equals(1)) {
//                            onBackPressed();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//
//    @Override
//    public void onBackPressed(){
//
//        PplusCommonUtil.Companion.hideKeyboard(this);
//        if(getSupportFragmentManager().getBackStackEntryCount() == 1) {
//            finish();
//        } else {
//            super.onBackPressed();
//        }
//    }
//}
