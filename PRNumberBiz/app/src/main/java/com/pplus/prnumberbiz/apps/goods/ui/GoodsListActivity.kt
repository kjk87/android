package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_goods_list.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

class GoodsListActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private var mAdapter: GoodsAdapter? = null

    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
    private var mLockListView = true
    private var mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_list
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recycler_goods_list.layoutManager = mLayoutManager
        mAdapter = GoodsAdapter(this)
        recycler_goods_list.adapter = mAdapter

        recycler_goods_list.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : GoodsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@GoodsListActivity, GoodsDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivityForResult(intent, Const.REQ_DETAIL)
            }

            override fun onRefresh() {
                mPage = 0
                listCall(mPage)
            }
        })

        text_goods_list_view_goods.setOnClickListener {
//            val intent = Intent(this, GoodsOtherListActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
        }

        text_goods_list_reg.setOnClickListener {
//            val intent = Intent(this, GoodsRegActivity::class.java)
            val intent = Intent(this, GoodsRegActivity2::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Const.REQ_REG)
        }

        text_goods_list_sort.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setContents(getString(R.string.word_sort_recent), getString(R.string.word_sort_past), getString(R.string.word_sort_high_price), getString(R.string.word_sort_low_price))
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                    when (event_alert.getValue()) {
                        1 -> {
                            text_goods_list_sort.setText(R.string.word_sort_recent)
                            mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"
                        }

                        2 -> {
                            text_goods_list_sort.setText(R.string.word_sort_past)
                            mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.asc}"
                        }
                        3 -> {
                            text_goods_list_sort.setText(R.string.word_sort_high_price)
                            mSortType = "${EnumData.GoodsSort.price.name},${EnumData.GoodsSort.desc}"
                        }
                        4 -> {
                            text_goods_list_sort.setText(R.string.word_sort_low_price)
                            mSortType = "${EnumData.GoodsSort.price.name},${EnumData.GoodsSort.asc}"
                        }
                    }
                    mPage = 0
                    listCall(mPage)
                }
            }).builder().show(this)
        }

        mPage = 0
        listCall(mPage)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["sort"] = mSortType
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()

                if (response != null) {

                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        text_goods_list_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
                        if (mTotalCount == 0) {
                            layout_goods_list_not_exist.visibility = View.VISIBLE
                        } else {
                            layout_goods_list_not_exist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_REG, Const.REQ_DETAIL -> {
                if (resultCode == Activity.RESULT_OK) {
                    mPage = 0
                    listCall(mPage)
                }
            }
        }
    }

//    var textTop: TextView? = null

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_pre_pay_deal), ToolbarOption.ToolbarMenu.LEFT)
        val view = layoutInflater.inflate(R.layout.item_top_goods_reg, null)
        val text_goods_reg = view.findViewById<TextView>(R.id.text_top_goods_reg)
        val text_other_goods = view.findViewById<TextView>(R.id.text_top_other_goods)

        text_goods_reg.setOnClickListener {
//            val intent = Intent(this, GoodsRegActivity::class.java)
            val intent = Intent(this, GoodsRegActivity2::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Const.REQ_REG)
        }

        text_other_goods.setOnClickListener {
//            val intent = Intent(this, GoodsOtherListActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivity(intent)
        }

        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, view, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    val intent = Intent(this, GoodsRegActivity::class.java)
                    val intent = Intent(this, GoodsRegActivity2::class.java)
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivityForResult(intent, Const.REQ_REG)
                }
            }
        }
    }
}
