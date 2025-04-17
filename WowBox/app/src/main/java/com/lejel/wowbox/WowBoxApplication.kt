package com.lejel.wowbox

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.pplus.utils.part.info.DeviceUtil
import com.pplus.utils.part.logs.LogUtil
import com.lejel.wowbox.apps.common.Foreground
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager

/**
 * Created by 김종경 on 2016-07-15.
 */
class WowBoxApplication : MultiDexApplication() {
    private val LOG_TAG = this.javaClass.simpleName
    override fun onCreate() {
        super.onCreate()
        context = this

        Foreground.init(this)
        LoginInfoManager.getInstance()
        DeviceUtil.initialize(context)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    override fun onTerminate() {
        super.onTerminate()
        mActivityList!!.clear()
    }

    companion object {
        var schemaData: String? = null
        lateinit var context: Context
        private var mActivityList: MutableList<Activity>? = ArrayList()
        @JvmStatic
        fun getActivityList(): List<Activity>{
            return mActivityList!!
        }

        fun setActivityList(activityList: MutableList<Activity>?) {
            mActivityList = activityList
        }

        val currentActivity: Activity?
            get() = if (mActivityList != null && mActivityList!!.size > 0) mActivityList!![mActivityList!!.size - 1] else null

        fun <E> containsInstance(clazz: Class<out E?>): Boolean {
            for (e in mActivityList!!) {
                LogUtil.d("", "e getName = " + e.javaClass.simpleName)
                if (clazz.isInstance(e)) {
                    return true
                }
            }
            return false
        }
    }
}