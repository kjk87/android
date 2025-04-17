package com.pplus.luckybol.apps.main.data

import android.os.Parcelable
import com.pplus.luckybol.core.network.model.dto.Contact
import kotlinx.parcelize.Parcelize

/**
 * Created By Lonnie on 2020/05/08
 *
 */

@Parcelize
data class ContactAdapterItem(var type: Int, var data:Contact? = null):Parcelable