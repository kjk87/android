package com.root37.buflexz.apps.gallery.data

import android.content.Context
import android.net.Uri
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.root37.buflexz.R
import com.root37.buflexz.apps.gallery.ui.ImageEditActivity
import com.root37.buflexz.databinding.ItemPagerImageBinding

class ImageEditPagerAdapter(private val mContext: Context, dataList: ArrayList<Uri>) : PagerAdapter() {
    private val mInflater: LayoutInflater
    var dataList = ArrayList<Uri>()
    private val views: SparseArray<View>

    init {
        this.dataList = dataList
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        views = SparseArray()
    }

    override fun destroyItem(container: ViewGroup, postion: Int, obj: Any) {
        var v: View? = obj as View
        if (v is ImageView) {
            v.setImageDrawable(null)
        }
        container.removeView(v)
        v = null
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val binding = ItemPagerImageBinding.inflate(mInflater, container, false)

        views.put(position, binding.root)
        val data = dataList[position]
        Glide.with(mContext).load(data.path).apply(RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(binding.imagePagerImage)
        binding.root.setOnClickListener { (mContext as ImageEditActivity).goFilter(position, data) }

        container.addView(binding.root)
        return binding.root
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}