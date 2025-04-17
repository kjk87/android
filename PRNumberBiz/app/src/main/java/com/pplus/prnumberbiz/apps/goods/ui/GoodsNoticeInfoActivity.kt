package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsNoticeInfoAdapter
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.GoodsNoticeInfo
import com.pplus.prnumberbiz.core.network.model.dto.NoticeInfo
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import kotlinx.android.synthetic.main.activity_goods_notice_info.*
import network.common.PplusCallback
import retrofit2.Call

class GoodsNoticeInfoActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_notice_info
    }

    var mItemList: MutableList<String>? = null
    var mGoodsNoticeInfo: GoodsNoticeInfo? = null
    var mAdapter: GoodsNoticeInfoAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mGoodsNoticeInfo = intent.getParcelableExtra(Const.DATA)

        text_goods_notice_info_title.setOnClickListener {
            finish()
        }

        text_goods_notice_info_save.setOnClickListener {
            save()
        }

        text_goods_notice_info_save2.setOnClickListener {
            save()
        }

        text_goods_notice_info_item.setOnClickListener {
            showItemList()
        }

        recycler_goods_notice_info.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mAdapter = GoodsNoticeInfoAdapter(this)
        recycler_goods_notice_info.adapter = mAdapter

        if (mGoodsNoticeInfo != null) {
            text_goods_notice_info_item.text = mGoodsNoticeInfo!!.category
            mAdapter!!.setDataList(mGoodsNoticeInfo!!.infoPropList!!.toMutableList())
            text_goods_notice_info_not_exist.visibility = View.GONE
            recycler_goods_notice_info.visibility = View.VISIBLE
        } else {
            text_goods_notice_info_not_exist.visibility = View.VISIBLE
            recycler_goods_notice_info.visibility = View.GONE
        }


        getGoodsItemList()
    }

    private fun save() {

        val goodsItem = text_goods_notice_info_item.text.toString()
        if (StringUtils.isEmpty(goodsItem)) {
            showAlert(R.string.msg_select_goods_item)
            return
        }

        val infoList = mAdapter!!.mDataList
        if (infoList != null && infoList.isNotEmpty()) {
            for (info in infoList) {
                if (info.required!! && StringUtils.isEmpty(info.value)) {
                    showAlert(getString(R.string.format_input, info.key))
                    return
                }
            }

            val goodsNoticeInfo = GoodsNoticeInfo()

            goodsNoticeInfo.pageSeqNo = LoginInfoManager.getInstance().user.page!!.no
            goodsNoticeInfo.goodsSeqNo = 0
            goodsNoticeInfo.infoPropList = infoList
            goodsNoticeInfo.category = text_goods_notice_info_item.text.toString()

            showProgress("")
            if (mGoodsNoticeInfo != null) {
                goodsNoticeInfo.seqNo = mGoodsNoticeInfo!!.seqNo
                LogUtil.e(LOG_TAG, "put")
                ApiBuilder.create().putPageGoodsInfo(goodsNoticeInfo).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                        hideProgress()
                        showAlert(R.string.msg_saved)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }else{
                ApiBuilder.create().postPageGoodsInfo(goodsNoticeInfo).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                    override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                        hideProgress()
                        showAlert(R.string.msg_saved)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }

                    override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                        hideProgress()
                    }
                }).build().call()
            }


        }


    }

    private fun getGoodsItemList() {
        showProgress("")
        ApiBuilder.create().goodsItemList.setCallback(object : PplusCallback<NewResultResponse<String>> {
            override fun onResponse(call: Call<NewResultResponse<String>>?, response: NewResultResponse<String>?) {
                hideProgress()
                if (response != null) {
                    mItemList = response.datas
                }

            }

            override fun onFailure(call: Call<NewResultResponse<String>>?, t: Throwable?, response: NewResultResponse<String>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun showItemList() {
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_select))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
        builder.setContents(*mItemList!!.toTypedArray())
        builder.setLeftText(getString(R.string.word_cancel))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {

                    AlertBuilder.EVENT_ALERT.LIST -> if (event_alert == AlertBuilder.EVENT_ALERT.LIST) {
                        val value = event_alert.getValue()
                        text_goods_notice_info_item.text = mItemList!![value - 1]
                        text_goods_notice_info_not_exist.visibility = View.GONE
                        recycler_goods_notice_info.visibility = View.VISIBLE
                        getNoticeInfoList(mItemList!![value - 1])
                    }
                }
            }
        }).builder().show(this)
    }

    private fun getNoticeInfoList(category: String) {
        val params = HashMap<String, String>()
        params["category"] = category
        showProgress("")
        ApiBuilder.create().getNoticeInfoListByItem(params).setCallback(object : PplusCallback<NewResultResponse<NoticeInfo>> {
            override fun onResponse(call: Call<NewResultResponse<NoticeInfo>>?, response: NewResultResponse<NoticeInfo>?) {
                hideProgress()
                if (response != null) {
                    mAdapter!!.setDataList(response.datas)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<NoticeInfo>>?, t: Throwable?, response: NewResultResponse<NoticeInfo>?) {
                hideProgress()
            }
        }).build().call()
    }
}
