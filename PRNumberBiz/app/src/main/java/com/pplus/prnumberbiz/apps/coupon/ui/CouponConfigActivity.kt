package com.pplus.prnumberbiz.apps.coupon.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.coupon.data.CouponConfigAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_coupon_config.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

class CouponConfigActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_coupon_config
    }

    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mIsLast = false
    private var mAdapter: CouponConfigAdapter? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        mLayoutManager = LinearLayoutManager(this)
        recycler_coupon_config.layoutManager = mLayoutManager
        mAdapter = CouponConfigAdapter(this)
        recycler_coupon_config.adapter = mAdapter

        recycler_coupon_config.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                        mPage++
                        listCall(mPage)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : CouponConfigAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@CouponConfigActivity, CouponConfigDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivityForResult(intent, Const.REQ_MODIFY)
            }

            override fun onRefresh() {
                mPage = 0
                listCall(mPage)
            }
        })

        mPage = 0
        listCall(mPage)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        params["isHotdeal"] = "false"
        params["isPlus"] = "false"
        params["isCoupon"] = "true"

        params["page"] = page.toString()
        params["sort"] = "represent,${EnumData.BuyGoodsSort.desc.name},${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        showProgress("")
        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()
                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mAdapter!!.clear()
                        mTotalCount = response.data.totalElements!!
                        text_coupon_config_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_coupon_count, FormatUtil.getMoneyType(mTotalCount.toString())))
                        if (mTotalCount == 0) {
                            layout_coupon_config_not_exist.visibility = View.VISIBLE
                        } else {
                            layout_coupon_config_not_exist.visibility = View.GONE
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
            Const.REQ_REG -> {
                if (resultCode == Activity.RESULT_OK) {
                    mPage = 0
                    listCall(mPage)
                }
            }
            Const.REQ_MODIFY->{
                mPage = 0
                listCall(mPage)
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_coupon_config), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_coupon_reg))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    val intent = Intent(this, CouponRegActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivityForResult(intent, Const.REQ_REG)
                }
            }
        }

    }
}
