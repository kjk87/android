package com.pplus.luckybol.core.network.service;


import com.pplus.luckybol.core.network.apis.ICdnApi;

import com.pplus.networks.core.NetworkHeader;
import com.pplus.networks.service.AbstractService;
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
