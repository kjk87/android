package com.pplus.luckybol.apps.main.data

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.core.database.DBManager
import com.pplus.luckybol.core.database.entity.ContactDao
import com.pplus.luckybol.core.network.model.dto.Contact
import com.pplus.luckybol.databinding.ItemContactBinding
import com.pplus.luckybol.databinding.ItemContactSectionBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil

class ContactSectionAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mDataList: MutableList<Contact>? = null
    var listener: OnItemClickListener? = null
    var mMemberCount = 0
    var mExistMember = true

    init {
        mDataList = arrayListOf()
    }

    companion object {
        const val TYPE_SECTION = 0
        const val TYPE_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        if (mExistMember) {
            return when (position) {
                0, mMemberCount + 1 -> TYPE_SECTION
                else -> TYPE_ITEM
            }
        } else {
            return when (position) {
                0 -> TYPE_SECTION
                else -> TYPE_ITEM
            }
        }
    }


    interface OnItemClickListener {

        fun onTabChanged(tab: Int)
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {

        this.listener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SECTION -> {
                val binding = ItemContactSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SectionHolder(binding)
            }
            else -> {
                val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ItemViewHolder(binding)
            }
        }

    }

    fun clear() {

        mDataList = arrayListOf()
        notifyDataSetChanged()
    }

    fun addAll(dataList: List<Contact>) {

        if (this.mDataList == null) {
            this.mDataList = arrayListOf()
        }

        this.mDataList!!.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mExistMember) {
            mDataList!!.size + 2
        } else {
            mDataList!!.size + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SectionHolder -> {
                if (mExistMember) {
                    holder.bindView(position == 0, position)
                } else {
                    holder.bindView(false, position)
                }
            }
            is ItemViewHolder -> {
                if(mDataList!!.isNotEmpty()){
                    if (mExistMember) {
                        if (position < mMemberCount + 1) {
                            val item = mDataList!![position - 1]
                            holder.bindView(item)
                        } else {
                            val item = mDataList!![position - 2]
                            holder.bindView(item)
                        }
                    } else {
                        val item = mDataList!![position - 1]
                        holder.bindView(item)
                    }
                }
            }
        }
    }

    inner class SectionHolder(val binding: ItemContactSectionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(isMember: Boolean, position: Int) {

            if(position == 0){
                binding.layoutContactSection.setBackgroundResource(R.drawable.bg_ffffff_radius_top_87)
            }else{
                binding.layoutContactSection.setBackgroundColor(ResourceUtil.getColor(itemView.context, R.color.white))
            }

            if (isMember) {
                binding.textContactSection.text = itemView.context.getString(R.string.format_friend_count, FormatUtil.getMoneyType(mMemberCount.toString()))
                binding.textContactSectionExistDesc.visibility = View.VISIBLE
            } else {
                binding.textContactSection.text = itemView.context.getString(R.string.word_unsigned_user)
                binding.textContactSectionExistDesc.visibility = View.GONE
            }
        }
    }

    inner class ItemViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
        }

        fun bindView(item: Contact) {
            val contacts = DBManager.getInstance(itemView.context).session.contactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.mobileNumber!!.replace(Const.APP_TYPE + "##", ""))).list()
            if (item.member != null) {
                if (contacts != null && contacts.size > 0) {
                    binding.textContactName.text = contacts[0].memberName
                } else {
                    binding.textContactName.text = item.member!!.nickname
                }

                if (item.member!!.profileAttachment != null) {
                    Glide.with(itemView.context).load(item.member!!.profileAttachment!!.url).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.ic_contact_profile_default).error(R.drawable.ic_contact_profile_default)).into(binding.imageContactProfile)
                } else {
                    binding.imageContactProfile.setImageResource(R.drawable.ic_contact_profile_default)
                }
                binding.textContactBuff.visibility = View.VISIBLE
                if (item.member!!.buffMemberList != null && item.member!!.buffMemberList!!.isNotEmpty()) {
                    val buffTitle = item.member!!.buffMemberList!![0].buff!!.title
                    binding.textContactBuff.setTextColor(ResourceUtil.getColor(itemView.context, R.color.color_fc5c57))
                    binding.textContactBuff.text = "#${buffTitle}"
                } else {
                    binding.textContactBuff.text = "#${itemView.context.getString(R.string.word_none_buff)}"
                    binding.textContactBuff.setTextColor(ResourceUtil.getColor(itemView.context, R.color.color_6e7780))
                }

                binding.textContactInvite.visibility = View.GONE
            } else {
                binding.imageContactProfile.setImageResource(R.drawable.ic_contact_profile_default)
                if (contacts != null && contacts.size > 0) {
                    binding.textContactName.text = contacts[0].memberName
                } else {
                    binding.textContactName.text = itemView.context.getString(R.string.word_unknown)
                }

                binding.textContactBuff.visibility = View.GONE
                binding.textContactInvite.visibility = View.VISIBLE
                binding.textContactInvite.setOnClickListener {
                    val recommendKey = LoginInfoManager.getInstance().user.recommendKey
                    val text = "${itemView.context.getString(R.string.format_invite_description, recommendKey)}\n${itemView.context.getString(R.string.format_msg_invite_url, LoginInfoManager.getInstance().user.recommendKey)}"
                    //        val text = "${getString(R.string.msg_invite_desc)}\n${getString(R.string.msg_invite_url)}"
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, text)
                    val chooserIntent = Intent.createChooser(intent, itemView.context.getString(R.string.word_share))
                    itemView.context.startActivity(chooserIntent)
                }
            }

        }
    }
}