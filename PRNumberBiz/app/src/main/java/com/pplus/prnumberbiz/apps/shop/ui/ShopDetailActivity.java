//package com.pplus.prnumberbiz.apps.shop.ui;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.SparseArray;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.common.ui.custom.SlidingTabLayout;
//import com.pplus.prnumberbiz.apps.post.ui.PostListFragment;
//import com.pplus.prnumberbiz.core.network.model.dto.Category;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ShopDetailActivity extends BaseActivity implements View.OnClickListener{
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
//        return R.layout.activity_shop_detail;
//    }
//
////    private View tab1, tab2, tab3, tab4;
////    private List<View> tabList;
//
//    private SlidingTabLayout mTabLayout;
//    private ViewPager mPager;
//    private ShopPagerAdapter mFragmentAdapter;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//        findViewById(R.id.image_shop_detail_back).setOnClickListener(this);
//
//        mTabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout_shop_detail);
//        mTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_8700ff));
//        mTabLayout.setDistributeEvenly(true);
//        mTabLayout.setCustomTabView(R.layout.item_shop_detail_tab, R.id.text_shop_detail_tab);
//        mTabLayout.setBottomBorder(getResources().getDimensionPixelSize(R.dimen.height_7));
//        mTabLayout.setDividerWidthHeight(0, 0);
//
//
//        mPager = (ViewPager) findViewById(R.id.pager_shop_detail);
//        mPager.setOffscreenPageLimit(3);
//        mFragmentAdapter = new ShopPagerAdapter(getSupportFragmentManager());
//        mPager.setAdapter(mFragmentAdapter);
//        mTabLayout.setViewPager(mPager);
//        mPager.setCurrentItem(getIntent().getIntExtra(Const.TAB, 0));
//
//    }
//
//    public class ShopPagerAdapter extends FragmentPagerAdapter{
//
//        SparseArray<Fragment> mFragmentArray;
////        String[] title = {getString(R.string.word_introduce), getString(R.string.word_promotion), getString(R.string.word_product), getString(R.string.word_coupon)};
//        String[] title = {getString(R.string.word_introduce), getString(R.string.word_promotion), getString(R.string.word_coupon)};
//
//        public ShopPagerAdapter(FragmentManager fm){
//
//            super(fm);
//            mFragmentArray = new SparseArray<Fragment>();
//            mFragmentArray.put(0, ShopIntroduceFragment.newInstance());
//            mFragmentArray.put(1, PostListFragment.newInstance());
////            mFragmentArray.put(2, ShopProductFragment.newInstance());
//            mFragmentArray.put(2, ShopCouponFragment.newInstance());
//        }
//
//        public SparseArray<Fragment> getFragMap(){
//
//            return mFragmentArray;
//        }
//
//        @Override
//        public String getPageTitle(int position){
//
//            return title[position];
//        }
//
//        @Override
//        public int getCount(){
//
//            return title.length;
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
//            return getFragment(position);
//        }
//    }
//
////    private View.OnClickListener tabClickListener = new View.OnClickListener(){
////
////        @Override
////        public void onClick(View view){
////            for(View tab : tabList){
////                tab.setSelected(false);
////            }
////            view.setSelected(true);
////
////            switch (view.getId()){
////                case R.id.layout_shop_detail_tab1:
////                    setFragment(ShopIntroduceFragment.newInstance());
////                    break;
////                case R.id.layout_shop_detail_tab2:
////                    setFragment(PostListFragment.newInstance());
////                    break;
////                case R.id.layout_shop_detail_tab3:
////                    setFragment(ShopProductFragment.newInstance());
////                    break;
////                case R.id.layout_shop_detail_tab4:
////                    setFragment(ShopCouponFragment.newInstance());
////                    break;
////            }
////        }
////    };
////
////    private void setFragment(BaseFragment fragment){
////
////        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
////        ft.replace(R.id.shop_detail_container, mapFragment, mapFragment.getClass().getSimpleName());
////        ft.commit();
////    }
//
//    @Override
//    public void onClick(View view){
//        switch (view.getId()){
//            case R.id.image_shop_detail_back:
//                onBackPressed();
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed(){
//
//        super.onBackPressed();
//        overridePendingTransition(R.anim.hold, R.anim.bottom_down);
//    }
//}
