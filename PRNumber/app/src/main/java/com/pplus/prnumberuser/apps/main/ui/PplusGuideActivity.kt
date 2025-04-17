//package com.pplus.prnumberuser.apps.main.ui
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.LinearLayout
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.main.data.MainPagerAdapter
//import com.pplus.prnumberuser.apps.my.ui.MyFavoriteActivity
//import com.pplus.prnumberuser.apps.page.ui.AroundPageActivity
//import kotlinx.android.synthetic.main.activity_pplus_guide.*
//
//class PplusGuideActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_pplus_guide
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//
//        val adapter1 = MainPagerAdapter(this)
//        adapter1.imageRes = intArrayOf(R.drawable.img_pplus_guide_top_1, R.drawable.img_pplus_guide_top_2, R.drawable.img_pplus_guide_top_3)
//        pager_pplus_guide1.adapter = adapter1
//
//        indicator_pplus_guide1.removeAllViews()
//        indicator_pplus_guide1.build(LinearLayout.HORIZONTAL, adapter1.count)
//        pager_pplus_guide1.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                indicator_pplus_guide1?.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        adapter1.setOnItemClickListener(object : MainPagerAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
//                when (position) {
//                    0 -> {
//                        scroll_pplus_guide.smoothScrollTo(0, layout_pplus_guide_hotdeal.top)
//                    }
//                    1 -> {
//                        scroll_pplus_guide.smoothScrollTo(0, layout_pplus_guide_plus.top)
//                    }
//                    2 -> {
//                        scroll_pplus_guide.smoothScrollTo(0, layout_pplus_guide_fav_tag.top)
//                    }
//                }
//            }
//        })
//
//        val adapter2 = MainPagerAdapter(this)
//        adapter2.imageRes = intArrayOf(R.drawable.img_pplus_guide_hotdeal_2_1, R.drawable.img_pplus_guide_hotdeal_2_2, R.drawable.img_pplus_guide_hotdeal_2_3)
//        pager_pplus_guide2.adapter = adapter2
//
//        indicator_pplus_guide2.removeAllViews()
//        indicator_pplus_guide2.build(LinearLayout.HORIZONTAL, adapter2.count)
//        pager_pplus_guide2.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                indicator_pplus_guide2?.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        layout_pplus_guide_go_hotdeal.setOnClickListener {
////            val intent = Intent(this, MainActivity::class.java)
//            val intent = Intent(this, AppMainActivity::class.java)
//            intent.putExtra(Const.KEY, Const.HOTDEAL)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        val adapter3 = MainPagerAdapter(this)
//        adapter3.imageRes = intArrayOf(R.drawable.img_pplus_guide_plus_2_1, R.drawable.img_pplus_guide_plus_2_2, R.drawable.img_pplus_guide_plus_2_3)
//        pager_pplus_guide3.adapter = adapter3
//
//        indicator_pplus_guide3.removeAllViews()
//        indicator_pplus_guide3.build(LinearLayout.HORIZONTAL, adapter3.count)
//        pager_pplus_guide3.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                indicator_pplus_guide3?.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        layout_pplus_guide_go_plus_goods.setOnClickListener {
//            val intent = Intent(this, AroundPageActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        val adapter4 = MainPagerAdapter(this)
//        adapter4.imageRes = intArrayOf(R.drawable.img_pplus_guide_favorite_2_1, R.drawable.img_pplus_guide_favorite_2_2, R.drawable.img_pplus_guide_favorite_2_3)
//        pager_pplus_guide4.adapter = adapter4
//
//        indicator_pplus_guide4.removeAllViews()
//        indicator_pplus_guide4.build(LinearLayout.HORIZONTAL, adapter4.count)
//        pager_pplus_guide4.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                indicator_pplus_guide4?.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        layout_pplus_guide_go_fav_tag.setOnClickListener {
//            val intent = Intent(this, MyFavoriteActivity::class.java)
////            intent.putExtra(Const.KEY, Const.FAV_TAG)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_pplus_guide), ToolbarOption.ToolbarMenu.LEFT)
//        return toolbarOption
//    }
//
//    override fun getOnToolbarClickListener(): OnToolbarListener {
//
//        return OnToolbarListener { v, toolbarMenu, tag ->
//            when (toolbarMenu) {
//                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
//                    onBackPressed()
//                }
//            }
//        }
//    }
//}
