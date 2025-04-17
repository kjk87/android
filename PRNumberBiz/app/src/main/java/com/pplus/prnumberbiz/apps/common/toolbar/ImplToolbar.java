package com.pplus.prnumberbiz.apps.common.toolbar;

import androidx.annotation.NonNull;

/**
 * Created by j2n on 2016. 8. 12..
 */
public interface ImplToolbar{

    /**
     * toolbar option 정의함
     * */
    @NonNull
    ToolbarOption getToolbarOption();

    OnToolbarListener getOnToolbarClickListener();
}
