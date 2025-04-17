// Copyright 2016 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.pplus.prnumberuser.core.chrome.customtab

import android.app.IntentService
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import android.view.View
import android.widget.RemoteViews
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Page

/**
 * A [BroadcastReceiver] that manages the interaction with the active Custom Tab.
 */
class BottomBarManager2 : IntentService("BottomBarManager2") {
    override fun onHandleIntent(intent: Intent?) {
        if (Const.onReceivePlus) {
            return
        }
        Const.onReceivePlus = true

        val clickedId = intent?.getIntExtra(CustomTabsIntent.EXTRA_REMOTEVIEWS_CLICKED_ID, -1)

//        val session = SessionHelper.getCurrentSession()
//        if(session == null){
//            LogUtil.e("BottomBarManager2", "session null")
//        }

        when (clickedId) {
            R.id.layout_page_chrome_plus -> {
                LogUtil.e("BottomBarManager2", "plus")
                val intent = Intent(this, PlusAlertActivity::class.java)
                intent.putExtra(Const.DATA, mPage)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                Const.onReceivePlus = false
            }
            R.id.layout_page_chrome_find_road -> {
                if (existDaummapApp(this)) {
                    val uri = Uri.parse("daummaps://route?ep=${mPage!!.latitude},${mPage!!.longitude}&by=CAR");
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                Const.onReceivePlus = false
            }
            R.id.layout_page_chrome_call -> {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage!!.phone!!))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                Const.onReceivePlus = false
            }
            R.id.layout_page_chrome_blog -> {
                val uri = Uri.parse("https://m.search.naver.com/search.naver?where=m_view&sm=mtb_jum&query=${mPage!!.name}");
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                Const.onReceivePlus = false
            }
        }
    }

    fun existDaummapApp(context: Context): Boolean {
        val pm = context.packageManager

        try {
            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNATURES) != null)
        } catch (e: PackageManager.NameNotFoundException) {
            return false;
        }
    }

    companion object {
        private var mPage: Page? = null

        /**
         * Creates a RemoteViews that will be shown as the bottom bar of the custom tab.
         *
         * @param page If true, a play icon will be shown, otherwise show a pause icon.
         * @return The created RemoteViews instance.
         */

        fun createRemoteViews(context: Context, page: Page): RemoteViews {

            mPage = page

            var plus = false
            if (page.plus != null) {
                plus = page.plus!!
            }
            val remoteViews = RemoteViews(context.packageName, R.layout.page_chrome_bottom)

            if (plus) {
                remoteViews.setTextViewCompoundDrawables(R.id.text_page_chrome_plus, 0, R.drawable.ic_web_plus_sel, 0, 0)
                remoteViews.setTextViewText(R.id.text_page_chrome_plus, context.getString(R.string.msg_plus_ing))
            } else {
                remoteViews.setTextViewCompoundDrawables(R.id.text_page_chrome_plus, 0, R.drawable.ic_web_plus_nor, 0, 0)
                remoteViews.setTextViewText(R.id.text_page_chrome_plus, context.getString(R.string.word_plus3))
            }

            if (mPage!!.latitude == null || mPage!!.longitude == null) {
                remoteViews.setViewVisibility(R.id.layout_page_chrome_find_road, View.GONE)
                remoteViews.setViewVisibility(R.id.view_page_chrome_bar1, View.GONE)
            } else {
                remoteViews.setViewVisibility(R.id.layout_page_chrome_find_road, View.VISIBLE)
                remoteViews.setViewVisibility(R.id.view_page_chrome_bar1, View.VISIBLE)
            }

            if (StringUtils.isNotEmpty(mPage!!.phone)) {
                remoteViews.setViewVisibility(R.id.layout_page_chrome_call, View.VISIBLE)
                remoteViews.setViewVisibility(R.id.view_page_chrome_bar1, View.VISIBLE)
                remoteViews.setViewVisibility(R.id.view_page_chrome_bar2, View.VISIBLE)
            } else {
                remoteViews.setViewVisibility(R.id.layout_page_chrome_call, View.GONE)
                remoteViews.setViewVisibility(R.id.view_page_chrome_bar1, View.GONE)
                remoteViews.setViewVisibility(R.id.view_page_chrome_bar2, View.GONE)
            }

            if(mPage!!.virtualPage!!){
                remoteViews.setViewVisibility(R.id.layout_page_chrome_plus, View.GONE)
                remoteViews.setViewVisibility(R.id.view_page_chrome_bar3, View.GONE)
            }else{
                remoteViews.setViewVisibility(R.id.layout_page_chrome_plus, View.VISIBLE)
                remoteViews.setViewVisibility(R.id.view_page_chrome_bar3, View.VISIBLE)
            }

            return remoteViews
        }

        /**
         * @return A list of View ids, the onClick event of which is handled by Custom Tab.
         */
        val clickableIDs = intArrayOf(R.id.layout_page_chrome_find_road, R.id.layout_page_chrome_call, R.id.layout_page_chrome_blog, R.id.layout_page_chrome_plus)

        /**
         * @return The PendingIntent that will be triggered when the user clicks on the Views listed by
         * [BottomBarManager2.getClickableIDs].
         */
        fun getOnClickPendingIntent(context: Context): PendingIntent {
            val serviceIntent = Intent(context, BottomBarManager2::class.java)
            return PendingIntent.getService(context, 0, serviceIntent, 0)
        }
    }
}
