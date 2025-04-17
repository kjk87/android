//package com.pplus.prnumberbiz.apps.shop.ui;
//
//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//
//public class ShopCouponActivity extends BaseActivity implements ImplToolbar{
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
//        return R.layout.activity_shop_coupon;
//    }
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.coupon_container, ShopCouponFragment.newInstance(), ShopCouponFragment.class.getSimpleName());
//        ft.commit();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_coupon_config), ToolbarOption.ToolbarMenu.LEFT);
//
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
