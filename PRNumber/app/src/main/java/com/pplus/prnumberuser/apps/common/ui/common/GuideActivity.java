//package com.pplus.prnumberuser.apps.common.ui.common;
//
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//
//import com.pplus.utils.part.apps.resource.ResourceUtil;
//import com.pplus.utils.part.pref.PreferenceUtil;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.common.ui.common.data.ViewPagerAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GuideActivity extends BaseActivity{
//
//    private ViewPager pager;
//    private List<Integer> mImageResource = new ArrayList<>();
//    private ViewPagerAdapter mTurorialAdapter;
//    private String key;
//    private LinearLayout layout_guide_dot;
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_guide;
//    }
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
////        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.pager).getLayoutParams();
////        params.setMargins(0, ResourceUtil.getStatusBarHeight(this), 0, 0);
//
//        layout_guide_dot = (LinearLayout) findViewById(R.id.layout_guide_dot);
//        key = getIntent().getStringExtra(Const.KEY);
//        pager = (ViewPager) findViewById(R.id.pager);
//
//        if(key.equals(Const.GUIDE_MAIN)) {
//            mImageResource.add(R.drawable.img_main_guide1);
//            mImageResource.add(R.drawable.img_main_guide2);
//        } else if(key.equals(Const.GUIDE_PLUS)) {
//            mImageResource.add(R.drawable.img_plus_guide);
//        } else if(key.equals(Const.GUIDE_CONTACT)) {
//            mImageResource.add(R.drawable.img_contact_guide);
//        } else if(key.equals(Const.GUIDE_CUSTOMER)) {
//            mImageResource.add(R.drawable.img_my_contact_guide1);
//            mImageResource.add(R.drawable.img_my_contact_guide2);
//        } else if(key.equals(Const.GUIDE_SEARCH)) {
//            mImageResource.add(R.drawable.img_search_guide);
//        } else if(key.equals(Const.GUIDE_PAGE_ME)) {
//            mImageResource.add(R.drawable.img_pr_my_guide);
//        } else if(key.equals(Const.GUIDE_PAGE_OTHER)) {
//            mImageResource.add(R.drawable.img_pr_guide);
//        }else if(key.equals(Const.GUIDE_EVENT_POST)) {
//            mImageResource.add(R.drawable.img_write_event_guide1);
//        } else if(key.equals(Const.GUIDE_ALL)) {
//            mImageResource.add(R.drawable.img_main_guide1);
//            mImageResource.add(R.drawable.img_main_guide2);
//            mImageResource.add(R.drawable.img_plus_guide);
//            mImageResource.add(R.drawable.img_contact_guide);
//            mImageResource.add(R.drawable.img_my_contact_guide1);
//            mImageResource.add(R.drawable.img_my_contact_guide2);
//            mImageResource.add(R.drawable.img_search_guide);
//            mImageResource.add(R.drawable.img_pr_my_guide);
//            mImageResource.add(R.drawable.img_pr_guide);
//            mImageResource.add(R.drawable.img_write_event_guide1);
//        }
//
//        mTurorialAdapter = new ViewPagerAdapter(this, mImageResource);
//        pager.setAdapter(mTurorialAdapter);
//        pager.setOffscreenPageLimit(mImageResource.size());
//
//        if(!key.equals(Const.GUIDE_ALL)) {
//            PreferenceUtil.getDefaultPreference(this).put(key, true);
//            findViewById(R.id.image_guide_close).setVisibility(View.GONE);
//            mTurorialAdapter.setOnItemClickListener(new ViewPagerAdapter.OnItemClickListener(){
//
//                @Override
//                public void onItemClick(int position){
//
//                    onBackPressed();
//
////                    if(key.equals(Const.GUIDE_PAGE_ME)) {
////                        if(pager.getCurrentItem() == 0) {
////                            pager.setCurrentItem(1);
////                        } else if(pager.getCurrentItem() == 1) {
////                            PreferenceUtil.getDefaultPreference(GuideActivity.this).put(key, true);
////                            onBackPressed();
////                        }
////                    } else {
////                        PreferenceUtil.getDefaultPreference(GuideActivity.this).put(key, true);
////                        onBackPressed();
////                    }
//                }
//            });
//        }
//        ((RelativeLayout.LayoutParams) findViewById(R.id.image_guide_close).getLayoutParams()).setMargins(0, ResourceUtil.getStatusBarHeight(this), 0, 0);
//        findViewById(R.id.image_guide_close).setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                onBackPressed();
//            }
//        });
//
//        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
//
//            }
//
//            @Override
//            public void onPageSelected(int position){
//
//                setIndicator(position, false);
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state){
//
//            }
//        });
//
//        setIndicator(0, true);
//    }
//
//    void setIndicator(int position, boolean isFirst){
//
//        if(mTurorialAdapter != null && mTurorialAdapter.getCount() > 1) {
//
//            for(int i = 0; i < mTurorialAdapter.getCount(); i++) {
//                ImageView iv = null;
//                if(isFirst) {
//                    iv = new ImageView(this); // 페이지 표시 이미지 뷰 생성
//
//                    LinearLayout.LayoutParams mLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                    if(i != 0) {
//                        mLayout.leftMargin = getResources().getDimensionPixelSize(R.dimen.width_16);
//                        iv.setLayoutParams(mLayout);
//                    }
//                } else {
//                    iv = (ImageView) layout_guide_dot.getChildAt(i);
//                }
//
//                // 첫 페이지 표시 이미지 이면 선택된 이미지로
//                if(i == position) {
//                    iv.setBackgroundResource(R.drawable.img_paging_on);
//                } else {
//                    // 나머지는 선택안된 이미지로
//                    iv.setBackgroundResource(R.drawable.img_paging_off);
//                }
//
//                if(isFirst) {
//                    // LinearLayout에 추가
//                    layout_guide_dot.addView(iv);
//                }
//            }
//        }
//    }
//
//}
