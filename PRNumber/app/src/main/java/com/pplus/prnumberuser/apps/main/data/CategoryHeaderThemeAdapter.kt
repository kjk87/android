//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import android.content.Intent
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.bumptech.glide.load.model.GlideUrl
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.category.data.CategoryAdapter
//import com.pplus.prnumberuser.apps.category.ui.CategoryPageActivity
//import com.pplus.prnumberuser.apps.common.mgmt.CategoryInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.custom.BottomItemOffsetDecoration
//import com.pplus.prnumberuser.apps.event.ui.EventActivity
//import com.pplus.prnumberuser.core.code.common.PageTypeCode
//import com.pplus.prnumberuser.core.network.model.dto.Category
//import kotlinx.android.synthetic.main.item_category_header.view.*
//import kotlinx.android.synthetic.main.item_theme.view.*
//import java.util.*
//
//
///**
// * Created by imac on 2018. 1. 8..
// */
//class CategoryHeaderThemeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private val TYPE_HEADER = 0
//    private val TYPE_ITEM = 1
//
//    var mContext: Context? = null
//    var mDataList: MutableList<Category>? = null
//    var listener: OnItemClickListener? = null
//    //    var mSnsList: List<Sns>? = null
//    var mTodayYear = 0
//    var mTodayMonth = 0
//    var mTodayDay = 0
//    var mTotalCount = 0
//    var type = PageTypeCode.store.name
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int, view: View)
//    }
//
//    constructor(context: Context, type: String) : super() {
//        this.mContext = context
//        this.mDataList = ArrayList()
//        this.type = type
//        val c = Calendar.getInstance()
//        mTodayYear = c.get(Calendar.YEAR)
//        mTodayMonth = c.get(Calendar.MONTH)
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH)
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    fun getItem(position: Int): Category {
//
//        return mDataList!![position]
//    }
//
//    fun getDataList(): MutableList<Category>? {
//
//        return mDataList
//    }
//
//    fun add(data: Category) {
//
//        if (mDataList == null) {
//            mDataList = ArrayList<Category>()
//        }
//        mDataList!!.add(data)
//        notifyDataSetChanged()
//    }
//
//    fun addAll(dataList: List<Category>) {
//
//        if (this.mDataList == null) {
//            this.mDataList = ArrayList<Category>()
//        }
//
//        this.mDataList!!.addAll(dataList)
//        notifyDataSetChanged()
//    }
//
//    fun replaceData(position: Int, data: Category) {
//
//        mDataList!!.removeAt(position)
//        mDataList!!.add(position, data)
//        notifyDataSetChanged()
//    }
//
//    fun clear() {
//
//        mDataList = ArrayList<Category>()
//        notifyDataSetChanged()
//    }
//
//    fun setDataList(dataList: MutableList<Category>) {
//
//        this.mDataList = dataList
//        notifyDataSetChanged()
//    }
//
//    class ViewHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        val recycler = itemView.recycler_category_header
//        val image_event = itemView.image_category_header_event
//
//        init {
//            recycler.addItemDecoration(BottomItemOffsetDecoration(itemView.context, R.dimen.height_30))
//        }
//    }
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val image = itemView.image_theme
//        init {
//
//        }
//
//    }
//
//    override fun getItemCount(): Int {
//        return mDataList!!.size + 1
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        if (holder is ViewHeader) {
//
//            val layoutManager = GridLayoutManager(mContext!!, 4)
//            holder.recycler.layoutManager = layoutManager
//
//            val adapter = CategoryAdapter()
//            holder.recycler.adapter = adapter
//
//            adapter.setOnItemClickListener(object : CategoryAdapter.OnItemClickListener {
//                override fun onItemClick(position: Int) {
//                    val category = adapter.getItem(position)
//
//                    val intent = Intent(mContext, CategoryPageActivity::class.java)
//                    intent.putExtra(Const.DATA, category)
//                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    mContext?.startActivity(intent)
//                }
//            })
//
//            when (type) {
//                PageTypeCode.shop.name -> {
//                    if (CategoryInfoManager.getInstance().categoryListShop != null) {
//                        adapter.addAll(CategoryInfoManager.getInstance().categoryListShop)
//                    }
//                }
//                PageTypeCode.person.name -> {
//                    if (CategoryInfoManager.getInstance().categoryListPerson != null) {
//                        adapter.addAll(CategoryInfoManager.getInstance().categoryListPerson)
//                    }
//                }
//            }
//
//            holder.image_event.setOnClickListener {
//                val intent = Intent(mContext, EventActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                mContext?.startActivity(intent)
////                (mContext as BaseActivity).startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//            }
//
//        } else if (holder is ViewHolder) {
//
//            val item = getItem(position - 1)
//
//            if (item.uuid != null) {
//                if (mContext != null) {
//                    val id = item.uuid
//                    val glideUrl = GlideUrl("${Const.API_URL}attachment/image?id=${id}")
//                    Glide.with(mContext!!).load(glideUrl).apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().placeholder(R.drawable.img_main_hotplace_default).error(R.drawable.img_main_hotplace_default)).into(holder.image)
//                }
//
//            } else {
//                holder.image.setImageResource(R.drawable.img_main_hotplace_default)
//            }
//
//            holder.itemView.setOnClickListener {
//                listener?.onItemClick(holder.adapterPosition - 1, it)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        if (viewType == TYPE_HEADER) {
//            return ViewHeader(LayoutInflater.from(parent.context).inflate(R.layout.item_category_header, parent, false))
//        } else if (viewType == TYPE_ITEM) {
//            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_theme, parent, false))
//        }
//        throw RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
//    }
//
//    private fun isPositionHeader(position: Int): Boolean {
//        return position == 0
//    }
//
//}