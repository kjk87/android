package com.pplus.prnumberbiz.core.network.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.apps.common.mgmt.CountryConfigManager;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.core.network.apis.IPRNumberApi;
import com.pplus.prnumberbiz.core.util.DebugConfig;

import network.core.NetworkHeader;
import network.service.AbstractService;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by J2N on 16. 6. 20..
 */
public class PRNumberService extends AbstractService<IPRNumberApi>{

    @Override
    public String getEndPoint(){

        String url = Const.API_URL;
//        if(!DebugConfig.isDebugMode()){
//            if(CountryConfigManager.getInstance().getConfig() != null && CountryConfigManager.getInstance().getConfig().getProperties() != null && StringUtils.isNotEmpty(CountryConfigManager.getInstance().getConfig().getProperties().getBaseUrl())){
//                url = CountryConfigManager.getInstance().getConfig().getProperties().getBaseUrl() + "/store/api/";
//            }
//        }


        return url;
    }

    @Override
    public OkHttpClient getCustomHttpClient(){

        return null;
    }

    @Override
    public Converter.Factory getCustomConverterFactory(){

        TypeAdapterFactory typeAdapterFactory = new GsonTypeAdapterFactory(this.getClass());
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).setLenient().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create();
        return GsonConverterFactory.create(gson);
    }

    @Override
    public void initializeHeader(NetworkHeader header){

        if(LoginInfoManager.getInstance().isMember()) {
            header.addHeader("sessionKey", LoginInfoManager.getInstance().getUser().getSessionKey());
        }else{
            header.removeHeaderforKey("sessionKey");
        }
        header.addHeader("Cache-Control", "no-cache");
    }
}
