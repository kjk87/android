package com.pplus.prnumberuser.apps.page.data

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.core.network.model.dto.NewsImage
import java.util.*


class NewsImagePagerAdapter : PagerAdapter {


    var dataList = ArrayList<NewsImage>()
        set(dataList) {

            field = ArrayList()
            this.dataList.addAll(dataList)
            notifyDataSetChanged()
        }
    private val mInflater: LayoutInflater
    var mListener: OnItemClickListener? = null

    fun setListener(listener: OnItemClickListener){
        mListener = listener
    }

    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    constructor(context: Context) : super() {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        views = SparseArray()
    }


    override fun destroyItem(container: ViewGroup, postion: Int, obj: Any) {

        var v: View? = obj as View
        if (v is ImageView) {
//            val imgView = v as ImageView?
            v.setImageDrawable(null)
        }
        container.removeView(v)
        v = null
    }

    override fun getCount(): Int {

        return this.dataList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val view = mInflater.inflate(R.layout.item_post_image, container, false)
//        views.put(position, view)
        val imageView = view.findViewById<View>(R.id.image_post) as ImageView

        val item = dataList[position]
        Glide.with(view.context).load(item.image).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView)

        imageView.setOnClickListener {
            if (mListener != null) {
                mListener!!.onItemClick(position)
            }
        }

        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }

    override fun getItemPosition(`object`: Any): Int {

        return POSITION_NONE
    }

    companion object {

        fun <C> asList(sparseArray: SparseArray<C>?): List<C>? {
            if (sparseArray == null) return null
            val arrayList = ArrayList<C>(sparseArray.size())
            for (i in 0 until sparseArray.size())
                arrayList.add(sparseArray.valueAt(i))
            return arrayList
        }
    }
}
