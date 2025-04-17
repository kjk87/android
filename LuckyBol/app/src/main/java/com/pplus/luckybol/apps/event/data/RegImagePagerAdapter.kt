package com.pplus.luckybol.apps.event.data

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.R
import com.pplus.luckybol.core.network.model.dto.Attachment
import com.pplus.luckybol.databinding.ItemPostWriteImageBinding


class RegImagePagerAdapter : PagerAdapter {


    var mDataList: ArrayList<String>? = null
    var viewList = arrayListOf<View>()
    var listener: OnItemChangeListener? = null
    private val mInflater: LayoutInflater


    interface OnItemChangeListener {

        fun onChange()
    }

    constructor(context: Context) : super() {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mDataList = ArrayList<String>()
        viewList = arrayListOf<View>()
//        views = SparseArray()
    }

    fun setOnItemChangeListener(listener: OnItemChangeListener) {
        this.listener = listener
    }

    fun getItem(position: Int): String {

        return mDataList!![position]
    }

    fun getDataList(): MutableList<String>? {

        return mDataList
    }

    fun add(data: String) {

        if (mDataList == null) {
            mDataList = ArrayList<String>()
        }
        mDataList!!.add(data)
        notifyDataSetChanged()
    }

    fun insertAttach(index: String, attachment: Attachment) {

        mDataList!![index.toInt()] = attachment.url!!
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<String>) {

        if (this.mDataList == null) {
            this.mDataList = ArrayList<String>()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    fun replaceData(position: Int, data: String) {

        mDataList!!.removeAt(position)
        mDataList!!.add(position, data)
        notifyDataSetChanged()
    }

    fun clear() {

        mDataList = ArrayList<String>()
        notifyDataSetChanged()
    }

    fun setDataList(dataList: ArrayList<String>) {

        this.mDataList = dataList
        notifyDataSetChanged()
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

        return mDataList!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val item = mDataList!![position]

        val binding = ItemPostWriteImageBinding.inflate(mInflater, container, false)

        val imageView = binding.imagePostWrite
        Glide.with(binding.root.context).load(item).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).into(imageView)


//        LogUtil.e("attached", "attach size : {}", attachedList.size)
//        if (position < attachedList.size) {
//
//            val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${attachedList[position]}")
//            Glide.with(view.context).load(glideUrl).apply(RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView)
//
//        } else {
//            val data = this.dataList[position - attachedList.size]
//            LogUtil.e("ImageAdapter", data)
//            Glide.with(view.context).load(data).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(imageView)
//        }

        binding.imagePostWriteDelete.visibility = View.VISIBLE
        binding.imagePostWriteDelete.setOnClickListener {
            mDataList!!.removeAt(position)
            notifyDataSetChanged()
            listener?.onChange()
        }

        container.addView(binding.root)
        return binding.root
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
