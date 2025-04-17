/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pplus.prnumberuser.apps.common.ui.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;


public class SlidingTabLayout2 extends HorizontalScrollView {

    /**
     * Allows complete control over the colors drawn in the tab layout. Set with {@link
     * #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

        /**
         * @return return the color of the divider drawn to the right of {@code position}.
         */
        int getDividerColor(int position);

    }

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private boolean mDistributeEvenly;

    private ViewPager2 mViewPager;
    private List<String> mTitleList;
    private ViewPager2.OnPageChangeCallback mViewPagerPageChangeCallback;
    private NormalListener mNormalListener;

    private final SlidingTabStrip2 mTabStrip;
    private boolean mIsChangeBold = true;
    private boolean mIsUnselected = false;

    public SlidingTabLayout2(Context context) {

        this(context, null);
    }

    public SlidingTabLayout2(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public SlidingTabLayout2(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new SlidingTabStrip2(context);
        mTabStrip.setGravity(Gravity.CENTER_VERTICAL);
        addView(mTabStrip, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setDividerWidthHeight(int width, int height) {

        mTabStrip.setDividerWidthHeight(width, height);
    }

    public void setIsChangeBold(boolean isChangeBold){
        this.mIsChangeBold = isChangeBold;
    }

    public void setBottomBorder(int height) {

        mTabStrip.setBottomBorderHeight(height);
    }

    public void setNormalListener(NormalListener listener){
        mNormalListener = listener;
    }

    /**
     * Set the custom {@link TabColorizer} to be used.
     * <p>
     * If you only require simple custmisation then you can use {@link
     * #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve similar
     * effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {

        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    public void setDistributeEvenly(boolean distributeEvenly) {

        mDistributeEvenly = distributeEvenly;
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same
     * color.
     */
    public void setSelectedIndicatorColors(int... colors) {

        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     */
    public void setDividerColors(int... colors) {

        mTabStrip.setDividerColors(colors);
    }

    /**
     * Set the {@link ViewPager.OnPageChangeListener}. When using {@link SlidingTabLayout2} you are
     * required to set any {@link ViewPager.OnPageChangeListener} through this method. This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeCallback(ViewPager2.OnPageChangeCallback listener) {

        mViewPagerPageChangeCallback = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId  id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {

        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager2 viewPager, List<String> titleList) {

        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        mTitleList = titleList;
        if (viewPager != null && mTitleList != null) {
            viewPager.registerOnPageChangeCallback(new InternalViewPagerCallback());
            populateTabStrip();
        }
    }

    /**
     * Create a default view to be used for tabs. This is called if a custom tab view is not set via
     * {@link #setCustomTabView(int, int)}.
     */
    protected TextView createDefaultTabView(Context context) {

        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
//        textView.setTypeface(Typeface.DEFAULT_BOLD);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private void populateTabStrip() {

        final FragmentStateAdapter adapter = (FragmentStateAdapter)mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < adapter.getItemCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip, false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
                tabTitleView.setSingleLine();
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            if (mDistributeEvenly) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            tabTitleView.setText(mTitleList.get(i));
            tabView.setOnClickListener(tabClickListener);

            mTabStrip.addView(tabView, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    protected void onAttachedToWindow() {

        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    public void setNormal(int i){
        mIsUnselected = false;
        View selectedChild = mTabStrip.getChildAt(i);
        selectedChild.setSelected(true);
        TextView textView = (TextView) selectedChild.findViewById(mTabViewTextViewId);
        if(mIsChangeBold){
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }
    }

    public void setUnselected(){
        mIsUnselected = true;
        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            mTabStrip.getChildAt(i).setSelected(false);
            TextView textView = (TextView) mTabStrip.getChildAt(i).findViewById(mTabViewTextViewId);
            if(mIsChangeBold){
                textView.setTypeface(Typeface.create(textView.getTypeface(), Typeface.NORMAL));
            }
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {

        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        for (int i = 0; i < mTabStrip.getChildCount(); i++) {
            mTabStrip.getChildAt(i).setSelected(false);
            TextView textView = (TextView) mTabStrip.getChildAt(i).findViewById(mTabViewTextViewId);
            if(mIsChangeBold){
                textView.setTypeface(Typeface.create(textView.getTypeface(), Typeface.NORMAL));
            }

        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        selectedChild.setSelected(true);
        TextView textView = (TextView) selectedChild.findViewById(mTabViewTextViewId);
        if(mIsChangeBold){
            textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        }


        if (selectedChild != null) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;

            if (tabIndex > 0 || positionOffset > 0) {
                // If we're not at the first child and are mid-scroll, make sure we obey the offset
                targetScrollX -= mTitleOffset;
            }

            scrollTo(targetScrollX, 0);
        }
    }

    private class InternalViewPagerCallback extends ViewPager2.OnPageChangeCallback {

        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null) ? (int) (positionOffset * selectedTitle.getWidth()) : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeCallback != null) {
                mViewPagerPageChangeCallback.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

            mScrollState = state;

            if (mViewPagerPageChangeCallback != null) {
                mViewPagerPageChangeCallback.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {

            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeCallback != null) {
                mViewPagerPageChangeCallback.onPageSelected(position);
            }

            if(mIsUnselected && mNormalListener != null){
                mNormalListener.onNormal();
            }

        }

    }

    public interface NormalListener {
        public void onNormal();
    }

    private class TabClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    if(mIsUnselected && mViewPager.getCurrentItem() == i){
                        if(mNormalListener != null){
                            mNormalListener.onNormal();
                        }
                        setNormal(i);
                    }
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

}
