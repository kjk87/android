//package com.pplus.prnumberuser.apps.page.data
//
//import android.content.Context
//import android.graphics.BitmapFactory
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.viewpager.widget.PagerAdapter
//import androidx.viewpager.widget.ViewPager
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.core.network.model.dto.Attachment
//import com.pplus.prnumberuser.core.util.PplusCommonUtil.Companion.getYoutubeVideoId
//import kotlinx.android.synthetic.main.item_introduce_image.view.*
//import java.io.File
//import java.util.*
//
///**
// * Created by KIMSOONHO on 2017-02-01.
// */
//class IntroduceImagePagerAdapter(private val mContext: Context) : PagerAdapter() {
//    private val mInflater: LayoutInflater
//    private var dataList: MutableList<Attachment> = ArrayList()
//    private var mListener: OnItemClickListener? = null
//
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    fun setListener(mListener: OnItemClickListener?) {
//        this.mListener = mListener
//    }
//
//    fun setDataList(dataList: List<Attachment>?) {
//        this.dataList = ArrayList()
//        this.dataList.addAll(dataList!!)
//        notifyDataSetChanged()
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        (container as ViewPager).removeView(`object` as View)
//    }
//
//    override fun getCount(): Int {
//        return dataList.size
//    }
//
//    fun getDataList(): List<Attachment> {
//        return dataList
//    }
//
//    fun getData(position: Int): Attachment {
//        return dataList[position]
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val view = mInflater.inflate(R.layout.item_introduce_image, container, false)
//        val imageView = view.image_introduce
//        val imagePlay = view.image_youtube_play
//        val data = dataList[position]
//        imagePlay.setOnClickListener {
////            val intent = Intent(mContext, YoutubePlayerActivity::class.java)
////            intent.putExtra(Const.DATA, getYoutubeVideoId(data.url))
////            mContext.startActivity(intent)
//        }
//        if (StringUtils.isNotEmpty(data.targetType) && data.targetType == "youtube") {
//            imagePlay.visibility = View.VISIBLE
//            Glide.with(mContext).load("http://img.youtube.com/vi/" + getYoutubeVideoId(data.url) + "/mqdefault.jpg").apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView)
//        } else {
//            imagePlay.visibility = View.GONE
//            val glideUrl = GlideUrl(Const.API_URL + "attachment/image?id=" + data.id)
//            Glide.with(mContext).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView)
//        }
//        imageView.setOnClickListener {
//            if (mListener != null) {
//                mListener!!.onItemClick(position)
//            }
//        }
//        container.addView(view)
//        return view
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view === `object`
//    }
//
//    fun getBitmapSize(fullname: String?): BitmapFactory.Options {
//        val options = BitmapFactory.Options()
//        options.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(File(fullname).absolutePath, options)
//        return options
//    }
//
//    override fun getItemPosition(`object`: Any): Int {
//        return POSITION_NONE
//    }
//
//    init {
//        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        dataList = ArrayList()
//    }
//}