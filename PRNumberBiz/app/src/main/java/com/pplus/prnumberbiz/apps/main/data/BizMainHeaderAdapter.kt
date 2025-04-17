package com.pplus.prnumberbiz.apps.main.data

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.PointF
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.nightonke.boommenu.BoomButtons.HamButton
import com.nightonke.boommenu.BoomMenuButton
import com.pple.pplus.utils.part.apps.resource.ResourceUtil
import com.pple.pplus.utils.part.format.FormatUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pple.pplus.utils.part.utils.time.DateFormatUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.goods.ui.GoodsRegActivity2
import com.pplus.prnumberbiz.apps.goods.ui.GoodsReviewActivity
import com.pplus.prnumberbiz.apps.goods.ui.PlusGoodsDetailActivity
import com.pplus.prnumberbiz.apps.pages.ui.OperationInfoActivity
import com.pplus.prnumberbiz.apps.post.ui.PostActivity
import com.pplus.prnumberbiz.apps.sale.ui.SaleHistoryActivity
import com.pplus.prnumberbiz.apps.sale.ui.SaleOrderProcessyActivity
import com.pplus.prnumberbiz.core.code.common.EnumData
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.Count
import com.pplus.prnumberbiz.core.network.model.dto.Goods
import com.pplus.prnumberbiz.core.network.model.dto.Sns
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import com.pplus.prnumberbiz.core.network.model.response.SubResultResponse
import com.pplus.prnumberbiz.core.util.PplusCommonUtil
import com.pplus.prnumberbiz.core.util.ToastUtil
import kotlinx.android.synthetic.main.item_biz_main_footer.view.*
import kotlinx.android.synthetic.main.item_biz_main_header.view.*
import kotlinx.android.synthetic.main.item_goods.view.*
import network.common.PplusCallback
import retrofit2.Call
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class BizMainHeaderAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder> {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1
    private val TYPE_FOOTER = 2

    //    var mPage: Page? = null
    var mContext: Context? = null
    var mDataList: MutableList<Goods>? = null
    var listener: OnItemClickListener? = null
    //    var mSnsList: List<Sns>? = null
    var mTodayYear = 0
    var mTodayMonth = 0
    var mTodayDay = 0
    var mTotalCount = 0

    interface OnItemClickListener {

        fun onItemClick(position: Int)
        fun refresh()
    }

    constructor(context: Context) : super() {
        this.mContext = context
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }

//    fun setSNS(snsList: List<Sns>?) {
//        mSnsList = snsList
//        notifyItemChanged(0)
//    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): Goods {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<Goods>? {

        return mDataList
    }

    fun add(data: Goods) {

        if (mDataList == null) {
            mDataList = ArrayList<Goods>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Goods>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<Goods>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: Goods) {

        mDataList!![position] = data
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<Goods>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<Goods>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val layout_biz_main_post = itemView.layout_biz_main_post
        val text_biz_main_post_count = itemView.text_biz_main_post_count
        val layout_biz_main_sale_history = itemView.layout_biz_main_sale_history
        val text_biz_main_sale_count = itemView.text_biz_main_sale_count
        val text_biz_main_review_count = itemView.text_biz_main_review_count
        val layout_biz_main_review = itemView.layout_biz_main_review
        val layout_biz_main_not_exist_hot_deal = itemView.layout_biz_main_not_exist_hot_deal
        val text_biz_main_hot_deal_reg = itemView.text_biz_main_hot_deal_reg
        val layout_biz_main_hot_deal = itemView.layout_biz_main_hot_deal
        val image_biz_main_hotdeal_image = itemView.image_biz_main_hotdeal_image
        val layout_biz_main_hotdeal_sold_status = itemView.layout_biz_main_hotdeal_sold_status
        val text_biz_main_hotdeal_sold_status = itemView.text_biz_main_hotdeal_sold_status
        val text_biz_main_hotdeal_name = itemView.text_biz_main_hotdeal_name
        val text_biz_main_hotdeal_sale_price = itemView.text_biz_main_hotdeal_sale_price
        val text_biz_main_hotdeal_origin_price = itemView.text_biz_main_hotdeal_origin_price
        val text_biz_main_hotdeal_remain_count = itemView.text_biz_main_hotdeal_remain_count
        val image_biz_main_hotdeal_more = itemView.image_biz_main_hotdeal_more
        val layout_biz_main_not_exist_plus_goods = itemView.layout_biz_main_not_exist_plus_goods
        val text_biz_main_hot_plus_goods = itemView.text_biz_main_hot_plus_goods
        val layout_biz_main_plus_goods = itemView.layout_biz_main_plus_goods
        val text_biz_main_plus_goods_reg = itemView.text_biz_main_plus_goods_reg

        init {
            text_biz_main_hotdeal_origin_price.paintFlags = text_biz_main_hotdeal_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            val params = HashMap<String, String>()
            params["boardNo"] = "" + LoginInfoManager.getInstance().user.page!!.prBoard!!.getNo()!!
            ApiBuilder.create().getBoardPostCount(params).setCallback(object : PplusCallback<NewResultResponse<Int>> {

                override fun onResponse(call: Call<NewResultResponse<Int>>, response: NewResultResponse<Int>) {
                    val count = response.data
                    text_biz_main_post_count?.text = FormatUtil.getMoneyType(count.toString())
                }
                override fun onFailure(call: Call<NewResultResponse<Int>>, t: Throwable, response: NewResultResponse<Int>) {

                }
            }).build().call()

            val params2 = HashMap<String, String>()

            params2["pageSeqNo"] = LoginInfoManager.getInstance().user.page!!.no.toString()
            if (LoginInfoManager.getInstance().user.page!!.type == EnumData.PageTypeCode.store.name) {
                params2["type"] = "0"
            } else {
                params2["type"] = "1"
            }

            ApiBuilder.create().getBuyCount(params2).setCallback(object : PplusCallback<NewResultResponse<Count>> {
                override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
                    if (response != null) {
                        val count = response.data.count
                        text_biz_main_sale_count?.text = FormatUtil.getMoneyType(count.toString())

                    }
                }

                override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {
                }
            }).build().call()
        }
    }

    class ViewFooter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val layout_btn = itemView.layout_biz_main_footer_company_info_btn
        val text_btn = itemView.text_biz_main_footer_company_info_btn
        val layout_company_info = itemView.layout_biz_main_footer_company_info
        init {

        }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        val image = itemView.image_goods_image
        val text_name = itemView.text_goods_name
        val text_origin_price = itemView.text_goods_origin_price
        val text_sale_price = itemView.text_goods_sale_price
        val text_reward = itemView.text_goods_reward
        val text_remain_count = itemView.text_goods_remain_count
        val layout_sold_status = itemView.layout_goods_sold_status
        val text_sold_status = itemView.text_goods_sold_status
        val image_goods_more = itemView.image_goods_more
        val view_bar = itemView.view_goods_bar
//        var text_send = itemView.text_goods_send
//        var view_bar2 = itemView.view_goods_bar2

        init {
            text_origin_price.paintFlags = text_origin_price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//            view_bar.visibility = View.GONE
//            text_send.visibility = View.VISIBLE
//            view_bar2.visibility = View.VISIBLE

        }
    }

    override fun getItemCount(): Int {
        return mDataList!!.size + 2
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {
            val page = LoginInfoManager.getInstance().user.page!!

            holder.layout_biz_main_post.setOnClickListener {
                val intent = Intent(holder.itemView.context, PostActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                (holder.itemView.context as BaseActivity).startActivity(intent)
            }

            holder.layout_biz_main_sale_history.setOnClickListener {
                if (page.type == EnumData.PageTypeCode.store.name) {
                    val intent = Intent(holder.itemView.context, SaleOrderProcessyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_SALE_HISTORY)
                } else {
                    val intent = Intent(holder.itemView.context, SaleHistoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_SALE_HISTORY)
                }
            }

            holder.text_biz_main_review_count.text = FormatUtil.getMoneyType(page.reviewCount.toString())

            holder.layout_biz_main_review.setOnClickListener {
                val intent = Intent(holder.itemView.context, GoodsReviewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                holder.itemView.context.startActivity(intent)
            }

            val params = HashMap<String, String>()
            params["pageSeqNo"] = page.no.toString()
            params["isHotdeal"] = "true"
            params["page"] = "0"
            params["sort"] = "${EnumData.GoodsSort.seqNo.name},${EnumData.GoodsSort.desc}"
            ApiBuilder.create().getGoods(params).setCallback(object : PplusCallback<NewResultResponse<SubResultResponse<Goods>>> {

                override fun onResponse(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, response: NewResultResponse<SubResultResponse<Goods>>?) {

                    if (response != null) {

                        if (response.data.content!!.isNotEmpty()) {
                            holder.layout_biz_main_not_exist_hot_deal.visibility = View.GONE
                            holder.layout_biz_main_hot_deal.visibility = View.VISIBLE
                            val hotDealGoods = response.data.content!![0]

                            holder.layout_biz_main_hot_deal.setOnClickListener {
                                val intent = Intent(holder.itemView.context, PlusGoodsDetailActivity::class.java)
                                intent.putExtra(Const.TYPE, EnumData.GoodsType.hotdeal.name)
                                intent.putExtra(Const.DATA, hotDealGoods)
                                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_DETAIL)
                            }

                            holder.text_biz_main_hotdeal_name.text = hotDealGoods.name

                            if (hotDealGoods.originPrice != null && hotDealGoods.originPrice!! > 0) {

                                if (hotDealGoods.originPrice!! <= hotDealGoods.price!!) {
                                    holder.text_biz_main_hotdeal_origin_price.visibility = View.GONE
                                } else {
                                    holder.text_biz_main_hotdeal_origin_price.text = mContext!!.getString(R.string.format_money_unit, FormatUtil.getMoneyType(hotDealGoods.originPrice.toString()))
                                    holder.text_biz_main_hotdeal_origin_price.visibility = View.VISIBLE
                                }

                            } else {
                                holder.text_biz_main_hotdeal_origin_price.visibility = View.GONE
                            }

                            holder.text_biz_main_hotdeal_sale_price.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_money_unit, FormatUtil.getMoneyType(hotDealGoods.price.toString())))


                            if (hotDealGoods.count != null && hotDealGoods.count != -1) {
                                var soldCount = 0
                                if (hotDealGoods.soldCount != null) {
                                    soldCount = hotDealGoods.soldCount!!
                                }
                                holder.text_biz_main_hotdeal_remain_count.visibility = View.VISIBLE
                                holder.text_biz_main_hotdeal_remain_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_remain_count, FormatUtil.getMoneyType((hotDealGoods.count!! - soldCount).toString())))
                            } else {
                                holder.text_biz_main_hotdeal_remain_count.visibility = View.GONE
                            }

                            if (hotDealGoods.attachments != null && hotDealGoods.attachments!!.images != null && hotDealGoods.attachments!!.images!!.isNotEmpty()) {
                                if (mContext != null) {
                                    val id = hotDealGoods.attachments!!.images!![0]
                                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
                                    Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image_biz_main_hotdeal_image)
                                }

                            } else {
                                holder.image_biz_main_hotdeal_image.setImageResource(R.drawable.prnumber_default_img)
                            }

                            holder.image_biz_main_hotdeal_more.setOnClickListener {
                                val builder = AlertBuilder.Builder()
                                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)

                                when (hotDealGoods.status) {
                                    EnumData.GoodsStatus.ing.status -> {
                                        builder.setContents(holder.itemView.context.getString(R.string.word_sold_stop), holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
                                    }
                                    EnumData.GoodsStatus.stop.status -> {
                                        builder.setContents(holder.itemView.context.getString(R.string.word_sold_resume), holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
                                    }
                                    else -> {
                                        builder.setContents(holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
                                    }
                                }

                                builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel))
                                builder.setOnAlertResultListener(object : OnAlertResultListener {

                                    override fun onCancel() {

                                    }

                                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                                        when (event_alert) {

                                            AlertBuilder.EVENT_ALERT.LIST -> {

                                                when (hotDealGoods.status) {
                                                    EnumData.GoodsStatus.ing.status, EnumData.GoodsStatus.stop.status -> {
                                                        when (event_alert.value) {
                                                            1 -> {
                                                                putGoodsStatus(hotDealGoods, 0)
                                                            }
                                                            2 -> {
                                                                val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                                                                intent.putExtra(Const.TYPE, EnumData.GoodsType.hotdeal.name)
                                                                intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                                                intent.putExtra(Const.DATA, hotDealGoods)
                                                                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
                                                            }
                                                            3 -> {
                                                                delete(hotDealGoods)
                                                            }
                                                        }
                                                    }
                                                    else -> {
                                                        when (event_alert.value) {
                                                            1 -> {
                                                                val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                                                                intent.putExtra(Const.TYPE, EnumData.GoodsType.hotdeal.name)
                                                                intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                                                intent.putExtra(Const.DATA, hotDealGoods)
                                                                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
                                                            }
                                                            2 -> {
                                                                delete(hotDealGoods)
                                                            }
                                                        }
                                                    }
                                                }


                                            }
                                        }
                                    }
                                }).builder().show(holder.itemView.context)
                            }


                            var ing = false

                            if (hotDealGoods.status == EnumData.GoodsStatus.soldout.status) {
                                holder.layout_biz_main_hotdeal_sold_status.visibility = View.VISIBLE
                                holder.text_biz_main_hotdeal_sold_status.setText(R.string.word_sold_out)
                                holder.text_biz_main_hotdeal_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
                                holder.text_biz_main_hotdeal_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                                holder.image_biz_main_hotdeal_more.visibility = View.GONE
                            } else if (hotDealGoods.status == EnumData.GoodsStatus.finish.status) {
                                holder.layout_biz_main_hotdeal_sold_status.visibility = View.VISIBLE
                                holder.text_biz_main_hotdeal_sold_status.setText(R.string.word_sold_finish)
                                holder.text_biz_main_hotdeal_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
                                holder.text_biz_main_hotdeal_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                                holder.image_biz_main_hotdeal_more.visibility = View.GONE
                            } else if (hotDealGoods.status == EnumData.GoodsStatus.stop.status) {
                                holder.layout_biz_main_hotdeal_sold_status.visibility = View.VISIBLE
                                holder.text_biz_main_hotdeal_sold_status.setText(R.string.word_sold_stop)
                                holder.text_biz_main_hotdeal_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.color_ff4646))
                                holder.text_biz_main_hotdeal_sold_status.setBackgroundResource(R.drawable.border_color_ff4646_2px)
                                holder.image_biz_main_hotdeal_more.visibility = View.VISIBLE
                            } else {

                                //"expireDatetime":"2018-10-18 15:00:59"

                                if (StringUtils.isNotEmpty(hotDealGoods.expireDatetime)) {
                                    val format = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
                                    var date = Date()
                                    try {
                                        date = format.parse(hotDealGoods.expireDatetime)
                                    } catch (e: ParseException) {
                                        e.printStackTrace()
                                    }

                                    val currentMillis = System.currentTimeMillis()
                                    if (date.time <= currentMillis) {
                                        holder.layout_biz_main_hotdeal_sold_status.visibility = View.VISIBLE
                                        holder.text_biz_main_hotdeal_sold_status.setText(R.string.word_sold_finish)
                                        holder.text_biz_main_hotdeal_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
                                        holder.text_biz_main_hotdeal_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                                        holder.image_biz_main_hotdeal_more.visibility = View.GONE
                                    } else {
                                        holder.layout_biz_main_hotdeal_sold_status.visibility = View.GONE
                                        holder.image_biz_main_hotdeal_more.visibility = View.VISIBLE
                                        ing = true
                                    }
                                } else {
                                    holder.layout_biz_main_hotdeal_sold_status.visibility = View.GONE
                                    holder.image_biz_main_hotdeal_more.visibility = View.VISIBLE
                                    ing = true
                                }
                            }

                            if (ing) {
                                holder.text_biz_main_hotdeal_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                                holder.text_biz_main_hotdeal_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                                holder.text_biz_main_hotdeal_remain_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
                            } else {
                                holder.text_biz_main_hotdeal_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                                holder.text_biz_main_hotdeal_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                                holder.text_biz_main_hotdeal_remain_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                            }

                        } else {
                            holder.layout_biz_main_not_exist_hot_deal.visibility = View.VISIBLE
                            holder.layout_biz_main_hot_deal.visibility = View.GONE

                            holder.text_biz_main_hot_deal_reg.setOnClickListener {
                                if (page.type == EnumData.PageTypeCode.store.name) {
                                    if (page.isShopOrderable!! || page.isDeliveryOrderable!! || page.isPackingOrderable!!) {
                                        val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                                        intent.putExtra(Const.TYPE, EnumData.GoodsType.hotdeal.name)
                                        intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                                        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REG)
                                    } else {
                                        val intent = Intent(holder.itemView.context, OperationInfoActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_SET_PAGE)
                                    }
                                } else {
                                    val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                                    intent.putExtra(Const.TYPE, EnumData.GoodsType.hotdeal.name)
                                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REG)
                                }
                            }
                        }
                    }

                }

                override fun onFailure(call: Call<NewResultResponse<SubResultResponse<Goods>>>?, t: Throwable?, response: NewResultResponse<SubResultResponse<Goods>>?) {

                }
            }).build().call()

            if (mDataList!!.isEmpty()) {
                holder.layout_biz_main_not_exist_plus_goods.visibility = View.VISIBLE
                holder.layout_biz_main_plus_goods.visibility = View.GONE
                holder.text_biz_main_hot_plus_goods.setOnClickListener {
                    val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                    intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REG)
                }
            } else {
                holder.layout_biz_main_not_exist_plus_goods.visibility = View.GONE
                holder.layout_biz_main_plus_goods.visibility = View.VISIBLE
                holder.text_biz_main_plus_goods_reg.setOnClickListener {
                    val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                    intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                    intent.putExtra(Const.MODE, EnumData.MODE.WRITE)
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_REG)
                }
            }

        }else if (holder is ViewFooter) {

            holder.layout_btn.setOnClickListener {
                if(holder.layout_company_info.visibility == View.GONE){
                    holder.text_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_up, 0)
                    holder.layout_company_info.visibility = View.VISIBLE
                }else if(holder.layout_company_info.visibility == View.VISIBLE){
                    holder.text_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_down, 0)
                    holder.layout_company_info.visibility = View.GONE
                }
            }

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)
            var ing = false

            if (item.status == EnumData.GoodsStatus.soldout.status) {
                holder.layout_sold_status.visibility = View.VISIBLE
                holder.text_sold_status.setText(R.string.word_sold_out)
                holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
                holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                holder.image_goods_more.visibility = View.GONE
            } else if (item.status == EnumData.GoodsStatus.finish.status) {
                holder.layout_sold_status.visibility = View.VISIBLE
                holder.text_sold_status.setText(R.string.word_sold_finish)
                holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
                holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                holder.image_goods_more.visibility = View.GONE
            } else if (item.status == EnumData.GoodsStatus.stop.status) {
                holder.layout_sold_status.visibility = View.VISIBLE
                holder.text_sold_status.setText(R.string.word_sold_stop)
                holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.color_ff4646))
                holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ff4646_2px)
                holder.image_goods_more.visibility = View.VISIBLE
            } else {

                //"expireDatetime":"2018-10-18 15:00:59"

                if (StringUtils.isNotEmpty(item.expireDatetime)) {
                    val format = SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.pattern, Locale.getDefault())
                    var date = Date()
                    try {
                        date = format.parse(item.expireDatetime)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    val currentMillis = System.currentTimeMillis()
                    if (date.time <= currentMillis) {
                        holder.image_goods_more.visibility = View.GONE
                        holder.layout_sold_status.visibility = View.VISIBLE
                        holder.text_sold_status.setText(R.string.word_sold_finish)
                        holder.text_sold_status.setTextColor(ResourceUtil.getColor(mContext!!, R.color.white))
                        holder.text_sold_status.setBackgroundResource(R.drawable.border_color_ffffff_2px)
                    } else {
                        holder.layout_sold_status.visibility = View.GONE
                        holder.image_goods_more.visibility = View.VISIBLE
                        ing = true
                    }
                } else {
                    holder.layout_sold_status.visibility = View.GONE
                    holder.image_goods_more.visibility = View.VISIBLE
                    ing = true
                }
            }

            if (ing) {
                holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.text_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_232323))
                holder.text_remain_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_579ffb))
            } else {
                holder.text_name.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_sale_price.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
                holder.text_remain_count.setTextColor(ResourceUtil.getColor(holder.itemView.context, R.color.color_b7b7b7))
            }

            holder.text_name.text = item.name
            if (item.originPrice != null && item.originPrice!! > 0) {

                if (item.originPrice!! <= item.price!!) {
                    holder.text_origin_price.visibility = View.GONE
                } else {
                    holder.text_origin_price.text = mContext!!.getString(R.string.format_money_unit, FormatUtil.getMoneyType(item.originPrice.toString()))
                    holder.text_origin_price.visibility = View.VISIBLE
                }

            } else {
                holder.text_origin_price.visibility = View.GONE
            }

            holder.text_sale_price.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_money_unit, FormatUtil.getMoneyType(item.price.toString())))

            if (item.rewardLuckybol != null && item.rewardLuckybol!! > 0) {
                holder.text_reward.visibility = View.VISIBLE
                holder.text_reward.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_reward_point, FormatUtil.getMoneyType(item.rewardLuckybol!!.toString())))

            } else {
                holder.text_reward.visibility = View.GONE
            }

            if (item.count != null && item.count != -1) {
                var soldCount = 0
                if (item.soldCount != null) {
                    soldCount = item.soldCount!!
                }
                holder.text_remain_count.visibility = View.VISIBLE
                holder.text_remain_count.text = PplusCommonUtil.fromHtml(mContext!!.getString(R.string.html_remain_count, FormatUtil.getMoneyType((item.count!! - soldCount).toString())))
            } else {
                holder.text_remain_count.visibility = View.GONE
            }

            if (item.attachments != null && item.attachments!!.images != null && item.attachments!!.images!!.isNotEmpty()) {
                if (mContext != null) {
                    val id = item.attachments!!.images!![0]
                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
                    Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image)
                }

            } else {
                holder.image.setImageResource(R.drawable.prnumber_default_img)
            }

            holder.image_goods_more.setOnClickListener {
                val builder = AlertBuilder.Builder()
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER)

                when (item.status) {
                    EnumData.GoodsStatus.ing.status -> {
                        builder.setContents(holder.itemView.context.getString(R.string.word_sold_stop), holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
                    }
                    EnumData.GoodsStatus.stop.status -> {
                        builder.setContents(holder.itemView.context.getString(R.string.word_sold_resume), holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
                    }
                    else -> {
                        builder.setContents(holder.itemView.context.getString(R.string.word_modified), holder.itemView.context.getString(R.string.word_delete))
                    }
                }

                builder.setLeftText(holder.itemView.context.getString(R.string.word_cancel))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {

                            AlertBuilder.EVENT_ALERT.LIST -> {

                                when (item.status) {
                                    EnumData.GoodsStatus.ing.status, EnumData.GoodsStatus.stop.status -> {
                                        when (event_alert.value) {
                                            1 -> {
                                                putGoodsStatus(item, holder.adapterPosition)
                                            }
                                            2 -> {
                                                val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                                                intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                                                intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                                intent.putExtra(Const.DATA, item)
                                                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
                                            }
                                            3 -> {
                                                delete(item)
                                            }
                                        }
                                    }
                                    else -> {
                                        when (event_alert.value) {
                                            1 -> {
                                                val intent = Intent(holder.itemView.context, GoodsRegActivity2::class.java)
                                                intent.putExtra(Const.TYPE, EnumData.GoodsType.plus.name)
                                                intent.putExtra(Const.MODE, EnumData.MODE.UPDATE)
                                                intent.putExtra(Const.DATA, item)
                                                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                (holder.itemView.context as BaseActivity).startActivityForResult(intent, Const.REQ_MODIFY)
                                            }
                                            2 -> {
                                                delete(item)
                                            }
                                        }
                                    }
                                }


                            }
                        }
                    }
                }).builder().show(holder.itemView.context)
            }

            holder.itemView.setOnClickListener {
                if (listener != null) {
                    listener!!.onItemClick(holder.adapterPosition - 1)
                }
            }
        }
    }

    private fun delete(item: Goods) {
        if (LoginInfoManager.getInstance().user.page!!.mainGoodsSeqNo == item.seqNo) {
            (mContext as BaseActivity).showAlert(R.string.msg_can_not_delete_main_goods)
            return
        }
        val builder = AlertBuilder.Builder()
        builder.setTitle(mContext!!.getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(mContext!!.getString(R.string.msg_question_delete_goods), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(mContext!!.getString(R.string.word_cancel)).setRightText(mContext!!.getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = item.seqNo.toString()
                        (mContext as BaseActivity).showProgress("")
                        ApiBuilder.create().deleteGoods(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                (mContext as BaseActivity).hideProgress()
                                ToastUtil.show(mContext, mContext!!.getString(R.string.msg_deleted_goods))

                                if (listener != null) {
                                    listener!!.refresh()
                                }
                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                (mContext as BaseActivity).hideProgress()
                                (mContext as BaseActivity).showAlert(R.string.msg_can_not_delete_history_goods)


                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(mContext)
    }


    private fun putGoodsStatus(goods: Goods, position: Int) {
        val builder = AlertBuilder.Builder()
        builder.setTitle(mContext!!.getString(R.string.word_notice_alert))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)

        builder.setLeftText(mContext!!.getString(R.string.word_cancel)).setRightText(mContext!!.getString(R.string.word_confirm))
        if (goods.status == EnumData.GoodsStatus.ing.status) {
            builder.addContents(AlertData.MessageData(mContext!!.getString(R.string.msg_alert_sale_stop1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
        } else if (goods.status == EnumData.GoodsStatus.stop.status) {
            builder.addContents(AlertData.MessageData(mContext!!.getString(R.string.msg_alert_sale_resume1), AlertBuilder.MESSAGE_TYPE.TEXT, 1))
        }
        builder.setOnAlertResultListener(object : OnAlertResultListener {
            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT?) {
                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                        val params = HashMap<String, String>()
                        params["seqNo"] = goods.seqNo.toString()
                        if (goods.status == EnumData.GoodsStatus.ing.status) {
                            params["status"] = EnumData.GoodsStatus.stop.status.toString()
                        } else if (goods.status == EnumData.GoodsStatus.stop.status) {
                            params["status"] = EnumData.GoodsStatus.ing.status.toString()
                        }
                        (mContext as BaseActivity).showProgress("")
                        ApiBuilder.create().putGoodsStatus(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
                            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                                (mContext as BaseActivity).hideProgress()
                                if (position != 0) {
                                    mDataList!![position - 1].status = params["status"]!!.toInt()
                                }

                                notifyItemChanged(position)

                            }

                            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                                (mContext as BaseActivity).hideProgress()
                            }
                        }).build().call()
                    }
                }
            }
        }).builder().show(mContext)
    }

    private fun setButtonPlace(bmb: BoomMenuButton) {
        val builder = (bmb.builders[0] as HamButton.Builder)
        val h = builder.buttonHeight
        val w = builder.buttonWidth

        val vm = bmb.buttonVerticalMargin
        val vm_0_5 = vm / 2
        val h_0_5 = h / 2

        val halfButtonNumber = bmb.builders.size / 2

        val pointList = arrayListOf<PointF>()

        if (bmb.builders.size % 2 == 0) {
            for (i in halfButtonNumber - 1 downTo 0)
                pointList.add(PointF(0f, -h_0_5 - vm_0_5 - i * (h + vm)))
            for (i in 0 until halfButtonNumber)
                pointList.add(PointF(0f, +h_0_5 + vm_0_5 + i * (h + vm)))
        } else {
            for (i in halfButtonNumber - 1 downTo 0)
                pointList.add(PointF(0f, -h - vm - i * (h + vm)))
            pointList.add(PointF(0f, 0f))
            for (i in 0 until halfButtonNumber)
                pointList.add(PointF(0f, +h + vm + i * (h + vm)))
        }

        bmb.customButtonPlacePositions = pointList
    }

    private fun setPiecePlace(bmb: BoomMenuButton) {
        val h = bmb.hamHeight
        val w = bmb.hamWidth

        val pn = bmb.builders.size
        val pn_0_5 = pn / 2
        val h_0_5 = h / 2
        val vm = bmb.pieceVerticalMargin
        val vm_0_5 = vm / 2

        val pointList = arrayListOf<PointF>()

        if (pn % 2 == 0) {
            for (i in pn_0_5 - 1 downTo 0)
                pointList.add(PointF(0f, -h_0_5 - vm_0_5 - i * (h + vm)))
            for (i in 0 until pn_0_5)
                pointList.add(PointF(0f, +h_0_5 + vm_0_5 + i * (h + vm)))
        } else {
            for (i in pn_0_5 - 1 downTo 0)
                pointList.add(PointF(0f, -h - vm - i * (h + vm)))
            pointList.add(PointF(0f, 0f))
            for (i in 0 until pn_0_5)
                pointList.add(PointF(0f, +h + vm + i * (h + vm)))
        }

        bmb.customPiecePlacePositions = pointList
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
            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.item_biz_main_header, parent, false))
        } else if (viewType == TYPE_ITEM) {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_goods, parent, false))
        } else if (viewType == TYPE_FOOTER) {
            return ViewFooter(LayoutInflater.from(parent.context).inflate(R.layout.item_biz_main_footer, parent, false))
        }
        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    override fun getItemViewType(position: Int): Int {

        if (position == 0) {
            return TYPE_HEADER
        } else if (position == mTotalCount+1) {
            return TYPE_FOOTER
        } else {
            return TYPE_ITEM
        }
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

}