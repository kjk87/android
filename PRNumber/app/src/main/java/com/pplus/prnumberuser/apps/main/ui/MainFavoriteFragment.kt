//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.LinearLayout
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.main.data.MainPagerAdapter
//import com.pplus.prnumberuser.apps.my.ui.MyFavoriteActivity
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_main_favorite.*
//
//class MainFavoriteFragment : BaseFragment<BaseActivity>() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            //            mTab = it.getString(Const.TAB)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_main_favorite
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//        pager_main_favorite.visibility = View.VISIBLE
//        val pagerAdapter = MainPagerAdapter(activity!!)
//        pagerAdapter.imageRes = intArrayOf(R.drawable.img_favorite_banner_1, R.drawable.img_favorite_banner_2, R.drawable.img_favorite_banner_3, R.drawable.img_favorite_banner_4, R.drawable.img_favorite_banner_5)
//        pager_main_favorite.adapter = pagerAdapter
//
//        indicator_main_favorite.setImageResId(R.drawable.indi_home)
//        indicator_main_favorite.visibility = View.VISIBLE
//        indicator_main_favorite.removeAllViews()
//        indicator_main_favorite.build(LinearLayout.HORIZONTAL, pagerAdapter.count)
//        pager_main_favorite.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                if (!isAdded) {
//                    return
//                }
//                indicator_main_favorite.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        text_main_favorite_tag.text = PplusCommonUtil.fromHtml(getString(R.string.html_set_favorite_tag))
//
//        layout_main_favorite_tag.setOnClickListener {
//            val intent = Intent(activity, MyFavoriteActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_DETAIL)
//
//        }
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_search"
//    }
//
//    companion object {
//
//
//        @JvmStatic
//        fun newInstance() =
//                MainFavoriteFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, tab.name)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
