package com.pplus.prnumberuser.core.network.model.dto

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

    override fun toString(): String {
        return "AppVersion{appKey='$appKey', status='$status', version='$version', clientProp=$clientProp, versionProp=$versionProp}"
    }
}