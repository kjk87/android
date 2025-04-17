//package com.pplus.prnumberbiz.apps.product;
//
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.content.ContextCompat;
//import android.os.Bundle;
//import android.support.v4.view.ViewPager;
//import android.util.SparseArray;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.custom.SlidingTabLayout;
//import com.pplus.prnumberbiz.core.network.model.dto.Category;
//
//import java.util.ArrayList;
//
//public class ProductConfigActivity extends BaseActivity implements ImplToolbar{
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
//        return R.layout.activity_product_config;
//    }
//
//    private SlidingTabLayout mTabLayout;
//    private ViewPager mPager;
//    private ProductPagerAdapter mFragmentAdapter;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mTabLayout = (SlidingTabLayout) findViewById(R.id.tabLayout_product_config_category);
//        mTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_f54641));
//        mTabLayout.setCustomTabView(R.layout.item_product_config_tab, R.id.text_product_config_category);
//        mTabLayout.setBottomBorder(getResources().getDimensionPixelSize(R.dimen.height_14));
//        mTabLayout.setDividerWidthHeight(0, 0);
//
//        mPager = (ViewPager) findViewById(R.id.pager_product_config);
//
//        ArrayList<Category> categoryList = new ArrayList<Category>();
//        Category category = new Category();
//        category.setNo(1l);
//        category.setName(getString(R.string.word_total));
//        categoryList.add(category);
//
//        if(getSupportFragmentManager().getFragments() != null) {
//            getSupportFragmentManager().getFragments().clear();
//        }
//
//        mFragmentAdapter = new ProductPagerAdapter(getSupportFragmentManager());
//        mFragmentAdapter.setTitle(categoryList);
//        mPager.setAdapter(mFragmentAdapter);
//        mTabLayout.setViewPager(mPager);
//        mPager.setCurrentItem(0);
//    }
//
//    public class ProductPagerAdapter extends FragmentPagerAdapter{
//
//        ArrayList<Category> mCategoryList;
//        SparseArray<Fragment> mFragmentArray;
//
//        public ProductPagerAdapter(FragmentManager fm){
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
//        public void setTitle(ArrayList<Category> categoryList){
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
//            Fragment fragment = ProductListFragment.newInstance(mCategoryList.get(position).getNo());
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
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_product_config), ToolbarOption.ToolbarMenu.LEFT);
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
