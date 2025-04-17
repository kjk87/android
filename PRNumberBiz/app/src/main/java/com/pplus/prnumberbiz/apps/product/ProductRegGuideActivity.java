//package com.pplus.prnumberbiz.apps.product;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.pple.pplus.utils.part.pref.PreferenceUtil;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//
//public class ProductRegGuideActivity extends BaseActivity{
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
//        return R.layout.activity_product_reg_guide;
//    }
//
//    private boolean mIsLook = false;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        findViewById(R.id.text_product_reg_guide_check).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                mIsLook = !mIsLook;
//                view.setSelected(mIsLook);
//            }
//        });
//
//        findViewById(R.id.text_product_reg_guide_confirm).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                PreferenceUtil.getDefaultPreference(ProductRegGuideActivity.this).put(Const.PRODUCT_GUIDE, mIsLook);
//                finish();
//            }
//        });
//    }
//}
