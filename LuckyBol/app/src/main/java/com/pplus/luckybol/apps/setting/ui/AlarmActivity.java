//package com.pplus.luckybol.apps.setting.ui;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.CompoundButton;
//
//import androidx.annotation.NonNull;
//
//import com.pplus.utils.part.utils.ArrayUtils;
//import com.pplus.luckybol.R;
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager;
//import com.pplus.luckybol.apps.common.toolbar.ImplToolbar;
//import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener;
//import com.pplus.luckybol.apps.common.toolbar.ToolbarOption;
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity;
//import com.pplus.luckybol.apps.common.ui.custom.SafeSwitchCompat;
//import com.pplus.luckybol.core.network.ApiBuilder;
//import com.pplus.luckybol.core.network.model.response.NewResultResponse;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
///**
// * 알림설정
// */
//public class AlarmActivity extends BaseActivity implements ImplToolbar, SafeSwitchCompat.OnSafeCheckedListener{
//
//    private SafeSwitchCompat switch_push;
//    private SafeSwitchCompat switch_pr_shop, switch_bol, switch_note;
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
//        return R.layout.activity_alarm;
//    }
//
//    private static final String ON = "1";
//    private static final String OFF = "0";
//
//    public static final int PR = 2;
//    public static final int RECEIVE_BOL = 4;
//    public static final int NOTE = 5;
//
//    Character[] pushChars;
//    private boolean pushActivate;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        pushActivate = LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().isPushActivate();
//        String pushMask = LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().getPushMask();
//        pushChars = ArrayUtils.toObject(pushMask.toCharArray());
//
//        switch_push = (SafeSwitchCompat) findViewById(R.id.switch_alim_push);
//
//        switch_pr_shop = (SafeSwitchCompat) findViewById(R.id.switch_alim_pr_shop);
//        switch_bol = (SafeSwitchCompat) findViewById(R.id.switch_alim_receive_bol);
//        switch_note = (SafeSwitchCompat) findViewById(R.id.switch_alim_note);
//
//        switch_push.setSafeCheck(pushActivate, SafeSwitchCompat.IGNORE);
//        switchEnable(pushActivate);
//        if(pushActivate) {
//            switch_pr_shop.setSafeCheck(pushChars[PR].toString().equals(ON), SafeSwitchCompat.IGNORE);
//            switch_bol.setSafeCheck(pushChars[RECEIVE_BOL].toString().equals(ON), SafeSwitchCompat.IGNORE);
//            switch_note.setSafeCheck(pushChars[NOTE].toString().equals(ON), SafeSwitchCompat.IGNORE);
//        } else {
//            switch_pr_shop.setSafeCheck(false, SafeSwitchCompat.IGNORE);
//            switch_bol.setSafeCheck(false, SafeSwitchCompat.IGNORE);
//            switch_note.setSafeCheck(false, SafeSwitchCompat.IGNORE);
//        }
//
//        switch_push.setOnSafeCheckedListener(this);
//        switch_pr_shop.setOnSafeCheckedListener(this);
//        switch_bol.setOnSafeCheckedListener(this);
//        switch_note.setOnSafeCheckedListener(this);
//    }
//
//    private void switchEnable(boolean enable){
//
//        switch_pr_shop.setClickable(enable);
//        switch_bol.setClickable(enable);
//        switch_note.setClickable(enable);
//    }
//
//    private void update(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("pushActivate", String.valueOf(pushActivate));
//
//        StringBuilder sb = new StringBuilder(pushChars.length);
//        for(Character c : pushChars)
//            sb.append(c.charValue());
//
//        final String pushMask = sb.toString();
//
//        params.put("pushMask", pushMask);
//
//        showProgress("");
//        ApiBuilder.create().updatePushConfig(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                hideProgress();
//                LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().setPushActivate(pushActivate);
//                LoginInfoManager.getInstance().getUser().getDevice().getInstalledApp().setPushMask(pushMask);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_alarm_setting), ToolbarOption.ToolbarMenu.LEFT);
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
//                    case LEFT:
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
//    public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck){
//
//        switch (compoundButton.getId()) {
//            case R.id.switch_alim_push:
//
//                switch_pr_shop.setOnSafeCheckedListener(null);
//                switch_bol.setOnSafeCheckedListener(null);
//                switch_note.setOnSafeCheckedListener(null);
//
//                pushActivate = isCheck;
//                switchEnable(pushActivate);
//
//                if(pushActivate) {
//                    switch_pr_shop.setSafeCheck(pushChars[PR].toString().equals(ON), SafeSwitchCompat.IGNORE);
//                    switch_bol.setSafeCheck(pushChars[RECEIVE_BOL].toString().equals(ON), SafeSwitchCompat.IGNORE);
//                    switch_note.setSafeCheck(pushChars[NOTE].toString().equals(ON), SafeSwitchCompat.IGNORE);
//                } else {
//                    switch_pr_shop.setSafeCheck(false, SafeSwitchCompat.IGNORE);
//                    switch_bol.setSafeCheck(false, SafeSwitchCompat.IGNORE);
//                    switch_note.setSafeCheck(false, SafeSwitchCompat.IGNORE);
//                }
//
//                switch_pr_shop.setOnSafeCheckedListener(this);
//                switch_bol.setOnSafeCheckedListener(this);
//                switch_note.setOnSafeCheckedListener(this);
//
//                update();
//                break;
//            case R.id.switch_alim_pr_shop:
//                if(pushActivate) {
//                    check(PR, isCheck);
//                    update();
//                }
//                break;
//            case R.id.switch_alim_receive_bol:
//                if(pushActivate) {
//                    check(RECEIVE_BOL, isCheck);
//                    update();
//                }
//                break;
//            case R.id.switch_alim_note:
//                if(pushActivate) {
//                    check(NOTE, isCheck);
//                    update();
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onAlwaysCalledListener(CompoundButton buttonView, boolean isChecked){
//
//    }
//
//    private void check(int pos, boolean isCheck){
//
//        if(isCheck) {
//            pushChars[pos] = ON.charAt(0);
//        } else {
//            pushChars[pos] = OFF.charAt(0);
//        }
//    }
//}
