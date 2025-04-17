package com.pplus.prnumberuser.apps.common.mgmt

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pplus.utils.part.pref.PreferenceUtil
import com.pplus.prnumberuser.PRNumberApplication
import com.pplus.prnumberuser.core.network.model.dto.BuyGoods

import java.lang.reflect.Type

/**
 * Created by 김종경 on 2016-10-06.
 */

class SelectedGoodsManager private constructor() {

    var buyGoodsList: MutableList<BuyGoods>? = null

    fun load(): SelectedGoodsManager {
        val data = PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).getString(KEY+mPageSeqNo)
        val typeOfSrc = object : TypeToken<SelectedGoodsManager>() {

        }.type

        val selectedGoodsManager = Gson().fromJson<SelectedGoodsManager>(data, typeOfSrc)

        if (selectedGoodsManager != null) {
            mSelectedGoodsManager = Gson().fromJson<SelectedGoodsManager>(data, typeOfSrc)
        }

        return mSelectedGoodsManager!!
    }

    fun save() {

        val typeOfSrc = object : TypeToken<SelectedGoodsManager>() {}.type
        val data = Gson().toJson(mSelectedGoodsManager, typeOfSrc)
        PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).put(KEY+mPageSeqNo, data)
    }

    fun clear(pageSeqNo:Long) {

        mSelectedGoodsManager = SelectedGoodsManager()
        mSelectedGoodsManager!!.buyGoodsList = null
        save()
    }

    companion object {

        // Preference
        val KEY = "selected_buy_goods_list"

        private var mSelectedGoodsManager: SelectedGoodsManager? = null
        private var mPageSeqNo:Long? = null

        fun getInstance(pageSeqNo:Long): SelectedGoodsManager {
            mPageSeqNo = pageSeqNo
            if (mSelectedGoodsManager == null) {
                mSelectedGoodsManager = SelectedGoodsManager()
                mSelectedGoodsManager!!.load()
            }
            return mSelectedGoodsManager!!
        }

    }
}
