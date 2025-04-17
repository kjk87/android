//package com.pplus.prnumberbiz.apps.signup.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//
//public class SignUpPreActivity extends BaseActivity{
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
//        return R.layout.activity_sgin_up_pre;
//    }
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        findViewById(R.id.image_sign_up_pre_close).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                finish();
//            }
//        });
//
//        findViewById(R.id.text_sign_up_pre_verification_me).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(SignUpPreActivity.this, VerificationMeActivity.class);
//                intent.putExtra(Const.KEY, Const.JOIN);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_JOIN);
//            }
//        });
//    }
//
//}
