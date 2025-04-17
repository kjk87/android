package com.pplus.prnumberuser.core.location

import android.content.Context
import android.location.Address
import android.location.Location
import android.os.AsyncTask
import android.location.Geocoder
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberuser.core.network.model.dto.LocationData
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import java.lang.Exception
import java.util.*


class GetAddressTask : AsyncTask<LocationData, Void, String>{

    var mContext:Context ? =null
    var listener: PplusCommonUtil.Companion.OnAddressCallListener? = null

    constructor(context: Context, listener:PplusCommonUtil.Companion.OnAddressCallListener?) : super(){
        mContext = context
        this.listener = listener
    }

    override fun doInBackground(vararg params: LocationData?): String {
        LogUtil.e("GetAddressTask", "doInBackground")
        val geocoder = Geocoder(mContext, Locale.getDefault())
        // Get the current location from the input parameter list

        try{
            val loc = params[0]
            val addresses = geocoder.getFromLocation(loc!!.latitude, loc.longitude, 10)

            if (addresses.size > 0) {
                val address = addresses[0]

                val addressFragments = with(address) {

                    (0..maxAddressLineIndex).map { getAddressLine(it) }
                }
                val addressString = addressFragments.joinToString(separator = "\n")

                return addressString.replace(address.countryName, "")
            }else{
                return "No address found"
            }
        }catch (e: Exception){
            return "No address found"
        }
    }

    override fun onPostExecute(result: String?) {
        LocationUtil.specifyLocationData?.address = result
        listener?.onResult(result!!)
    }
}