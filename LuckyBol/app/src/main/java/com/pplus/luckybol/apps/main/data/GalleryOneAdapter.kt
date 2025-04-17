package com.pplus.luckybol.apps.main.data

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.main.ui.OneGalleryActivity
import com.pplus.luckybol.core.network.model.dto.GalleryData
import com.pplus.luckybol.databinding.ItemGalleryOneBinding
import java.util.*

/**
 * Created by 김종경 on 2015-06-17.
 */
class GalleryOneAdapter(private val context: Context) : RecyclerView.Adapter<GalleryOneAdapter.ViewHolder>() {
    private var mGalleryList: ArrayList<GalleryData>
    private var mBeforePosition = -1
    fun addAll(dataList: ArrayList<GalleryData>) {
        mGalleryList = dataList
        notifyDataSetChanged()
    }

    fun add(data: GalleryData) {
        if (mBeforePosition != -1) {
            mGalleryList[mBeforePosition].checked = false
        }
        mGalleryList.add(0, data)
        notifyDataSetChanged()
    }

    fun clear() {
        mGalleryList.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemGalleryOneBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageGallery = binding.imageGalleryOne
        val layout_gallery_on_check = binding.layoutGalleryOnCheck

        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGalleryOneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mGalleryList[position]
        Glide.with(context).load(item.imageUri).apply(RequestOptions().centerCrop().placeholder(R.drawable.luckybol_default_img).error(R.drawable.luckybol_default_img)).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?,
                                      model: Any,
                                      target: Target<Drawable?>,
                                      isFirstResource: Boolean): Boolean {
                item.isBroken = true
                return false
            }

            override fun onResourceReady(resource: Drawable?,
                                         model: Any,
                                         target: Target<Drawable?>,
                                         dataSource: DataSource,
                                         isFirstResource: Boolean): Boolean {
                return false
            }
        }).into(holder.imageGallery)
        holder.itemView.setOnClickListener {
            if (!item.isBroken!!) {
                if (mBeforePosition != -1) {
                    mGalleryList[mBeforePosition].checked = false
                }
                if (mBeforePosition != holder.absoluteAdapterPosition) {
                    mBeforePosition = holder.absoluteAdapterPosition
                    mGalleryList[holder.absoluteAdapterPosition].checked = true
                    (context as OneGalleryActivity).setSelect(item)
                    notifyDataSetChanged()
                }
            } else {
                Toast.makeText(context, R.string.msg_do_not_attach_broken_image, Toast.LENGTH_SHORT).show()
            }
        }
        if (item.checked!!) {
            holder.layout_gallery_on_check.visibility = View.VISIBLE
        } else {
            holder.layout_gallery_on_check.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mGalleryList.size
    }

    init {
        mGalleryList = ArrayList()
    }
}