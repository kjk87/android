package com.pplus.prnumberuser.apps.common.mgmt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberuser.PRNumberApplication;
import com.pplus.prnumberuser.core.network.ApiController;
import com.pplus.prnumberuser.core.network.model.dto.CountryConfig;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016-09-08.
 */
public class CountryConfigManager{

    private CountryConfig config;

    public CountryConfig getConfig(){

        return config;
    }

    public void setConfig(CountryConfig config){

        this.config = config;
    }

    // Preference
    public static final String COUNTRY_CONFIG = "country_config";

    private static CountryConfigManager mCountryConfigManager = null;

    public static CountryConfigManager getInstance(){

        if(mCountryConfigManager == null) {
            mCountryConfigManager = new CountryConfigManager();
            mCountryConfigManager.load();
        }

        return mCountryConfigManager;
    }

    public void load(){

        String data = PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).getString(COUNTRY_CONFIG);
        Type typeOfSrc = new TypeToken<CountryConfigManager>(){

        }.getType();
        CountryConfigManager manager = new Gson().fromJson(data, typeOfSrc);
        if(manager != null) {
            mCountryConfigManager = manager;
        }
    }

    public void save(){

        Type typeOfSrc = new TypeToken<CountryConfigManager>(){

        }.getType();
        String data = new Gson().toJson(mCountryConfigManager, typeOfSrc);

        PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).put(COUNTRY_CONFIG, data);

        ApiController.getPRNumberService().updateHeaders();
    }

    public void clear(){

        //        PreferenceUtil.getPreference(PPlusApplication.getContext(), SHARED_LOGIN_INFO).clear();
        mCountryConfigManager.setConfig(null);
        mCountryConfigManager.save();
        ApiController.getPRNumberService().updateHeaders();
    }
}
