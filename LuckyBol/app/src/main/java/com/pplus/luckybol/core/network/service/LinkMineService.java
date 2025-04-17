package com.pplus.luckybol.core.network.service;

import com.pplus.luckybol.core.network.apis.ILinkMineApi;
import com.pplus.networks.core.NetworkHeader;
import com.pplus.networks.service.AbstractService;

import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by j2n on 2016. 11. 25..
 */

public class LinkMineService extends AbstractService<ILinkMineApi> {

    @Override
    public String getEndPoint(){

        return "http://api.linkmine.co.kr/";
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
