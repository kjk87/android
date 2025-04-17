package com.pplus.luckybol.core.network.service;


import com.pplus.luckybol.LuckyBolApplication;
import com.pplus.luckybol.R;
import com.pplus.luckybol.core.network.apis.IMapApi;

import com.pplus.networks.core.NetworkHeader;
import com.pplus.networks.service.AbstractService;
import okhttp3.OkHttpClient;
import retrofit2.Converter;

/**
 * Created by J2N on 16. 6. 20..
 */
public class MapService extends AbstractService<IMapApi> {

    @Override
    public String getEndPoint(){

        //map base url
        return "https://dapi.kakao.com/";
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

                header.addHeader("Authorization", "KakaoAK "+ LuckyBolApplication.getContext().getString(R.string.kakao_rest_key));
        //        header.addHeader("HTTP_CLIENT_ACCT", NetworkUtil.getLuhnNuber("user_key"));
    }
}
