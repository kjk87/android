//package com.pplus.prnumberuser.apps.page.ui
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentStatePagerAdapter
//import androidx.core.content.ContextCompat
//import android.util.SparseArray
//import android.view.ViewGroup
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.goods.ui.PageGoodsFragment
//import com.pplus.prnumberuser.apps.post.ui.PagePostFragment
//import kotlinx.android.synthetic.main.activity_plus_friend.*
//
//class PageNewsActivity : BaseActivity(), ImplToolbar {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_plus_friend
//    }
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//        tabLayout_plus_friend.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.color_232323))
//        tabLayout_plus_friend.setCustomTabView(R.layout.item_goods_category_tab, R.id.text_goods_category_tab)
//        tabLayout_plus_friend.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_10))
//        tabLayout_plus_friend.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_80), 0)
//
//        val adapter = PagerAdapter(supportFragmentManager)
//        pager_plus_friend.adapter = adapter
//        pager_plus_friend.offscreenPageLimit = 2
//        tabLayout_plus_friend.setViewPager(pager_plus_friend)
//        pager_plus_friend.currentItem = 0
//    }
//
//    inner class PagerAdapter(fm: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {
//
//        var fragMap: SparseArray<androidx.fragment.app.Fragment>
//            internal set
//
//        private val titles = arrayOf(getString(R.string.word_news), getString(R.string.word_plus_goods_title))
//
//        init {
//            fragMap = SparseArray()
//        }
//
//        override fun getPageTitle(position: Int): String? {
//
//            return titles[position]
//        }
//
//        override fun getCount(): Int {
//            return titles.size
//        }
//
//        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            super.destroyItem(container, position, `object`)
//            fragMap.remove(position)
//        }
//
//        fun clear() {
//
//            fragMap = SparseArray()
//            notifyDataSetChanged()
//        }
//
//        fun getFragment(key: Int): androidx.fragment.app.Fragment {
//
//            return fragMap.get(key)
//        }
//
//        override fun getItem(position: Int): androidx.fragment.app.Fragment {
//
//            when (position) {
//                0 -> {
//                    return PagePostFragment.newInstance(intent.getParcelableExtra(Const.PAGE))
//                }
//                else -> {
//                    return PageGoodsFragment.newInstance(intent.getParcelableExtra(Const.PAGE))
//                }
//            }
//        }
//    }
//
//    override fun getToolbarOption(): ToolbarOption {
//
//        val toolbarOption = ToolbarOption(this)
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_news), ToolbarOption.ToolbarMenu.LEFT)
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
