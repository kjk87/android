//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentStatePagerAdapter
//import androidx.core.content.ContextCompat
//import android.util.SparseArray
//import android.view.View
//import android.view.ViewGroup
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.page.ui.PageListByCodeFragment
//import com.pplus.prnumberuser.apps.search.ui.LocationAroundPageActivity
//import com.pplus.prnumberuser.apps.search.ui.SearchActivity
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_main_pr.*
//
//class MainPRFragment : BaseFragment<BaseActivity>() {
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
//        return R.layout.fragment_main_pr
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
//        layout_main_pr_search.setOnClickListener {
//            val intent = Intent(activity, SearchActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        if(parentActivity is AppMainActivity){
//            image_main_pr_back.visibility = View.GONE
//        }
//
//        image_main_pr_back.setOnClickListener {
//            activity?.finish()
//        }
//
////        image_main_pr_pad.setOnClickListener {
////            val intent = Intent(activity, PadActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
//
//        tabLayout_main_pr.setSelectedIndicatorColors(ContextCompat.getColor(activity!!, android.R.color.transparent))
//        tabLayout_main_pr.setCustomTabView(R.layout.item_black_tab, R.id.text_black_tab)
//        tabLayout_main_pr.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_0))
//        tabLayout_main_pr.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_30), 0)
//
//
//        pager_pr.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
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
//
//        text_main_pr_view_map.setOnClickListener {
//            val intent = Intent(activity, LocationAroundPageActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//        text_main_pr_address.setSingleLine()
//        text_main_pr_address.setOnClickListener {
//            val intent = Intent(activity, LocationSelectActivity::class.java)
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width / 2
//            val y = location[1] + it.height / 2
//            intent.putExtra(Const.X, x)
//            intent.putExtra(Const.Y, y)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity!!.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//        }
//
//        PplusCommonUtil.alertLocation(parentActivity, false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//            override fun onSuccess() {
//                if (!isAdded) {
//                    return
//                }
//                PplusCommonUtil.callAddress(LocationUtil.getSpecifyLocationData(), object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                    override fun onResult(address: String) {
//
//                        if (!isAdded) {
//                            return
//                        }
//
//                        text_main_pr_address?.text = address
//                    }
//                })
//                initPager()
//            }
//        })
//
//    }
//
//    private fun initPager() {
//
////        mAdapter!!.size = 2
////        mAdapter!!.notifyDataSetChanged()
//
//        mAdapter = PagerAdapter(childFragmentManager)
//        pager_pr.adapter = mAdapter
//        pager_pr.offscreenPageLimit = 1
//        tabLayout_main_pr.setViewPager(pager_pr)
////        if(LoginInfoManager.getInstance().user.agentCode != Const.GAL_COM_CODE){
////            pager_pr.currentItem = 1
////        }else{
////            pager_pr.currentItem = 0
////        }
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_LOCATION_CODE -> {
//                PplusCommonUtil.alertLocation(parentActivity, false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//                    override fun onSuccess() {
//                        if (!isAdded) {
//                            return
//                        }
//                        PplusCommonUtil.callAddress(LocationUtil.getSpecifyLocationData(), object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                            override fun onResult(address: String) {
//
//                                if (!isAdded) {
//                                    return
//                                }
//
//                                text_main_pr_address?.text = address
//                            }
//                        })
//                        initPager()
//                    }
//                })
//            }
//        }
//    }
//
//    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//
//
//        var fragMap: SparseArray<Fragment>
//            internal set
//
//        private val titles = arrayOf(getString(R.string.word_woodongee), getString(R.string.word_pr_story))
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
////            return titles.size
//            return 1
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
////            var isOnly = true
////
////            when (position) {
////                0 -> {
////                    isOnly = true
////                }
////                1 -> {
////                    isOnly = false
////                }
////            }
//            return PageListByCodeFragment.newInstance(true)
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_number_search"
//
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                MainPRFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, type)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
