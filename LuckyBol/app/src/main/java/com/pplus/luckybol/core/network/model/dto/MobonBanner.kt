package com.pplus.luckybol.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 2..
 */
class MobonBanner(var t0: String? = null,
                  var pcode: String? = null,
                  var pnm: String? = null,
                  var site_name: String? = null,
                  var site_title: String? = null,
                  var price: String? = null,
                  var img: String? = null,
                  var purl: String? = null,
                  var logo: String? = null,
                  var logo2: String? = null,
                  var desc: String? = null,
                  var desc_web: String? = null,
                  var site_url: String? = null,
                  var site_desc1: String? = null,
                  var site_desc2: String? = null,
                  var site_desc3: String? = null,
                  var site_desc4: String? = null,
                  var icon19: String? = null,
                  var icon20: String? = null,
                  var edge_yn: String? = null,
                  var imageSqreYn: String? = null,
                  var advrtsReplcNm: String? = null,
                  var point: String? = null,
                  var mimg_720_120: String? = null,
                  var mimg_250_250: String? = null,
                  var mimg_120_600: String? = null,
                  var mimg_728_90: String? = null,
                  var mimg_300_180: String? = null,
                  var mimg_800_1500: String? = null,
                  var mimg_160_300: String? = null,
                  var mimg_300_65: String? = null,
                  var mimg_850_800: String? = null,
                  var mimg_960_100: String? = null,
                  var mimg_160_600: String? = null,
                  var mimg_640_350: String? = null,
                  var mimg_300_250: String? = null,
                  var mimg_320_100: String? = null,
                  var mimg_300_300: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(t0)
        writeString(pcode)
        writeString(pnm)
        writeString(site_name)
        writeString(site_title)
        writeString(price)
        writeString(img)
        writeString(purl)
        writeString(logo)
        writeString(logo2)
        writeString(desc)
        writeString(desc_web)
        writeString(site_url)
        writeString(site_desc1)
        writeString(site_desc2)
        writeString(site_desc3)
        writeString(site_desc4)
        writeString(icon19)
        writeString(icon20)
        writeString(edge_yn)
        writeString(imageSqreYn)
        writeString(advrtsReplcNm)
        writeString(point)
        writeString(mimg_720_120)
        writeString(mimg_250_250)
        writeString(mimg_120_600)
        writeString(mimg_728_90)
        writeString(mimg_300_180)
        writeString(mimg_800_1500)
        writeString(mimg_160_300)
        writeString(mimg_300_65)
        writeString(mimg_850_800)
        writeString(mimg_960_100)
        writeString(mimg_160_600)
        writeString(mimg_640_350)
        writeString(mimg_300_250)
        writeString(mimg_320_100)
        writeString(mimg_300_300)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MobonBanner> = object : Parcelable.Creator<MobonBanner> {
            override fun createFromParcel(source: Parcel): MobonBanner = MobonBanner(source)
            override fun newArray(size: Int): Array<MobonBanner?> = arrayOfNulls(size)
        }
    }
}