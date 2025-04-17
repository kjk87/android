package com.pplus.luckybol.core.network.service

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import com.pplus.luckybol.Const
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.core.network.apis.IPRNumberApi
import com.pplus.networks.core.NetworkHeader
import com.pplus.networks.service.AbstractService
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by J2N on 16. 6. 20..
 */
class PRNumberService : AbstractService<IPRNumberApi>() {
    override fun getEndPoint(): String {
        return Const.API_URL
    }

    override fun getCustomHttpClient(): OkHttpClient? {
        return null
    }

    override fun getCustomConverterFactory(): Converter.Factory {
        val typeAdapterFactory: TypeAdapterFactory = GsonTypeAdapterFactory(this.javaClass)
        val gson = GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).setLenient().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create()
        return GsonConverterFactory.create(gson)
    }

    override fun initializeHeader(header: NetworkHeader) {
        if (LoginInfoManager.getInstance().isMember) {
            header.addHeader("sessionKey", LoginInfoManager.getInstance().user!!.sessionKey)
        } else {
            header.removeHeaderforKey("sessionKey")
        }
        header.addHeader("Cache-Control", "no-cache")
    }
}