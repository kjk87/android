//package com.pplus.prnumberuser.apps.main.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.util.SparseArray
//import android.view.View
//import android.view.ViewGroup
//import android.widget.CompoundButton
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentStatePagerAdapter
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.alert.AlertHotDealLiveActivity
//import com.pplus.prnumberuser.apps.common.mgmt.CategoryInfoManager
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.goods.ui.GoodsLikeActivity
//import com.pplus.prnumberuser.apps.search.ui.LocationAroundGoodsActivity
//import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.CategoryMajor
//import com.pplus.prnumberuser.core.network.model.dto.Count
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_main_hot_deal.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MainHotDealFragment : BaseFragment<BaseActivity>() {
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_main_hot_deal
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    var mAdapter: PagerAdapter? = null
//    var mSort = "distance"
//
//    override fun init() {
//        tabLayout_hot_deal.setIsChangeBold(false)
//        tabLayout_hot_deal.setSelectedIndicatorColors(ContextCompat.getColor(activity!!, R.color.color_232323))
//        tabLayout_hot_deal.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
//        tabLayout_hot_deal.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
////        tabLayout_category_page.setDistributeEvenly(false)
//        tabLayout_hot_deal.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)
//
//
//        text_hot_deal_address.setSingleLine()
//        text_hot_deal_address.setOnClickListener {
//            val intent = Intent(activity, LocationSelectActivity::class.java)
//            val location = IntArray(2)
//            it.getLocationOnScreen(location)
//            val x = location[0] + it.width / 2
//            val y = location[1] + it.height / 2
//            intent.putExtra(Const.X, x)
//            intent.putExtra(Const.Y, y)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_LOCATION_CODE)
//        }
//
//        text_main_hot_deal_sort_distance.setOnClickListener {
//
//            text_main_hot_deal_sort_distance.isSelected = true
//            text_main_hot_deal_sort_discount.isSelected = false
//
//            mSort = "distance"
//
//            for (fragment in childFragmentManager.fragments) {
//                (fragment as HotDealListFragment).setData()
//            }
//        }
//
//        text_main_hot_deal_sort_discount.setOnClickListener {
//
//            text_main_hot_deal_sort_distance.isSelected = false
//            text_main_hot_deal_sort_discount.isSelected = true
//
//            mSort = "discount_ratio,${EnumData.GoodsSort.desc}"
//
//            for (fragment in childFragmentManager.fragments) {
//                (fragment as HotDealListFragment).setData()
//            }
//        }
//
//        text_main_hot_deal_sort_distance.isSelected = true
//        text_main_hot_deal_sort_discount.isSelected = false
//
//        mSort = "distance"
//
////        text_main_hot_deal_sort.setOnClickListener {
////            val builder = AlertBuilder.Builder()
////            builder.setContents(getString(R.string.word_sort_distance), getString(R.string.word_sort_discount), getString(R.string.word_sort_recent))
////            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
////
////                override fun onCancel() {
////
////                }
////
////                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
////                    when (event_alert.getValue()) {
////                        1 -> {
////                            mSort = "distance"
////                            text_main_hot_deal_sort.text = getString(R.string.word_sort_distance)
////                        }
//////                        2 -> {
//////                            mSort = "reward_luckybol,${EnumData.GoodsSort.desc}"
//////                            text_main_hot_deal_sort.text = getString(R.string.word_sort_reward)
//////                        }
////                        2 -> {
////                            mSort = "discount_ratio,${EnumData.GoodsSort.desc}"
////                            text_main_hot_deal_sort.text = getString(R.string.word_sort_discount)
////                        }
////                        3 -> {
////                            mSort = "seq_no,${EnumData.GoodsSort.desc}"
////                            text_main_hot_deal_sort.text = getString(R.string.word_sort_recent)
////                        }
////                    }
////
////                    for (fragment in childFragmentManager.fragments) {
////                        (fragment as HotDealListFragment).setData()
////                    }
////                }
////            }).builder().show(activity)
////        }
//
//        check_main_hot_deal_sort.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//                if(isChecked){
//                    if (PreferenceUtil.getDefaultPreference(activity).get(Const.GUIDE_HOT_DEAL_LIVE, true)) {
//                        val intent = Intent(activity, AlertHotDealLiveActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        startActivity(intent)
//                    }
//                }
//
//                for (fragment in childFragmentManager.fragments) {
//                    (fragment as HotDealListFragment).setData()
//                }
//            }
//        })
//
//        text_main_hot_deal_like_count.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, GoodsLikeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        text_hot_deal_view_map.setOnClickListener {
//            val intent = Intent(activity, LocationAroundGoodsActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        text_main_hot_deal_login.setOnClickListener {
//            val intent = Intent(activity, SnsLoginActivity::class.java)
//            activity?.startActivityForResult(intent, Const.REQ_SIGN_IN)
//        }
//
//        loginCheck()
//        setData()
//    }
//
//    private fun loginCheck(){
//        if (LoginInfoManager.getInstance().isMember) {
//            getGoodsLikeCount()
//            text_main_hot_deal_login.visibility = View.GONE
//            text_main_hot_deal_title.visibility = View.VISIBLE
//        }else{
//            text_main_hot_deal_login.visibility = View.VISIBLE
//            text_main_hot_deal_title.visibility = View.GONE
//            text_main_hot_deal_like_count.text = "0"
//        }
//    }
//
//    private fun getGoodsLikeCount() {
//        val params = HashMap<String, String>()
//
//        params["memberSeqNo"] = LoginInfoManager.getInstance().user.no.toString()
//        ApiBuilder.create().getGoodsLikeCount(params).setCallback(object : PplusCallback<NewResultResponse<Count>> {
//            override fun onResponse(call: Call<NewResultResponse<Count>>?, response: NewResultResponse<Count>?) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                if (response != null && response.data != null) {
//                    if(response.data.count!! < 100){
//                        text_main_hot_deal_like_count?.text = response.data.count.toString()
//                    }else{
//                        text_main_hot_deal_like_count?.text = "99+"
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Count>>?, t: Throwable?, response: NewResultResponse<Count>?) {
//
//            }
//        }).build().call()
//    }
//
//    private fun setData() {
//
//        PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//            override fun onSuccess() {
//                if (!isAdded) {
//                    return
//                }
//                PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                    override fun onResult(address: String) {
//
//                        if (!isAdded) {
//                            return
//                        }
//
//                        text_hot_deal_address?.text = address
//                    }
//                })
//                getCategory()
//            }
//        })
//    }
//
//    private fun getCategory() {
//
//        val categoryList = CategoryInfoManager.getInstance().categoryList.filter {
//            it.type == "offline" || it.type == "common"
//        }
//
//        val list = arrayListOf<CategoryMajor>()
//
//        val category = CategoryMajor()
//        category.seqNo = -1L
//        category.name = getString(R.string.word_hot_deal)
//        list.add(category)
//
//        if (categoryList != null) {
//            list.addAll(categoryList)
//        }
//
//        mAdapter = PagerAdapter(childFragmentManager)
//        pager_hot_deal.adapter = mAdapter
//        mAdapter!!.setTitle(list)
//        tabLayout_hot_deal.setViewPager(pager_hot_deal)
//        pager_hot_deal.currentItem = currentPos
//    }
//
//    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//
//        internal var mCategoryList: MutableList<CategoryMajor>
//        var fragMap: SparseArray<Fragment>
//            internal set
//
//        init {
//            fragMap = SparseArray()
//            mCategoryList = ArrayList()
//        }
//
//        fun setTitle(categoryList: MutableList<CategoryMajor>) {
//
//            this.mCategoryList = categoryList
//            notifyDataSetChanged()
//        }
//
//        override fun getPageTitle(position: Int): String? {
//
//            if (position == 0) {
//                return getString(R.string.word_hot_deal)
//            }
//
//            return mCategoryList[position].name
//        }
//
//        override fun getCount(): Int {
//
//            return mCategoryList.size
//        }
//
//        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            super.destroyItem(container, position, `object`)
//            fragMap.remove(position)
//        }
//
//        fun clear() {
//
//            mCategoryList.clear()
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
//            val fragment = HotDealListFragment.newInstance(mCategoryList[position])
////            val fragment = CategoryGoodsFragment.newInstance(mCategoryList[position])
//            fragMap.put(position, fragment)
//            return fragment
//        }
//    }
//
//    var currentPos = 0
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        loginCheck()
//        when (requestCode) {
//            Const.REQ_LOCATION_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    currentPos = pager_hot_deal.currentItem
//                    setData()
//                }
//            }
//        }
//    }
//
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            //            param1 = it.getString(ARG_PARAM1)
////            param2 = it.getString(ARG_PARAM2)
//        }
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                MainHotDealFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
