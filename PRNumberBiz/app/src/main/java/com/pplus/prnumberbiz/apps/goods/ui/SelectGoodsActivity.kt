package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.pple.pplus.utils.part.format.FormatUtil
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.SelectGoodsAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.sns.kakao.KakaoLinkUtil
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_select_goods.*
import network.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

class SelectGoodsActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_select_goods
    }

    private var mAdapter: SelectGoodsAdapter? = null

    private var mPage: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mSortType = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"

    private var mIsKakao = false
    private var mIsLast = false


    override fun initializeView(savedInstanceState: Bundle?) {

        mIsKakao = intent.getBooleanExtra(Const.KAKAO, false)

        mLayoutManager = LinearLayoutManager(this)
        recycler_select_goods.layoutManager = mLayoutManager
        mAdapter = SelectGoodsAdapter(this)
        recycler_select_goods.adapter = mAdapter

        recycler_select_goods.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter!!.setOnItemClickListener(object : SelectGoodsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                mAdapter!!.mSelectGoods = mAdapter!!.getItem(position)
                mAdapter!!.notifyDataSetChanged()
            }
        })

        mPage = 0
        listCall(mPage)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["status"] = EnumData.GoodsStatus.ing.status.toString()
        params["expired"] = "false"
//        params["expired"] = "0" //expired ( 1: 기한 완료,  0  : 기한 남은것, null : 전체)
        params["sort"] = mSortType
        params["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
        if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
            params["type"] = "0"
        }else{
            params["type"] = "1"
        }
        params["isPlus"] = "true"
        params["isHotdeal"] = "true"
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {
                hideProgress()

                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                        text_select_goods_total_count.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_total_count, FormatUtil.getMoneyType(mTotalCount.toString())))
                        if (mTotalCount == 0) {
                            layout_select_goods_not_exist.visibility = View.VISIBLE
                        } else {
                            layout_select_goods_not_exist.visibility = View.GONE
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

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_goods), ToolbarOption.ToolbarMenu.LEFT)
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete))
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {

                    if(mAdapter!!.mSelectGoods != null){

                        val goods = mAdapter!!.mSelectGoods!!

                        if(mIsKakao){
                            val id = goods.attachments!!.images!![0]
                            val url = getString(R.string.format_msg_page_url, "goods_detail.php?seqNo=${goods.seqNo}")
                            KakaoLinkUtil.getInstance(this@SelectGoodsActivity).sendKakaoUrl("${goods.name}\n${getString(R.string.format_money_unit, FormatUtil.getMoneyType(goods.price.toString()))}", "${Const.API_URL}attachment/image?id=${id}", url)
                        }else{
                            val data = Intent()
                            data.putExtra(Const.GOODS, mAdapter!!.mSelectGoods)
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }else{
                        showAlert(R.string.msg_select_goods)
                    }
                }
            }
        }
    }
}
