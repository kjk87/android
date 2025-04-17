//package com.pplus.prnumberuser.apps.main.ui
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
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.alert.AlertProfileSetActivity
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.ui.EventByGroupFragment
//import com.pplus.prnumberuser.apps.setting.ui.ProfileConfigActivity
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.CategoryFavorite
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import kotlinx.android.synthetic.main.fragment_main_event.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//
//class MainEventFragment : BaseFragment<BaseActivity>() {
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
//        return R.layout.fragment_main_event
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
//        tabLayout_main_event.setIsChangeBold(false)
//        tabLayout_main_event.setSelectedIndicatorColors(ContextCompat.getColor(activity!!, R.color.color_232323))
//        tabLayout_main_event.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
//        tabLayout_main_event.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
//        tabLayout_main_event.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)
//
//
//        pager_main_event.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
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
//        layout_main_event_floating.setOnClickListener {
//            if (PreferenceUtil.getDefaultPreference(activity).get(Const.GUIDE_PROFILE_SET, true)) {
//                val intent = Intent(activity, AlertProfileSetActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_SET_PROFILE)
//            }else{
//                val intent = Intent(activity, ProfileConfigActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_SET_PROFILE)
//            }
//        }
//
//        layout_main_event_floating.visibility = View.GONE
//        if(LoginInfoManager.getInstance().isMember){
//            checkFloatingVisible()
//        }
//        initPager()
//
//    }
//
//    private fun checkFloatingVisible(){
//        if(LoginInfoManager.getInstance().user.activeArea1Value == null && LoginInfoManager.getInstance().user.activeArea2Value == null){
//            layout_main_event_floating.visibility = View.VISIBLE
//        }else{
//            showProgress("")
//            ApiBuilder.create().myFavoriteCategoryList.setCallback(object : PplusCallback<NewResultResponse<CategoryFavorite>> {
//                override fun onResponse(call: Call<NewResultResponse<CategoryFavorite>>?, response: NewResultResponse<CategoryFavorite>?) {
//                    hideProgress()
//                    if (response?.datas != null && response.datas.isNotEmpty()) {
//                        layout_main_event_floating.visibility = View.GONE
//                    }else{
//                        layout_main_event_floating.visibility = View.VISIBLE
//                    }
//
//                }
//
//                override fun onFailure(call: Call<NewResultResponse<CategoryFavorite>>?, t: Throwable?, response: NewResultResponse<CategoryFavorite>?) {
//                    hideProgress()
//                    layout_main_event_floating.visibility = View.VISIBLE
//                }
//            }).build().call()
//        }
//    }
//
//    private fun initPager() {
//
////        mAdapter!!.size = 2
////        mAdapter!!.notifyDataSetChanged()
//
//        mAdapter = PagerAdapter(childFragmentManager)
//        pager_main_event.adapter = mAdapter
//        pager_main_event.offscreenPageLimit = 2
//        tabLayout_main_event.setViewPager(pager_main_event)
//        pager_main_event.currentItem = 0
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_SIGN_IN, Const.REQ_SET_PROFILE->{
//                if(LoginInfoManager.getInstance().isMember){
//                    checkFloatingVisible()
//                }
//            }
//        }
//    }
//
//    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//
//        var fragMap: SparseArray<Fragment>
//            internal set
//
//        private val titles = arrayOf(getString(R.string.word_daily_event), getString(R.string.word_brand_event))
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
//
//            when (position) {
//                0 -> {
//                    return EventByGroupFragment.newInstance(1)
//                }
//                else -> {
//                    return EventByGroupFragment.newInstance(2)
//                }
//            }
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                MainEventFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, type)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
