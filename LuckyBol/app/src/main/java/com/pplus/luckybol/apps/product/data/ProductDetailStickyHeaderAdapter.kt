package com.pplus.luckybol.apps.product.data

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.product.ui.*
import com.pplus.luckybol.core.code.common.EnumData
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.*
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.*
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProductDetailStickyHeaderAdapter(var mProductPrice: ProductPrice) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var recyclerItemList: ArrayList<ProductReviewAdapterItem> = ArrayList()
    var mDataList: MutableList<ProductReview>? = null
    var listener: OnItemClickListener? = null
    var mTab = 0

    init {
        mTab = 0
        setListData()
    }

    companion object {
        const val TYPE_TOP = 0
        const val TYPE_HOLDER = 1
        const val TYPE_DETAIL = 2
        const val TYPE_EVAL = 3
        const val TYPE_EMPTY = 4
        const val TYPE_LIST = 5
    }

    override fun getItemViewType(position: Int): Int {
        when (mTab) {
            0 -> {
                return when (position) {
                    0 -> TYPE_TOP
                    1 -> TYPE_HOLDER
                    else -> TYPE_DETAIL
                }
            }
            else -> {
                return when (position) {
                    0 -> TYPE_TOP
                    1 -> TYPE_HOLDER
                    2 -> {
                        if (mDataList!!.isEmpty()) {
                            TYPE_EMPTY
                        }else{
                            TYPE_EVAL
                        }
                    }
                    else -> {
                        TYPE_LIST
                    }
                }
            }
        }

    }


    interface OnItemClickListener {

        fun onTabChanged(tab: Int)
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            TYPE_TOP -> {
                val binding = HeaderProductShipDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                TopViewHolder(binding)
            }
            TYPE_HOLDER -> {
                val binding = HolderProductShipDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HolderViewHolder(binding)
            }
            TYPE_DETAIL -> {
                val binding = DetailProductShipDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                DetailViewHolder(binding)
            }
            TYPE_EMPTY -> {
                val binding = EmptyProductShipDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EmptyViewHolder(binding)
            }
            TYPE_EVAL -> {
                val binding = HeaderReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                EvalViewHolder(binding)
            }
            else -> {
                val binding = ItemGoodsReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }
        }

    }

    fun isHolder(position: Int): Boolean {
        return recyclerItemList[position].type == TYPE_HOLDER
    }

    fun clear() {

        mDataList = arrayListOf()
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductReview>) {

        if (this.mDataList == null) {
            this.mDataList = java.util.ArrayList<ProductReview>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun setListData() {
        recyclerItemList.clear()
        recyclerItemList.add(ProductReviewAdapterItem(TYPE_TOP))
        recyclerItemList.add(ProductReviewAdapterItem(TYPE_HOLDER))

        when (mTab) {
            0 -> {
                recyclerItemList.add(ProductReviewAdapterItem(TYPE_DETAIL))
            }
            1 -> {
                if (mDataList!!.isEmpty()) {
                    recyclerItemList.add(ProductReviewAdapterItem(TYPE_EMPTY))
                } else {
                    recyclerItemList.add(ProductReviewAdapterItem(TYPE_EVAL))
                    for (data in mDataList!!) {
                        recyclerItemList.add(ProductReviewAdapterItem(TYPE_LIST, data))
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = recyclerItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TopViewHolder -> {
                holder.bindView()
            }
            is HolderViewHolder -> {
                holder.bindView()
            }
            is DetailViewHolder -> {
                holder.bindView()
            }
            is EvalViewHolder -> {
                holder.bindView()
            }
            is EmptyViewHolder -> {
                holder.bindView()
            }
            is ItemViewHolder -> {
                val item = recyclerItemList[position].data!!
                holder.bindView(item)
            }
        }
    }

    inner class TopViewHolder(val binding: HeaderProductShipDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            val imageList = mProductPrice.product!!.imageList
            if (imageList != null && imageList.isNotEmpty()) {

                if (imageList.size > 1) {
                    binding.indicatorProductShipDetail.visibility = View.VISIBLE
                } else {
                    binding.indicatorProductShipDetail.visibility = View.GONE
                }

                val imageAdapter = ProductImagePagerAdapter(itemView.context)
                imageAdapter.dataList = imageList as java.util.ArrayList<ProductImage>

                binding.pagerProductShipDetail.adapter = imageAdapter
                binding.indicatorProductShipDetail.removeAllViews()
                binding.indicatorProductShipDetail.build(LinearLayout.HORIZONTAL, imageList.size)
                binding.pagerProductShipDetail.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                    override fun onPageScrolled(position: Int,
                                                positionOffset: Float,
                                                positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {

                        binding.indicatorProductShipDetail.setCurrentItem(position)
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })

                imageAdapter.mListener = object : ProductImagePagerAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(itemView.context, ProductDetailViewerActivity::class.java)
                        intent.putExtra(Const.POSITION, binding.pagerProductShipDetail.currentItem)
                        intent.putExtra(Const.DATA, imageAdapter.dataList)
                        (itemView.context as BaseActivity).startActivity(intent)
                    }

                }
            }

            binding.textProductShipDetailName.text = mProductPrice.product!!.name

            binding.imageProductShipDetailShare.setOnClickListener {
                var text = ""
                if(Const.API_URL.startsWith("https://api.prnumber.com")){
                    text = "${mProductPrice.product!!.name}\n${itemView.context.getString(R.string.format_msg_product_url, mProductPrice.code)}"
                }else{
                    text = "${mProductPrice.product!!.name}\n${itemView.context.getString(R.string.format_stage_msg_product_url, mProductPrice.code)}"
                }
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, text)
                val chooserIntent = Intent.createChooser(intent, itemView.context.getString(R.string.word_share))
                itemView.context.startActivity(chooserIntent)
            }

            if (mProductPrice.originPrice != null && mProductPrice.originPrice!! > 0) {

                if (mProductPrice.originPrice!! <= mProductPrice.price!!) {
                    binding.textProductShipDetailOriginPrice.visibility = View.GONE
                } else {
                    binding.textProductShipDetailOriginPrice.text = itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(mProductPrice.originPrice.toString()))
                    binding.textProductShipDetailOriginPrice.visibility = View.VISIBLE
                    binding.textProductShipDetailOriginPrice.paintFlags = binding.textProductShipDetailOriginPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }

            } else {
                binding.textProductShipDetailOriginPrice.visibility = View.GONE
            }

            if (mProductPrice.discountRatio != null && mProductPrice.discountRatio!!.toInt() > 0) {
                binding.textProductShipDetailDiscountRatio.visibility = View.VISIBLE
                binding.textProductShipDetailDiscountRatio.text = itemView.context.getString(R.string.format_percent2, mProductPrice.discountRatio!!.toInt().toString())
            } else {
                binding.textProductShipDetailDiscountRatio.visibility = View.GONE
            }
            binding.textProductShipDetailSalePrice.text = FormatUtil.getMoneyType(mProductPrice.price.toString())
            if(mProductPrice.isPoint != null && mProductPrice.isPoint!!){
                binding.textProductShipDetailSavePoint.visibility = View.VISIBLE
                binding.textProductShipDetailSavePoint.text = PplusCommonUtil.fromHtml(itemView.context.getString(R.string.html_save_cash, FormatUtil.getMoneyTypeFloat(mProductPrice.point!!.toString())))
            }else{
                binding.textProductShipDetailSavePoint.visibility = View.GONE
            }

            when (mProductPrice.productDelivery!!.type) { // 1:무료, 2:유료, 3:조건부 무료
                EnumData.DeliveryType.free.type -> {
                    binding.textProductShipDetailDeliveryFee.text = itemView.context.getString(R.string.word_free_ship)
                    binding.textProductShipDetailDeliveryMinPrice.visibility = View.GONE
                }
                EnumData.DeliveryType.pay.type -> {
                    if (mProductPrice.productDelivery!!.deliveryFee!! > 0) {
                        binding.textProductShipDetailDeliveryFee.text = itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(mProductPrice.productDelivery!!.deliveryFee!!.toInt().toString()))
                        binding.textProductShipDetailDeliveryMinPrice.visibility = View.GONE
                    } else {
                        binding.textProductShipDetailDeliveryFee.text = itemView.context.getString(R.string.word_free_ship)
                    }
                }
                EnumData.DeliveryType.conditionPay.type -> {
                    if (mProductPrice.productDelivery!!.deliveryFee!! > 0) {
                        binding.textProductShipDetailDeliveryFee.text = itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(mProductPrice.productDelivery!!.deliveryFee!!.toInt().toString()))
                        if (mProductPrice.productDelivery!!.deliveryMinPrice != null && mProductPrice.productDelivery!!.deliveryMinPrice!! > 0) {
                            binding.textProductShipDetailDeliveryMinPrice.text = itemView.context.getString(R.string.format_delivery_min_price, FormatUtil.getMoneyType(mProductPrice.productDelivery!!.deliveryMinPrice!!.toInt().toString()))
                            binding.textProductShipDetailDeliveryMinPrice.visibility = View.VISIBLE
                        } else {
                            binding.textProductShipDetailDeliveryMinPrice.visibility = View.GONE
                        }
                    }
                }
            }
            if (mProductPrice.productDelivery!!.isAddFee != null && mProductPrice.productDelivery!!.isAddFee!!) {
                if (mProductPrice.productDelivery!!.deliveryAddFee1 != null && mProductPrice.productDelivery!!.deliveryAddFee1!! > 0) {
                    binding.textProductShipDetailDeliveryAddFee1.visibility = View.VISIBLE
                    binding.textProductShipDetailDeliveryAddFee1.text = itemView.context.getString(R.string.format_delivery_add_fee1, FormatUtil.getMoneyType(mProductPrice.productDelivery!!.deliveryAddFee1!!.toInt().toString()))
                } else {
                    binding.textProductShipDetailDeliveryAddFee1.visibility = View.GONE
                }

                if (mProductPrice.productDelivery!!.deliveryAddFee2 != null && mProductPrice.productDelivery!!.deliveryAddFee2!! > 0) {
                    binding.textProductShipDetailDeliveryAddFee2.visibility = View.VISIBLE
                    binding.textProductShipDetailDeliveryAddFee2.text = itemView.context.getString(R.string.format_delivery_add_fee2, FormatUtil.getMoneyType(mProductPrice.productDelivery!!.deliveryAddFee2!!.toInt().toString()))
                } else {
                    binding.textProductShipDetailDeliveryAddFee2.visibility = View.GONE
                }
            } else {
                binding.textProductShipDetailDeliveryAddFee1.visibility = View.GONE
                binding.textProductShipDetailDeliveryAddFee2.visibility = View.GONE
            }

            val params = HashMap<String, String>()
            params["productSeqNo"] = mProductPrice.product!!.seqNo.toString()
            ApiBuilder.create().getProductInfoByProductSeqNo(params).setCallback(object : PplusCallback<NewResultResponse<ProductInfo>> {
                override fun onResponse(call: Call<NewResultResponse<ProductInfo>>?,
                                        response: NewResultResponse<ProductInfo>?) {
                    if (response?.data != null) {
                        binding.layoutProductShipDetailProductInfo.visibility = View.VISIBLE
                        val productInfo = response.data!!

                        var noneAllData = true
                        if(StringUtils.isNotEmpty(productInfo.model)){
                            noneAllData = false
                            binding.textProductShipDetailModelName.text = productInfo.model
                            binding.layoutProductShipDetailModelName.visibility = View.VISIBLE
                        }else{
                            binding.layoutProductShipDetailModelName.visibility = View.GONE
                        }

                        if(StringUtils.isNotEmpty(productInfo.modelCode)){
                            noneAllData = false
                            binding.textProductShipDetailModelCode.text = productInfo.modelCode
                            binding.layoutProductShipDetailModelCode.visibility = View.VISIBLE
                        }else{
                            binding.layoutProductShipDetailModelCode.visibility = View.GONE
                        }

                        if(StringUtils.isNotEmpty(productInfo.brand)){
                            noneAllData = false
                            binding.textProductShipDetailBrandName.text = productInfo.brand
                            binding.layoutProductShipDetailBrandName.visibility = View.VISIBLE
                        }else{
                            binding.layoutProductShipDetailBrandName.visibility = View.GONE
                        }

                        if(StringUtils.isNotEmpty(productInfo.menufacturer)){
                            noneAllData = false
                            binding.textProductShipDetailManufacturerName.text = productInfo.menufacturer
                            binding.layoutProductShipDetailManufacturerName.visibility = View.VISIBLE
                        }else{
                            binding.layoutProductShipDetailManufacturerName.visibility = View.GONE
                        }

                        if(StringUtils.isNotEmpty(productInfo.origin)){
                            noneAllData = false
                            binding.textProductShipDetailOrigin.text = productInfo.origin
                            binding.layoutProductShipDetailOrigin.visibility = View.VISIBLE
                        }else{
                            binding.layoutProductShipDetailOrigin.visibility = View.GONE
                        }

                        if(StringUtils.isNotEmpty(productInfo.menufacturedDate)){
                            noneAllData = false
                            binding.textProductShipDetailManufactureDate.text = productInfo.menufacturedDate
                            binding.layoutProductShipDetailManufactureDate.visibility = View.VISIBLE
                        }else{
                            binding.layoutProductShipDetailManufactureDate.visibility = View.GONE
                        }

                        if(noneAllData){
                            binding.layoutProductShipDetailProductInfo.visibility = View.GONE
                        }
                    }else{
                        binding.layoutProductShipDetailProductInfo.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<ProductInfo>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<ProductInfo>?) {
                    binding.layoutProductShipDetailProductInfo.visibility = View.GONE
                }
            }).build().call()

        }
    }

    inner class HolderViewHolder(val binding: HolderProductShipDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
            binding.textProductShipDetailInfoTab.setOnClickListener {
                binding.textProductShipDetailInfoTab.isSelected = true
                binding.textProductShipDetailReviewTab.isSelected = false
                if (mTab != 0) {
                    mTab = 0
                    setListData()
                    listener?.onTabChanged(mTab)
                }

            }

            binding.textProductShipDetailReviewTab.setOnClickListener {
                binding.textProductShipDetailInfoTab.isSelected = false
                binding.textProductShipDetailReviewTab.isSelected = true
                if (mTab != 1) {
                    mTab = 1
                    setListData()
                    listener?.onTabChanged(mTab)
                }
            }
            when (mTab) {
                0 -> {
                    binding.textProductShipDetailInfoTab.isSelected = true
                    binding.textProductShipDetailReviewTab.isSelected = false
                }
                1 -> {
                    binding.textProductShipDetailInfoTab.isSelected = false
                    binding.textProductShipDetailReviewTab.isSelected = true
                }
            }
        }
    }

    inner class DetailViewHolder(val binding: DetailProductShipDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        var isOpen = false
        init {
            binding.webviewProductShipDetail.webChromeClient = WebChromeClient()
            binding.webviewProductShipDetail.webViewClient = WebViewClient()
            binding.webviewProductShipDetail.settings.javaScriptEnabled = true
            binding.webviewProductShipDetail.settings.javaScriptCanOpenWindowsAutomatically = true
            binding.webviewProductShipDetail.settings.setAppCacheEnabled(true)
            binding.webviewProductShipDetail.settings.cacheMode = WebSettings.LOAD_DEFAULT
            binding.webviewProductShipDetail.settings.loadWithOverviewMode = true
            binding.webviewProductShipDetail.settings.useWideViewPort = true
            binding.webviewProductShipDetail.settings.allowContentAccess = true
            binding.webviewProductShipDetail.settings.domStorageEnabled = true
            binding.webviewProductShipDetail.settings.builtInZoomControls = true
            binding.webviewProductShipDetail.settings.displayZoomControls = false
            val dir = itemView.context.cacheDir
            if (!dir.exists()) {
                dir.mkdirs()
            }
            binding.webviewProductShipDetail.settings.setAppCachePath(dir.path)
            binding.webviewProductShipDetail.settings.allowFileAccess = true
            binding.webviewProductShipDetail.settings.setSupportMultipleWindows(true)
            binding.webviewProductShipDetail.settings.mixedContentMode = 0
//            binding.webviewProductShipDetail.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        }

        fun bindView() {

            binding.webviewProductShipDetail.loadData("<div style='width:100%;text-align:center'>${mProductPrice.product!!.contents}</div>", "text/html", "utf-8")

//            binding.layout_product_ship_detail_open_webview.setOnClickListener {
//                if (isOpen) {
//                    LogUtil.e("WEBVIEW", "CLOSE")
//                    isOpen = false
//                    binding.text_product_ship_detail_open_webview.text = itemView.context.getString(R.string.msg_open_view_product_info)
//                    binding.text_product_ship_detail_open_webview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
//                    (binding.webview_product_ship_detail.layoutParams as LinearLayout.LayoutParams).height = itemView.resources.getDimensionPixelSize(R.dimen.height_900)
//                    binding.webview_product_ship_detail.requestLayout()
//                } else {
//                    isOpen = true
//                    LogUtil.e("WEBVIEW", "OPEN")
//                    (binding.webview_product_ship_detail.layoutParams as LinearLayout.LayoutParams).height = LinearLayout.LayoutParams.WRAP_CONTENT
//                    binding.text_product_ship_detail_open_webview.text = itemView.context.getString(R.string.msg_close_view_product_info)
//                    binding.text_product_ship_detail_open_webview.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
//                    binding.webview_product_ship_detail.minimumHeight = itemView.resources.getDimensionPixelSize(R.dimen.height_900)
//                    binding.webview_product_ship_detail.requestLayout()
//
//                }
//            }

            binding.layoutProductShipDetailPurchaseAddInfo.setOnClickListener {
                val intent = Intent(itemView.context, PurchaseAddInfoActivity::class.java)
                intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                itemView.context.startActivity(intent)
            }

            binding.layoutProductShipDetailNoticeInfo.setOnClickListener {
                val intent = Intent(itemView.context, ProductNoticeActivity::class.java)
                intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                itemView.context.startActivity(intent)
            }

            binding.layoutProductShipDetailRefundExchangeInfo.setOnClickListener {
                val intent = Intent(itemView.context, ProductRefundExchangeInfoActivity::class.java)
                intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                itemView.context.startActivity(intent)
            }

            binding.layoutProductShipDetailSellerInfo.setOnClickListener {
                val intent = Intent(itemView.context, ProductSellerInfoActivity::class.java)
                intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                itemView.context.startActivity(intent)
            }

            binding.layoutProductShipDetailWarning.setOnClickListener {
                val intent = Intent(itemView.context, ProductWarningActivity::class.java)
                intent.putExtra(Const.PRODUCT_PRICE, mProductPrice)
                itemView.context.startActivity(intent)
            }
        }
    }

    inner class EvalViewHolder(val binding: HeaderReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {

            val params = HashMap<String, String>()
            params["productPriceSeqNo"] = mProductPrice.seqNo.toString()
            ApiBuilder.create().getProductReviewCountGroupByEval(params).setCallback(object : PplusCallback<NewResultResponse<ProductReviewCountEval>> {
                override fun onResponse(call: Call<NewResultResponse<ProductReviewCountEval>>?,
                                        response: NewResultResponse<ProductReviewCountEval>?) {

                    if (response?.datas != null) {
                        val productReviewCountEvalList = response.datas!!
                        binding.layoutHeaderReviewEvalBar.removeAllViews()
                        if(mProductPrice.avgEval ==  null){
                            mProductPrice.avgEval = 0.0f
                        }
                        val eval = String.format("%.1f", mProductPrice.avgEval)
                        binding.textHeaderReviewAvgEval.text = eval
                        binding.gradeBarHeaderReview.build(eval)
                        var totalCount = 0
                        for (productCountEval in productReviewCountEvalList) {
                            totalCount += productCountEval.count!!
                        }

                        for (productCountEval in productReviewCountEvalList) {

                            val evalBarBinding = ItemEvalBarBinding.inflate(LayoutInflater.from(itemView.context), LinearLayout(itemView.context), false)
                            binding.layoutHeaderReviewEvalBar.addView(evalBarBinding.root)
                            (evalBarBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = itemView.context.resources.getDimensionPixelSize(R.dimen.width_36)
                            if (productCountEval.count == 0) {
                                evalBarBinding.textEvalBarCount.visibility = View.INVISIBLE
                            } else {
                                evalBarBinding.textEvalBarCount.visibility = View.VISIBLE
                                evalBarBinding.textEvalBarCount.text = productCountEval.count.toString()
                            }

                            if (totalCount > 0) {
                                evalBarBinding.layoutEvalBarBg.weightSum = totalCount.toFloat()
                            }

                            (evalBarBinding.viewEvalBarRate.layoutParams as LinearLayout.LayoutParams).weight = productCountEval.count!!.toFloat()

                            evalBarBinding.textEvalBarEval.text = itemView.context.getString(R.string.format_eval, productCountEval.eval.toString())

                        }

                        binding.txtHeaderReviewTotalCount.text = itemView.context.getString(R.string.format_review3, FormatUtil.getMoneyType(totalCount.toString()))
                    }
                }

                override fun onFailure(call: Call<NewResultResponse<ProductReviewCountEval>>?,
                                       t: Throwable?,
                                       response: NewResultResponse<ProductReviewCountEval>?) {
                }
            }).build().call()
        }
    }

    inner class EmptyViewHolder(binding: EmptyProductShipDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView() {
        }
    }

    inner class ItemViewHolder(val binding: ItemGoodsReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        internal var mTodayYear: Int = 0
        internal var mTodayMonth: Int = 0
        internal var mTodayDay: Int = 0

        init {
            val c = Calendar.getInstance()
            mTodayYear = c.get(Calendar.YEAR)
            mTodayMonth = c.get(Calendar.MONTH)
            mTodayDay = c.get(Calendar.DAY_OF_MONTH)
        }

        fun bindView(item: ProductReview) {

            if (item.member != null) {
                binding.textGoodsReviewName.text = item.member!!.nickname
                if (item.member!!.profileAttachment != null) {
                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
                    Glide.with(itemView.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(binding.imageGoodsReviewProfile)
                } else {
                    binding.imageGoodsReviewProfile.setImageResource(R.drawable.img_commerce_user_profile_default)
                }
            }

            binding.textGoodsReviewContents.text = item.review

            if (StringUtils.isNotEmpty(item.reviewReply)) {
                binding.layoutGoodsReviewReply.visibility = View.VISIBLE
                binding.textGoodsReviewReply.text = item.reviewReply
                if (StringUtils.isNotEmpty(item.reviewReplyDate)) {
                    binding.textGoodsReviewReplyDate.text = PplusCommonUtil.getDateFormat(item.reviewReplyDate!!)
                }

            } else {
                binding.layoutGoodsReviewReply.visibility = View.GONE
            }

            if (item.eval != null) {
                val eval = String.format("%.1f", item.eval!!.toFloat())
                binding.gradeBarGoodsReview.build(eval)
            } else {
                val eval = String.format("%.1f", 0f)
                binding.gradeBarGoodsReview.build(eval)
            }

            try {
                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
                val c = Calendar.getInstance()
                c.time = d

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
                    val output = SimpleDateFormat("a HH:mm", Locale.getDefault())
                    binding.textGoodsReviewRegDate.text = output.format(d)
                } else {
                    val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                    binding.textGoodsReviewRegDate.text = output.format(d)
                }

            } catch (e: Exception) {

            }

            if (item.imageList != null && item.imageList!!.isNotEmpty()) {
                binding.layoutGoodsReviewImage.visibility = View.VISIBLE
                binding.pagerGoodsReviewImage.visibility = View.VISIBLE
                val imageAdapter = ProductReviewImagePagerAdapter(itemView.context)
                imageAdapter.dataList = item.imageList as java.util.ArrayList<ProductReviewImage>
                binding.pagerGoodsReviewImage.adapter = imageAdapter
                binding.indicatorGoodsReview.visibility = View.VISIBLE
                binding.indicatorGoodsReview.removeAllViews()
                binding.indicatorGoodsReview.build(LinearLayout.HORIZONTAL, item.imageList!!.size)
                binding.pagerGoodsReviewImage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                    override fun onPageScrolled(position: Int,
                                                positionOffset: Float,
                                                positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {

                        binding.indicatorGoodsReview.setCurrentItem(position)
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })

                //            pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
                imageAdapter.setListener(object : ProductReviewImagePagerAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        if (listener != null) {
                            listener!!.onItemClick(adapterPosition - 3)
                        }
                    }
                })
            } else {
                binding.indicatorGoodsReview.removeAllViews()
                binding.indicatorGoodsReview.visibility = View.GONE
                binding.pagerGoodsReviewImage.visibility = View.GONE
                binding.pagerGoodsReviewImage.adapter = null
                binding.layoutGoodsReviewImage.visibility = View.GONE
            }

            itemView.setOnClickListener {
                listener?.onItemClick(absoluteAdapterPosition - 3)
            }
        }
    }
}