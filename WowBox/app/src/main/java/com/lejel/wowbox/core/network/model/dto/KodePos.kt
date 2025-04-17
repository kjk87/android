package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class KodePos(var id: Int? = null,
              var parentId: Int? = null,
              var provinsi: String? = null,
              var kabkota: String? = null,
              var kecamatan: String? = null,
              var kelurahan: String? = null,
              var kodePos: String? = null) : Parcelable {

}