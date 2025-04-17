package com.lejel.wowbox.apps.gallery.data

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.gallery.ui.GalleryActivity
import com.lejel.wowbox.core.network.model.dto.GalleryData
import com.lejel.wowbox.core.util.ToastUtil
import com.lejel.wowbox.databinding.ItemGalleryBinding
import java.util.*

/**
 * Created by 김종경 on 2015-06-17.
 */
class GalleryGridAdapter() : RecyclerView.Adapter<GalleryGridAdapter.ViewHolder>() {
    private var mMaxCount = Const.IMAGE_UPLOAD_MAX_COUNT
    private var mGalleryList: ArrayList<GalleryData>
    var selectGalleryList: ArrayList<GalleryData>
    private var mLastCheckPosition = -1
    var isBlocking = false
    fun setMaxCount(maxCount: Int) {
        mMaxCount = maxCount
    }

    fun selectLastPhoto() {
        if (selectGalleryList.size < mMaxCount) {
            selectGalleryList.add(mGalleryList[0])
            notifyDataSetChanged()
        }
    }

    fun addAll(dataList: ArrayList<GalleryData>?) {
        mGalleryList = ArrayList()
        mGalleryList.addAll(dataList!!)
        notifyDataSetChanged()
    }

    fun add(data: GalleryData) {
        mGalleryList.add(0, data)
        notifyDataSetChanged()
    }

    fun clear() {
        mGalleryList.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageGallery = binding.imageGallery
        val text_gallery_select = binding.textGallerySelect

        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mGalleryList[position]
        Glide.with(holder.itemView.context).load(item.imageUri).apply(RequestOptions().centerCrop()).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                item.isBroken = true
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                return false
            }
        }).into(holder.imageGallery)
        holder.itemView.setOnClickListener {
            if (!isBlocking) {
                if (!item.isBroken!!) {
                    if (mLastCheckPosition != holder.absoluteAdapterPosition) {
                        if (!selectGalleryList.contains(item)) {
                            if (selectGalleryList.size < mMaxCount) {
                                mGalleryList[holder.absoluteAdapterPosition].checked = true
                                selectGalleryList.add(item)
                                mLastCheckPosition = holder.absoluteAdapterPosition
                                (holder.itemView.context as GalleryActivity).addImage(item)
                                notifyDataSetChanged()
                            } else {
                                ToastUtil.show(holder.itemView.context, holder.itemView.context.getString(R.string.format_msg_image_count, mMaxCount))
                            }
                        } else {
                            mLastCheckPosition = holder.absoluteAdapterPosition
                            (holder.itemView.context as GalleryActivity).changeImage(selectGalleryList.indexOf(item)) //                            notifyDataSetChanged();
                        }
                    } else {
                        if (selectGalleryList.contains(item)) {
                            (holder.itemView.context as GalleryActivity).removeImage(selectGalleryList.indexOf(item))
                            selectGalleryList.remove(item)
                            mGalleryList[holder.absoluteAdapterPosition].checked = false
                            mLastCheckPosition = if (selectGalleryList.size > 0) {
                                mGalleryList.indexOf(selectGalleryList[selectGalleryList.size - 1])
                            } else {
                                -1
                            }
                            notifyDataSetChanged()
                        }
                    }
                } else {
                    ToastUtil.show(holder.itemView.context, R.string.msg_do_not_attach_broken_image)
                }
            }
        }
        if (item.checked!! && !selectGalleryList.contains(item)) {
            selectGalleryList.add(item)
            mLastCheckPosition = holder.absoluteAdapterPosition
            (holder.itemView.context as GalleryActivity).addImage(item)
        }
        if (item.checked!!) {
            holder.text_gallery_select.visibility = View.VISIBLE
            holder.text_gallery_select.text = (selectGalleryList.indexOf(item) + 1).toString()
        } else {
            holder.text_gallery_select.visibility = View.GONE
            holder.text_gallery_select.text = ""
        }
    }

    override fun getItemCount(): Int {
        return mGalleryList.size
    }

    init {
        selectGalleryList = ArrayList()
        mGalleryList = ArrayList()
    }
}