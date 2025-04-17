//package com.pplus.prnumberuser.apps.page.data
//
//import android.app.Activity
//import android.content.ClipData
//import android.content.ClipboardManager
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Paint
//import android.net.Uri
//import androidx.fragment.app.FragmentManager
//import androidx.viewpager.widget.ViewPager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import com.google.android.gms.maps.model.BitmapDescriptorFactory
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.maps.model.MarkerOptions
//import com.kakao.kakaonavi.KakaoNaviParams
//import com.kakao.kakaonavi.KakaoNaviService
//import com.kakao.kakaonavi.Location
//import com.kakao.kakaonavi.NaviOptions
//import com.kakao.kakaonavi.options.CoordType
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.data.GoodsImagePagerAdapter
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailActivity
//import com.pplus.prnumberuser.apps.page.ui.LocationPageActivity
//import com.pplus.prnumberuser.apps.menu.ui.CategoryMenuActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.code.common.SnsTypeCode
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsReview
//import com.pplus.prnumberuser.core.network.model.dto.Page
//import com.pplus.prnumberuser.core.network.model.dto.Sns
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.PplusNumberUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.item_goods_review.view.*
//import kotlinx.android.synthetic.main.item_page_header.view.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class PageHeaderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mPage: Page? = null
//    var mContext: Context? = null
//    var mDataList: MutableList<GoodsReview>? = null
//    var listener: OnItemClickListener? = null
//    //    var mSnsList: List<Sns>? = null
//    var mTodayYear = 0
//    var mTodayMonth = 0
//    var mTodayDay = 0
//    var mTotalCount = 0
//    var fragmentManager: FragmentManager
//    var mIsLoading = false
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//        fun onReviewClick()
//    }
//
//    constructor(context: Context, fragmentManager: FragmentManager) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        this.fragmentManager = fragmentManager
//        val c = Calendar.getInstance()
//        mTodayYear = c.get(Calendar.YEAR)
//        mTodayMonth = c.get(Calendar.MONTH)
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
//    }
//
//    fun setPage(page: Page) {
//        this.mPage = page
//    }
//
////    fun setSNS(snsList: List<Sns>?) {
////        mSnsList = snsList
////    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): GoodsReview {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<GoodsReview>? {
//
//        return mDataList
//    }
//
//    fun add(data: GoodsReview) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<GoodsReview>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<GoodsReview>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<GoodsReview>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: GoodsReview) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<GoodsReview>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<GoodsReview>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        //        val text_facebook = itemView.text_page_facebook
////        val text_kakao = itemView.text_page_kakao
////        val text_twitter = itemView.text_page_twitter
////        val text_instagram = itemView.text_page_instagram
////        val text_blog = itemView.text_page_blog
////        val text_homepage = itemView.text_page_homepage
//        val text_page_goods = itemView.text_page_goods
//        val text_page_goods_review = itemView.text_page_goods_review
//        val layout_page_share = itemView.layout_page_share
//        val layout_page_goods = itemView.layout_page_goods
//        val layout_page_goods_review = itemView.layout_page_goods_review
//        val layout_pr_number = itemView.layout_page_number
//        val text_number = itemView.text_page_number
//        val layout_introduction = itemView.layout_page_introduction
//        val text_introduction = itemView.text_page_introduction
//        val layout_phone = itemView.layout_page_phone
//        val text_phone = itemView.text_page_phone
//        val text_call = itemView.text_page_call
//        val layout_opening_hours = itemView.layout_page_open_hours
//        val text_opening_hours = itemView.text_page_opening_hours
//        val layout_parking_info = itemView.layout_page_parking_info
//        val text_parking_info = itemView.text_page_parking_info
//        val layout_convenience_info = itemView.layout_page_convenience_info
//        val text_convenience_info = itemView.text_page_convenience_info
//        val layout_kakao_id = itemView.layout_page_kakao_id
//        val text_kakao_id = itemView.text_page_kakao_id
//        val image_kakao_id_copy = itemView.image_page_kakao_id_copy
//        val layout_email = itemView.layout_page_email
//        val text_email = itemView.text_page_email
//        val layout_address = itemView.layout_page_address
//        val text_address = itemView.text_page_address
//        val layout_map = itemView.layout_page_map
//        val map = itemView.map_page
//        //        val container_map = itemView.container_home_map
//        val image_map_full = itemView.image_page_map_full
//        val text_total_review_count = itemView.text_page_goods_review_total_count
//        val layout_map_option = itemView.layout_page_info_map_option
//        val layout_find_road = itemView.layout_page_find_road
//        val layout_call_taxi = itemView.layout_page_call_taxi
//        val layout_navigation = itemView.layout_page_navigation
//        val layout_copy_address = itemView.layout_page_copy_address
//        val layout_main_goods = itemView.layout_page_main_goods
//        val image_goods = itemView.image_page_goods_image
//        val text_goods_name = itemView.text_page_goods_name
//        val layout_goods_discount = itemView.layout_page_goods_discount
//        val text_discount = itemView.text_page_goods_discount
//        val text_sale_price = itemView.text_page_goods_sale_price
//        val layout_origin_price = itemView.layout_page_goods_origin_price
//        val text_origin_price = itemView.text_page_goods_origin_price
//        val text_remain_count = itemView.text_page_goods_remain_count
//        val image_goods_sold_out = itemView.image_page_goods_sold_out
//        val text_goods_point = itemView.text_page_goods_point
//        val view_goods_bar = itemView.view_page_goods_bar
//        val text_avg_grade = itemView.text_page_goods_review_avg_grade
//        val grade_bar_avg = itemView.grade_bar_page_goods_review_avg
//
//        init {
//            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
////            container_map.setLayerType(View.LAYER_TYPE_HARDWARE, null)
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image_profile = itemView.image_goods_review_profile
//        val text_name = itemView.text_goods_review_name
//        val layout_image = itemView.layout_goods_review_image
//        val pager_image = itemView.pager_goods_review_image
//        val indicator = itemView.indicator_goods_review
//        val text_contents = itemView.text_goods_review_contents
//        val text_regDate = itemView.text_goods_review_regDate
////        val grade_bar = itemView.grade_bar_goods_review
//
//    }
//
//    override fun getItemCount(): Int {
//        if (mPage == null) {
//            return mDataList!!.size
//        }
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//
//            if(mPage!!.goodsCount != null){
//                holder.text_page_goods.text = mContext!!.getString(R.string.format_goods, FormatUtil.getMoneyType(mPage!!.goodsCount.toString()))
//            }else{
//                holder.text_page_goods.text = mContext!!.getString(R.string.format_goods, FormatUtil.getMoneyType("0"))
//            }
//            if(mPage!!.reviewCount != null){
//                holder.text_page_goods_review.text = mContext!!.getString(R.string.format_review, FormatUtil.getMoneyType(mPage!!.reviewCount.toString()))
//                holder.text_total_review_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_review, FormatUtil.getMoneyType(mPage!!.reviewCount.toString())))
//            }else{
//                holder.text_page_goods_review.text = mContext!!.getString(R.string.format_review, FormatUtil.getMoneyType("0"))
//                holder.text_total_review_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_review, FormatUtil.getMoneyType("0")))
//            }
//
//            holder.layout_page_share.setOnClickListener {
//                val shareText = mPage!!.catchphrase + "\n" + mContext!!.getString(R.string.format_msg_page_url, "index.php?no=" + mPage!!.no!!)
//
//                val intent = Intent(Intent.ACTION_SEND)
//                intent.action = Intent.ACTION_SEND
//                intent.type = "text/plain"
//                intent.putExtra(Intent.EXTRA_TEXT, shareText)
//                val chooserIntent = Intent.createChooser(intent, mContext!!.getString(R.string.word_share_page))
//                mContext!!.startActivity(chooserIntent)
//            }
//
//            holder.layout_page_goods.setOnClickListener {
//
//                if(mPage!!.isShopOrderable!! || mPage!!.isPackingOrderable!! || mPage!!.isDeliveryOrderable!!){
//                    val intent = Intent(mContext, CategoryMenuActivity::class.java)
//                    intent.putExtra(Const.PAGE, mPage)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    (mContext!! as BaseActivity).startActivity(intent)
//                }
//
//
////                val intent = Intent(mContext, GoodsListActivity::class.java)
////                intent.putExtra(Const.PAGE, mPage)
////                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////                (mContext!! as BaseActivity).startActivityForResult(intent, Const.REQ_GOODS)
//            }
//
//            holder.layout_page_goods_review.setOnClickListener {
//
//                if(listener != null){
//                    listener!!.onReviewClick()
//                }
//            }
//
//            if (mPage!!.avgEval != null) {
//                val avgEval = String.format("%.1f", mPage!!.avgEval)
//                holder.text_avg_grade.text = avgEval
//                holder.grade_bar_avg.build(avgEval)
//            } else {
//                holder.text_avg_grade.text = "0.0"
//                holder.grade_bar_avg.build("0.0")
//            }
//            if (mPage!!.numberList != null && mPage!!.numberList!!.isNotEmpty()) {
//                holder.layout_pr_number.visibility = View.VISIBLE
//                holder.text_number.text = PplusNumberUtil.getPrNumberFormat(mPage!!.numberList!![0].number)
//            } else {
//                holder.layout_pr_number.visibility = View.GONE
//            }
//
//            if (mPage!!.mainGoodsSeqNo != null) {
//
//                val params = HashMap<String, String>()
//                params["seqNo"] = mPage!!.mainGoodsSeqNo.toString()
//                ApiBuilder.create().getOneGoods(params).setCallback(object : PplusCallback<NewResultResponse<Goods>> {
//                    override fun onResponse(call: Call<NewResultResponse<Goods>>?, response: NewResultResponse<Goods>?) {
//                        if (response != null && response.data != null) {
//                            holder.layout_main_goods.visibility = View.VISIBLE
//                            holder.view_goods_bar.visibility = View.VISIBLE
//                            val goods = response.data
//
//                            holder.text_goods_name.text = goods.name
//                            holder.layout_origin_price.visibility = View.GONE
//                            holder.text_discount.visibility = View.GONE
//                            holder.layout_goods_discount.visibility = View.GONE
//                            if (goods.originPrice != null) {
//
//                                val origin_price = goods.originPrice!!
//
//                                if (origin_price > goods.price!!) {
//                                    holder.layout_origin_price.visibility = View.VISIBLE
//                                    holder.text_discount.visibility = View.VISIBLE
//                                    holder.layout_goods_discount.visibility = View.VISIBLE
//
//                                    holder.text_origin_price.text = FormatUtil.getMoneyType(origin_price.toString())
//
//                                    val percent = (100 - (goods.price!!.toFloat() / origin_price.toFloat() * 100)).toInt()
//                                    holder.text_discount.text = FormatUtil.getMoneyType(percent.toString())
//                                }
//                            }
//
//                            holder.layout_goods_discount.visibility = View.GONE
//
//                            if (goods.status == EnumData.GoodsStatus.soldout.status) {
//                                holder.image_goods_sold_out.visibility = View.VISIBLE
//                            } else {
//                                holder.image_goods_sold_out.visibility = View.GONE
//                            }
//
//                            holder.text_sale_price.text = FormatUtil.getMoneyType(goods.price.toString())
//                            val point = (goods!!.price!! * 0.01).toInt()
//                            holder.text_goods_point.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_cash_unit2, "+${FormatUtil.getMoneyType(point.toString())}"))
//
//                            if (goods.count != null && goods.count != -1) {
//                                var soldCount = 0
//                                if (goods.soldCount != null) {
//                                    soldCount = goods.soldCount!!
//                                }
//                                holder.text_remain_count.visibility = View.VISIBLE
//                                holder.text_remain_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_main_goods_remain_count, FormatUtil.getMoneyType((goods.count!! - soldCount).toString())))
//                            } else {
//                                holder.text_remain_count.visibility = View.GONE
//                            }
//
//                            if (goods.attachments != null && goods.attachments!!.images != null && goods.attachments!!.images!!.isNotEmpty()) {
//                                val id = goods.attachments!!.images!![0]
//
//                                val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
//                                Glide.with(holder.image_goods.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image_goods)
//
//                            } else {
//                                holder.image_goods.setImageResource(R.drawable.prnumber_default_img)
//                            }
//
//                            holder.layout_main_goods.setOnClickListener {
//
//                                if(goods!!.status != EnumData.GoodsStatus.ing.status){
//                                    ToastUtil.show(mContext, R.string.msg_finish_sale_goods)
//                                    return@setOnClickListener
//                                }
//
//                                val intent = Intent(mContext, GoodsDetailActivity::class.java)
//                                intent.putExtra(Const.DATA, goods)
//                                (mContext as Activity).startActivityForResult(intent, Const.REQ_GOODS_DETAIL)
//                            }
//                        } else {
//                            holder.layout_main_goods.visibility = View.GONE
//                            holder.view_goods_bar.visibility = View.GONE
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<NewResultResponse<Goods>>?, t: Throwable?, response: NewResultResponse<Goods>?) {
//                    }
//                }).build().call()
//
//            } else {
//                holder.layout_main_goods.visibility = View.GONE
//                holder.view_goods_bar.visibility = View.GONE
//            }
//
////            holder.text_twitter.isSelected = false
////            holder.text_facebook.isSelected = false
////            holder.text_instagram.isSelected = false
////            holder.text_kakao.isSelected = false
////            holder.text_blog.isSelected = false
////            holder.text_homepage.isSelected = false
////
////            holder.text_twitter.setOnClickListener(null)
////            holder.text_facebook.setOnClickListener(null)
////            holder.text_instagram.setOnClickListener(null)
////            holder.text_kakao.setOnClickListener(null)
////            holder.text_blog.setOnClickListener(null)
////            holder.text_homepage.setOnClickListener(null)
////
////            if (mSnsList != null && !mSnsList!!.isEmpty()) {
////                for (sns in mSnsList!!) {
////                    if (StringUtils.isNotEmpty(sns.url)) {
////                        when (SnsTypeCode.valueOf(sns.type)) {
////
////                            SnsTypeCode.twitter -> {
////                                holder.text_twitter.isSelected = true
////                                holder.text_twitter.tag = sns
////                                holder.text_twitter.setOnClickListener(onSnsClickListener)
////                            }
////                            SnsTypeCode.facebook -> {
////                                holder.text_facebook.isSelected = true
////                                holder.text_facebook.tag = sns
////                                holder.text_facebook.setOnClickListener(onSnsClickListener)
////                            }
////                            SnsTypeCode.instagram -> {
////                                holder.text_instagram.isSelected = true
////                                holder.text_instagram.tag = sns
////                                holder.text_instagram.setOnClickListener(onSnsClickListener)
////                            }
////                            SnsTypeCode.kakao -> {
////                                holder.text_kakao.isSelected = true
////                                holder.text_kakao.tag = sns
////                                holder.text_kakao.setOnClickListener(onSnsClickListener)
////                            }
////                            SnsTypeCode.blog -> {
////                                holder.text_blog.isSelected = true
////                                holder.text_blog.tag = sns
////                                holder.text_blog.setOnClickListener(onSnsClickListener)
////                            }
////                            SnsTypeCode.homepage -> {
////                                holder.text_homepage.isSelected = true
////                                holder.text_homepage.tag = sns
////                                holder.text_homepage.setOnClickListener(onSnsClickListener)
////                            }
////                        }
////                    }
////                }
////            }
//
//            if (StringUtils.isNotEmpty(mPage!!.catchphrase) || StringUtils.isNotEmpty(mPage!!.introduction)) {
//                holder.layout_introduction.visibility = View.VISIBLE
//
//                var catchphrase = ""
//                if(StringUtils.isNotEmpty(mPage!!.catchphrase)){
//                    catchphrase = mPage!!.catchphrase!!
//                }
//
//                var introduction = ""
//                if(StringUtils.isNotEmpty(mPage!!.introduction)){
//                    introduction = mPage!!.introduction!!
//                }
//
//                holder.text_introduction.text = "$catchphrase\n$introduction"
//            } else {
//                holder.layout_introduction.visibility = View.GONE
//            }
//
//            if (StringUtils.isNotEmpty(mPage!!.phone)) {
//                holder.layout_phone.visibility = View.VISIBLE
//                holder.text_phone.text = FormatUtil.getPhoneNumber(mPage!!.phone)
//                holder.text_phone.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage!!.phone!!))
//                    (mContext as BaseActivity).startActivity(intent)
//                }
//
//                holder.text_call.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPage!!.phone!!))
//                    (mContext as BaseActivity).startActivity(intent)
//                }
//            } else {
//                holder.layout_phone.visibility = View.GONE
//            }
//
//            if (mPage!!.address == null || StringUtils.isEmpty(mPage!!.address!!.roadBase)) {
//                holder.layout_address.visibility = View.GONE
//                holder.layout_map_option.visibility = View.GONE
//                holder.layout_map.visibility = View.GONE
//            } else {
//                holder.layout_address.visibility = View.VISIBLE
//                var detailAddress = ""
//                if (StringUtils.isNotEmpty(mPage!!.address!!.roadDetail)) {
//                    detailAddress = mPage!!.address!!.roadDetail
//                }
//                holder.text_address.text = "${mPage!!.address!!.roadBase} $detailAddress"
//
//                if (mPage!!.latitude != null && mPage!!.longitude != null) {
//                    holder.layout_map.visibility = View.VISIBLE
//
//                    holder.map.onCreate(null)
//                    holder.map.getMapAsync(object : OnMapReadyCallback {
//                        override fun onMapReady(googMap: GoogleMap) {
//                            LogUtil.e("GOOGLEMAP", "onMapReady")
//                            googMap.uiSettings.isScrollGesturesEnabled = false
//                            googMap.uiSettings.isZoomGesturesEnabled = false
//                            val latLng = LatLng(mPage!!.latitude!!, mPage!!.longitude!!)
//                            googMap.addMarker(MarkerOptions().position(latLng).title(mPage!!.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)))
//                            googMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
//
//                            googMap.setOnMapClickListener {
//                                val intent = Intent(mContext, LocationPageActivity::class.java)
//                                intent.putExtra(Const.PAGE, mPage)
//                                (mContext as Activity).startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//                            }
//                        }
//                    })
//
//                    holder.image_map_full.setOnClickListener {
//                        val intent = Intent(mContext, LocationPageActivity::class.java)
//                        intent.putExtra(Const.PAGE, mPage)
//                        (mContext as Activity).startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//                    }
//                } else {
//                    holder.layout_map.visibility = View.GONE
//                }
//            }
//
//            if (mPage!!.properties == null) {
//                holder.layout_opening_hours.visibility = View.GONE
//                holder.layout_parking_info.visibility = View.GONE
//                holder.layout_convenience_info.visibility = View.GONE
//                holder.layout_kakao_id.visibility = View.GONE
//                holder.layout_email.visibility = View.GONE
//            } else {
//
//
//                if (StringUtils.isNotEmpty(mPage!!.properties!!.openingHours)) {
//                    holder.layout_opening_hours.visibility = View.VISIBLE
//                    holder.text_opening_hours.text = mPage!!.properties!!.openingHours
//                } else {
//                    holder.layout_opening_hours.visibility = View.GONE
//                }
//
//                if (StringUtils.isNotEmpty(mPage!!.properties!!.parkingInfo)) {
//                    holder.layout_parking_info.visibility = View.VISIBLE
//                    holder.text_parking_info.text = mPage!!.properties!!.parkingInfo
//                } else {
//                    holder.layout_parking_info.visibility = View.GONE
//                }
//
//                if (StringUtils.isNotEmpty(mPage!!.properties!!.convenienceInfo)) {
//                    holder.layout_convenience_info.visibility = View.VISIBLE
//                    holder.text_convenience_info.text = mPage!!.properties!!.convenienceInfo
//                } else {
//                    holder.layout_convenience_info.visibility = View.GONE
//                }
//
//                if (StringUtils.isNotEmpty(mPage!!.properties!!.kakaoId)) {
//                    holder.layout_kakao_id.visibility = View.VISIBLE
//                    holder.text_kakao_id.text = mPage!!.properties!!.kakaoId
//                    holder.image_kakao_id_copy.setOnClickListener {
//                        val clipboard = mContext?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                        val clip = ClipData.newPlainText(mContext?.getString(R.string.word_kakao_id), mPage!!.properties!!.kakaoId)
//                        clipboard.primaryClip = clip
//                        ToastUtil.show(mContext, R.string.msg_copied_clipboard)
//                    }
//                } else {
//                    holder.layout_kakao_id.visibility = View.GONE
//                }
//
//                if (StringUtils.isNotEmpty(mPage!!.properties!!.email)) {
//                    holder.layout_email.visibility = View.VISIBLE
//                    holder.text_email.text = mPage!!.properties!!.email
//                } else {
//                    holder.layout_email.visibility = View.GONE
//                }
//            }
//
//            holder.layout_find_road.setOnClickListener {
//                if (existDaummapApp()) {
//                    val uri = Uri.parse("daummaps://route?ep=${mPage!!.latitude},${mPage!!.longitude}&by=CAR");
//                    val intent = Intent(Intent.ACTION_VIEW, uri)
//                    mContext?.startActivity(intent)
//                } else {
//                    val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    mContext?.startActivity(intent)
//                }
//
//            }
//
//            holder.layout_call_taxi.setOnClickListener {
//                LogUtil.e("TAXI", "https://t.kakao.com/launch?type=taxi&dest_lat=${mPage!!.latitude}&dest_lng=${mPage!!.longitude}&ref=pplus\"")
//                val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${mPage!!.latitude}&dest_lng=${mPage!!.longitude}&ref=pplus")
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                mContext?.startActivity(intent)
//            }
//
//            holder.layout_navigation.setOnClickListener {
//                val destination = Location.newBuilder(mPage!!.name, mPage!!.longitude!!, mPage!!.latitude!!).build()
//                val builder = KakaoNaviParams.newBuilder(destination)
//                        .setNaviOptions(NaviOptions.newBuilder().setCoordType(CoordType.WGS84).build())
//                KakaoNaviService.getInstance().shareDestination(mContext, builder.build());
//            }
//
//            holder.layout_copy_address.setOnClickListener {
//                if (mPage!!.address != null && StringUtils.isNotEmpty(mPage!!.address!!.roadBase)) {
//                    var detailAddress = ""
//                    if (StringUtils.isNotEmpty(mPage!!.address!!.roadDetail)) {
//                        detailAddress = mPage!!.address!!.roadDetail
//                    }
//
//                    val clipboard = mContext?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//
//                    val clip = ClipData.newPlainText("address", "${mPage!!.address!!.roadBase} $detailAddress")
//                    clipboard.primaryClip = clip
//                    ToastUtil.show(mContext, R.string.msg_copied_clipboard)
//                } else {
//                    ToastUtil.showAlert(mContext, mContext?.getString(R.string.msg_not_exist_address))
//                }
//            }
//
//        } else if (holder is ViewHolder) {
//
//            val item = getItem(position - 1)
//
//            if (item.member != null) {
//                holder.text_name.text = item.member!!.nickname
//                if (item.member!!.profileAttachment != null) {
//                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
//                    Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
//                } else {
//                    holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
//                }
//            }
//
//            holder.text_contents.text = item.review
//
////            val eval = String.format("%.1f", item.eval!!.toFloat())
////            holder.grade_bar.build(eval)
//
//            try {
//                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
//                val c = Calendar.getInstance()
//                c.time = d
//
//                val year = c.get(Calendar.YEAR)
//                val month = c.get(Calendar.MONTH)
//                val day = c.get(Calendar.DAY_OF_MONTH)
//
//                if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
//                    val output = SimpleDateFormat("a HH:mm", Locale.getDefault())
//                    holder.text_regDate.text = output.format(d)
//                } else {
//                    val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
//                    holder.text_regDate.text = output.format(d)
//                }
//
//            } catch (e: Exception) {
//
//            }
//
//            if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
//                holder.layout_image.visibility = View.VISIBLE
//                holder.pager_image.visibility = View.VISIBLE
//                val imageAdapter = GoodsImagePagerAdapter(mContext!!)
//                imageAdapter.dataList = item.attachments!!.images!! as ArrayList<String>
//                holder.pager_image.adapter = imageAdapter
//                holder.indicator.visibility = View.VISIBLE
//                holder.indicator.removeAllViews()
//                holder.indicator.build(LinearLayout.HORIZONTAL, item.attachments!!.images!!.size)
//                holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                    }
//
//                    override fun onPageSelected(position: Int) {
//
//                        holder.indicator.setCurrentItem(position)
//                    }
//
//                    override fun onPageScrollStateChanged(state: Int) {
//
//                    }
//                })
//
////            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
//                imageAdapter.setListener(object : GoodsImagePagerAdapter.OnItemClickListener {
//                    override fun onItemClick(position: Int) {
//                        if (listener != null) {
//                            listener!!.onItemClick(holder.adapterPosition - 1)
//                        }
//                    }
//                })
//            } else {
//                holder.indicator.removeAllViews()
//                holder.indicator.visibility = View.GONE
//                holder.pager_image.visibility = View.GONE
//                holder.pager_image.adapter = null
//                holder.layout_image.visibility = View.GONE
//            }
//
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(holder.adapterPosition - 1)
//            }
//        }
//    }
//
//    fun existDaummapApp(): Boolean {
//        val pm = mContext!!.packageManager
//
//        try {
//            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNATURES) != null)
//        } catch (e: PackageManager.NameNotFoundException) {
//            return false;
//        }
//    }
//
//    private val onSnsClickListener = View.OnClickListener { v ->
//        val sns = v.tag as Sns
//        snsEvent(sns)
//    }
//
//    private fun snsEvent(sns: Sns) {
//        // SNS 페이지 이동
//        if (StringUtils.isNotEmpty(sns.url)) {
//            // 계정으로 이동
//            mContext?.startActivity(PplusCommonUtil.getOpenSnsIntent(mContext!!, SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.item_page_header, parent, false))
//        } else if (viewType == TYPE_ITEM) {
//            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_goods_review, parent, false))
//        }
//        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
//    }
//
//    private fun isPositionHeader(position: Int): Boolean {
//        return position == 0
//    }
//
//}