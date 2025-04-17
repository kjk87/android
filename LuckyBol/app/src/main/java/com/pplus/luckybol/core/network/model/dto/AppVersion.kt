package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class AppVersion(var appKey: String? = null,
                 var status: String? = null,
                 var version: String? = null,
                 var clientProp: ClientProp? = null,
                 var versionProp: VersionProp? = null) : Parcelable {

}