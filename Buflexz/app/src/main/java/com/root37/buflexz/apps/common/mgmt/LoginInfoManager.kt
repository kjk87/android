package com.root37.buflexz.apps.common.mgmt

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.utils.part.utils.StringUtils
import com.root37.buflexz.BuflexzApplication
import com.root37.buflexz.core.network.ApiController
import com.root37.buflexz.core.network.model.dto.Member

/**
 * Created by Administrator on 2016-09-08.
 */
class LoginInfoManager {
    var member: Member? = null
    fun load() {
        val data = PreferenceUtil.getDefaultPreference(BuflexzApplication.context).getString(SHARED_LOGIN_INFO)
        val typeOfSrc = object : TypeToken<LoginInfoManager?>() {}.type
        val manager = Gson().fromJson<LoginInfoManager>(data, typeOfSrc)
        if (manager != null) {
            mLoginInfoManager = manager
        }
    }

    fun save() {
        val typeOfSrc = object : TypeToken<LoginInfoManager?>() {}.type
        val data = Gson().toJson(mLoginInfoManager, typeOfSrc)
        PreferenceUtil.getDefaultPreference(BuflexzApplication.context).put(SHARED_LOGIN_INFO, data)
        ApiController.apiService.updateHeaders()
    }

    fun clear() {
        mLoginInfoManager!!.member = null
        mLoginInfoManager!!.save()
        ApiController.apiService.updateHeaders()
    }

    /**
     * 회원 여부
     *
     * @return
     */
    fun isMember(): Boolean {
        return mLoginInfoManager!!.member != null && StringUtils.isNotEmpty(mLoginInfoManager!!.member!!.userKey)
    }

    companion object {
        // Preference
        const val SHARED_LOGIN_INFO = "login_info"
        private var mLoginInfoManager: LoginInfoManager? = null

        @JvmStatic
        fun getInstance(): LoginInfoManager {
            if (mLoginInfoManager == null) {
                mLoginInfoManager = LoginInfoManager()
                mLoginInfoManager!!.load()
            }
            return mLoginInfoManager!!
        }
    }
}