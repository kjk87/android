package com.pplus.luckybol.core.network.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by imac on 2017. 8. 23..
 */
@Parcelize
class Coord(var x: Double? = null,
            var y: Double? = null) : Parcelable {

    override fun toString(): String {
        return "Coord{x=$x, y=$y}"
    }

}