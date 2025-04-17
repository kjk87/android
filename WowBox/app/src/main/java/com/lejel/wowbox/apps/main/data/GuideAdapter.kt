package com.lejel.wowbox.apps.main.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.lejel.wowbox.R
import com.lejel.wowbox.databinding.ItemGuideBinding


/**
 * Created by imac on 2018. 1. 8..
 */
class GuideAdapter() : RecyclerView.Adapter<GuideAdapter.ViewHolder>() {

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

    class ViewHolder(val binding: ItemGuideBinding) : RecyclerView.ViewHolder(binding.root) {

        init {

        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.imageGuide1.visibility = View.GONE
        holder.binding.imageGuide2.visibility = View.GONE
        holder.binding.imageGuide3.visibility = View.GONE
        holder.binding.imageGuide4.visibility = View.GONE
        holder.binding.imageGuide5.visibility = View.GONE
        when(position){
            0->{
                holder.binding.textGuideTitle.setText(R.string.word_lucky_ball)
                holder.binding.imageGuide1.visibility = View.VISIBLE
            }
            1->{
                holder.binding.textGuideTitle.setText(R.string.word_time_event)
                holder.binding.imageGuide2.visibility = View.VISIBLE
            }
            2->{
                holder.binding.textGuideTitle.setText(R.string.word_lucky_draw_upper_en)
                holder.binding.imageGuide3.visibility = View.VISIBLE
            }
            3->{
                holder.binding.textGuideTitle.setText(R.string.word_invite_friend_upper)
                holder.binding.imageGuide4.visibility = View.VISIBLE
            }
            4->{
                holder.binding.textGuideTitle.setText(R.string.word_sign_up_upper)
                holder.binding.imageGuide5.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}