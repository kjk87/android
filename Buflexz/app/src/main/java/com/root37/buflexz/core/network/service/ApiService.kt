package com.root37.buflexz.core.network.service

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import com.pplus.networks.core.NetworkHeader
import com.pplus.networks.service.AbstractService
import com.root37.buflexz.Const
import com.root37.buflexz.apps.common.mgmt.LoginInfoManager
import com.root37.buflexz.core.network.apis.IApi
import com.root37.buflexz.core.util.PplusCommonUtil
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by J2N on 16. 6. 20..
 */
class ApiService : AbstractService<IApi>() {

    override fun initialize() {
        api = networkCore.retrofit.create(IApi::class.java)
    }

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
        if (LoginInfoManager.getInstance().isMember()) {
            header.addHeader("token", LoginInfoManager.getInstance().member!!.token)
        } else {
            header.removeHeaderforKey("token")
        }
        header.addHeader("device", PplusCommonUtil.getDeviceID())
//        header.addHeader("Cache-Control", "no-cache")
    }
}