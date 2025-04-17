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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.apps.page.data.PageMenuAdapterItem;
import com.pplus.prnumberuser.apps.page.data.PageMenuStickyHeaderAdapter;
import com.pplus.utils.part.logs.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class PageMenuSlidingTabLayout extends HorizontalScrollView {

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

    private RecyclerView mRecyclerView;
    private List<String> mTitleList;
    private RecyclerView.OnScrollListener mRecyclerViewOnScrollListener;

    private final PageMenuSlidingTabStrip mTabStrip;
    private boolean mIsChangeBold = true;

    public PageMenuSlidingTabLayout(Context context) {

        this(context, null);
    }

    public PageMenuSlidingTabLayout(Context context, AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public PageMenuSlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);

        mTabStrip = new PageMenuSlidingTabStrip(context);
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

    public void setOnScrollChangeCallback(RecyclerView.OnScrollListener listener) {

        mRecyclerViewOnScrollListener = listener;
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
    public void setRecyclerView(RecyclerView recyclerView, List<String> titleList) {

        mTabStrip.removeAllViews();

        mRecyclerView = recyclerView;
        mTitleList = titleList;
        if (recyclerView != null && mTitleList != null) {
            recyclerView.addOnScrollListener(new InternalOnScrollListener());
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

//        final PageMenuStickyHeaderAdapter adapter = (PageMenuStickyHeaderAdapter)mRecyclerView.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        for (int i = 0; i < mTitleList.size(); i++) {
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

        if (mRecyclerView != null) {
            int position = ((LinearLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            int titlePos = 0;
            PageMenuStickyHeaderAdapter adapter = (PageMenuStickyHeaderAdapter)mRecyclerView.getAdapter();
            if(position > 2 && position < adapter.getRecyclerItemList().size()){
                titlePos = adapter.getRecyclerItemList().get(position).getDataPos();

                if(adapter.getMDelegateList() != null && adapter.getMDelegateList().size() > 0){
                    titlePos++;
                }
            }

            scrollToTab(titlePos, 0);
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

    private class InternalOnScrollListener extends RecyclerView.OnScrollListener {

        private int mScrollState;


        @Override
        public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            mScrollState = newState;

            if (mRecyclerViewOnScrollListener != null) {
                mRecyclerViewOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }
        }

        @Override
        public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager layoutManager = (LinearLayoutManager)mRecyclerView.getLayoutManager();
            int firstPosition = layoutManager.findFirstVisibleItemPosition();
            View firstVisibleView = layoutManager.findViewByPosition(firstPosition);
            PageMenuStickyHeaderAdapter adapter = (PageMenuStickyHeaderAdapter)mRecyclerView.getAdapter();
            LogUtil.e("firstPosition", "firstPosition : "+firstPosition);
            LogUtil.e("firstPosition", "height : "+firstVisibleView.getMeasuredHeight());

            if(firstPosition > 1 && firstVisibleView.getMeasuredHeight() < getResources().getDimensionPixelSize(R.dimen.height_321)){
                firstPosition++;
            }
            if(adapter.getMDelegateList() != null && adapter.getMDelegateList().size() > 0){
                if(firstPosition == 2){
                    int top = firstVisibleView.getTop();
                    if(firstVisibleView.getMeasuredHeight() + top <= getResources().getDimensionPixelSize(R.dimen.height_321)){
                        firstPosition++;
                    }
                }else{
                    firstPosition++;
                }

            }else{
                firstPosition++;
            }


            ArrayList<PageMenuAdapterItem> itemList =  adapter.getRecyclerItemList();
            int titlePos = 0;
            if(firstPosition > 2 && firstPosition < itemList.size()){
                titlePos = itemList.get(firstPosition).getDataPos();

                if(adapter.getMDelegateList() != null && adapter.getMDelegateList().size() > 0){
                    titlePos++;
                }

            }



            mTabStrip.onRecyclerViewOnScrollChanged(titlePos, 0f);
//            View selectedTitle = mTabStrip.getChildAt(titlePos);
//            int extraOffset = (selectedTitle != null) ? (int) (positionOffset * selectedTitle.getWidth()) : 0;
            if(!isClick){
                scrollToTab(titlePos, 0);
            }else{
                isClick = false;
            }


            if (mRecyclerViewOnScrollListener != null) {
                mRecyclerViewOnScrollListener.onScrolled(recyclerView, dx, dy);
            }
        }
    }

    private boolean isClick = false;

    private class TabClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            PageMenuStickyHeaderAdapter adapter = (PageMenuStickyHeaderAdapter)mRecyclerView.getAdapter();

            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    isClick = true;
                    LinearLayoutManager layoutManager = (LinearLayoutManager)mRecyclerView.getLayoutManager();

                    if(adapter.getMDelegateList() != null && adapter.getMDelegateList().size() > 0){
                        if(i == 0){
                            layoutManager.scrollToPositionWithOffset(2, getResources().getDimensionPixelSize(R.dimen.height_321));
//                            mRecyclerView.smoothScrollToPosition(2);
                        }else{
                            int groupPos = ((PageMenuStickyHeaderAdapter)mRecyclerView.getAdapter()).getGroupPosition(i-1);
                            LogUtil.e("groupPos", "groupPos : {}", groupPos);
//                            mRecyclerView.smoothScrollToPosition(groupPos);
                            layoutManager.scrollToPositionWithOffset(groupPos, getResources().getDimensionPixelSize(R.dimen.height_321));
                        }
                    }else{
                        int groupPos = ((PageMenuStickyHeaderAdapter)mRecyclerView.getAdapter()).getGroupPosition(i-1);
//                        mRecyclerView.smoothScrollToPosition(groupPos);
                        layoutManager.scrollToPositionWithOffset(groupPos, getResources().getDimensionPixelSize(R.dimen.height_321));
                    }
                    scrollToTab(i, 0);
                    return;
                }
            }
        }
    }

}
