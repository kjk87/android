package com.pplus.prnumberuser.apps.common.mgmt

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.prnumberuser.PRNumberApplication
import com.pplus.prnumberuser.core.network.model.dto.CategoryFirst

/**
 * Created by 김종경 on 2016-10-06.
 */
class CategoryFirstManager private constructor() {
    var categoryFirstList: List<CategoryFirst>? = null

    fun load() {
        val data = PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).getString(SHARED_CATEGORY_FIRST)
        val typeOfSrc = object : TypeToken<CategoryFirstManager?>() {}.type
        val categoryInfoManager = Gson().fromJson<CategoryFirstManager>(data, typeOfSrc)
        if (categoryInfoManager != null) {
            mCategoryFirstManager = Gson().fromJson(data, typeOfSrc)
        }
    }

    fun save() {
        val typeOfSrc = object : TypeToken<CategoryFirstManager?>() {}.type
        val data = Gson().toJson(mCategoryFirstManager, typeOfSrc)
        PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).put(SHARED_CATEGORY_FIRST, data)
    }

    fun clear() {

        //PreferenceUtil.getPreference(mContext, SHARED_CATEGORY_INFO).clear();
        mCategoryFirstManager = CategoryFirstManager()
        mCategoryFirstManager!!.categoryFirstList = null
        save()
    }

    fun isCalled(): Boolean {
        return mCategoryFirstManager!!.categoryFirstList != null
    }

    companion object {
        // Preference
        const val SHARED_CATEGORY_FIRST = "category_first"
        private var mCategoryFirstManager: CategoryFirstManager? = null
        fun getInstance(): CategoryFirstManager{
            if (mCategoryFirstManager == null) {
                mCategoryFirstManager = CategoryFirstManager()
                mCategoryFirstManager!!.load()
            }
            return mCategoryFirstManager!!
        }
    }
}