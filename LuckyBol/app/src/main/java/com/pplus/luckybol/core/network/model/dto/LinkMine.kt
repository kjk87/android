package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class LinkMine(var idx: Int? = null,
               var name: String? = null,
               var kpi: String? = null,
               var type: Int? = null, //광고형식 (1:NCPI, 2:CPA,3: CPC)
               var icon_img: String? = null,
               var rwd_price: Int? = null,
               var day_remain: Int? = null,
               var day_remain_android: Int? = null,
               var day_remain_ios: Int? = null,
               var day_limit: Int? = null,
               var day_limit_android: Int? = null,
               var day_limit_ios: Int? = null,
               var summary: String? = null,
               var summary_short: String? = null,
               var url: String? = null,
               var url_short: String? = null,
               var url_fb: String? = null,
               var os: Int? = null,
               var ban1: String? = null,
               var ban_line: String? = null,
               var ban_half: String? = null,
               var ban_lock: String? = null,
               var ban_line1: String? = null,
               var ban_line2: String? = null,
               var ban_box: String? = null,
               var ban_box1: String? = null,
               var evt_open_nm: String? = null,
               var evt_goal_nm: String? = null,
               var evt_token: String? = null,
               var mda_type: String? = null) : Parcelable {
    constructor(source: Parcel) : this(source.readValue(Int::class.java.classLoader) as Int?, source.readString(), source.readString(), source.readValue(Int::class.java.classLoader) as Int?, source.readString(), source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Int::class.java.classLoader) as Int?, source.readValue(Int::class.java.classLoader) as Int?, source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readValue(Int::class.java.classLoader) as Int?, source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(idx)
        writeString(name)
        writeString(kpi)
        writeValue(type)
        writeString(icon_img)
        writeValue(rwd_price)
        writeValue(day_remain)
        writeValue(day_remain_android)
        writeValue(day_remain_ios)
        writeValue(day_limit)
        writeValue(day_limit_android)
        writeValue(day_limit_ios)
        writeString(summary)
        writeString(summary_short)
        writeString(url)
        writeString(url_short)
        writeString(url_fb)
        writeValue(os)
        writeString(ban1)
        writeString(ban_line)
        writeString(ban_half)
        writeString(ban_lock)
        writeString(ban_line1)
        writeString(ban_line2)
        writeString(ban_box)
        writeString(ban_box1)
        writeString(evt_open_nm)
        writeString(evt_goal_nm)
        writeString(evt_token)
        writeString(mda_type)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LinkMine> = object : Parcelable.Creator<LinkMine> {
            override fun createFromParcel(source: Parcel): LinkMine = LinkMine(source)
            override fun newArray(size: Int): Array<LinkMine?> = arrayOfNulls(size)
        }
    }
}