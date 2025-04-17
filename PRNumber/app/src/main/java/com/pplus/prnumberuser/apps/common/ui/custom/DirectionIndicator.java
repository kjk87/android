package com.pplus.prnumberuser.apps.common.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pplus.prnumberuser.R;


/**
 * Created by Windows7-00 on 2016-10-21.
 */

public class DirectionIndicator extends LinearLayout {

    private int mImageResId = R.drawable.indi_post;
    private int mIndicatorCount = 0;
    private ImageView[] mIndicators = null;

    public DirectionIndicator(Context context) {
        super(context);
        initLayout();
    }

    public DirectionIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout();
    }

    public DirectionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout() {
        //do nothing
    }

    public void setImageResId(int resId) {
        this.mImageResId = resId;
    }

    public void build(int orientation, int count) {
        this.setOrientation(orientation);
        this.setGravity(Gravity.CENTER);
        mIndicatorCount = count;

        mIndicators = new ImageView[mIndicatorCount];
        for (int i = 0; i < mIndicatorCount; i++) {
            mIndicators[i] = new ImageView(getContext());
            LayoutParams lp = null;
            if (orientation == LinearLayout.HORIZONTAL) {
                lp = new LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                lp.rightMargin = getContext().getResources().getDimensionPixelSize(R.dimen.height_12);
            } else {
                lp = new LayoutParams(
                        getContext().getResources().getDimensionPixelSize(R.dimen.width_24),
                        getContext().getResources().getDimensionPixelSize(R.dimen.height_24));

                lp.bottomMargin = getContext().getResources().getDimensionPixelSize(R.dimen.height_18);
            }
            mIndicators[i].setImageResource(mImageResId);
            mIndicators[i].setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            this.addView(mIndicators[i], lp);
        }

        setCurrentItem(0);
    }

    public void setCurrentItem(int position) {
        if (position < 0 || position > mIndicatorCount - 1) {
            return;
        }
        for (int i = 0; i < mIndicatorCount; i++) {
            if (position == i) {
                mIndicators[i].setSelected(true);
            } else {
                mIndicators[i].setSelected(false);
            }
        }
    }

}
