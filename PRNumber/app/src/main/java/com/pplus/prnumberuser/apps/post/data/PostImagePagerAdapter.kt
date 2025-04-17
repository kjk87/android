package com.pplus.prnumberuser.apps.post.data

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.Attachment
import com.pplus.prnumberuser.databinding.ItemPostImageBinding
import com.pplus.utils.part.info.DeviceUtil
import java.io.File
import java.util.*

/**
 * Created by KIMSOONHO on 2017-02-01.
 */

class PostImagePagerAdapter : PagerAdapter {

    private var mInflater: LayoutInflater
    private var dataList: MutableList<Attachment> = ArrayList()
    private var mListener: OnItemClickListener? = null
    private val width: Int
    private val height: Int

    constructor(context: Context, dataList: List<Attachment>) : super() {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        this.dataList = ArrayList()
        this.dataList.addAll(dataList)
        width = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS - context.resources.getDimensionPixelSize(R.dimen.width_144)
        height = (width * 0.75).toInt()
    }

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    fun setListener(mListener: OnItemClickListener) {

        this.mListener = mListener

    }


    fun setDataList(dataList: List<Attachment>) {

        this.dataList = ArrayList()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        (container as ViewPager).removeView(`object` as View)
    }

    fun getDataList(): List<Attachment> {

        return dataList
    }

    override fun getCount(): Int {

        return dataList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val binding = ItemPostImageBinding.inflate(mInflater, container, false)

        val imageView = binding.imagePost

        val data = dataList[position]

        val id = data.id
        val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
        Glide.with(imageView.context).load(glideUrl).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView)

        imageView.setOnClickListener {
            if (mListener != null) {
                mListener!!.onItemClick(position)
            }
        }

        container.addView(binding.root)
        return binding.root
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
