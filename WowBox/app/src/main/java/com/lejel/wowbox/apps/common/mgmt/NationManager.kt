package com.lejel.wowbox.apps.common.mgmt

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pplus.utils.part.pref.PreferenceUtil
import com.lejel.wowbox.WowBoxApplication
import com.lejel.wowbox.core.network.model.dto.Nation

/**
 * Created by Administrator on 2016-09-08.
 */
class NationManager {
    var nationMap: Map<String, Nation>? = null
    fun load() {
        val data = PreferenceUtil.getDefaultPreference(WowBoxApplication.context).getString(SHARED_NATION)
        val typeOfSrc = object : TypeToken<NationManager?>() {}.type
        val manager = Gson().fromJson<NationManager>(data, typeOfSrc)
        if (manager != null) {
            mNationManager = manager
        }
    }

    fun save() {
        val typeOfSrc = object : TypeToken<NationManager?>() {}.type
        val data = Gson().toJson(mNationManager, typeOfSrc)
        PreferenceUtil.getDefaultPreference(WowBoxApplication.context).put(SHARED_NATION, data)
    }

    fun clear() {
        mNationManager!!.nationMap = null
        mNationManager!!.save()
    }

    companion object {
        // Preference
        const val SHARED_NATION = "nation"
        private var mNationManager: NationManager? = null

        @JvmStatic
        fun getInstance(): NationManager {
            if (mNationManager == null) {
                mNationManager = NationManager()
                mNationManager!!.load()
            }
            return mNationManager!!
        }
    }
}