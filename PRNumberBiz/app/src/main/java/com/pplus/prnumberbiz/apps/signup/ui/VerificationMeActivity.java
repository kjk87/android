package com.pplus.prnumberbiz.apps.signup.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;

public class VerificationMeActivity extends BaseActivity{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_verification_me;
    }

    private String mKey, mMobileNumber;

    public String getKey(){

        return mKey;
    }

    public String getMobileNumber(){

        return mMobileNumber;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        mKey = getIntent().getStringExtra(Const.KEY);
        mMobileNumber = getIntent().getStringExtra(Const.MOBILE_NUMBER);

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.verification_me_container, VerificationMeFragment.newInstance(), VerificationMeFragment.class.getSimpleName());
//        ft.commit();

        verification("");
    }

    public void verification(String params){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.verification_me_container, VerificationMeStep2Fragment.newInstance(params), VerificationMeStep2Fragment.class.getSimpleName());
//        ft.addToBackStack("");
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQ_JOIN:
                if(resultCode == RESULT_OK) {
                    setResult(RESULT_OK);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
                break;
        }
    }

//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.RIGHT);
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
//        if(mKey.equals(Const.MOBILE_NUMBER)){
//            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//            builder.setTitle(getString(R.string.word_notice_alert));
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_change_mobile_stop), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
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
//                            finish();
//                            break;
//                    }
//                }
//            }).builder().show(this);
//        }else{
//            super.onBackPressed();
//        }
//
//
//    }
}
