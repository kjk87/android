package com.pplus.prnumberuser.core.network.service

import com.pplus.networks.core.NetworkHeader
import com.pplus.networks.service.AbstractService
import com.pplus.prnumberuser.PRNumberApplication
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.apis.IMapApi
import okhttp3.OkHttpClient
import retrofit2.Converter

/**
 * Created by J2N on 16. 6. 20..
 */
class MapService : AbstractService<IMapApi?>() {
    override fun getEndPoint(): String {

        //map base url
        return "https://dapi.kakao.com/"
    }

    override fun getCustomHttpClient(): OkHttpClient? {
        return null
    }

    override fun getCustomConverterFactory(): Converter.Factory? {
        return null
    }

    override fun initializeHeader(header: NetworkHeader) {
        header.addHeader("Authorization", "KakaoAK " + PRNumberApplication.getContext().getString(R.string.kakao_rest_key)) //        header.addHeader("HTTP_CLIENT_ACCT", NetworkUtil.getLuhnNuber("user_key"));
    }
}