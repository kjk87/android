//package com.pplus.prnumberbiz.apps.signup.ui;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.util.PplusNumberUtil;
//
//public class SignUpCompleteActivity extends BaseActivity{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public String getSID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_sign_up_complete;
//    }
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        //        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.layout_signUp_complete).getLayoutParams();
//        //        params.setMargins(0, ResourceUtil.getStatusBarHeight(this), 0, 0);
//
//        if(LoginInfoManager.getInstance().getUser().getPage().getNumberList() != null && StringUtils.isNotEmpty(LoginInfoManager.getInstance().getUser().getPage().getNumberList().get(0).getNumber())) {
//            String number = PplusNumberUtil.getPrNumberFormat(LoginInfoManager.getInstance().getUser().getPage().getNumberList().get(0).getNumber());
//            ((TextView) findViewById(R.id.text_signUp_complete_prnumber1)).setText(number);
//            ((TextView) findViewById(R.id.text_signUp_complete_prnumber2)).setText(number);
//        }
//
//        findViewById(R.id.text_signUp_complete_start).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                setResult(RESULT_OK);
//                finish();
//            }
//        });
//    }
//
//    @Override
//    public void onBackPressed(){
//
//    }
//}
