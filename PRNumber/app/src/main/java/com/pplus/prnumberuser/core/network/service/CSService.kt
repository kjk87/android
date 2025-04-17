package com.pplus.prnumberuser.core.network.service

import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapterFactory
import com.pplus.networks.core.NetworkHeader
import com.pplus.networks.service.AbstractService
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.core.network.apis.ICSApi
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by J2N on 16. 6. 20..
 */
class CSService : AbstractService<ICSApi>() {
    override fun getEndPoint(): String {

        return Const.CS_URL
    }

    override fun getCustomHttpClient(): OkHttpClient? {
        return null
    }

    override fun getCustomConverterFactory(): Converter.Factory? {
        val typeAdapterFactory: TypeAdapterFactory = GsonTypeAdapterFactory(this.javaClass)
        val gson = GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).setLenient().create()
        return GsonConverterFactory.create(gson)
    }

    override fun initializeHeader(header: NetworkHeader) {}
}