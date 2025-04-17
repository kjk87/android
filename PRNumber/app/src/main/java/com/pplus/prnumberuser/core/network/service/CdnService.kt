package com.pplus.prnumberuser.core.network.service

import com.pplus.networks.core.NetworkHeader
import com.pplus.networks.service.AbstractService
import com.pplus.prnumberuser.core.network.apis.ICdnApi
import okhttp3.OkHttpClient
import retrofit2.Converter

/**
 * Created by J2N on 16. 6. 20..
 */
class CdnService : AbstractService<ICdnApi?>() {
    override fun getEndPoint(): String {

        //map base url
        return "https://cdn.prnumber.com/"
    }

    override fun getCustomHttpClient(): OkHttpClient? {
        return null
    }

    override fun getCustomConverterFactory(): Converter.Factory? {
        return null
    }

    override fun initializeHeader(header: NetworkHeader) {}
}