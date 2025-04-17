//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.app.Activity.RESULT_OK
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import android.widget.LinearLayout
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.ui.PlayActivity
//import com.pplus.prnumberuser.apps.main.data.CategoryHeaderThemeAdapter
//import com.pplus.prnumberuser.apps.main.data.MainPagerAdapter
//import com.pplus.prnumberuser.apps.search.ui.LocationAroundPageActivity
//import com.pplus.prnumberuser.apps.search.ui.SearchActivity
//import com.pplus.prnumberuser.apps.theme.ui.ThemeDetailActivity
//import com.pplus.prnumberuser.core.code.common.PageTypeCode
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Category
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_main_home.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MainHomeFragment : BaseFragment<BaseActivity>() {
//
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
//        return R.layout.fragment_main_home
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    private var mTotalCount: Int = 0
//    private var mLockListView = false
//    private var mPaging = 0
//    private var mAdapter: CategoryHeaderThemeAdapter? = null
//    private var mLayoutManager: androidx.recyclerview.widget.LinearLayoutManager? = null
//
//    override fun init() {
//
////        appbar_main_home.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
////            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
////                if (!isAdded) {
////                    return
////                }
////                if (verticalOffset <= -collapsing_main_home.height + toolbar_main_home.height) {
////                    //toolbar is collapsed here
////                    //write your code here
////
////                    if (image_main_home_address.tag != "collapsed") {
////                        image_main_home_address.tag = "collapsed"
////
////                        image_main_home_address.setImageResource(R.drawable.ic_top_location)
////                        text_main_home_address.setTextColor(ResourceUtil.getColor(activity, R.color.color_232323))
////                        image_main_home_address_arrow.setImageResource(R.drawable.ic_top_location_arrow)
////                        image_main_home_search.setImageResource(R.drawable.ic_gift_search)
////                        image_main_home_view_map.setImageResource(R.drawable.ic_top_map)
////                    }
////
////
////                } else {
////                    if (image_main_home_address.tag != "uncollapsed") {
////                        image_main_home_address.tag = "uncollapsed"
////                        image_main_home_address.setImageResource(R.drawable.ic_top_location_white)
////                        text_main_home_address.setTextColor(ResourceUtil.getColor(activity, R.color.white))
////                        image_main_home_address_arrow.setImageResource(R.drawable.ic_top_location_arrow_white)
////                        image_main_home_search.setImageResource(R.drawable.ic_gift_search_white)
////                        image_main_home_view_map.setImageResource(R.drawable.ic_top_map_white)
////                    }
////
////
////                }
////            }
////        })
//
//        text_main_home_address.setSingleLine()
//
//        pager_main_home.visibility = View.VISIBLE
//        val pagerAdapter = MainPagerAdapter(activity!!)
//        pager_main_home.adapter = pagerAdapter
//        pager_main_home.interval = 4000
//        pager_main_home.startAutoScroll()
//
//
//        pagerAdapter.setOnItemClickListener(object : MainPagerAdapter.OnItemClickListener{
//            override fun onItemClick(position: Int) {
//                setBannerAction(position)
//            }
//        })
//        text_main_home_banner_more.setOnClickListener {
//            val intent = Intent(activity, BannerListActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_BANNER)
//        }
//
//        indicator_main_home.visibility = View.VISIBLE
//        indicator_main_home.removeAllViews()
//        indicator_main_home.build(LinearLayout.HORIZONTAL, pagerAdapter.count)
//        pager_main_home.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                if (!isAdded) {
//                    return
//                }
//                indicator_main_home.setCurrentItem(position)
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//        })
//
//        mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
//        recycler_main_home.layoutManager = mLayoutManager!!
//        mAdapter = CategoryHeaderThemeAdapter(activity!!, PageTypeCode.store.name)
//        recycler_main_home.adapter = mAdapter
////        recycler_main_home.addItemDecoration(BottomItemOffsetDecoration(activity!!, R.dimen.height_60))
//
//        text_main_home_address.setOnClickListener {
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
//        image_main_home_search.setOnClickListener {
//            val intent = Intent(activity, SearchActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            activity!!.startActivityForResult(intent, Const.REQ_SEARCH)
//        }
//
//        image_main_home_view_map.setOnClickListener {
//            val intent = Intent(activity, LocationAroundPageActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity!!.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//        }
//
//        mAdapter!!.setOnItemClickListener(object : CategoryHeaderThemeAdapter.OnItemClickListener {
//
//            override fun onItemClick(position: Int, view: View) {
//                val intent = Intent(activity, ThemeDetailActivity::class.java)
//                intent.putExtra(Const.DATA, mAdapter!!.getItem(position))
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }
//        })
//
//        getLocation()
//        getCategoryAll()
//    }
//
//    private fun getLocation() {
//        PplusCommonUtil.alertLocation(parentActivity, false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//            override fun onSuccess() {
//                if (!isAdded) {
//                    return
//                }
//
//                PplusCommonUtil.callAddress(LocationUtil.getSpecifyLocationData(), object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                    override fun onResult(address: String) {
//
//                        if (!isAdded) {
//                            return
//                        }
//                        text_main_home_address?.text = address
//                    }
//                })
//
//            }
//        })
//    }
//
//    private fun getCategoryAll() {
//        val params = HashMap<String, String>()
//        params["thema"] = "true"
//        ApiBuilder.create().getCategoryAll(params).setCallback(object : PplusCallback<NewResultResponse<Category>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Category>>, response: NewResultResponse<Category>) {
//
//                val categoryList = response.datas
//                mAdapter!!.addAll(categoryList)
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Category>>, t: Throwable, response: NewResultResponse<Category>) {
//
//            }
//        }).build().call()
//    }
//
//    private fun setBannerAction(position:Int){
//        when (position) {
//            0 -> {
//                recycler_main_home.smoothScrollToPosition(3)
//                appbar_main_home.setExpanded(false, false)
//            }
//            1 -> {
////                if (parentActivity is AppMainActivity2) {
////                    (parentActivity as AppMainActivity2).setGoodsFragment()
////                }
//            }
//            2 -> {
////                if (parentActivity is AppMainActivity2) {
////                    (parentActivity as AppMainActivity2).setPlusGoodsFragment()
////                }
//            }
//            3 -> {
//                PplusCommonUtil.openChromeWebView(activity!!, getString(R.string.msg_goods_url)+"?timestamp=" + System.currentTimeMillis())
//            }
//            4 -> {
//                PplusCommonUtil.openChromeWebView(activity!!, getString(R.string.msg_point_url)+"?timestamp=" + System.currentTimeMillis())
//            }
//            5 -> {
//                PplusCommonUtil.openChromeWebView(activity!!, getString(R.string.msg_seller2_url)+"?timestamp=" + System.currentTimeMillis())
//            }
//            6 -> {
//                val intent = Intent(activity, PlayActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_LOCATION_CODE -> {
//                getLocation()
//            }
//            Const.REQ_BANNER -> if (resultCode == RESULT_OK) {
//                if(data != null){
//                    val pos = data.getIntExtra(Const.POSITION, 0)
//                    setBannerAction(pos)
//                }
//            }
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main"
//    }
//
//    companion object {
//
//
//        @JvmStatic
//        fun newInstance() =
//                MainHomeFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, tab.name)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
