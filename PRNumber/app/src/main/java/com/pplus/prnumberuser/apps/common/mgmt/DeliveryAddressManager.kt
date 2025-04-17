package com.pplus.prnumberuser.apps.common.mgmt

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pplus.prnumberuser.PRNumberApplication
import com.pplus.prnumberuser.core.network.model.dto.DeliveryAddress
import com.pplus.utils.part.pref.PreferenceUtil

/**
 * Created by 김종경 on 2016-10-06.
 */
class DeliveryAddressManager private constructor() {
    var deliveryAddressList: MutableList<DeliveryAddress>? = null

    fun load() {
        val data = PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).getString(SHARED_DELIVERY_ADDRESS)
        val typeOfSrc = object : TypeToken<DeliveryAddressManager?>() {}.type
        val categoryInfoManager = Gson().fromJson<DeliveryAddressManager>(data, typeOfSrc)
        if (categoryInfoManager != null) {
            mDeliveryAddressManager = Gson().fromJson(data, typeOfSrc)
        }
    }

    fun save() {
        val typeOfSrc = object : TypeToken<DeliveryAddressManager?>() {}.type
        val data = Gson().toJson(mDeliveryAddressManager, typeOfSrc)
        PreferenceUtil.getDefaultPreference(PRNumberApplication.getContext()).put(SHARED_DELIVERY_ADDRESS, data)
    }

    fun clear() {

        //PreferenceUtil.getPreference(mContext, SHARED_CATEGORY_INFO).clear();
        mDeliveryAddressManager = DeliveryAddressManager()
        mDeliveryAddressManager!!.deliveryAddressList = null
        save()
    }

    fun isCalled(): Boolean {
        return mDeliveryAddressManager!!.deliveryAddressList != null
    }

    companion object {
        // Preference
        const val SHARED_DELIVERY_ADDRESS = "delivery_address"
        private var mDeliveryAddressManager: DeliveryAddressManager? = null
        fun getInstance(): DeliveryAddressManager{
            if (mDeliveryAddressManager == null) {
                mDeliveryAddressManager = DeliveryAddressManager()
                mDeliveryAddressManager!!.load()
            }
            return mDeliveryAddressManager!!
        }
    }
}