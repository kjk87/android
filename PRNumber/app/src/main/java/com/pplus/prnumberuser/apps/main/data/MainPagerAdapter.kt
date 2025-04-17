//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import androidx.viewpager.widget.PagerAdapter
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//
//import com.pplus.prnumberuser.R
//import kotlinx.android.synthetic.main.item_main_pager.view.*
//
//class MainPagerAdapter : androidx.viewpager.widget.PagerAdapter {
//
//    private val mInflater: LayoutInflater
//    private var listener: OnItemClickListener? = null
//    var imageRes = intArrayOf(R.drawable.img_home_event_banner_1, R.drawable.img_home_event_banner_2, R.drawable.img_home_event_banner_3, R.drawable.img_home_event_banner_4, R.drawable.img_home_event_banner_5, R.drawable.img_home_event_banner_6)
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
////        this.mContext = context
//
//        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    }
//
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//
//        this.listener = listener
//    }
//
//    override fun destroyItem(container: ViewGroup, postion: Int, obj: Any) {
//
//        var v: View? = obj as View
//        container.removeView(v)
//        v = null
//    }
//
//    override fun getCount(): Int {
//
//        return imageRes.size
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//
//        val view = mInflater.inflate(R.layout.item_main_pager, null, false)
//        val image = view.image_main_pager
//        image.setImageResource(imageRes[position])
//        image.setOnClickListener {
//            if (listener != null) {
//                listener!!.onItemClick(position)
//            }
//        }
//
//        container.addView(view)
//        return view
//    }
//
//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//
//        return view === `object`
//    }
//}
