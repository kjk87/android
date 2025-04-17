package com.lejel.wowbox.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by j2n on 2016. 7. 26..
 */
@Parcelize
class Kabkota(var id: Int? = null,
              var parentId: Int? = null,
              var kabkota: String? = null) : Parcelable {

}