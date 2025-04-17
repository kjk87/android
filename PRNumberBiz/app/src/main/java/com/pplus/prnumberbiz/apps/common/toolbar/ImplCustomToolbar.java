package com.pplus.prnumberbiz.apps.common.toolbar;

import androidx.annotation.IdRes;

/**
 * Created by j2n on 2016. 8. 12..
 */
public interface ImplCustomToolbar extends ImplToolbar{

    /**
     * Toolbar Res Id를 반환합니다.
     */
    @IdRes
    Integer getToolbarRes();

}
