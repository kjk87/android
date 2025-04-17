//package com.pplus.prnumberuser.apps.goods.data
//
//import android.content.ClipData
//import android.content.ClipboardManager
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Paint
//import android.net.Uri
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.recyclerview.widget.RecyclerView
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
//import com.kakao.sdk.common.util.KakaoCustomTabsClient
//import com.kakao.sdk.navi.NaviClient
//import com.kakao.sdk.navi.model.CoordType
//import com.kakao.sdk.navi.model.Location
//import com.kakao.sdk.navi.model.NaviOption
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.goods.ui.GoodsDetailViewerActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsInfoActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsNoticeInfoActivity
//import com.pplus.prnumberuser.apps.goods.ui.GoodsRefundInfoActivity
//import com.pplus.prnumberuser.apps.page.data.GoodsReviewPagerAdapter
//import com.pplus.prnumberuser.core.network.model.dto.Goods
//import com.pplus.prnumberuser.core.network.model.dto.GoodsImage
//import com.pplus.prnumberuser.core.network.model.dto.GoodsReview
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import kotlinx.android.synthetic.main.header_goods.view.*
//import kotlinx.android.synthetic.main.item_goods_review.view.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class GoodsHeaderReviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mGoods: Goods? = null
//    var mContext: Context? = null
//    var mDataList: MutableList<GoodsReview>? = null
//    var listener: OnItemClickListener? = null
//    var mTotalCount = 0
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//    }
//
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
//        val pager = itemView.pager_header_goods_image
//        val indicator = itemView.indicator_header_goods
//        val text_goods_name = itemView.text_header_goods_name
//        val text_discount_ratio = itemView.text_header_goods_discount_ratio
//        val text_origin_price = itemView.text_header_goods_origin_price
//        val text_sale_price = itemView.text_header_goods_sale_price
//        val text_remain_count = itemView.text_header_goods_remain_count
//        val text_contents = itemView.text_header_goods_contents
//        val layout_expire_date = itemView.layout_header_goods_expire_date
//        val text_expire_date = itemView.text_header_goods_expire_date
//        val text_dayofweeks = itemView.text_header_goods_dayofweeks
//        val text_use_time = itemView.text_header_goods_use_time
//        val layout_point = itemView.layout_header_goods_point
//        val text_point = itemView.text_header_goods_point
//        val text_goods_notice_info = itemView.text_header_goods_notice_info
//        val text_refund_exchange_info = itemView.text_header_refund_exchange_info
//        val text_buy_warning = itemView.text_header_buy_warning
//        val text_page_name = itemView.text_header_goods_page_name
//        val layout_page_go = itemView.layout_header_goods_page_go
//        val layout_page_phone = itemView.layout_header_goods_page_phone
//        val text_page_phone = itemView.text_header_goods_page_phone
//        val text_page_call = itemView.text_header_goods_page_call
//        val layout_page_open_hours = itemView.layout_header_goods_page_open_hours
//        val text_page_opening_hours = itemView.text_header_goods_page_opening_hours
//        val layout_page_holiday = itemView.layout_header_goods_page_holiday
//        val text_page_holiday = itemView.text_header_goods_page_holiday
//        val layout_page_parking = itemView.layout_header_goods_page_parking
//        val text_page_parking = itemView.text_header_goods_page_parking
//        val layout_page_address = itemView.layout_header_goods_page_address
//        val text_page_address = itemView.text_header_goods_page_address
//        val layout_page_map = itemView.layout_header_goods_page_map
//        val map_page = itemView.map_header_goods_page
//        val layout_page_map_option = itemView.layout_header_goods_page_map_option
//        val layout_page_find_road = itemView.layout_header_goods_page_find_road
//        val layout_page_call_taxi = itemView.layout_header_goods_page_call_taxi
//        val layout_page_navigation = itemView.layout_header_goods_page_navigation
//        val layout_page_copy_address = itemView.layout_header_goods_page_copy_address
//        val text_page_review_count = itemView.text_header_goods_review_count
//        val text_page_review_grade = itemView.text_header_goods_page_review_grade
//        val grade_bar_page_review_total = itemView.grade_bar_header_goods_page_review_total
//
//        init {
//            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val image_profile = itemView.image_goods_review_profile
//        val text_name = itemView.text_goods_review_name
//        val layout_image = itemView.layout_goods_review_image
//        val pager_image = itemView.pager_goods_review_image
//        val indicator = itemView.indicator_goods_review
//        val text_contents = itemView.text_goods_review_contents
//        val text_regDate = itemView.text_goods_review_regDate
//        val grade_bar = itemView.grade_bar_goods_review
//        val layout_reply = itemView.layout_goods_review_reply
//        val text_reply = itemView.text_goods_review_reply
//        val text_reply_date = itemView.text_goods_review_reply_date
//
//        init {
//        }
//    }
//
//    override fun getItemCount(): Int {
//        if (mGoods == null) {
//            return mDataList!!.size
//        }
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//            val item = mGoods!!
//            if (item.goodsImageList != null && item.goodsImageList!!.isNotEmpty()) {
//
//                if (item.goodsImageList!!.size > 1) {
//                    holder.indicator.visibility = View.VISIBLE
//                } else {
//                    holder.indicator.visibility = View.GONE
//                }
//
//                val imageAdapter = GoodsImagePagerAdapter(mContext!!)
//                imageAdapter.dataList = item.goodsImageList!! as ArrayList<GoodsImage>
//
//                holder.pager.adapter = imageAdapter
//                holder.indicator.removeAllViews()
//                holder.indicator.build(LinearLayout.HORIZONTAL, item.goodsImageList!!.size)
//                holder.pager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
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
//                imageAdapter.mListener = object : GoodsImagePagerAdapter.OnItemClickListener {
//                    override fun onItemClick(position: Int) {
//                        val intent = Intent(holder.itemView.context, GoodsDetailViewerActivity::class.java)
//                        intent.putExtra(Const.POSITION, holder.pager.currentItem)
//                        intent.putExtra(Const.DATA, imageAdapter.dataList)
//                        holder.itemView.context.startActivity(intent)
//                    }
//
//                }
//
//            }
//
//            if (item.count != -1) {
//                var soldCount = 0
//                if (item.soldCount != null) {
//                    soldCount = item.soldCount!!
//                }
//                holder.text_remain_count.visibility = View.VISIBLE
//                holder.text_remain_count.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_goods_remain_count, FormatUtil.getMoneyType((item.count!! - soldCount).toString())))
//            } else {
//                holder.text_remain_count.visibility = View.GONE
//            }
//
//            holder.text_goods_name.text = item.name
//
//            if (item.originPrice != null && item.originPrice!! > 0) {
//
//                if (item.originPrice!! <= item.price!!) {
//                    holder.text_origin_price.visibility = View.GONE
//                } else {
//                    holder.text_origin_price.text = holder.itemView.context.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
//                    holder.text_origin_price.visibility = View.VISIBLE
//                }
//
//            } else {
//                holder.text_origin_price.visibility = View.GONE
//            }
//
//            if (item.discountRatio != null && item.discountRatio!! > 0) {
//                holder.text_discount_ratio.visibility = View.VISIBLE
//                holder.text_discount_ratio.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_percent_unit, item.discountRatio!!.toInt().toString()))
//            } else {
//                holder.text_discount_ratio.visibility = View.GONE
//            }
//            holder.text_sale_price.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))
//            holder.text_contents.text = item.description
//
//            if (StringUtils.isNotEmpty(item.expireDatetime)) {
//                holder.layout_expire_date.visibility = View.VISIBLE
//                holder.text_expire_date.text = SimpleDateFormat("yyyy.MM.dd").format(DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.expireDatetime)) + " " + holder.itemView.context.getString(R.string.word_until)
//            } else {
//                holder.layout_expire_date.visibility = View.GONE
//            }
//
//            if (item.allDays != null) {
//                if (item.allDays!!) {
//                    holder.text_use_time.text = holder.itemView.context.getString(R.string.word_every_time_use)
//                } else {
//                    if(StringUtils.isNotEmpty(item.startTime) && StringUtils.isNotEmpty(item.endTime)){
//                        holder.text_use_time.text = item.startTime + "~" + item.endTime
//                    }else{
//                        holder.text_use_time.text = holder.itemView.context.getString(R.string.word_every_time_use)
//                    }
//                }
//            } else {
//                holder.text_use_time.text = holder.itemView.context.getString(R.string.word_every_time_use)
//            }
//
//            if (item.allWeeks != null) {
//                if (item.allWeeks!!) {
//                    holder.text_dayofweeks.text = holder.itemView.context.getString(R.string.word_every_dayofweek)
//                } else {
//                    if(StringUtils.isNotEmpty(item.dayOfWeeks)){
//                        val dayOfWeek = item.dayOfWeeks!!.replace(",", "/").replace("0", holder.itemView.context.getString(R.string.word_mon)).replace("1", holder.itemView.context.getString(R.string.word_tue))
//                                .replace("2", holder.itemView.context.getString(R.string.word_wed)).replace("3", holder.itemView.context.getString(R.string.word_thu)).replace("4", holder.itemView.context.getString(R.string.word_fri))
//                                .replace("5", holder.itemView.context.getString(R.string.word_sat)).replace("6", holder.itemView.context.getString(R.string.word_sun))
//
//                        holder.text_dayofweeks.text = dayOfWeek
//                    }else{
//                        holder.text_dayofweeks.text = holder.itemView.context.getString(R.string.word_every_dayofweek)
//                    }
//                }
//            } else {
//                holder.text_dayofweeks.text = holder.itemView.context.getString(R.string.word_every_dayofweek)
//            }
//
//            if(item.page!!.point != null && item.page!!.point!! > 0){
//                holder.layout_point.visibility = View.VISIBLE
////            holder.text_point.text = PplusCommonUtil.fromHtml(holder.itemView.context.getString(R.string.html_reward_point4, "${item.page!!.point!!}%"))
//                val point = (item.price!!*(item.page!!.point!!/100f)).toInt()
//                holder.text_point.text = holder.itemView.context.getString(R.string.format_point_with_desc, FormatUtil.getMoneyType(point.toString()))
//            }else{
//                holder.layout_point.visibility = View.GONE
//            }
//
//            holder.text_goods_notice_info.setOnClickListener {
//                val intent = Intent(holder.itemView.context, GoodsNoticeInfoActivity::class.java)
//                intent.putExtra(Const.DATA, item)
//                intent.putExtra(Const.KEY, Const.REFUND)
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_refund_exchange_info.setOnClickListener {
//                val intent = Intent(holder.itemView.context, GoodsRefundInfoActivity::class.java)
//                intent.putExtra(Const.DATA, item)
//                intent.putExtra(Const.KEY, Const.REFUND)
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_buy_warning.setOnClickListener {
//                val intent = Intent(holder.itemView.context, GoodsInfoActivity::class.java)
//                intent.putExtra(Const.KEY, Const.WARNING)
//                intent.putExtra(Const.GOODS, item)
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.text_page_name.text = item.page!!.name
//            holder.layout_page_go.setOnClickListener {
//                val location = IntArray(2)
//                it.getLocationOnScreen(location)
//                val x = location[0] + it.width / 2
//                val y = location[1] + it.height / 2
//                PplusCommonUtil.goPage(holder.itemView.context, item.page!!, x, y)
//            }
//
//            if (StringUtils.isNotEmpty(item.page!!.phone)) {
//                holder.layout_page_phone.visibility = View.VISIBLE
//                holder.text_page_phone.text = FormatUtil.getPhoneNumber(item.page!!.phone)
//                holder.text_page_phone.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.page!!.phone!!))
//                    holder.itemView.context.startActivity(intent)
//                }
//
//                holder.text_page_call.setOnClickListener {
//                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.page!!.phone!!))
//                    holder.itemView.context.startActivity(intent)
//                }
//            } else {
//                holder.layout_page_phone.visibility = View.GONE
//            }
//
//            holder.text_page_review_count.text = holder.itemView.context.getString(R.string.format_review_count, FormatUtil.getMoneyType(mTotalCount.toString()))
//
//            if (StringUtils.isNotEmpty(item.page!!.openHours)) {
//                holder.layout_page_open_hours.visibility = View.VISIBLE
//                holder.text_page_opening_hours.text = item.page!!.openHours
//            } else {
//                holder.layout_page_open_hours.visibility = View.GONE
//            }
//
//            if (StringUtils.isNotEmpty(item.page!!.holiday)) {
//                holder.layout_page_holiday.visibility = View.VISIBLE
//                holder.text_page_holiday.text = item.page!!.holiday
//            } else {
//                holder.layout_page_holiday.visibility = View.GONE
//            }
//
//            if (item.page!!.isParkingAvailable!!) {
//
//                if (item.page!!.isValetParkingAvailable!!) {
//                    holder.text_page_parking.text = "${holder.itemView.context.getString(R.string.word_parking_enable)}\n${holder.itemView.context.getString(R.string.word_valet_parking_enable)}"
//                } else {
//                    holder.text_page_parking.text = "${holder.itemView.context.getString(R.string.word_parking_enable)}\n${holder.itemView.context.getString(R.string.word_valet_parking_disable)}"
//                }
//            } else {
//                holder.text_page_parking.text = holder.itemView.context.getString(R.string.word_parking_disable)
//            }
//
//            if (StringUtils.isEmpty(item.page!!.roadAddress)) {
//                holder.layout_page_address.visibility = View.GONE
//                holder.layout_page_map.visibility = View.GONE
//                holder.layout_page_map_option.visibility = View.GONE
//            } else {
//                holder.layout_page_address.visibility = View.VISIBLE
//                var detailAddress = ""
//                if (StringUtils.isNotEmpty(item.page!!.roadDetailAddress)) {
//                    detailAddress = item.page!!.roadDetailAddress!!
//                }
//                holder.text_page_address.text = "${item.page!!.roadAddress} $detailAddress"
//
//                if (item.page!!.latitude != null && item.page!!.longitude != null) {
//                    holder.layout_page_map.visibility = View.VISIBLE
//
//                    holder.map_page.onCreate(null)
//                    holder.map_page.getMapAsync(object : OnMapReadyCallback {
//                        override fun onMapReady(googMap: GoogleMap) {
//                            LogUtil.e("GOOGLEMAP", "onMapReady")
//                            googMap.uiSettings.isScrollGesturesEnabled = false
//                            googMap.uiSettings.isZoomGesturesEnabled = false
//                            val latLng = LatLng(item.page!!.latitude!!, item.page!!.longitude!!)
//                            googMap.addMarker(MarkerOptions().position(latLng).title(item.page!!.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)))
//                            googMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
//
////                            googMap.setOnMapClickListener {
////                                val intent = Intent(activity, LocationPageActivity::class.java)
////                                intent.putExtra(Const.PAGE, item.page!!)
////                                activity?.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
////                            }
//                        }
//                    })
//                }
//            }
//
//            holder.layout_page_find_road.setOnClickListener {
//                if (existDaummapApp(holder.itemView.context)) {
//                    val uri = Uri.parse("daummaps://route?ep=${item.page!!.latitude},${item.page!!.longitude}&by=CAR");
//                    val intent = Intent(Intent.ACTION_VIEW, uri)
//                    holder.itemView.context.startActivity(intent)
//                } else {
//                    val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=net.daum.android.map"))
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    holder.itemView.context.startActivity(intent)
//                }
//
//            }
//
//            holder.layout_page_call_taxi.setOnClickListener {
//                val uri = Uri.parse("https://t.kakao.com/launch?type=taxi&dest_lat=${item.page!!.latitude}&dest_lng=${item.page!!.longitude}&ref=pplus")
//                val intent = Intent(Intent.ACTION_VIEW, uri)
//                holder.itemView.context.startActivity(intent)
//            }
//
//            holder.layout_page_navigation.setOnClickListener {
//                if (NaviClient.instance.isKakaoNaviInstalled(holder.itemView.context)) {
//                    LogUtil.e("kakonavi", "카카오내비 앱으로 길안내 가능")
//                    holder.itemView.context.startActivity(
//                            NaviClient.instance.navigateIntent(
//                                    Location(item.page!!.name!!, item.page!!.longitude.toString(), item.page!!.latitude.toString()),
//                                    NaviOption(coordType = CoordType.WGS84)
//                            )
//                    )
//                } else {
//                    LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")
//
//                    // 웹 브라우저에서 길안내
//                    // 카카오내비가 설치되지 않은 곳에서 활용할 수 있습니다.
//                    val uri =
//                            NaviClient.instance.navigateWebUrl(
//                                    Location(item.page!!.name!!, item.page!!.longitude.toString(), item.page!!.latitude.toString()),
//                                    NaviOption(coordType = CoordType.WGS84)
//                            )
//
//                    // CustomTabs로 길안내
//                    KakaoCustomTabsClient.openWithDefault(holder.itemView.context, uri)
//                }
//            }
//
//            holder.layout_page_copy_address.setOnClickListener {
//                if (StringUtils.isNotEmpty(item.page!!.roadAddress)) {
//                    var detailAddress = ""
//                    if (StringUtils.isNotEmpty(item.page!!.roadDetailAddress)) {
//                        detailAddress = item.page!!.roadDetailAddress!!
//                    }
//
//                    val clipboard = holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//
//                    val clip = ClipData.newPlainText("address", "${item.page!!.roadAddress} $detailAddress")
//                    clipboard.setPrimaryClip(clip)
//                    ToastUtil.show(holder.itemView.context, R.string.msg_copied_clipboard)
//                } else {
//                    ToastUtil.showAlert(holder.itemView.context, holder.itemView.context.getString(R.string.msg_not_exist_address))
//                }
//            }
//
//            if (item.page!!.avgEval != null) {
//                val avgEval = String.format("%.1f", item.page!!.avgEval)
//                holder.text_page_review_grade.text = avgEval
//                holder.grade_bar_page_review_total.build(avgEval)
//            } else {
//                holder.text_page_review_grade.text = "0.0"
//                holder.grade_bar_page_review_total.build("0.0")
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
//            if (StringUtils.isNotEmpty(item.reviewReply)) {
//                holder.layout_reply.visibility = View.VISIBLE
//                holder.text_reply.text = item.reviewReply
//                if(StringUtils.isNotEmpty(item.reviewReplyDate)){
//                    holder.text_reply_date.text = PplusCommonUtil.getDateFormat(item.reviewReplyDate!!)
//                }
//
//            } else {
//                holder.layout_reply.visibility = View.GONE
//            }
//
//            if (item.eval != null) {
//                val eval = String.format("%.1f", item.eval!!.toFloat())
//                holder.grade_bar.build(eval)
//            } else {
//                val eval = String.format("%.1f", 0f)
//                holder.grade_bar.build(eval)
//            }
//
//            try {
//                holder.text_regDate.text = PplusCommonUtil.getDateFormat(item.regDatetime!!)
//
//            } catch (e: Exception) {
//
//            }
//
//            if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
//                holder.layout_image.visibility = View.VISIBLE
//                holder.pager_image.visibility = View.VISIBLE
//                val imageAdapter = GoodsReviewPagerAdapter(mContext!!)
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
//                imageAdapter.setListener(object : GoodsReviewPagerAdapter.OnItemClickListener {
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
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.header_goods, parent, false))
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
//    private fun existDaummapApp(context: Context): Boolean {
//        val pm = context.packageManager
//
//        try {
//            return (pm.getPackageInfo("net.daum.android.map", PackageManager.GET_SIGNING_CERTIFICATES) != null)
//        } catch (e: PackageManager.NameNotFoundException) {
//            return false;
//        }
//    }
//}