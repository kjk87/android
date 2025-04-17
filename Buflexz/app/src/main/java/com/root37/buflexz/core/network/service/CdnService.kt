package com.root37.buflexz.core.network.service

import com.pplus.networks.core.NetworkCore
import com.pplus.networks.core.NetworkHeader
import com.pplus.networks.service.AbstractService
import com.root37.buflexz.Const
import com.root37.buflexz.core.network.apis.ICdnApi
import okhttp3.OkHttpClient
import retrofit2.Converter
import java.lang.reflect.ParameterizedType

/**
 * Created by J2N on 16. 6. 20..
 */
class CdnService : AbstractService<ICdnApi>() {

    override fun initialize() {
        api = networkCore.retrofit.create(ICdnApi::class.java)
    }

    override fun getEndPoint(): String {

        //map base url
        return Const.CDN_URL
    }

    override fun getCustomHttpClient(): OkHttpClient? {
        return null
    }

    override fun getCustomConverterFactory(): Converter.Factory? {
        return null
    }

    override fun initializeHeader(header: NetworkHeader) {}
}