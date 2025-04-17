package com.pplus.luckybol.apps.product.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.data.ProductReviewAdapter
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.network.model.response.SubResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityGoodsReviewBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import retrofit2.Call
import java.util.*

class ProductReviewActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityGoodsReviewBinding

    override fun getLayoutView(): View {
        binding = ActivityGoodsReviewBinding.inflate(layoutInflater)
        return binding.root
    }

    private var mAdapter: ProductReviewAdapter? = null
    var mProductPrice: ProductPrice? = null
    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true

    override fun initializeView(savedInstanceState: Bundle?) {
        mProductPrice = intent.getParcelableExtra(Const.DATA)

//        if (mGoods!!.avgEval != null) {
//            val avgEval = String.format("%.1f", mGoods!!.avgEval)
//            text_goods_review_grade.text = avgEval
//            grade_bar_goods_review_total.build(avgEval)
//        } else {
//            text_goods_review_grade.text = "0.0"
//            grade_bar_goods_review_total.build("0.0")
//        }

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerGoodsReview.layoutManager = mLayoutManager
        mAdapter = ProductReviewAdapter()
        binding.recyclerGoodsReview.adapter = mAdapter

        binding.recyclerGoodsReview.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (totalItemCount < mTotalCount && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        mAdapter!!.setOnItemClickListener(object : ProductReviewAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {

                val productReview = mAdapter!!.getItem(position)

                if(productReview.member!!.seqNo == LoginInfoManager.getInstance().user.no){
                    val contents = arrayOf(getString(R.string.word_modified), getString(R.string.word_delete))
                    val builder = AlertBuilder.Builder()
                    builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
                    builder.setContents(*contents)
                    builder.setLeftText(getString(R.string.word_cancel))
                    builder.setOnAlertResultListener(object : OnAlertResultListener {

                        override fun onCancel() {

                        }

                        override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                            when(event_alert){
                                AlertBuilder.EVENT_ALERT.LIST->{
                                    when (event_alert.getValue()) {
                                        1 -> {
                                            val intent = Intent(this@ProductReviewActivity, ProductReviewRegActivity::class.java)
                                            intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                            intent.putExtra(Const.DATA, productReview)
                                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            modifyLauncher.launch(intent)
                                        }
                                        2 -> {
                                            val params = HashMap<String, String>()
                                            params["seqNo"] = productReview.seqNo.toString()

                                            showProgress("")
                                            ApiBuilder.create().deleteProductReview(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                                                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                                    hideProgress()
                                                    showAlert(R.string.msg_deleted)
                                                    mPaging = 0
                                                    listCall(mPaging)
                                                }

                                                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                                    hideProgress()
                                                }
                                            }).build().call()
                                        }
                                    }
                                }
                                else -> {}
                            }
                        }
                    }).builder().show(this@ProductReviewActivity)

                }
            }
        })

        mPaging = 0
        listCall(mPaging)
    }

    private fun listCall(page: Int) {

        mLockListView = true
        val params = HashMap<String, String>()

        params["productPriceSeqNo"] = mProductPrice!!.seqNo.toString()
        params["sort"] = "${EnumData.BuyGoodsSort.seqNo.name},${EnumData.BuyGoodsSort.desc.name}"
        params["page"] = page.toString()

        showProgress("")
        ApiBuilder.create().getProductReviewByProductPriceSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<ProductReview>>> {

            override fun onResponse(call: Call<NewResultResponse<SubResultResponse<ProductReview>>>?, response: NewResultResponse<SubResultResponse<ProductReview>>?) {
                hideProgress()

                if (response != null) {

                    if (response.data!!.first!!) {
                        mTotalCount = response.data!!.totalElements!!
                        mAdapter!!.clear()
                        binding.textGoodsReviewCount.text = PplusCommonUtil.fromHtml(getString(R.string.html_review, FormatUtil.getMoneyType(mTotalCount.toString())))
                        if (mTotalCount == 0) {
                            binding.layoutGoodsReviewNotExist.visibility = View.VISIBLE
                        } else {
                            binding.layoutGoodsReviewNotExist.visibility = View.GONE
                        }
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data!!.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductReview>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<ProductReview>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    val modifyLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK){
            mPaging = 0
            listCall(mPaging)
        }
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_goods_review), ToolbarOption.ToolbarMenu.LEFT)
//        val textRightTop = TextView(ContextThemeWrapper(this, R.style.buttonStyle))
//        textRightTop.setText(R.string.word_review_write)
//        textRightTop.isClickable = true
//        textRightTop.gravity = Gravity.CENTER
//        textRightTop.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.width_66), 0)
//        textRightTop.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_fc5c57))
//        textRightTop.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.textSize_45pt).toFloat())
//        textRightTop.setSingleLine()
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, textRightTop, 0)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    onBackPressed()
                }
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
//                    val intent = Intent(this, GoodsReviewWriteActivity::class.java)
//                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
//                    intent.putExtra(Const.GOODS, mGoods)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivityForResult(intent, Const.REQ_REVIEW)
                }
            }
        }
    }
}
