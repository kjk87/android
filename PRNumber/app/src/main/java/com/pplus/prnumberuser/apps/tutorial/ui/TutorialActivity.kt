//package com.pplus.prnumberuser.apps.tutorial.ui
//
//import android.os.Bundle
//import androidx.viewpager.widget.ViewPager
//import android.widget.LinearLayout
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.tutorial.data.TutorialPagerAdapter
//import kotlinx.android.synthetic.main.activity_tutorial.*
//
//class TutorialActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_tutorial
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        val adapter = TutorialPagerAdapter(this)
//        pager_tutorial.adapter = adapter
//        indicator_tutorial.setImageResId(R.drawable.indi_tutorial)
//        indicator_tutorial.removeAllViews()
//        indicator_tutorial.build(LinearLayout.HORIZONTAL, adapter.count)
//        pager_tutorial.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//
//                indicator_tutorial.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//    }
//
//}
