package com.pplus.luckybol.apps.event.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonArray
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.core.network.ApiController
import com.pplus.luckybol.databinding.ActivityLinkMineListBinding
import com.pplus.utils.part.logs.LogUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LinkMineListActivity : BaseActivity() {

    override fun getPID(): String? {
        return ""
    }

    private lateinit var binding: ActivityLinkMineListBinding

    override fun getLayoutView(): View {
        binding = ActivityLinkMineListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        listCall()
    }

    private fun listCall() {
        showProgress("")
        ApiController.linkMineApi.adsList.enqueue(object : Callback<JsonArray> {
            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                hideProgress()
                LogUtil.e(LOG_TAG, "onFailure : {}", t.toString())
            }

            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                hideProgress()
                val data = response.body()
                LogUtil.e(LOG_TAG, data.toString())
                if (data != null && data.size() > 0) {
                    LogUtil.e(LOG_TAG, data.get(0).asJsonObject.get("ban_half").asString)
                    Glide.with(this@LinkMineListActivity).load(data.get(0).asJsonObject.get("ban1").asString).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(binding.imageLinkMineBanner)
                }
            }
        })
    }
}