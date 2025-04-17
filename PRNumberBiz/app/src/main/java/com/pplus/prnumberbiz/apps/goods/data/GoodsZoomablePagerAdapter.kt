package com.pplus.prnumberbiz.apps.goods.data

import android.content.Context
import android.graphics.BitmapFactory
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pple.pplus.utils.part.info.DeviceUtil
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.ui.common.ZoomImageView
import com.pplus.prnumberbiz.core.util.PplusCommonUtil

import java.io.File
import java.util.ArrayList

import it.sephiroth.android.library.imagezoom.ImageViewTouch


class GoodsZoomablePagerAdapter : androidx.viewpager.widget.PagerAdapter {

    private var mInflater: LayoutInflater
    private var dataList = ArrayList<String>()

    var mOnSingleTapListener: onSingleTapListener? = null

    fun setOnSingleTapListener(listener: onSingleTapListener) {
        mOnSingleTapListener = listener
    }

    interface onSingleTapListener {
        fun onSingleTap()
    }

    constructor(context: Context, dataList:ArrayList<String>) : super() {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.dataList = dataList
    }

    override fun destroyItem(container: ViewGroup, postion: Int, obj: Any) {

        var v: View? = obj as View
        if (v is ImageView) {
            val imgView = v as ImageView?
            imgView?.setImageDrawable(null)
        }
        container.removeView(v)
        v = null
    }

    fun getData(position: Int): String {

        return dataList[position]
    }

    override fun getCount(): Int {

        return dataList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = mInflater.inflate(R.layout.item_zoomable, container, false)

        val imageView = view.findViewById<View>(R.id.imageView) as ZoomImageView
        val imagePlay = view.findViewById<View>(R.id.image_detail_youtube_play) as ImageView

        val item = dataList[position]

        imagePlay.visibility = View.GONE
        val glideUrl = GlideUrl(Const.API_URL + "attachment/image?id=${item}")
        Glide.with(imageView.context).load(glideUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView)

        imageView.setSingleTapListener {
            if (mOnSingleTapListener != null) {
                mOnSingleTapListener!!.onSingleTap()
            }
        }

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }

    fun getBitmapSize(fullname: String): BitmapFactory.Options {

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(fullname).absolutePath, options)
        return options
    }

}
