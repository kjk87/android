package com.root37.buflexz.apps.common.toolbar

/**
 * Created by j2n on 2016. 8. 12..
 */
interface ImplToolbar {
    /**
     * toolbar option 정의함
     */
    fun getToolbarOption(): ToolbarOption?
    fun getOnToolbarClickListener(): OnToolbarListener?
}