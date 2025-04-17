//package com.pplus.prnumberuser.apps.mobilegift.ui;
//
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.viewpager.widget.ViewPager;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.common.ui.common.data.ViewPagerAdapter;
//import com.pplus.prnumberuser.apps.common.ui.custom.DirectionIndicator;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MobileGiftGuideActivity extends BaseActivity implements ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutView(){
//
//        return R.layout.activity_mobile_gift_guide;
//    }
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        ViewPager pager = (ViewPager)findViewById(R.id.pager_mobile_gift_guide);
//
//        DirectionIndicator indicator = (DirectionIndicator)findViewById(R.id.indicator_mobile_gift_guide);
//
//        List<Integer> imageList = new ArrayList<>();
//        imageList.add(R.drawable.img_gift_guide_1);
//        imageList.add(R.drawable.img_gift_guide_2);
//        imageList.add(R.drawable.img_gift_guide_3);
//        imageList.add(R.drawable.img_gift_guide_4);
//        indicator.build(LinearLayout.HORIZONTAL, imageList.size());
//
//        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageList);
//        pager.setAdapter(adapter);
//        pager.setOffscreenPageLimit(imageList.size());
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_mobile_gift_use_method), ToolbarOption.ToolbarMenu.LEFT);
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
