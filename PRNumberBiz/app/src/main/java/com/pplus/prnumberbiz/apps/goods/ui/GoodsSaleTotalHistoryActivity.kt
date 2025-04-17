package com.pplus.prnumberbiz.apps.goods.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsHistoryAdapter
import kotlinx.android.synthetic.main.activity_goods_sale_total_history.*
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.network.model.dto.Count
import com.pplus.prnumberbiz.core.network.model.dto.Price
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap


class GoodsSaleTotalHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_order list"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_sale_total_history
    }

    var mAdapter: GoodsHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mTotalCount: Int = 0
    private var mLockListView = false
    private var mPaging = 0
    private var mIsLast = false
    private var mProcess = ""

    override fun initializeView(savedInstanceState: Bundle?) {

        val buyGoods = intent.getParcelableExtra<BuyGoods>(Const.DATA)

        if (buyGoods != null) {

            val intent = Intent(this, GoodsUseAlertActivity::class.java)
            intent.putExtra(Const.DATA, buyGoods)
            startActivity(intent)
        }

        layout_goods_total_history_expand.setOnClickListener {
            if (layout_goods_total_history.visibility == View.VISIBLE) {
                layout_goods_total_history.visibility = View.GONE
                layout_goods_total_history_expand.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_4f94eb))
                image_goods_total_history_expand.setImageResource(R.drawable.btn_selling_list_arrow_down)
            } else {
                layout_goods_total_history.visibility = View.VISIBLE
                layout_goods_total_history_expand.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_f0f0f0))
                image_goods_total_history_expand.setImageResource(R.drawable.btn_selling_list_arrow_up)
            }
        }

        mLayoutManager = LinearLayoutManager(this)
        recycler_goods_sale_total_history.layoutManager = mLayoutManager
        mAdapter = GoodsHistoryAdapter(this)
        recycler_goods_sale_total_history.adapter = mAdapter

        recycler_goods_sale_total_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (!mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : GoodsHistoryAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {

                val intent = Intent(this@GoodsSaleTotalHistoryActivity, GoodsSaleHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
            }
        })

        layout_goods_total_history_pay_count.setOnClickListener {
            text_goods_sale_total_history_sort.setText(R.string.word_pay_complete)
            mProcess = "1"
            mPaging = 0
            listCall(mPaging)
        }
        layout_goods_total_history_use_count.setOnClickListener {
            text_goods_sale_total_history_sort.setText(R.string.word_use_complete)
            mProcess = "3"
            mPaging = 0
            listCall(mPaging)
        }

        layout_goods_total_history_cancel_count.setOnClickListener {
            text_goods_sale_total_history_sort.setText(R.string.word_buy_cancel)
            mProcess = "2"
            mPaging = 0
            listCall(mPaging)
        }

        text_goods_sale_total_history_sort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_total), getString(R.string.word_pay_complete), getString(R.string.word_use_complete), getString(R.string.word_buy_cancel))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.getValue()) {
                        1 -> {
                            text_goods_sale_total_history_sort.setText(R.string.word_total)
                            mProcess = ""
                        }
                        2 -> {
                            text_goods_sale_total_history_sort.setText(R.string.word_pay_complete)
                            mProcess = "1"
                        }

                        3 -> {
                            text_goods_sale_total_history_sort.setText(R.string.word_use_complete)
                            mProcess = "3"
                        }
                        4 -> {
                            text_goods_sale_total_history_sort.setText(R.string.word_buy_cancel)
                            mProcess = "2"
                        }
                    }
                    mPaging = 0
                    listCall(mPaging)
                }
            }).builder().show(this)
        }

        getData()

        mProcess = ""
        mPaging = 0
        listCall(mPaging)
    }

    private fun getData() {
        totalPrice()
        getCount(1)
        getCount(2)
        getCount(3)
    }

    private fun totalPrice() {
        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["process"] = "3"
        ApiBuilder.create().getBuyGoodsPrice(params).setCallback(object : PplusCallback<NewResultResponse<Price>> {
            override fun onResponse(call: Call<NewResultResponse<Price>>?, response: NewResultResponse<Price>?) {
                if (response != null && response.data != null) {
                    var price = 0

                    if (response.data.price != null) {
                        price = response.data.price!!
                    }
                    text_goods_total_history_sale_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(price.toString())))
                } else {
                    text_goods_total_history_sale_price.text = PplusCommonUtil.fromHtml(getString(R.string.html_money_unit, "0"))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Price>>?, t: Throwable?, response: NewResultResponse<Price>?) {

            }
        }).build().call()
    }

    private fun getCount(process: Int) {
        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["process"] = process.toString()
        ApiBuilder.create().getBuyGoodsCount(params).setCallback(object : PplusCallback<NewResultResponse<Count>> {
            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
                if (response != null && response.data != null) {

                    when (process) {
                        1 -> {
                            text_goods_total_history_pay_count.text = FormatUtil.getMoneyType(response.data.count.toString())
                        }
                        2 -> {
                            text_goods_total_history_cancel_count.text = FormatUtil.getMoneyType(response.data.count.toString())
                        }
                        3 -> {
                            text_goods_total_history_use_count.text = FormatUtil.getMoneyType(response.data.count.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["page"] = page.toString()
        if (StringUtils.isNotEmpty(mProcess)) {
            params["process"] = mProcess
        }

        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc}"
        showProgress("")
        ApiBuilder.create().getBuyGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuyGoods>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuyGoods>>>?, response: NewResultResponse<SubResultResponse<BuyGoods>>?) {
                hideProgress()

                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
//                        text_goods_sale_total_history_count.text = getString(R.string.format_goods_sale_history, FormatUtil.getMoneyType(mTotalCount.toString()))
                        mAdapter!!.clear()
                    }

                    mLockListView = false

                    val dataList = response.data.content!!
                    mAdapter!!.addAll(dataList)
                }
            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<BuyGoods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<BuyGoods>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sale_history_config), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_selling_info)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    val intent = Intent(this@GoodsSaleTotalHistoryActivity, SalePriceInfoAlertActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
