package com.pplus.prnumberbiz.apps.common.impl;

import android.view.View;

/**
 * Created by j2n on 2016. 8. 9..
 */
public interface ImplFragment{

    int getLayoutResourceId();

    void initializeView(View container);

    void init();
}
