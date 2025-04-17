package com.pplus.prnumberbiz.apps.common.ui.custom;

import android.view.MotionEvent;

/**
 * Created by Windows7-00 on 2016-10-22.
 */

public class TouchClickChecker{

    private static final int DEFAULT_TOCUH_SIZE = 15;

    private float downX;
    private float downY;

    private float touchSize = DEFAULT_TOCUH_SIZE;

    private boolean isClick = false;

    public interface OnTouchClickListener {
        public void onClick();
    }

    public OnTouchClickListener mListener = null;

    public TouchClickChecker() {}

    public TouchClickChecker(OnTouchClickListener l) {
        this.mListener = l;
    }

    public boolean onTouch(MotionEvent event) {
        boolean isClick = false;
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            downX = event.getX();
            downY = event.getY();
        }
        if (MotionEvent.ACTION_UP == event.getAction()) {
            isClick = isClick(event.getX(), event.getY());
            if (isClick) {
                if (mListener != null) {
                    mListener.onClick();
                }
            }
        }

        return isClick;
    }

    private boolean isClick(float upX, float upY) {
        if (upX > downX - touchSize && upX < downX + touchSize
                && upY > downY - touchSize && upY < downY + touchSize) {
            isClick = true;
        } else {
            isClick = false;
        }

        return isClick;
    }
}
