package com.pplus.prnumberbiz.apps.goods.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsSaleHistoryAdapter
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.BuyGoods
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.Price
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.activity_goods_sale_history.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*

class GoodsSaleHistoryActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return "Main_product detail_order list"
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_goods_sale_history
    }

    var mStart = ""
    var mEnd = ""
    private var mAdapter: GoodsSaleHistoryAdapter? = null

    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mGoods: Goods? = null
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {

        mGoods = intent.getParcelableExtra(Const.GOODS)

        mLayoutManager = LinearLayoutManager(this)
        recycler_goods_sale_history.layoutManager = mLayoutManager
        mAdapter = GoodsSaleHistoryAdapter(this)
        recycler_goods_sale_history.adapter = mAdapter

        recycler_goods_sale_history.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

//        text_goods_sale_history_start_date.setOnClickListener {
//            val intent = Intent(this, DatePickerActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_START_DATE)
//        }
//
//        text_goods_sale_history_end_date.setOnClickListener {
//            val intent = Intent(this, DatePickerActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            startActivityForResult(intent, Const.REQ_END_DATE)
//        }
//
//        val startCalendar = Calendar.getInstance()
//        startCalendar!!.add(Calendar.DATE, -7)
//        val endCalendar = Calendar.getInstance()
//
//        mStart = startCalendar.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)) + " 00:00:00"
//        text_goods_sale_history_start_date.text = getString(R.string.format_date, startCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(startCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(startCalendar.get(Calendar.DAY_OF_MONTH)))
//
//        mEnd = endCalendar!!.get(Calendar.YEAR).toString() + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1) + "-" + DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)) + " 23:59:59"
//        text_goods_sale_history_end_date.text = getString(R.string.format_date, endCalendar.get(Calendar.YEAR).toString(), DateFormatUtils.formatTime(endCalendar.get(Calendar.MONTH) + 1), DateFormatUtils.formatTime(endCalendar.get(Calendar.DAY_OF_MONTH)))

        mAdapter!!.setOnItemClickListener(object : GoodsSaleHistoryAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {

                val intent = Intent(this@GoodsSaleHistoryActivity, GoodsSaleHistoryDetailActivity::class.java)
                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
                startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
            }
        })

        if (mGoods!!.attachments != null && mGoods!!.attachments!!.images != null && mGoods!!.attachments!!.images!!.isNotEmpty()) {
            val id = mGoods!!.attachments!!.images!![0]
            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
            Glide.with(this).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_goods_sale_history_goods_image)

        } else {
            image_goods_sale_history_goods_image.setImageResource(R.drawable.prnumber_default_img)
        }

        text_goods_sale_history_goods_name.text = mGoods!!.name
        totalPrice()
        mPaging = 0
        listCall(mPaging)
    }

    private fun totalPrice() {
        val params = HashMap<String, String>()

        params["goodsSeqNo"] = mGoods!!.seqNo.toString()
        params["process"] = "3"
        ApiBuilder.create().getBuyGoodsPrice(params).setCallback(object : PplusCallback<NewResultResponse<Price>> {
            override fun onResponse(call: Call<NewResultResponse<Price>>?, response: NewResultResponse<Price>?) {

                var price = 0
                if (response != null && response.data != null) {
                    if(response.data.price != null){
                        price = response.data.price!!
                    }

                }

                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mGoods!!.regDatetime)
                val output = SimpleDateFormat("yyyy.MM.dd")

                if (mGoods!!.count != -1) {
                    var soldCount = 0
                    if (mGoods!!.soldCount != null) {
                        soldCount = mGoods!!.soldCount!!
                    }
                    val remainCount = mGoods!!.count!! - soldCount
                    text_goods_sale_history_goods_info.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_info1, FormatUtil.getMoneyType(mGoods!!.price.toString()), remainCount.toString(), output.format(d), FormatUtil.getMoneyType(price.toString())))

                } else {
                    text_goods_sale_history_goods_info.text = PplusCommonUtil.fromHtml(getString(R.string.html_goods_info2, FormatUtil.getMoneyType(mGoods!!.price.toString()), output.format(d), FormatUtil.getMoneyType(price.toString())))
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Price>>?, t: Throwable?, response: NewResultResponse<Price>?) {

            }
        }).build().call()
    }

    private fun listCall(page: Int) {

        val params = HashMap<String, String>()

        params["goodsSeqNo"] = mGoods!!.seqNo.toString()
        params["page"] = page.toString()
        params["process"] = "3"
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc}"
        showProgress("")
        ApiBuilder.create().getBuyGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<BuyGoods>>> {
            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<BuyGoods>>>?, response: NewResultResponse<SubResultResponse<BuyGoods>>?) {
                hideProgress()

                if (response != null) {
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        if(mTotalCount == 0){
                            layout_goods_sale_not_exist.visibility = View.VISIBLE
                        }else{
                            layout_goods_sale_not_exist.visibility = View.GONE
                        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_START_DATE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && StringUtils.isNotEmpty(data.getStringExtra(Const.DATA))) {
                        val date = data.getStringExtra(Const.DATA)
                        text_goods_sale_history_start_date.text = date

                        mStart = date.replace(".", "-")
                    }
                }
            }
            Const.REQ_END_DATE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && StringUtils.isNotEmpty(data.getStringExtra(Const.DATA))) {
                        val date = data.getStringExtra(Const.DATA)
                        text_goods_sale_history_end_date.text = date
                        mEnd = date.replace(".", "-")
                    }
                }
            }
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sale_history), ToolbarOption.ToolbarMenu.LEFT)

        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
            }
        }
    }
}
