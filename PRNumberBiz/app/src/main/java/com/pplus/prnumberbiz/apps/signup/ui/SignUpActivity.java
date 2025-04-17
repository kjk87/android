//package com.pplus.prnumberbiz.apps.signup.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentTransaction;
//import android.view.View;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//
///**
// * 회원가입
// */
//public class SignUpActivity extends BaseActivity implements ImplToolbar{
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
//        return R.layout.activity_sign_up;
//    }
//
//    User paramsJoin;
//    private String mKey;
//
//    public String getKey(){
//
//        return mKey;
//    }
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mKey = getIntent().getStringExtra(Const.KEY);
//        paramsJoin = getIntent().getParcelableExtra(Const.JOIN);
//        if(mKey.equals(Const.JOIN)){
//            signUpSelect(paramsJoin);
//        }else{
//            signUpInput(paramsJoin);
//        }
//
//    }
//
//    private void signUpSelect(User params){
//        BaseFragment fragment = SignUpSelectAccountFragment.newInstance(params);
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.signUp_container, fragment, fragment.getClass().getSimpleName());
//        ft.commit();
//    }
//
//    public void signUpInput(User params){
//
//        BaseFragment fragment = SignUpInputFragment.newInstance(params);
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.signUp_container, fragment, fragment.getClass().getSimpleName());
//        ft.commit();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_signUp), ToolbarOption.ToolbarMenu.LEFT);
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
//                            close();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Const.REQ_JOIN:
//                    setResult(RESULT_OK);
//                    finish();
//                    break;
//            }
//        }
//    }
//
//    private void close(){
//
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setTitle(getString(R.string.word_notice_alert));
//        builder.addContents(new AlertData.MessageData(getString(R.string.msg_close_signUp), AlertBuilder.MESSAGE_TYPE.TEXT, 4));
//        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//        builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//            @Override
//            public void onCancel(){
//
//            }
//
//            @Override
//            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                switch (event_alert) {
//                    case RIGHT:
//                        finish();
//                        break;
//                }
//            }
//        }).builder().show(this);
//    }
//
//    //    @Override
//    //    public void onBackPressed(){
//    //
//    //        close();
//    //    }
//}
