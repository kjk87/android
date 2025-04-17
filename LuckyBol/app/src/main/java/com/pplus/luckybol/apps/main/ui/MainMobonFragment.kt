//package com.pplus.luckybol.apps.main.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.SparseArray
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentStatePagerAdapter
//import androidx.viewpager.widget.ViewPager
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.alert.AlertProfileSetActivity
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.common.ui.base.BaseFragment
//import com.pplus.luckybol.apps.mobon.ui.MobonMallFragment
//import com.pplus.luckybol.apps.setting.ui.ProfileConfigActivity
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.CategoryFavorite
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.networks.common.PplusCallback
//import com.pplus.utils.part.pref.PreferenceUtil
//import kotlinx.android.synthetic.main.fragment_main_mobon.*
//import retrofit2.Call
//
//class MainMobonFragment : BaseFragment<BaseActivity>() {
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
//        return R.layout.fragment_main_mobon
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mAdapter: PagerAdapter? = null
//
//    override fun init() {
//
//
//        tabLayout_main_mobon.setIsChangeBold(false)
//        tabLayout_main_mobon.setSelectedIndicatorColors(ContextCompat.getColor(activity!!, R.color.color_232323))
//        tabLayout_main_mobon.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
//        tabLayout_main_mobon.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
//        tabLayout_main_mobon.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)
//
//
//        pager_main_mobon.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {}
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
//
//            override fun onPageSelected(position: Int) {
//                when (position) {
////                    0 -> {
////                        setSelect(layout_main_pr_store, layout_main_pr_person, layout_main_pr_friend)
////                        setBold(text_main_pr_store, text_main_pr_person, text_main_pr_friend)
////                    }
////                    1 -> {
////                        setSelect(layout_main_pr_person, layout_main_pr_store, layout_main_pr_friend)
////                        setBold(text_main_pr_person, text_main_pr_store, text_main_pr_friend)
////                    }
////                    2->{
////                        setSelect(layout_main_pr_friend, layout_main_pr_person, layout_main_pr_store)
////                        setBold(text_main_pr_friend, text_main_pr_person, text_main_pr_store)
////                    }
//                }
//            }
//        })
//        initPager()
//
//    }
//
//
//    private fun initPager() {
//
////        mAdapter!!.size = 2
////        mAdapter!!.notifyDataSetChanged()
//
//        mAdapter = PagerAdapter(childFragmentManager)
//        pager_main_mobon.adapter = mAdapter
//        pager_main_mobon.offscreenPageLimit = 2
//        tabLayout_main_mobon.setViewPager(pager_main_mobon)
//        pager_main_mobon.currentItem = 0
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
////            Const.REQ_SIGN_IN, Const.REQ_SET_PROFILE->{
////                if(LoginInfoManager.getInstance().isMember){
////                    checkFloatingVisible()
////                }
////            }
//        }
//    }
//
//    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//
//        var fragMap: SparseArray<Fragment>
//            internal set
//
//        private val titles = arrayOf(getString(R.string.word_woman_clothes), getString(R.string.word_men_clothes))
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
//        fun getFragment(key: Int): Fragment {
//
//            return fragMap.get(key)
//        }
//
//        override fun getItem(position: Int): Fragment {
//
//            return MobonMallFragment.newInstance(position)
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_shopping"
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                MainMobonFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, type)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
