//package com.pplus.prnumberuser.apps.main.data
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.prnumberuser.R
//import kotlinx.android.synthetic.main.item_main_pad_pager.view.*
//
//class MainPadPagerAdapter : androidx.viewpager.widget.PagerAdapter {
//
//    private val mInflater: LayoutInflater
//    private var listener: OnItemClickListener? = null
//
//        var banners = intArrayOf(R.drawable.img_banner_1234, R.drawable.img_banner_1253, R.drawable.img_banner_7979, R.drawable.img_banner_4444, R.drawable.img_banner_4949, R.drawable.img_banner_7777)
//    var numbers = arrayOf("1234", "1253", "7979", "4444", "4949", "7777")
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
//        return numbers.size
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//
//        val view = mInflater.inflate(R.layout.item_main_pad_pager, null, false)
//
//        view.image_main_pad_banner.setImageResource(banners[position])
//
//        view.image_main_pad_banner.setOnClickListener {
//            listener?.onItemClick(numbers[position])
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
