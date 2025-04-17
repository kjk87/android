package com.pplus.prnumberbiz.apps.common.mgmt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.PRNumberBizApplication;
import com.pplus.prnumberbiz.core.network.ApiController;
import com.pplus.prnumberbiz.core.network.model.dto.User;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016-09-08.
 */
public class LoginInfoManager {

    private User user;

    public User getUser(){

        return user;
    }

    public void setUser(User user){

        this.user = user;
    }

    // Preference
    public static final String SHARED_LOGIN_INFO = "login_info";

    private static LoginInfoManager mLoginInfoManager = null;

    public static LoginInfoManager getInstance(){

        if(mLoginInfoManager == null) {
            mLoginInfoManager = new LoginInfoManager();
            mLoginInfoManager.load();
        }

        return mLoginInfoManager;
    }

    public void load(){

        String data = PreferenceUtil.getDefaultPreference(PRNumberBizApplication.getContext()).getString(SHARED_LOGIN_INFO);
        Type typeOfSrc = new TypeToken<LoginInfoManager>(){

        }.getType();
        LoginInfoManager manager = new Gson().fromJson(data, typeOfSrc);
        if(manager != null) {
            mLoginInfoManager = manager;
        }
    }

    public void save(){

        Type typeOfSrc = new TypeToken<LoginInfoManager>(){

        }.getType();
        String data = new Gson().toJson(mLoginInfoManager, typeOfSrc);

        PreferenceUtil.getDefaultPreference(PRNumberBizApplication.getContext()).put(SHARED_LOGIN_INFO, data);

        ApiController.getPRNumberService().updateHeaders();
    }

    public void clear(){

        //        PreferenceUtil.getPreference(PPlusApplication.getContext(), SHARED_LOGIN_INFO).clear();
        mLoginInfoManager.setUser(null);
        mLoginInfoManager.save();
        ApiController.getPRNumberService().updateHeaders();
    }


    /**
     * 회원 여부
     *
     * @return
     */
    public boolean isMember(){

        if(mLoginInfoManager.getUser() != null && StringUtils.isNotEmpty(mLoginInfoManager.getUser().getLoginId()) && StringUtils.isNotEmpty(mLoginInfoManager.getUser().getPassword())) {
            return true;
        }
        return false;
    }
}
