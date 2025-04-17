package com.pplus.prnumberbiz.apps.main.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.viewpager.widget.ViewPager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.logs.LogUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.data.GoodsImagePagerAdapter
import com.pplus.prnumberbiz.apps.goods.ui.GoodsDetailActivity
import com.pplus.prnumberbiz.apps.post.ui.PostActivity
import com.pplus.prnumberbiz.apps.shop.ui.LocationShopActivity
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.model.dto.GoodsReview
import com.pplus.prnumberbiz.core.network.model.dto.Page
import com.pplus.prnumberbiz.core.network.model.dto.Sns
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import kotlinx.android.synthetic.main.item_goods_review.view.*
import kotlinx.android.synthetic.main.item_home_header.view.*
import java.text.SimpleDateFormat


/**
 * Created by imac on 2018. 1. 8..
 */
class MyPageHeaderAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder> {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var mPage: Page? = null
    var mContext: Context? = null
    var mDataList: MutableList<GoodsReview>? = null
    var listener: OnItemClickListener? = null
    var mSnsList: List<Sns>? = null
    var mTodayYear = 0
    var mTodayMonth = 0
    var mTodayDay = 0
    var changePage = true

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }

    fun setPage(page: Page) {
        this.mPage = page
        changePage = true
        notifyItemChanged(0)
    }

    fun setSNS(snsList: List<Sns>?) {
        mSnsList = snsList
        notifyItemChanged(0)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): GoodsReview {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<GoodsReview>? {

        return mDataList
    }

    fun add(data: GoodsReview) {

        if (mDataList == null) {
            mDataList = ArrayList<GoodsReview>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<GoodsReview>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<GoodsReview>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: GoodsReview) {

        mDataList!![position] = data
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<GoodsReview>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<GoodsReview>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val text_page_name = itemView.text_my_page_header_name
        val text_post = itemView.text_my_page_header_post
        //        val text_facebook = itemView.text_home_facebook
//        val text_kakao = itemView.text_home_kakao
//        val text_twitter = itemView.text_home_twitter
//        val text_instagram = itemView.text_home_instagram
//        val text_blog = itemView.text_home_blog
//        val text_homepage = itemView.text_home_homepage
        val layout_introduction = itemView.layout_home_introduction
        val text_introduction = itemView.text_home_introduction
        val layout_mobile = itemView.layout_home_mobile_number
        val text_mobile = itemView.text_home_mobile_number
        val layout_opening_hours = itemView.layout_home_open_hours
        val text_opening_hours = itemView.text_home_opening_hours
        val layout_parking_info = itemView.layout_home_parking_info
        val text_parking_info = itemView.text_home_parking_info
        val layout_convenience_info = itemView.layout_home_convenience_info
        val text_convenience_info = itemView.text_home_convenience_info
        val layout_kakao_id = itemView.layout_home_kakao_id
        val text_kakao_id = itemView.text_home_kakao_id
        val layout_email = itemView.layout_home_email
        val text_email = itemView.text_home_email
        val layout_address = itemView.layout_home_address
        val text_address = itemView.text_home_address
        val layout_map = itemView.layout_home_map
        val map = itemView.map_home
        //        val container_map = itemView.container_home_map
        val image_map_full = itemView.image_home_map_full
        val text_total_review_count = itemView.text_page_goods_review_total_count
        val text_avg_grade = itemView.text_page_goods_review_avg_grade
        val grade_bar_avg = itemView.grade_bar_page_goods_review_avg

        init {
//            container_map.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val image_profile = itemView.image_goods_review_profile
        val text_name = itemView.text_goods_review_name
        val layout_image = itemView.layout_goods_review_image
        val pager_image = itemView.pager_goods_review_image
        val indicator = itemView.indicator_goods_review
        val text_contents = itemView.text_goods_review_contents
        val text_regDate = itemView.text_goods_review_regDate
        val grade_bar = itemView.grade_bar_goods_review

        init {

        }

    }

    override fun getItemCount(): Int {
        if (mPage == null) {
            return mDataList!!.size
        }
        return mDataList!!.size + 1
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {

            holder.text_page_name.text = mPage!!.name

            if (mPage!!.reviewCount != null) {
                holder.text_total_review_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_review, FormatUtil.getMoneyType(mPage!!.reviewCount.toString())))
            } else {
                holder.text_total_review_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_review, FormatUtil.getMoneyType("0")))
            }

            if (mPage!!.avgEval != null) {
                val avgEval = String.format("%.1f", mPage!!.avgEval)
                holder.text_avg_grade.text = avgEval
                holder.grade_bar_avg.build(avgEval)
            } else {
                holder.text_avg_grade.text = "0.0"
                holder.grade_bar_avg.build("0.0")
            }

            holder.text_post.setOnClickListener {
                val intent = Intent(mContext, PostActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                mContext?.startActivity(intent)
            }

//            val params = HashMap<String, String>()
//            params["no"] = mPage!!.no.toString()
//            ApiBuilder.create().getPageEval(params).setCallback(object : PplusCallback<NewResultResponse<PageEval>> {
//                override fun onResponse(call: Call<NewResultResponse<PageEval>>?, response: NewResultResponse<PageEval>?) {
//                    if(response != null && response.data != null){
//                        val pageEval = response.data
//
//                        holder.text_total_review_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_goods_review, FormatUtil.getMoneyType(pageEval.reviewCount.toString())))
//
//                        if(pageEval.avgEval != null){
//                            val avgEval = String.format("%.1f", pageEval.avgEval)
//                            holder.text_avg_grade.text = avgEval
//                            holder.grade_bar_avg.build(avgEval)
//                        }else{
//                            holder.text_avg_grade.text = "0.0"
//                            holder.grade_bar_avg.build("0.0")
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<PageEval>>?, t: Throwable?, response: NewResultResponse<PageEval>?) {
//
//                }
//            }).build().call()

//            holder.text_review.setOnClickListener {
//                val intent = Intent(mContext, ReviewActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                mContext?.startActivity(intent)
//            }

//            holder.text_twitter.isSelected = false
//            holder.text_facebook.isSelected = false
//            holder.text_instagram.isSelected = false
//            holder.text_kakao.isSelected = false
//            holder.text_blog.isSelected = false
//            holder.text_homepage.isSelected = false
//
//            val snsConfigClickListener = View.OnClickListener { v ->
//                val intent = Intent(mContext, SnsSyncActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                (mContext as BaseActivity).startActivityForResult(intent, Const.REQ_SYNC_SNS)
//            }
//
//            holder.text_twitter.setOnClickListener(snsConfigClickListener)
//            holder.text_facebook.setOnClickListener(snsConfigClickListener)
//            holder.text_instagram.setOnClickListener(snsConfigClickListener)
//            holder.text_kakao.setOnClickListener(snsConfigClickListener)
//            holder.text_blog.setOnClickListener(snsConfigClickListener)
//            holder.text_homepage.setOnClickListener(snsConfigClickListener)
//
//            if (mSnsList != null && !mSnsList!!.isEmpty()) {
//                for (sns in mSnsList!!) {
//                    if (StringUtils.isNotEmpty(sns.url)) {
//                        when (SnsTypeCode.valueOf(sns.type)) {
//
//                            SnsTypeCode.twitter -> {
//                                holder.text_twitter.isSelected = true
//                                holder.text_twitter.tag = sns
//                                holder.text_twitter.setOnClickListener(onSnsClickListener)
//                            }
//                            SnsTypeCode.facebook -> {
//                                holder.text_facebook.isSelected = true
//                                holder.text_facebook.tag = sns
//                                holder.text_facebook.setOnClickListener(onSnsClickListener)
//                            }
//                            SnsTypeCode.instagram -> {
//                                holder.text_instagram.isSelected = true
//                                holder.text_instagram.tag = sns
//                                holder.text_instagram.setOnClickListener(onSnsClickListener)
//                            }
//                            SnsTypeCode.kakao -> {
//                                holder.text_kakao.isSelected = true
//                                holder.text_kakao.tag = sns
//                                holder.text_kakao.setOnClickListener(onSnsClickListener)
//                            }
//                            SnsTypeCode.blog -> {
//                                holder.text_blog.isSelected = true
//                                holder.text_blog.tag = sns
//                                holder.text_blog.setOnClickListener(onSnsClickListener)
//                            }
//                            SnsTypeCode.homepage -> {
//                                holder.text_homepage.isSelected = true
//                                holder.text_homepage.tag = sns
//                                holder.text_homepage.setOnClickListener(onSnsClickListener)
//                            }
//                        }
//                    }
//                }
//            }

            if (StringUtils.isNotEmpty(mPage!!.catchphrase) || StringUtils.isNotEmpty(mPage!!.introduction)) {
                holder.layout_introduction.visibility = View.VISIBLE

                var introduce = ""
                if (StringUtils.isNotEmpty(mPage!!.introduction)) {
                    introduce = mPage!!.introduction!!
                }


                holder.text_introduction.text = "${mPage!!.catchphrase}\n${introduce}"
            } else {
                holder.layout_introduction.visibility = View.GONE
            }

            if (StringUtils.isNotEmpty(mPage!!.phone)) {
                holder.layout_mobile.visibility = View.VISIBLE
                holder.text_mobile.text = FormatUtil.getPhoneNumber(mPage!!.phone)
            } else {
                holder.layout_mobile.visibility = View.GONE
            }

            if (mPage!!.address == null || StringUtils.isEmpty(mPage!!.address!!.roadBase)) {
                holder.layout_address.visibility = View.GONE
            } else {
                holder.layout_address.visibility = View.VISIBLE
                var detailAddress = ""
                if (StringUtils.isNotEmpty(mPage!!.address!!.roadDetail)) {
                    detailAddress = mPage!!.address!!.roadDetail
                }
                holder.text_address.text = "${mPage!!.address!!.roadBase} $detailAddress"
            }

            if (mPage!!.properties == null) {
                holder.layout_opening_hours.visibility = View.GONE
                holder.layout_parking_info.visibility = View.GONE
                holder.layout_convenience_info.visibility = View.GONE
                holder.layout_kakao_id.visibility = View.GONE
                holder.layout_email.visibility = View.GONE
            } else {

                if (StringUtils.isNotEmpty(mPage!!.properties!!.openingHours)) {
                    holder.layout_opening_hours.visibility = View.VISIBLE
                    holder.text_opening_hours.text = mPage!!.properties!!.openingHours
                } else {
                    holder.layout_opening_hours.visibility = View.GONE
                }

                if (StringUtils.isNotEmpty(mPage!!.properties!!.parkingInfo)) {
                    holder.layout_parking_info.visibility = View.VISIBLE
                    holder.text_parking_info.text = mPage!!.properties!!.parkingInfo
                } else {
                    holder.layout_parking_info.visibility = View.GONE
                }

                if (StringUtils.isNotEmpty(mPage!!.properties!!.convenienceInfo)) {
                    holder.layout_convenience_info.visibility = View.VISIBLE
                    holder.text_convenience_info.text = mPage!!.properties!!.convenienceInfo
                } else {
                    holder.layout_convenience_info.visibility = View.GONE
                }

                if (StringUtils.isNotEmpty(mPage!!.properties!!.kakaoId)) {
                    holder.layout_kakao_id.visibility = View.VISIBLE
                    holder.text_kakao_id.text = mPage!!.properties!!.kakaoId
                } else {
                    holder.layout_kakao_id.visibility = View.GONE
                }

                if (StringUtils.isNotEmpty(mPage!!.properties!!.email)) {
                    holder.layout_email.visibility = View.VISIBLE
                    holder.text_email.text = mPage!!.properties!!.email
                } else {
                    holder.layout_email.visibility = View.GONE
                }
            }

            if (mPage!!.latitude != null && mPage!!.longitude != null) {

                holder.map.onCreate(null)
                holder.map.getMapAsync {
                    LogUtil.e("GOOGLEMAP", "onMapReady")
                    it.uiSettings.isScrollGesturesEnabled = false
                    it.uiSettings.isZoomGesturesEnabled = false
                    val latLng = LatLng(mPage!!.latitude!!, mPage!!.longitude!!)
                    it.addMarker(MarkerOptions().position(latLng).title(mPage!!.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location)))
                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))

                    it.setOnMapClickListener {
                        val intent = Intent(mContext, LocationShopActivity::class.java)
                        intent.putExtra(Const.PAGE, mPage)
                        (mContext as Activity).startActivityForResult(intent, Const.REQ_LOCATION_CODE)
                    }
                }

                holder.image_map_full.setOnClickListener {
                    val intent = Intent(mContext, LocationShopActivity::class.java)
                    intent.putExtra(Const.PAGE, mPage)
                    (mContext as Activity).startActivityForResult(intent, Const.REQ_LOCATION_CODE)
                }

                holder.layout_map.visibility = View.VISIBLE
            } else {
                holder.layout_map.visibility = View.GONE
            }

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            if (item.member != null) {
                holder.text_name.text = item.member!!.nickname
                if (item.member!!.profileAttachment != null) {
                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
                    Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
                } else {
                    holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
                }
            }

            holder.text_contents.text = item.review

            val eval = String.format("%.1f", item.eval!!.toFloat())
            holder.grade_bar.build(eval)

            try {
                val d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.regDatetime)
                val c = Calendar.getInstance()
                c.time = d

                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                if (mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
                    val output = SimpleDateFormat("a HH:mm", Locale.getDefault())
                    holder.text_regDate.text = output.format(d)
                } else {
                    val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                    holder.text_regDate.text = output.format(d)
                }

            } catch (e: Exception) {

            }

            if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
                holder.text_contents.maxLines = 2
                holder.layout_image.visibility = View.VISIBLE
                holder.pager_image.visibility = View.VISIBLE
                val imageAdapter = GoodsImagePagerAdapter(mContext!!)
                imageAdapter.dataList = item.attachments!!.images!! as ArrayList<String>
                holder.pager_image.adapter = imageAdapter
                holder.indicator.visibility = View.VISIBLE
                holder.indicator.removeAllViews()
                holder.indicator.build(LinearLayout.HORIZONTAL, item.attachments!!.images!!.size)
                holder.pager_image.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                    }

                    override fun onPageSelected(position: Int) {

                        holder.indicator.setCurrentItem(position)
                    }

                    override fun onPageScrollStateChanged(state: Int) {

                    }
                })

//            holder.pager_image.currentItem = if (viewPageStates.containsKey(position)) viewPageStates.get(position) else 0
                imageAdapter.setListener(object : GoodsImagePagerAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        if (listener != null) {
                            listener!!.onItemClick(holder.adapterPosition - 1)
                        }
                    }
                })
            } else {
                holder.indicator.removeAllViews()
                holder.indicator.visibility = View.GONE
                holder.pager_image.visibility = View.GONE
                holder.pager_image.adapter = null
                holder.layout_image.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                listener?.onItemClick(holder.adapterPosition)
            }
        }
    }

    private val onSnsClickListener = View.OnClickListener { v ->
        val sns = v.tag as Sns
        snsEvent(sns)
    }

    private fun snsEvent(sns: Sns) {
        // SNS 페이지 이동
        if (StringUtils.isNotEmpty(sns.url)) {
            // 계정으로 이동
            mContext?.startActivity(PplusCommonUtil.getOpenSnsIntent(mContext!!, SnsTypeCode.valueOf(sns.type), sns.url, sns.isLinkage))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.item_home_header, parent, false))
        } else if (viewType == TYPE_ITEM) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_goods_review, parent, false))
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

}