package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.ads.ui.SendEventActivity
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.customer.ui.PlusActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.Group
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import kotlinx.android.synthetic.main.activity_plus_goods.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.*

class PlusGoodsActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private var mAdapter: GoodsAdapter? = null

    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"
    private var mIsLast = false

    override fun getLayoutRes(): Int {
        return R.layout.activity_plus_goods
    }

    override fun initializeView(savedInstanceState: Bundle?) {

        appbar_plus_goods.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {

                if (verticalOffset <= -collapsing_plus_goods.height + toolbar_plus_goods.height) {
                    //toolbar is collapsed here
                    //write your code here
                    text_plus_goods_title.visibility = View.VISIBLE
//                    text_biz_main_user_mode.visibility = View.GONE
                } else {
                    text_plus_goods_title.visibility = View.GONE
//                    text_biz_main_user_mode.visibility = View.VISIBLE
                }
            }
        })

        image_plus_goods_back.setOnClickListener {
            onBackPressed()
        }

        mLayoutManager = LinearLayoutManager(this)
        recycler_plus_goods.layoutManager = mLayoutManager
        mAdapter = GoodsAdapter(this)
        recycler_plus_goods.adapter = mAdapter
        recycler_plus_goods.addItemDecoration(CustomItemOffsetDecoration(this, R.dimen.height_180))
        recycler_plus_goods.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : GoodsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val intent = Intent(this@PlusGoodsActivity, PlusGoodsDetailActivity::class.java)
                intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivityForResult(intent, Const.REQ_DETAIL)
            }

            override fun onRefresh() {
                mPage = 0
                listCall(mPage)
            }
        })

        text_plus_goods_plus_count.setOnClickListener {
            val intent = Intent(this, PlusActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivityForResult(intent, Const.REQ_SALE_HISTORY)
        }

        layout_plus_goods_today_event.setOnClickListener {
            val intent = Intent(this, SendEventActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        layout_plus_goods_push_send.setOnClickListener {
//            val intent = Intent(this, PushSendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }

//        text_plus_goods_reg.setOnClickListener {
//            val intent = Intent(this, GoodsRegActivity2::class.java)
//            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//            intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_REG)
//        }

        layout_plus_goods_reg.setOnClickListener {
            val intent = Intent(this, GoodsRegActivity2::class.java)
            intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
            intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivityForResult(intent, Const.REQ_REG)
        }

        getPlusCount()

        mPage = 0
        listCall(mPage)
    }

    private inner class CustomItemOffsetDecoration(private val mLastOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(context: Context, @DimenRes lastOffsetId: Int) : this(context.resources.getDimensionPixelSize(lastOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            if (mAdapter!!.itemCount > 0 && position == mAdapter!!.itemCount - 1) {
                outRect.bottom = mLastOffset
            }

        }
    }

    private fun getPlusCount() {
        val params = HashMap<String, String>()
        params["no"] = "" + LoginInfoManager.getInstance().user.page!!.no!!

        ApiBuilder.create().getFanGroupAll(params).setCallback(object : PplusCallback<NewResultResponse<Group>> {

            override fun onResponse(call: Call<NewResultResponse<Group>>, response: NewResultResponse<Group>) {
                for (group in response.datas) {
                    if (group.isDefaultGroup) {
                        text_plus_goods_plus_count.text = FormatUtil.getMoneyType(group.count.toString())
                        text_plus_goods_title.text = getString(R.string.format_plus_count, FormatUtil.getMoneyType(group.count.toString()))
                        break
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Group>>, t: Throwable, response: NewResultResponse<Group>) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["sort"] = mSortType
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
            params["type"] = "0"
        } else {
            params["type"] = "1"
        }
        params["isPlus"] = "true"
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()

                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!

                        text_plus_goods_count.text = getString(R.string.format_event_goods_count, FormatUtil.getMoneyType(mTotalCount.toString()))

                        mAdapter!!.clear()
                        if (mTotalCount == 0) {
                            layout_plus_goods_not_exist.visibility = View.VISIBLE
                            layout_plus_goods_exist.visibility = View.GONE
                        } else {
                            layout_plus_goods_not_exist.visibility = View.GONE
                            layout_plus_goods_exist.visibility = View.VISIBLE
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
            Const.REQ_REG, Const.REQ_MODIFY -> {
                if (resultCode == Activity.RESULT_OK) {
                    mPage = 0
                    listCall(mPage)
                }
            }
            Const.REQ_DETAIL->{
                mPage = 0
                listCall(mPage)
            }
            Const.REQ_SALE_HISTORY -> {
                getPlusCount()
            }
        }
    }
}
