package com.pplus.luckybol.apps.product.data

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.ui.common.ZoomImageView
import com.pplus.luckybol.core.network.model.dto.GoodsImage
import com.pplus.luckybol.core.network.model.dto.ProductImage
import java.io.File
import java.util.*


class ProductZoomablePagerAdapter : PagerAdapter {

    private var mInflater: LayoutInflater
    private var dataList = ArrayList<ProductImage>()

    var mOnSingleTapListener: onSingleTapListener? = null

    fun setOnSingleTapListener(listener: onSingleTapListener) {
        mOnSingleTapListener = listener
    }

    interface onSingleTapListener {
        fun onSingleTap()
    }

    constructor(context: Context, dataList:ArrayList<ProductImage>) : super() {
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

    fun getData(position: Int): ProductImage {

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
        Glide.with(imageView.context).load(item.image).apply(RequestOptions().fitCenter().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(imageView)

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
