package com.lejel.wowbox.apps.event.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.databinding.ItemGetLuckyBallGuideBinding
import com.lejel.wowbox.databinding.ItemPlayGuideBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class GetLuckyBallGuideAdapter() : RecyclerView.Adapter<GetLuckyBallGuideAdapter.ViewHolder>() {

    var listener: OnItemClickListener? = null
    var launcher: ActivityResultLauncher<Intent>? = null


    interface OnItemClickListener {

        fun onItemClick(position: Int)
    }

    init {
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }

    class ViewHolder(val binding: ItemGetLuckyBallGuideBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.layoutGetLuckyBallGuide1.visibility = View.GONE
        holder.binding.layoutGetLuckyBallGuide2.visibility = View.GONE
        holder.binding.layoutGetLuckyBallGuide3.visibility = View.GONE
        holder.binding.layoutGetLuckyBallGuide4.visibility = View.GONE
        when(position){
            0->{
                holder.binding.layoutGetLuckyBallGuide1.visibility = View.VISIBLE
            }
            1->{
                holder.binding.layoutGetLuckyBallGuide2.visibility = View.VISIBLE
            }
            2->{
                holder.binding.layoutGetLuckyBallGuide3.visibility = View.VISIBLE
            }
            3->{
                holder.binding.layoutGetLuckyBallGuide4.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGetLuckyBallGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}