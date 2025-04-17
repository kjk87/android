package com.pplus.luckybol.apps.common.ui.common;

import android.content.Context;
import android.util.AttributeSet;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class ZoomImageView extends ImageViewTouch{

    public ZoomImageView(Context context) {
        super(context);

        init();
    }


    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        setMinScale(1.0f);
        setDoubleTapEnabled(false);
    }

}