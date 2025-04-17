package com.pplus.prnumberuser.apps.product.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.goods.ui.AlertGoodsLikeCompleteActivity
import com.pplus.prnumberuser.apps.product.data.ProductDetailStickyHeaderAdapter
import com.pplus.prnumberuser.core.code.common.EnumData
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.*
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.network.model.response.SubResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.core.util.ToastUtil
import com.pplus.prnumberuser.databinding.ActivityProductShipDetailBinding
import com.pplus.prnumberuser.databinding.ItemOptionBinding
import com.pplus.prnumberuser.databinding.ItemSelectedOptionBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.util.*
import kotlin.collections.HashMap

class ProductShipDetailActivity : BaseActivity() {
    override fun getPID(): String {
        return ""
    }

    private lateinit var binding: ActivityProductShipDetailBinding

    override fun getLayoutView(): View {
        binding = ActivityProductShipDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    var mProductPrice: ProductPrice? = null
    var mProductOptionTotal: ProductOptionTotal? = null
    var mIsLike = false
    var mCount = 1
    private var mAdapter: ProductDetailStickyHeaderAdapter? = null
    private var mPaging: Int = 0
    private var mTotalCount = 0
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLockListView = true
    private var mIsLast = false

    override fun initializeView(savedInstanceState: Bundle?) {
        mProductPrice = intent.getParcelableExtra(Const.DATA)

        binding.imageProductShipDetailBack.setOnClickListener {
            onBackPressed()
        }

        binding.layoutProductShipDetailBottom.visibility = View.GONE

        mLayoutManager = LinearLayoutManager(this)
        binding.recyclerProductShipDetail.layoutManager = mLayoutManager
        binding.recyclerProductShipDetail.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = mLayoutManager!!.findFirstVisibleItemPosition()
                if (mAdapter!!.isHolder(position) || position > 1) {
                    binding.layoutProductShipDetailTab.visibility = View.VISIBLE
                } else {
                    binding.layoutProductShipDetailTab.visibility = View.GONE
                }
            }
        })

        binding.textProductShipDetailHoldInfoTab.setOnClickListener {
            binding.textProductShipDetailHoldInfoTab.isSelected = true
            binding.textProductShipDetailHoldReviewTab.isSelected = false
            if (mAdapter!!.mTab != 0) {
                mAdapter!!.mTab = 0
                mAdapter!!.setListData()
            }

        }

        binding.textProductShipDetailHoldReviewTab.setOnClickListener {
            binding.textProductShipDetailHoldInfoTab.isSelected = false
            binding.textProductShipDetailHoldReviewTab.isSelected = true
            if (mAdapter!!.mTab != 1) {
                mAdapter!!.mTab = 1
                mAdapter!!.setListData()
            }
        }

        binding.textProductShipDetailHoldInfoTab.isSelected = true
        binding.textProductShipDetailHoldReviewTab.isSelected = false

        binding.recyclerProductShipDetail.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            internal var pastVisibleItems: Int = 0
            internal var visibleItemCount: Int = 0
            internal var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                visibleItemCount = mLayoutManager!!.childCount
                totalItemCount = mLayoutManager!!.itemCount
                pastVisibleItems = mLayoutManager!!.findFirstVisibleItemPosition()
                if (!mLockListView) {
                    if (mAdapter!!.mTab == 1 && !mIsLast && visibleItemCount + pastVisibleItems >= totalItemCount * Const.NEXT_RECYCLERVIEW_MARGIN) {
                        mPaging++
                        listCall(mPaging)
                    }
                }
            }
        })

        binding.layoutProductShipDetailLike.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(this, signInLauncher)) {
                return@setOnClickListener
            }

            if (!mIsLike) {
                postLike()
            } else {
                deleteLike()
            }
        }

        binding.textProductShipDetailShowOption.setOnClickListener {
            if (mProductPrice!!.product!!.salesType!! == EnumData.SalesType.TICKET.type) {

                val intent = Intent(this, AlertProductNoShowActivity::class.java)
                intent.putExtra(Const.TYPE, "detail")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                noShowLauncher.launch(intent)
            }else{
                scrollInAnimation()
            }

        }

        binding.imageProductShipDetailNotOptionClose.setOnClickListener {
            scrollOutAnimation()
        }

        binding.imageProductShipDetailBottomClose.setOnClickListener {
            scrollOutAnimation()
        }

        binding.imageProductShipDetailNotOptionMinus.setOnClickListener {
            if (mCount > 1) {
                mCount--
            }
            binding.textProductShipDetailNotOptionCount.text = mCount.toString()
            setTotalPrice()
        }

        binding.imageProductShipDetailNotOptionPlus.setOnClickListener {
            if (mProductPrice!!.product!!.salesType!! == EnumData.SalesType.SHIPPING.type) {
                if (mProductPrice!!.product!!.count != -1) {
                    val remainCount = mProductPrice!!.product!!.count!! - mProductPrice!!.product!!.soldCount!!
                    if (mCount < remainCount) {
                        mCount++
                    }
                } else {
                    mCount++
                }
            }else if (mProductPrice!!.product!!.salesType!! == EnumData.SalesType.TICKET.type) {
                val remainCount = mProductPrice!!.dailyCount!! - mProductPrice!!.dailySoldCount!!
                if (mCount < remainCount) {
                    mCount++
                }
            }

            binding.textProductShipDetailNotOptionCount.text = mCount.toString()
            setTotalPrice()
        }

        binding.textProductShipDetailBuy.setOnClickListener {

            if (!PplusCommonUtil.loginCheck(this, signInLauncher)) {
                return@setOnClickListener
            }

            //            PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            //                override fun reload() {
            //
            //                }
            //            })

            var totalCount = 0
            if (mProductPrice!!.product!!.useOption != null && mProductPrice!!.product!!.useOption!!) {
                if (mSelectPurchaseProductOptionList.isEmpty()) {
                    ToastUtil.show(this, R.string.msg_select_option)
                    return@setOnClickListener
                }

                for (purchaseProductOption in mSelectPurchaseProductOptionList) {
                    totalCount += purchaseProductOption.amount!!
                }
            } else {
                totalCount = mCount
            }

            val purchaseProductList = ArrayList<PurchaseProduct>()
            val purchaseProduct = PurchaseProduct()
            purchaseProduct.product = mProductPrice!!.product
            purchaseProduct.productPriceData = mProductPrice
            purchaseProduct.count = totalCount
            purchaseProduct.startTime = mProductPrice!!.startTime
            purchaseProduct.endTime = mProductPrice!!.endTime

            if (mProductPrice!!.product!!.useOption != null && mProductPrice!!.product!!.useOption!!) {
                purchaseProduct.purchaseProductOptionSelectList = mSelectPurchaseProductOptionList
            }
            purchaseProductList.add(purchaseProduct)

            if (mProductPrice!!.product!!.salesType!! == EnumData.SalesType.SHIPPING.type) { //            val intent = Intent(this@ProductShipDetailActivity, PurchaseProductShipActivity::class.java)
                val intent = Intent(this@ProductShipDetailActivity, PurchaseProductShipPgActivity::class.java)
                intent.putParcelableArrayListExtra(Const.PURCHASE_PRODUCT, purchaseProductList)
                startActivity(intent)
            } else if (mProductPrice!!.product!!.salesType!! == EnumData.SalesType.TICKET.type) {

                if (StringUtils.isNotEmpty(mProductPrice!!.startTime) && StringUtils.isNotEmpty(mProductPrice!!.endTime)) {
                    val startMin = (mProductPrice!!.startTime!!.split(":")[0].toInt() * 60) + mProductPrice!!.startTime!!.split(":")[1].toInt()
                    val endMin = (mProductPrice!!.endTime!!.split(":")[0].toInt() * 60) + mProductPrice!!.endTime!!.split(":")[1].toInt()
                    val cal = Calendar.getInstance()
                    val currentMin = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

                    val purchaseWait = CountryConfigManager.getInstance().config.properties!!.purchaseWait!!
                    if (currentMin >= startMin - purchaseWait) {
                        showAlert(R.string.msg_impossible_purchase_time)
                        return@setOnClickListener
                    }
                }

                if (mProductPrice!!.dailySoldCount == null) {
                    mProductPrice!!.dailySoldCount = 0
                }

                if (mProductPrice!!.dailyCount != null && (mProductPrice!!.dailyCount!! < (mProductPrice!!.dailySoldCount!! + totalCount))) {
                    showAlert(R.string.msg_impossible_daily_count)
                    return@setOnClickListener
                }


                val intent = Intent(this@ProductShipDetailActivity, PurchaseProductTicketPgActivity::class.java)
                intent.putParcelableArrayListExtra(Const.PURCHASE_PRODUCT, purchaseProductList)
                startActivity(intent)
            }

        }

        binding.textProductShipDetailTitle.setSingleLine()

        setData()
    }

    private fun postLike() {

        val productLike = ProductLike()
        productLike.memberSeqNo = LoginInfoManager.getInstance().user.no
        productLike.productSeqNo = mProductPrice!!.productSeqNo
        productLike.productPriceSeqNo = mProductPrice!!.seqNo
        showProgress("")
        ApiBuilder.create().insertProductLike(productLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                mIsLike = true
                binding.layoutProductShipDetailLike.isSelected = true
                val intent = Intent(this@ProductShipDetailActivity, AlertGoodsLikeCompleteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun deleteLike() {
        val productLike = ProductLike()
        productLike.memberSeqNo = LoginInfoManager.getInstance().user.no
        productLike.productSeqNo = mProductPrice!!.productSeqNo
        productLike.productPriceSeqNo = mProductPrice!!.seqNo
        showProgress("")
        ApiBuilder.create().deleteProductLike(productLike).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                mIsLike = false
                binding.layoutProductShipDetailLike.isSelected = false
                ToastUtil.show(this@ProductShipDetailActivity, R.string.msg_delete_goods_like)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
            }
        }).build().call()
    }

    var mRemainCount = 0

    private fun setData() {
        val params = HashMap<String, String>()

        params["seqNo"] = mProductPrice!!.seqNo.toString()

        showProgress("")
        ApiBuilder.create().getProductPrice(params).setCallback(object : PplusCallback<NewResultResponse<ProductPrice>> {
            override fun onResponse(call: Call<NewResultResponse<ProductPrice>>?, response: NewResultResponse<ProductPrice>?) {
                hideProgress()
                if (response?.data != null) {
                    mProductPrice = response.data

                    binding.imageProductShipDetailShare.setOnClickListener {
                        var url = ""
                        if (Const.API_URL.startsWith("https://api")) {
                            url = getString(R.string.format_product_url, mProductPrice!!.pageSeqNo.toString(), mProductPrice!!.code)
                        } else {
                            url = getString(R.string.format_stage_product_url, mProductPrice!!.pageSeqNo.toString(), mProductPrice!!.code)
                        }
                        val text = "${mProductPrice!!.product!!.name}\n${url}"
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, text)
                        val chooserIntent = Intent.createChooser(intent, getString(R.string.word_share))
                        startActivity(chooserIntent)
                    }

                    mRemainCount = mProductPrice!!.product!!.count!! - mProductPrice!!.product!!.soldCount!!
                    mAdapter = ProductDetailStickyHeaderAdapter(mProductPrice!!)
                    binding.recyclerProductShipDetail.adapter = mAdapter

                    mAdapter!!.setOnItemClickListener(object : ProductDetailStickyHeaderAdapter.OnItemClickListener {
                        override fun onTabChanged(tab: Int) {
                            when (tab) {
                                0 -> {
                                    binding.textProductShipDetailHoldInfoTab.isSelected = true
                                    binding.textProductShipDetailHoldReviewTab.isSelected = false
                                }
                                1 -> {
                                    binding.textProductShipDetailHoldInfoTab.isSelected = false
                                    binding.textProductShipDetailHoldReviewTab.isSelected = true
                                }
                            }
                        }

                        override fun onItemClick(position: Int) {

                            val productReview = mAdapter!!.mDataList!![position]

                            if (productReview.member!!.seqNo == LoginInfoManager.getInstance().user.no) {
                                val contents = arrayOf(getString(R.string.word_modified), getString(R.string.word_delete))
                                val builder = AlertBuilder.Builder()
                                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)
                                builder.setContents(*contents)
                                builder.setLeftText(getString(R.string.word_cancel))
                                builder.setOnAlertResultListener(object : OnAlertResultListener {

                                    override fun onCancel() {

                                    }

                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                        when (event_alert) {
                                            AlertBuilder.EVENT_ALERT.LIST -> {
                                                when (event_alert.getValue()) {
                                                    1 -> {
                                                        val intent = Intent(this@ProductShipDetailActivity, ProductReviewRegActivity::class.java)
                                                        intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                                        intent.putExtra(Const.DATA, productReview)
                                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                                        startActivityForResult(intent, Const.REQ_MODIFY)
                                                    }
                                                    2 -> {
                                                        val params = java.util.HashMap<String, String>()
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
                                        }
                                    }
                                }).builder().show(this@ProductShipDetailActivity)

                            }
                        }
                    })


                    if (mProductPrice!!.product!!.salesType!! == EnumData.SalesType.SHIPPING.type) {
                        binding.layoutProductShipDetailShippingMethod.visibility = View.VISIBLE
                        binding.layoutProductShipDetailShippingPrice.visibility = View.VISIBLE

                        when (mProductPrice!!.productDelivery!!.method) {
                            1 -> {
                                binding.textProductShipDetailDeliveryMethod.text = getString(R.string.word_delivery2)
                            }
                            2 -> {
                                binding.textProductShipDetailDeliveryMethod.text = getString(R.string.word_direct_delivery)
                            }
                        }
                        var deliveryPaymentMethode = ""
                        when (mProductPrice!!.productDelivery!!.paymentMethod) {
                            "before" -> {
                                deliveryPaymentMethode = getString(R.string.word_before_pay_delivery_fee)
                            }
                            "after" -> {
                                deliveryPaymentMethode = getString(R.string.word_after_pay_delivery_fee)
                            }
                        }

                        when (mProductPrice!!.productDelivery!!.type) { // 1:무료, 2:유료, 3:조건부 무료
                            EnumData.DeliveryType.free.type -> {
                                binding.textProductShipDetailDeliveryFee.text = getString(R.string.word_free_ship)
                                binding.textProductShipDetailDeliveryMinPrice.visibility = View.GONE
                            }
                            EnumData.DeliveryType.pay.type -> {
                                if (mProductPrice!!.productDelivery!!.deliveryFee!! > 0) {
                                    binding.textProductShipDetailDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryFee!!.toInt().toString())) + "($deliveryPaymentMethode)"
                                    binding.textProductShipDetailDeliveryMinPrice.visibility = View.GONE
                                } else {
                                    binding.textProductShipDetailDeliveryFee.text = getString(R.string.word_free_ship)
                                }
                            }
                            EnumData.DeliveryType.conditionPay.type -> {
                                if (mProductPrice!!.productDelivery!!.deliveryFee!! > 0) {
                                    binding.textProductShipDetailDeliveryFee.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryFee!!.toInt().toString())) + "($deliveryPaymentMethode)"
                                    binding.textProductShipDetailDeliveryMinPrice.text = getString(R.string.format_delivery_min_price, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryMinPrice!!.toInt().toString()))
                                    binding.textProductShipDetailDeliveryMinPrice.visibility = View.VISIBLE
                                }
                            }
                        }

                        if (mProductPrice!!.productDelivery!!.isAddFee != null && mProductPrice!!.productDelivery!!.isAddFee!!) {
                            if (mProductPrice!!.productDelivery!!.deliveryAddFee1 != null && mProductPrice!!.productDelivery!!.deliveryAddFee1!! > 0) {
                                binding.textProductShipDetailDeliveryAddFee1.visibility = View.VISIBLE
                                binding.textProductShipDetailDeliveryAddFee1.text = getString(R.string.format_delivery_add_fee1, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryAddFee1!!.toInt().toString()))
                            } else {
                                binding.textProductShipDetailDeliveryAddFee1.visibility = View.GONE
                            }

                            if (mProductPrice!!.productDelivery!!.deliveryAddFee2 != null && mProductPrice!!.productDelivery!!.deliveryAddFee2!! > 0) {
                                binding.textProductShipDetailDeliveryAddFee2.visibility = View.VISIBLE
                                binding.textProductShipDetailDeliveryAddFee2.text = getString(R.string.format_delivery_add_fee2, FormatUtil.getMoneyType(mProductPrice!!.productDelivery!!.deliveryAddFee2!!.toInt().toString()))
                            } else {
                                binding.textProductShipDetailDeliveryAddFee2.visibility = View.GONE
                            }
                        } else {
                            binding.textProductShipDetailDeliveryAddFee1.visibility = View.GONE
                            binding.textProductShipDetailDeliveryAddFee2.visibility = View.GONE
                        }
                    } else {
                        binding.layoutProductShipDetailShippingMethod.visibility = View.GONE
                        binding.layoutProductShipDetailShippingPrice.visibility = View.GONE
                    }

                    if (mProductPrice!!.product!!.useOption != null && mProductPrice!!.product!!.useOption!!) {
                        binding.scrollProductShipDetailOption.visibility = View.VISIBLE
                        binding.viewProductShipDetailOptionBar.visibility = View.VISIBLE
                        binding.layoutProductShipDetailNotOption.visibility = View.GONE //                        layout_product_ship_detail_total_price.setBackgroundColor(ResourceUtil.getColor(this@ProductShipDetailActivity, R.color.color_f0f0f0))
                        getOption()
                    } else {
                        binding.scrollProductShipDetailOption.visibility = View.GONE
                        binding.viewProductShipDetailOptionBar.visibility = View.GONE
                        binding.layoutProductShipDetailNotOption.visibility = View.VISIBLE //                        layout_product_ship_detail_total_price.setBackgroundColor(ResourceUtil.getColor(this@ProductShipDetailActivity, R.color.white))
                        setTotalPrice()
                    }

                    if (LoginInfoManager.getInstance().isMember) {
                        checkLike()
                    }

                    binding.textProductShipDetailTitle.text = mProductPrice!!.product!!.name

                    mPaging = 0
                    listCall(mPaging)
                } else {
                    showAlert(R.string.msg_not_exist_goods2)
                    finish()
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProductPrice>>?, t: Throwable?, response: NewResultResponse<ProductPrice>?) {
                hideProgress()
            }
        }).build().call()
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
                    mIsLast = response.data.last!!
                    if (response.data.first!!) {
                        mTotalCount = response.data.totalElements!!
                        mAdapter!!.clear()
                    }

                    mLockListView = false
                    mAdapter!!.addAll(response.data.content!!)
                }

            }

            override fun onFailure(call: Call<NewResultResponse<SubResultResponse<ProductReview>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<ProductReview>>?) {
                hideProgress()
                mLockListView = false
            }
        }).build().call()
    }

    var mItemList = arrayListOf<ProductOptionItem>()
    var mOptionTextList = arrayListOf<TextView>()
    var mSelectPurchaseProductOptionList = arrayListOf<PurchaseProductOption>()

    private fun getOption() {
        val params = HashMap<String, String>()

        params["productSeqNo"] = mProductPrice!!.product!!.seqNo.toString()
        showProgress("")
        ApiBuilder.create().getProductOption(params).setCallback(object : PplusCallback<NewResultResponse<ProductOptionTotal>> {
            override fun onResponse(call: Call<NewResultResponse<ProductOptionTotal>>?, response: NewResultResponse<ProductOptionTotal>?) {
                hideProgress()
                if (response?.data != null) {
                    mProductOptionTotal = response.data
                    initOption()

                } else {
                    binding.scrollProductShipDetailOption.visibility = View.GONE
                    binding.viewProductShipDetailOptionBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<NewResultResponse<ProductOptionTotal>>?, t: Throwable?, response: NewResultResponse<ProductOptionTotal>?) {
                hideProgress()
            }
        }).build().call()
    }

    private fun initOption() {
        val productOptionList = mProductOptionTotal!!.productOptionList
        val productOptionItemList = mProductOptionTotal!!.productOptionItemList
        val productOptionDetailList = mProductOptionTotal!!.productOptionDetailList

        binding.layoutProductShipDetailOption.removeAllViews()
        if (productOptionList != null && productOptionList.isNotEmpty()) {
            mItemList = arrayListOf()
            mOptionTextList = arrayListOf<TextView>()
            for (i in productOptionList.indices) {
                mItemList.add(ProductOptionItem())
                val productOption = productOptionList[i]
                val optionBinding = ItemOptionBinding.inflate(layoutInflater)
                optionBinding.textOptionName.setTextColor(ResourceUtil.getColor(this@ProductShipDetailActivity, R.color.color_737373))
                optionBinding.textOptionName.text = productOption.name
                mOptionTextList.add(optionBinding.textOptionName)
                optionBinding.root.setOnClickListener {

                    when (mProductPrice!!.product!!.optionType) {
                        EnumData.OptionType.single.name -> {
                            val selectedDetailList = productOptionDetailList!!.filter {
                                it.optionSeqNo == productOption.seqNo
                            }

                            LogUtil.e(LOG_TAG, "size : {}", selectedDetailList.size)
                            val intent = Intent(this@ProductShipDetailActivity, AlertProductOptionActivity::class.java)
                            intent.putParcelableArrayListExtra(Const.DETAIL, selectedDetailList as ArrayList)
                            intent.putExtra(Const.POSITION, i)
                            intent.putExtra(Const.NAME, productOption.name)
                            intent.putExtra(Const.OPTION_TYPE, mProductPrice!!.product!!.optionType)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            optionDetailLauncher.launch(intent)
                        }
                        EnumData.OptionType.union.name -> {
                            if (i != 0 && mItemList[i - 1].seqNo == null) {
                                ToastUtil.show(this@ProductShipDetailActivity, R.string.msg_select_before_option)
                                return@setOnClickListener
                            }
                            if (i < productOptionList.size - 1) {
                                val selectedItemList = productOptionItemList!!.filter {
                                    it.optionSeqNo == productOption.seqNo
                                }
                                val intent = Intent(this@ProductShipDetailActivity, AlertProductOptionActivity::class.java)
                                intent.putParcelableArrayListExtra(Const.ITEM, selectedItemList as ArrayList)
                                intent.putExtra(Const.POSITION, i)
                                intent.putExtra(Const.NAME, productOption.name)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                optionLauncher.launch(intent)
                            } else {
                                val selectedDetailList = productOptionDetailList!!.filter {
                                    when (productOptionList.size) {
                                        1 -> {
                                            true
                                        }
                                        2 -> {
                                            it.depth1ItemSeqNo == mItemList[0].seqNo

                                        }
                                        else -> {
                                            it.depth1ItemSeqNo == mItemList[0].seqNo && it.depth2ItemSeqNo == mItemList[1].seqNo
                                        }
                                    }
                                }

                                val intent = Intent(this@ProductShipDetailActivity, AlertProductOptionActivity::class.java)
                                intent.putParcelableArrayListExtra(Const.DETAIL, selectedDetailList as ArrayList)
                                intent.putExtra(Const.POSITION, i)
                                intent.putExtra(Const.NAME, productOption.name)
                                intent.putExtra(Const.OPTION_TYPE, mProductPrice!!.product!!.optionType)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                optionDetailLauncher.launch(intent)
                            }
                        }
                    }
                }
                binding.layoutProductShipDetailOption.addView(optionBinding.root)
            }
        } else {
            binding.scrollProductShipDetailOption.visibility = View.GONE
            binding.viewProductShipDetailOptionBar.visibility = View.GONE
        }
    }

    private fun checkLike() {
        val params = HashMap<String, String>()

        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
        params["productPriceSeqNo"] = mProductPrice!!.seqNo.toString()
        params["productSeqNo"] = mProductPrice!!.product!!.seqNo.toString()

        showProgress("")
        mIsLike = false
        binding.layoutProductShipDetailLike.isEnabled = false
        ApiBuilder.create().getProductLikeCheck(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                binding.layoutProductShipDetailLike.isEnabled = true
                binding.layoutProductShipDetailLike.isSelected = true
                mIsLike = true
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                binding.layoutProductShipDetailLike.isEnabled = true
            }
        }).build().call()
    }

    var animating = false

    private fun scrollOutAnimation() {
        val scrollOutAnimator = ObjectAnimator.ofFloat(binding.layoutProductShipDetailBottom, "translationY", 0f, binding.layoutProductShipDetailBottom.height.toFloat()).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    animating = false
                    binding.layoutProductShipDetailBottom.visibility = View.GONE
                }
            })
        }

        if (!animating) {
            scrollOutAnimator.start()
        }
    }

    private fun scrollInAnimation() {
        val scrollInAnimator = ObjectAnimator.ofFloat(binding.layoutProductShipDetailBottom, "translationY", resources.getDimension(R.dimen.height_840), 0f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {

                    binding.layoutProductShipDetailBottom.visibility = View.VISIBLE
                    animating = true
                }

                override fun onAnimationCancel(animation: Animator?) {
                    animating = true
                }

                override fun onAnimationEnd(animation: Animator?) {
                    animating = false
                }
            })
        }

        if (!animating) {
            scrollInAnimator.start()
        }
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setData()
        }
    }

    val noShowLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            scrollInAnimation()
        }
    }

    val optionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {

                val item = data.getParcelableExtra<ProductOptionItem>(Const.DATA)
                val position = data.getIntExtra(Const.POSITION, 0)
                mItemList[position] = item!!
                mOptionTextList[position].text = item.item
                mOptionTextList[position].setTextColor(ResourceUtil.getColor(this, R.color.color_232323))
            }
        }
    }

    val optionDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) { // There are no request codes
            val data = result.data
            if (data != null) {

                val detail = data.getParcelableExtra<ProductOptionDetail>(Const.DATA)

                var isContain = false
                for (i in mSelectPurchaseProductOptionList.indices) {
                    val selectBuyGoodsOption = mSelectPurchaseProductOptionList[i]
                    if (selectBuyGoodsOption.productOptionDetailSeqNo == detail!!.seqNo) {
                        isContain = true
                        val remainCount = detail.amount!! - detail.soldCount!!
                        if (selectBuyGoodsOption.amount!! < remainCount) {
                            selectBuyGoodsOption.amount = selectBuyGoodsOption.amount!! + 1
                        }
                        setChangeOptionView(mSelectBindingList[i], selectBuyGoodsOption)
                        break
                    }
                }

                if (!isContain) {
                    val purchaseProductOption = PurchaseProductOption()
                    purchaseProductOption.amount = 1
                    purchaseProductOption.productSeqNo = mProductPrice!!.productSeqNo
                    purchaseProductOption.productOptionDetailSeqNo = detail!!.seqNo
                    purchaseProductOption.price = detail.price!!
                    if (detail.item1 != null) {
                        purchaseProductOption.depth1 = detail.item1!!.item
                    }
                    if (detail.item2 != null) {
                        purchaseProductOption.depth2 = detail.item2!!.item
                    }
                    mSelectPurchaseProductOptionList.add(purchaseProductOption)
                    addSelectOption(purchaseProductOption, detail)
                }


                binding.layoutProductShipDetailSelectBar.visibility = View.VISIBLE
                binding.layoutProductShipDetailSelectItem.visibility = View.VISIBLE
                mSingleTotalCount++
                initOption()
            }
        }
    }

    var mSelectBindingList = arrayListOf<ItemSelectedOptionBinding>()
    var mSingleTotalCount = 0

    private fun addSelectOption(option: PurchaseProductOption, detail: ProductOptionDetail) {
        val selectedOptionBinding = ItemSelectedOptionBinding.inflate(layoutInflater)
        when (mProductPrice!!.product!!.optionType) {
            EnumData.OptionType.single.name -> {
                selectedOptionBinding.textSelectedOption.text = "${option.depth1}"
            }
            EnumData.OptionType.union.name -> {
                when (mItemList.size) {
                    1 -> {
                        selectedOptionBinding.textSelectedOption.text = "${option.depth1}"
                    }
                    else -> {
                        selectedOptionBinding.textSelectedOption.text = "${option.depth1} / ${option.depth2}"
                    }
                }
            }
        }


        selectedOptionBinding.imageSelectedOptionDelete.setOnClickListener {
            mSingleTotalCount -= option.amount!!
            mSelectPurchaseProductOptionList.remove(option)
            binding.layoutProductShipDetailSelectItem.removeView(selectedOptionBinding.root)
            setTotalPrice()
            checkSelectOption()
        }

        selectedOptionBinding.imageSelectedOptionMinus.setOnClickListener {
            if (option.amount!! > 1) {
                mSingleTotalCount--
                option.amount = option.amount!! - 1
                setChangeOptionView(selectedOptionBinding, option)
            }
        }
        selectedOptionBinding.imageSelectedOptionPlus.setOnClickListener {
            when (mProductPrice!!.product!!.optionType) {
                EnumData.OptionType.single.name -> {
                    if (mSingleTotalCount < mRemainCount) {
                        mSingleTotalCount++
                        option.amount = option.amount!! + 1
                        setChangeOptionView(selectedOptionBinding, option)
                    }
                }
                EnumData.OptionType.union.name -> {
                    val remainCount = detail.amount!! - detail.soldCount!!
                    if (option.amount!! < remainCount) {
                        option.amount = option.amount!! + 1
                        setChangeOptionView(selectedOptionBinding, option)
                    }
                }
            }
        }
        setChangeOptionView(selectedOptionBinding, option)
        mSelectBindingList.add(selectedOptionBinding)
        binding.layoutProductShipDetailSelectItem.addView(selectedOptionBinding.root)
    }

    private fun setChangeOptionView(selectedOptionBinding: ItemSelectedOptionBinding, option: PurchaseProductOption) {
        selectedOptionBinding.textSelectedOptionPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(((mProductPrice!!.price!! + option.price!!) * option.amount!!).toString()))
        selectedOptionBinding.textSelectedOptionCount.text = option.amount.toString()
        setTotalPrice()
    }

    var mTotalPrice = 0

    private fun setTotalPrice() {
        binding.layoutProductShipDetailTotalPrice.visibility = View.VISIBLE
        var totalPrice = 0
        if (mProductPrice!!.product!!.useOption != null && mProductPrice!!.product!!.useOption!!) {
            for (purchaseProductOption in mSelectPurchaseProductOptionList) {
                totalPrice += (mProductPrice!!.price!!.toInt() + purchaseProductOption.price!!) * purchaseProductOption.amount!!
            }
        } else {
            totalPrice = mProductPrice!!.price!!.toInt() * mCount
        }



        binding.textProductShipDetailTotalPrice.text = getString(R.string.format_money_unit, FormatUtil.getMoneyType(totalPrice.toString()))

        if (mProductPrice!!.product!!.salesType!! == EnumData.SalesType.SHIPPING.type) {
            when (mProductPrice!!.productDelivery!!.type) { // 1:무료, 2:유료, 3:조건부 무료
                EnumData.DeliveryType.free.type -> {

                }
                EnumData.DeliveryType.pay.type -> {
                    if (mProductPrice!!.productDelivery!!.deliveryFee!! > 0) {
                        mTotalPrice += mProductPrice!!.productDelivery!!.deliveryFee!!.toInt()
                    }
                }
                EnumData.DeliveryType.conditionPay.type -> {
                    if (mProductPrice!!.productDelivery!!.deliveryFee!! > 0) {
                        if (mProductPrice!!.productDelivery!!.deliveryMinPrice != null && mProductPrice!!.productDelivery!!.deliveryMinPrice!! < totalPrice) {
                            mTotalPrice += mProductPrice!!.productDelivery!!.deliveryFee!!.toInt()
                        }
                    }
                }
            }
        }


        mTotalPrice += totalPrice
    }

    private fun checkSelectOption() {
        if (mSelectPurchaseProductOptionList.isEmpty()) {
            binding.layoutProductShipDetailSelectBar.visibility = View.GONE
            binding.layoutProductShipDetailSelectItem.visibility = View.GONE
        } else {
            binding.layoutProductShipDetailSelectBar.visibility = View.VISIBLE
            binding.layoutProductShipDetailSelectItem.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (binding.layoutProductShipDetailBottom.visibility == View.VISIBLE) {
            scrollOutAnimation()
        } else {
            super.onBackPressed()
        }
    }
}