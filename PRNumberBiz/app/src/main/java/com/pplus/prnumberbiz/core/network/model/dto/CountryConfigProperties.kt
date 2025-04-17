package com.pplus.prnumberbiz.core.network.model.dto

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by imac on 2018. 1. 8..
 */
class CountryConfigProperties(var baseUrl: String? = null,
                              var lmsPrice: Int? = null,
                              var smsPrice: Int? = null,
                              var pushPrice: Int? = null,
                              var adLmsPrice: Int? = null,
                              var adSmsPrice: Int? = null,
                              var advertisePrice: Int? = null,
                              var recommendBol: Int? = null,
                              var recommendeeBol: Int? = null,
                              var activateRecommendBol: Int? = null,
                              var bolRatio: Int? = null,
                              var taxRatio: Int? = null,
                              var inquiryBoard: Long? = null,
                              var suggestBoard: Long? = null,
                              var coopBoard: Long? = null,
                              var offerBoard: Long? = null,
                              var sysTypeBoard: Long? = null,
                              var sysTypeLastUpdate: String? = null,
                              var recommendKeyword: String? = null,
                              var inviteImageUrl: String? = null,
                              var weeklyRanking1: Int? = null,
                              var weeklyRanking2: Int? = null,
                              var weeklyRanking3: Int? = null,
                              var monthlyRanking1: Int? = null,
                              var monthlyRanking2: Int? = null,
                              var monthlyRanking3: Int? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?,
            source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(baseUrl)
        writeValue(lmsPrice)
        writeValue(smsPrice)
        writeValue(pushPrice)
        writeValue(adLmsPrice)
        writeValue(adSmsPrice)
        writeValue(advertisePrice)
        writeValue(recommendBol)
        writeValue(recommendeeBol)
        writeValue(activateRecommendBol)
        writeValue(bolRatio)
        writeValue(taxRatio)
        writeValue(inquiryBoard)
        writeValue(suggestBoard)
        writeValue(coopBoard)
        writeValue(offerBoard)
        writeValue(sysTypeBoard)
        writeString(sysTypeLastUpdate)
        writeString(recommendKeyword)
        writeString(inviteImageUrl)
        writeValue(weeklyRanking1)
        writeValue(weeklyRanking2)
        writeValue(weeklyRanking3)
        writeValue(monthlyRanking1)
        writeValue(monthlyRanking2)
        writeValue(monthlyRanking3)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CountryConfigProperties> = object : Parcelable.Creator<CountryConfigProperties> {
            override fun createFromParcel(source: Parcel): CountryConfigProperties = CountryConfigProperties(source)
            override fun newArray(size: Int): Array<CountryConfigProperties?> = arrayOfNulls(size)
        }
    }
}