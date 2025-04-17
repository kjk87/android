//package com.pplus.prnumberuser.apps.my.ui
//
//import android.os.Bundle
//import androidx.viewpager.widget.ViewPager
//import android.widget.LinearLayout
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.common.data.ViewPagerAdapter
//import kotlinx.android.synthetic.main.activity_use_guide.*
//import java.util.*
//
//class UseGuideActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutView(): Int {
//        return R.layout.activity_use_guide
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        indicator_use_guide.setImageResId(R.drawable.indi_guide)
//
//        var imageResource: MutableList<Int> = ArrayList()
//        imageResource.add(R.drawable.img_popup_guide_1)
//        imageResource.add(R.drawable.img_popup_guide_2)
//        imageResource.add(R.drawable.img_popup_guide_3)
//        imageResource.add(R.drawable.img_popup_guide_4)
//        imageResource.add(R.drawable.img_popup_guide_5)
//        imageResource.add(R.drawable.img_popup_guide_6)
//
//        indicator_use_guide.build(LinearLayout.HORIZONTAL, imageResource.size)
//
//        val adapter = ViewPagerAdapter(this, imageResource)
//        pager_use_guide.adapter = adapter
//        pager_use_guide.offscreenPageLimit = imageResource.size
//
//        pager_use_guide.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener{
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                indicator_use_guide.setCurrentItem(position)
//            }
//        })
//
//        image_use_guide_close.setOnClickListener {
//            onBackPressed()
//        }
//    }
//
//}
