//package com.pplus.prnumberbiz.apps.mobilegift.ui;
//
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewPager;
//import android.util.SparseArray;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.custom.SlidingTabLayout;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftCategory;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class MobileGiftActivity extends BaseActivity implements ImplToolbar{
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
//        return R.layout.activity_mobile_gift;
//    }
//
//    private SlidingTabLayout mTabLayout;
//    private ViewPager mPager;
//    private PagerAdapter mFragmentAdapter;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mTabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout_mobile_gift_category);
//        mTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_579ffb));
//        mTabLayout.setCustomTabView(R.layout.item_mobile_gift_tab, R.id.text_mbile_gift_category);
//        mTabLayout.setBottomBorder(getResources().getDimensionPixelSize(R.dimen.height_7));
//        mTabLayout.setDividerWidthHeight(getResources().getDimensionPixelSize(R.dimen.width_80), 0);
//        mPager = (ViewPager) findViewById(R.id.page_mobile_gift);
//
//        getCategory();
//    }
//
//    private void getCategory(){
//
//        ApiBuilder.create().getMobileGiftCategoryAll().setCallback(new PplusCallback<NewResultResponse<MobileGiftCategory>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<MobileGiftCategory>> call, NewResultResponse<MobileGiftCategory> response){
//
//                mFragmentAdapter = new PagerAdapter(getSupportFragmentManager());
//
//                List<MobileGiftCategory> categoryList = new ArrayList<>();
//                MobileGiftCategory category = new MobileGiftCategory();
//                category.setName(getString(R.string.word_total));
//                categoryList.add(category);
//                categoryList.addAll(response.getDatas());
//                mFragmentAdapter.setTitle(categoryList);
//                mPager.setAdapter(mFragmentAdapter);
//                mTabLayout.setViewPager(mPager);
//                mPager.setCurrentItem(0);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<MobileGiftCategory>> call, Throwable t, NewResultResponse<MobileGiftCategory> response){
//
//            }
//        }).build().call();
//    }
//
//    public class PagerAdapter extends FragmentStatePagerAdapter{
//
//        List<MobileGiftCategory> mCategoryList;
//        SparseArray<Fragment> mFragmentArray;
//
//        public PagerAdapter(FragmentManager fm){
//
//            super(fm);
//            mFragmentArray = new SparseArray<Fragment>();
//            mCategoryList = new ArrayList<>();
//        }
//
//        public SparseArray<Fragment> getFragMap(){
//
//            return mFragmentArray;
//        }
//
//        public void setTitle(List<MobileGiftCategory> categoryList){
//
//            this.mCategoryList = categoryList;
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public String getPageTitle(int position){
//
//            return mCategoryList.get(position).getName();
//        }
//
//        @Override
//        public int getCount(){
//
//            return mCategoryList.size();
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object){
//
//            super.destroyItem(container, position, object);
//            mFragmentArray.remove(position);
//        }
//
//        public void clear(){
//
//            mCategoryList.clear();
//            mFragmentArray = new SparseArray<Fragment>();
//            notifyDataSetChanged();
//        }
//
//        public Fragment getFragment(int key){
//
//            return mFragmentArray.get(key);
//        }
//
//        @Override
//        public Fragment getItem(int position){
//
//            Fragment fragment = MobileGiftListFragment.newInstance(mCategoryList.get(position));
//            mFragmentArray.put(position, fragment);
//            return fragment;
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_mobile_gift), ToolbarOption.ToolbarMenu.LEFT);
//
//        View view = getLayoutInflater().inflate(R.layout.item_top_right, null);
//        TextView textView = (TextView) view.findViewById(R.id.text_top_right);
//        textView.setText(R.string.word_gift_history);
//        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
//        textView.setTextColor(ResourceUtil.getColor(this, R.color.black));
//        textView.setBackgroundResource(R.drawable.border_color_3a3a3a_2px);
//        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize_30pt));
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, view, 0);
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
//                    case RIGHT:
//                        if(tag.equals(1)) {
//                            Intent intent = new Intent(MobileGiftActivity.this, MobileGiftHistoryActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivity(intent);
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}
