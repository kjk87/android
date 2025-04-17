package com.pplus.prnumberuser.apps.page.data

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.PageImage
import com.pplus.prnumberuser.databinding.ItemIntroduceImageBinding
import java.io.File
import java.util.*

/**
 * Created by KIMSOONHO on 2017-02-01.
 */
class PageImagePagerAdapter(private val mContext: Context) : PagerAdapter() {
    private val mInflater: LayoutInflater
    private var dataList: MutableList<PageImage> = ArrayList()
    private var mListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setListener(mListener: OnItemClickListener?) {
        this.mListener = mListener
    }

    fun setDataList(dataList: List<PageImage>?) {
        this.dataList = ArrayList()
        this.dataList.addAll(dataList!!)
        notifyDataSetChanged()
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }

    override fun getCount(): Int {
        return dataList.size
    }

    fun getDataList(): List<PageImage> {
        return dataList
    }

    fun getData(position: Int): PageImage {
        return dataList[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding = ItemIntroduceImageBinding.inflate(mInflater, container, false)
        val imageView = binding.imageIntroduce
        val imagePlay = binding.imageYoutubePlay
        val item = dataList[position]

        imagePlay.visibility = View.GONE
        Glide.with(mContext).load(item.image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView)

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

    fun getBitmapSize(fullname: String?): BitmapFactory.Options {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(File(fullname).absolutePath, options)
        return options
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    init {
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dataList = ArrayList()
    }
}