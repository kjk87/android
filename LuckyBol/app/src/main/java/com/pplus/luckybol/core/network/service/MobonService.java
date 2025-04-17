package com.pplus.luckybol.core.network.service;

import com.pplus.luckybol.core.network.apis.IMobonApi;

import com.pplus.networks.core.NetworkHeader;
import com.pplus.networks.service.AbstractService;
import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by j2n on 2016. 11. 25..
 */

public class MobonService extends AbstractService<IMobonApi> {

    @Override
    public String getEndPoint(){

        return "http://www.mediacategory.com/servlet/";
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
