//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.page.ui.OnlinePageActivity
//import com.pplus.prnumberuser.apps.theme.ui.ThemeActivity
//import kotlinx.android.synthetic.main.item_main_location_pager.view.*
//
//class MainLocationPagerAdapter : androidx.viewpager.widget.PagerAdapter {
//
//    private val mInflater: LayoutInflater
//    private var listener: OnItemClickListener? = null
//
//    var banners = intArrayOf(R.drawable.img_home_banner_1, R.drawable.img_home_banner_2)
//
//    interface OnItemClickListener {
//
//        fun onItemClick(number: String)
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
//        return banners.size
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//
//        val view = mInflater.inflate(R.layout.item_main_location_pager, null, false)
//
//        view.image_main_location_banner.setImageResource(banners[position])
//
//        view.image_main_location_banner.setOnClickListener {
//            when(position){
//                0->{
//                    val intent = Intent(view.context, ThemeActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    view.context.startActivity(intent)
//                }
//                1->{
//                    val intent = Intent(view.context, OnlinePageActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    view.context.startActivity(intent)
//                }
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
