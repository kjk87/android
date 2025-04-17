package com.pplus.luckybol.core.chrome.customtab

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.browser.customtabs.*
import com.pplus.utils.part.logs.LogUtil
import com.pplus.luckybol.core.chrome.shared.CustomTabsHelper
import com.pplus.luckybol.core.chrome.shared.ServiceConnection
import com.pplus.luckybol.core.chrome.shared.ServiceConnectionCallback
import com.pplus.luckybol.core.network.model.dto.Page

class CustomTabUtil {

    companion object {
        val LOG_TAG = this.javaClass.simpleName

        private var mCustomTabsSession: CustomTabsSession? = null
        var mClient: CustomTabsClient? = null
        private var mConnection: CustomTabsServiceConnection? = null
        var mPackageNameToBind: String? = null


        fun bindCustomTabsService(context: Context, listener: ServiceConnectionCallback) {
            if (mClient != null) return

            LogUtil.e(LOG_TAG, "mClient")
            if (TextUtils.isEmpty(mPackageNameToBind)) {
                mPackageNameToBind = CustomTabsHelper.getPackageNameToUse(context)
                if (mPackageNameToBind == null) return
            }
            mConnection = ServiceConnection(listener)
            val ok = CustomTabsClient.bindCustomTabsService(context, mPackageNameToBind, mConnection!!)
            if (ok) {

            } else {
                mConnection = null
            }
        }

        fun unbindCustomTabsService(context: Context) {
            if (mConnection == null) return
            context.unbindService(mConnection!!)
            mClient = null
            mCustomTabsSession = null
        }

        fun getSession(): CustomTabsSession? {
            if (mClient == null) {
                mCustomTabsSession = null
            } else if (mCustomTabsSession == null) {
                mCustomTabsSession = mClient!!.newSession(NavigationCallback())
                SessionHelper.setCurrentSession(mCustomTabsSession)
            }
            return mCustomTabsSession
        }

        class NavigationCallback : CustomTabsCallback() {
            override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
                LogUtil.e("CustomTabUtil", "onNavigationEvent: Code = $navigationEvent")
            }
        }

        fun prepareBottombar(context: Context, builder: CustomTabsIntent.Builder, page: Page) {
//        builder.setSecondaryToolbarViews(BottomBarManager.createRemoteViews(context, page),
//                BottomBarManager.clickableIDs, BottomBarManager.getOnClickPendingIntent(context))

//            builder.setSecondaryToolbarViews(BottomBarManager2.createRemoteViews(context, page),
//                    BottomBarManager2.clickableIDs, BottomBarManager2.getOnClickPendingIntent(context))

        }
    }


}