//package com.pplus.prnumberbiz.apps.setting.ui;
//
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.annotation.NonNull;
//import android.support.v4.content.ContextCompat;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.guide.ui.SignUpGuideActivity;
//import com.pplus.prnumberbiz.apps.common.ui.common.WebViewActivity;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Terms;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
///**
// * 앱 설정
// */
//public class AppSettingActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    private boolean isCallWebview = true; // 웹뷰로 호출하는 경우
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
//        return R.layout.activity_app_setting;
//    }
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        LinearLayout layoutMenu = (LinearLayout) findViewById(R.id.layout_setting_menu);
//
//        String[] headers = getResources().getStringArray(R.array.setting);
//        ArrayList<String[]> childList = new ArrayList<>();
//
//        childList.add(getResources().getStringArray(R.array.setting_account));
//        childList.add(getResources().getStringArray(R.array.setting_config_info));
//        childList.add(getResources().getStringArray(R.array.setting_cs));
//        childList.add(getResources().getStringArray(R.array.setting_alarm));
//
//        View viewHeader, viewChild;
//        TextView textHeaderTitle;
//        View lineView;
//        for(int i = 0; i < headers.length; i++) {
//
//            lineView = new View(this);
//            lineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
//            lineView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_e5e5e5));
//            layoutMenu.addView(lineView);
//
//            //header 추가
//            viewHeader = getLayoutInflater().inflate(R.layout.item_app_setting_h, new LinearLayout(this));
//            textHeaderTitle = (TextView) viewHeader.findViewById(R.id.text_app_setting_headerTitle);
//            textHeaderTitle.setText(headers[i]);
//            layoutMenu.addView(viewHeader);
//
//            for(int j = 0; j < childList.get(i).length; j++) {
//
//                lineView = new View(this);
//                lineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
//                lineView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_e5e5e5));
//                layoutMenu.addView(lineView);
//
//                //child추가
//                viewChild = getLayoutInflater().inflate(R.layout.item_app_setting_c, new LinearLayout(this));
//                TextView textChildTitle = (TextView) viewChild.findViewById(R.id.text_app_setting_childTitle);
//
//                if(i == 1 && j == 2) {
//                    TextView textConfigValue = (TextView) viewChild.findViewById(R.id.text_app_setting_configValue);
//                    textConfigValue.setTextColor(ContextCompat.getColor(this, R.color.color_f15433));
//                    try {
//                        PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//
//                        textConfigValue.setText(pinfo.versionName);
//
//                    } catch (Exception e) {
//
//                    }
//
//                }
//                textChildTitle.setText(childList.get(i)[j]);
//                viewChild.setId(Integer.parseInt(i + "" + j));
//                viewChild.setOnClickListener(mSettingClickListener);
//                layoutMenu.addView(viewChild);
//            }
//        }
//
//        lineView = new View(this);
//        lineView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
//        lineView.setBackgroundColor(ContextCompat.getColor(this, R.color.color_dfdfdf));
//        layoutMenu.addView(lineView);
//
//        if(LoginInfoManager.getInstance().isMember()) {
//            ((TextView) findViewById(R.id.text_signOut)).setText(R.string.word_log_out);
//        } else {
//            ((TextView) findViewById(R.id.text_signOut)).setText(R.string.word_signIn);
//        }
//        findViewById(R.id.text_signOut).setOnClickListener(this);
//        //        findViewById(R.id.text_secession).setOnClickListener(this);
//    }
//
//    private View.OnClickListener mSettingClickListener = new View.OnClickListener(){
//
//        @Override
//        public void onClick(View view){
//
//            Intent intent = null;
//            switch (view.getId()) {
//                case 0://계정관리
//                    intent = new Intent(view.getContext(), AccountConfigActivity.class);
//                    startActivity(intent);
//                    break;
//                case 1:
//                    break;
//                case 10: // 서비스소개
//                    intent = new Intent(view.getContext(), SignUpGuideActivity.class);
//                    intent.putExtra(Const.KEY, Const.GUIDE_PERSON_ALL);
//                    startActivity(intent);
//                    break;
//                case 11://약관
//                    ApiBuilder.create().getActiveTermsAll(getPackageName()).setCallback(new PplusCallback<NewResultResponse<Terms>>(){
//
//                        @Override
//                        public void onResponse(Call<NewResultResponse<Terms>> call, NewResultResponse<Terms> response){
//
//                            List<Terms> mTermsList = response.getDatas();
//
//                            if(mTermsList != null && mTermsList.size() > 0) {
//                                Intent intent = new Intent(AppSettingActivity.this, WebViewActivity.class);
//                                intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true);
//                                intent.putExtra(Const.TITLE, mTermsList.get(0).getSubject());
//                                intent.putExtra(Const.WEBVIEW_URL, mTermsList.get(0).getUrl());
//                                intent.putParcelableArrayListExtra(Const.TERMS_LIST, (ArrayList<? extends Parcelable>) mTermsList);
//                                startActivity(intent);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<NewResultResponse<Terms>> call, Throwable t, NewResultResponse<Terms> response){
//
//                        }
//                    }).build().call();
//                    break;
//                case 12://버전정보
//                    intent = new Intent(view.getContext(), ServiceVersionActivity.class);
//                    startActivity(intent);
//                    break;
//                case 20://문의하기
//                    intent = new Intent(view.getContext(), InquiryActivity.class);
//                    startActivity(intent);
//                    break;
//                case 21://공지사항
//                    intent = new Intent(view.getContext(), NoticeActivity.class);
//                    startActivity(intent);
//                    break;
//                case 22://FAQ
//                    intent = new Intent(view.getContext(), FAQActivity.class);
//                    startActivity(intent);
//                    break;
//                case 30://알림설정
//                    intent = new Intent(view.getContext(), AlimActivity.class);
//                    startActivity(intent);
//                    break;
//                case 31://알림보관함
//                    intent = new Intent(view.getContext(), AlarmContainerActivity.class);
//                    startActivity(intent);
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public void onClick(View view){
//
//        Intent intent = null;
//        LogUtil.e(LOG_TAG, "id : {}", view.getId());
//        switch (view.getId()) {
//            case R.id.text_signOut:// 로그인 or 로그아웃
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_notice_alert));
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_logout), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//                builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        switch (event_alert) {
//                            case RIGHT:
//                                PplusCommonUtil.Companion.logOutAndRestart();
//                                break;
//                        }
//                    }
//                }).builder().show(this);
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Const.REQ_SIGN_IN:
//                    if(LoginInfoManager.getInstance().isMember()) {
//                        ((TextView) findViewById(R.id.text_signOut)).setText(R.string.word_log_out);
//                    } else {
//                        ((TextView) findViewById(R.id.text_signOut)).setText(R.string.word_signIn);
//                    }
//                    break;
//            }
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_setting), ToolbarOption.ToolbarMenu.LEFT);
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
//}
