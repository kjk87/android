package com.pplus.prnumberuser.apps.page.data

import android.os.Parcelable
import com.pplus.prnumberuser.core.network.model.dto.OrderMenu
import com.pplus.prnumberuser.core.network.model.dto.OrderMenuGroup
import kotlinx.parcelize.Parcelize

/**
 * Created By Lonnie on 2020/05/08
 *
 */

@Parcelize
data class PageMenuAdapterItem(var type: Int, var group:OrderMenuGroup? = null, var menu:OrderMenu? = null, var groupPos:Int? = null, var dataPos:Int? = null):Parcelable