package com.pplus.luckybol.apps.product.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.ProductReview
import com.pplus.luckybol.core.network.model.dto.ProductReviewCountEval
import com.pplus.luckybol.core.network.model.dto.ProductReviewImage
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.HeaderReviewBinding
import com.pplus.luckybol.databinding.ItemEvalBarBinding
import com.pplus.luckybol.databinding.ItemGoodsReviewBinding
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import com.pplus.utils.part.utils.time.DateFormatUtils
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by imac on 2018. 1. 8..
 */
class ProductReviewHeaderAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    var mDataList: MutableList<ProductReview>? = null
    var mProductReviewCountEvalList: List<ProductReviewCountEval>? = null
    var mAvgEval = 0.0f
    var listener: OnItemClickListener? = null
    var mTotalCount = 0
    internal var mTodayYear: Int = 0
    internal var mTodayMonth: Int = 0
    internal var mTodayDay: Int = 0

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
        this.mDataList = ArrayList()
        val c = Calendar.getInstance()
        mTodayYear = c.get(Calendar.YEAR)
        mTodayMonth = c.get(Calendar.MONTH)
        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    fun getItem(position: Int): ProductReview {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<ProductReview>? {

        return mDataList
    }

    fun add(data: ProductReview) {

        if (mDataList == null) {
            mDataList = ArrayList<ProductReview>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<ProductReview>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<ProductReview>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: ProductReview) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<ProductReview>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: MutableList<ProductReview>) {

        this.mDataList = dataList
        notifyDataSetChanged()
    }

    class ViewHeader(binding: HeaderReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val text_avg_eval = binding.textHeaderReviewAvgEval
        val grade_bar = binding.gradeBarHeaderReview
        val layout_eval_bar = binding.layoutHeaderReviewEvalBar
        val text_total_count = binding.txtHeaderReviewTotalCount

        init {
        }
    }

    class ViewHolder(binding: ItemGoodsReviewBinding) : RecyclerView.ViewHolder(binding.root) {

        val image_profile = binding.imageGoodsReviewProfile
        val text_name = binding.textGoodsReviewName
        val layout_image = binding.layoutGoodsReviewImage
        val pager_image = binding.pagerGoodsReviewImage
        val indicator = binding.indicatorGoodsReview
        val text_contents = binding.textGoodsReviewContents
        val text_regDate = binding.textGoodsReviewRegDate
        val grade_bar = binding.gradeBarGoodsReview
        val layout_reply = binding.layoutGoodsReviewReply
        val text_reply = binding.textGoodsReviewReply
        val text_reply_date = binding.textGoodsReviewReplyDate

        init {
        }
    }

    override fun getItemCount(): Int {
        if(mProductReviewCountEvalList != null){
            return mDataList!!.size + 1
        }else{
            return mDataList!!.size
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ViewHeader) {

            holder.text_avg_eval.text = mAvgEval.toString()

            val eval = String.format("%.1f", mAvgEval)
            holder.grade_bar.build(eval)
            var totalCount = 0
            for(productCountEval in mProductReviewCountEvalList!!){
                totalCount += productCountEval.count!!
            }

            for(productCountEval in mProductReviewCountEvalList!!){

                val evalBarBinding = ItemEvalBarBinding.inflate(LayoutInflater.from(holder.itemView.context), LinearLayout(holder.itemView.context), false)
                holder.layout_eval_bar.addView(evalBarBinding.root)
                (evalBarBinding.root.layoutParams as LinearLayout.LayoutParams).marginEnd = holder.itemView.context.resources.getDimensionPixelSize(R.dimen.width_36)
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

                evalBarBinding.textEvalBarEval.text = holder.itemView.context.getString(R.string.format_eval, productCountEval.eval.toString())

            }

            holder.text_total_count.text = holder.itemView.context.getString(R.string.format_review3, FormatUtil.getMoneyType(totalCount.toString()))

        } else if (holder is ViewHolder) {

            val item = getItem(position - 1)

            if(item.member != null){
                holder.text_name.text = item.member!!.nickname
                if(item.member!!.profileAttachment != null){
                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${item.member!!.profileAttachment!!.id}")
                    Glide.with(holder.itemView.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_commerce_user_profile_default).error(R.drawable.img_commerce_user_profile_default)).into(holder.image_profile)
                }else{
                    holder.image_profile.setImageResource(R.drawable.img_commerce_user_profile_default)
                }
            }

            holder.text_contents.text = item.review

            if (StringUtils.isNotEmpty(item.reviewReply)) {
                holder.layout_reply.visibility = View.VISIBLE
                holder.text_reply.text = item.reviewReply
                if(StringUtils.isNotEmpty(item.reviewReplyDate)){
                    holder.text_reply_date.text = PplusCommonUtil.getDateFormat(item.reviewReplyDate!!)
                }

            } else {
                holder.layout_reply.visibility = View.GONE
            }

            if (item.eval != null) {
                val eval = String.format("%.1f", item.eval!!.toFloat())
                holder.grade_bar.build(eval)
            } else {
                val eval = String.format("%.1f", 0f)
                holder.grade_bar.build(eval)
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
                    holder.text_regDate.text = output.format(d)
                } else {
                    val output = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                    holder.text_regDate.text = output.format(d)
                }

            } catch (e: Exception) {

            }

            if (item.imageList != null && item.imageList!!.isNotEmpty()) {
                holder.layout_image.visibility = View.VISIBLE
                holder.pager_image.visibility = View.VISIBLE
                val imageAdapter = ProductReviewImagePagerAdapter(holder.itemView.context)
                imageAdapter.dataList = item.imageList as ArrayList<ProductReviewImage>
                holder.pager_image.adapter = imageAdapter
                holder.indicator.visibility = View.VISIBLE
                holder.indicator.removeAllViews()
                holder.indicator.build(LinearLayout.HORIZONTAL, item.imageList!!.size)
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
                imageAdapter.setListener(object : ProductReviewImagePagerAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {
                        if (listener != null) {
                            listener!!.onItemClick(holder.absoluteAdapterPosition - 1)
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
                listener?.onItemClick(holder.absoluteAdapterPosition - 1)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            return ViewHeader(HeaderReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else if (viewType == TYPE_ITEM) {
            return ViewHolder(ItemGoodsReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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