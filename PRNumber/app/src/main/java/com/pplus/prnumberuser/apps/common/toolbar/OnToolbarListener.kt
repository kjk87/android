package com.pplus.prnumberuser.apps.common.toolbar

import android.view.View
import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption.ToolbarMenu

/**
 * Created by j2n on 2016. 8. 16..
 */
interface OnToolbarListener {
    fun onClick(v: View?, toolbarMenu: ToolbarMenu?, tag: Any?)
}