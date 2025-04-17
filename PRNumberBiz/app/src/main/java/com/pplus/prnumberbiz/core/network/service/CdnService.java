package com.pplus.prnumberbiz.core.network.service;


import com.pplus.prnumberbiz.core.network.apis.ICdnApi;

import network.core.NetworkHeader;
import network.service.AbstractService;
import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by J2N on 16. 6. 20..
 */
public class CdnService extends AbstractService<ICdnApi> {

    @Override
    public String getEndPoint(){

        //map base url
        return "https://cdn.prnumber.com/";
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

    }
}
