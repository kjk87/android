//package com.pplus.prnumberuser.apps.tutorial.data
//
//import android.app.Activity
//import android.app.Activity.RESULT_OK
//import android.content.Context
//import android.content.Intent
//import androidx.viewpager.widget.PagerAdapter
//import android.util.SparseArray
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import kotlinx.android.synthetic.main.item_tutorial.view.*
//import java.util.*
//
//
//class TutorialPagerAdapter : androidx.viewpager.widget.PagerAdapter {
//
//    private val mInflater: LayoutInflater
//    var mListener: OnItemClickListener? = null
//
//    fun setListener(listener: OnItemClickListener) {
//        mListener = listener
//    }
//
//    interface OnItemClickListener {
//
//        fun onItemClick(position: Int)
//    }
//
//    constructor(context: Context) : super() {
//        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
////        views = SparseArray()
//    }
//
//
//    override fun destroyItem(container: ViewGroup, postion: Int, obj: Any) {
//
//        var v: View? = obj as View
//        if (v is ImageView) {
////            val imgView = v as ImageView?
//            v.setImageDrawable(null)
//        }
//        container.removeView(v)
//        v = null
//    }
//
//    override fun getCount(): Int {
//
//        return 4
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//
//        val view = mInflater.inflate(R.layout.item_tutorial, container, false)
//
//        when (position) {
//
//            0 -> {
//                view.layout_tutorial0.visibility = View.VISIBLE
//                view.layout_tutorial1.visibility = View.GONE
//                view.layout_tutorial2.visibility = View.GONE
//                view.layout_tutorial4.visibility = View.GONE
//            }
//            1 -> {
//                view.layout_tutorial0.visibility = View.GONE
//                view.layout_tutorial1.visibility = View.VISIBLE
//                view.layout_tutorial2.visibility = View.GONE
//                view.layout_tutorial4.visibility = View.GONE
//            }
//            2 -> {
//                view.layout_tutorial0.visibility = View.GONE
//                view.layout_tutorial1.visibility = View.GONE
//                view.layout_tutorial2.visibility = View.VISIBLE
//                view.layout_tutorial4.visibility = View.GONE
//            }
//            3 -> {
//                view.layout_tutorial0.visibility = View.GONE
//                view.layout_tutorial1.visibility = View.GONE
//                view.layout_tutorial2.visibility = View.GONE
//                view.layout_tutorial4.visibility = View.VISIBLE
//
//                view.text_tutorial_login.setOnClickListener {
//                    PreferenceUtil.getDefaultPreference(view.context).put(Const.TUTORIAL, false)
//                    val data = Intent()
//                    data.putExtra(Const.KEY, Const.LOGIN)
//                    (view.context as Activity).setResult(RESULT_OK, data)
//                    (view.context as Activity).finish()
//
//                }
//
//                view.text_tutorial_join.setOnClickListener {
//                    PreferenceUtil.getDefaultPreference(view.context).put(Const.TUTORIAL, false)
//                    val data = Intent()
//                    data.putExtra(Const.KEY, Const.JOIN)
//                    (view.context as Activity).setResult(RESULT_OK, data)
//                    (view.context as Activity).finish()
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
//
//    override fun getItemPosition(`object`: Any): Int {
//
//        return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
//    }
//
//    companion object {
//
//        fun <C> asList(sparseArray: SparseArray<C>?): List<C>? {
//            if (sparseArray == null) return null
//            val arrayList = ArrayList<C>(sparseArray.size())
//            for (i in 0 until sparseArray.size())
//                arrayList.add(sparseArray.valueAt(i))
//            return arrayList
//        }
//    }
//}
