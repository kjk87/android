package com.pplus.prnumberbiz.core.network.service;


import com.pplus.prnumberbiz.core.network.apis.IMapApi;

import network.core.NetworkHeader;
import network.service.AbstractService;
import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by J2N on 16. 6. 20..
 */
public class MapService extends AbstractService<IMapApi>{

    @Override
    public String getEndPoint(){

        //map base url
        return "https://apis.daum.net/";
    }

    @Override
    public OkHttpClient getCustomHttpClient(){

        return null;
    }

    @Override
    public Converter.Factory getCustomConverterFactory(){

        return null;
    }

    @Override
    public void initializeHeader(NetworkHeader header){

        //        header.addHeader("HTTP_API_HASH", NetworkUtil.getSha256Hmac(DEVICE_ID));
        //        header.addHeader("HTTP_CLIENT_ACCT", NetworkUtil.getLuhnNuber("user_key"));
    }
}
