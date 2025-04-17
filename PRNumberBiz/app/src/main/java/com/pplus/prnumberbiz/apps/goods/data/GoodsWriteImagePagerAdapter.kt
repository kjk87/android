//package com.pplus.prnumberbiz.apps.goods.data
//
//import android.content.Context
//import android.support.v4.view.PagerAdapter
//import android.support.v4.view.ViewPager
//import android.util.SparseArray
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pple.pplus.utils.part.logs.LogUtil
//import com.pplus.prnumberbiz.Const
//import com.pplus.prnumberbiz.R
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberbiz.core.network.model.dto.Attachment
//import java.util.*
//
//
//class GoodsWriteImagePagerAdapter : PagerAdapter {
//
//
//    var dataList = ArrayList<String>()
//    var attachList = SparseArray<Attachment>()
//    var attachedList = arrayListOf<String>()
//    var listener: OnItemClickListener? = null
//    private val mInflater: LayoutInflater
//
//    interface OnItemClickListener {
//
//        fun onGallery()
//    }
//
//    constructor(context: Context) : super() {
//        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//
//
//    fun getAttachList(): ArrayList<Attachment> {
//        val arrayList = arrayListOf<Attachment>()
//
//        for (i in 0 until attachList.size())
//            arrayList.add(attachList.valueAt(i))
//        return arrayList
//    }
//
//    fun addData(data: String) {
//
//        this.dataList.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun insertAttach(url: String, attachment: Attachment) {
//
//        attachList.put(this.dataList.indexOf(url), attachment)
//    }
//
//    override fun destroyItem(container: ViewGroup, postion: Int, obj: Any) {
//
//        (container as ViewPager).removeView(obj as View)
//    }
//
//    override fun getCount(): Int {
//
//        return attachedList.size + this.dataList.size
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//
//        val view = mInflater.inflate(R.layout.item_post_write_image, container, false)
////        views.put(position, view)
//        val imageView = view.findViewById<View>(R.id.image_post_write) as ImageView
//        LogUtil.e("attached", "attach size : {}", attachedList.size)
//        if (position < attachedList.size) {
//
//            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${attachedList[position]}")
//            Glide.with(view.context).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView)
//        } else {
//            val data = this.dataList[position - attachedList.size]
//            LogUtil.e("ImageAdapter", data)
//            Glide.with(view.context).load(data).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(imageView)
//        }
//
//        imageView.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setLeftText(view.context.getString(R.string.word_cancel))
//            builder.setContents(view.context.getString(R.string.word_add), view.context.getString(R.string.word_delete))
//
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//
//                    when (event_alert.getValue()) {
////                        1 -> (view.context as GoodsReviewWriteActivity).goPostGallery()
//                        2 -> {
//                            if (position < attachedList.size) {
//                                attachedList.removeAt(position)
//                            } else {
//                                attachList.remove(position - attachedList.size)
//                                dataList.removeAt(position - attachedList.size)
//                            }
//                            notifyDataSetChanged()
////                            (view.context as GoodsReviewWriteActivity).setImageRefresh(position)
//                        }
//                    }
//                }
//            }).builder().show(view.context)
//        }
//        container.addView(view)
//        return view
//    }
//
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//
//        return view === `object`
//    }
//
//    override fun getItemPosition(`object`: Any): Int {
//
//        return POSITION_NONE
//    }
//}
