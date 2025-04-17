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
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener
//import com.pplus.prnumberuser.apps.common.mgmt.CategoryInfoManager
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.coupon.ui.CouponContainerActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Category
//import com.pplus.prnumberuser.core.network.model.dto.Count
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_main_hot_deal.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MainCouponFragment : BaseFragment<BaseActivity>() {
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
//
//        tabLayout_coupon.setSelectedIndicatorColors(ContextCompat.getColor(activity!!, R.color.color_232323))
//        tabLayout_coupon.setCustomTabView(R.layout.item_category_tab, R.id.text_category_tab)
//        tabLayout_coupon.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
////        tabLayout_category_page.setDistributeEvenly(false)
//        tabLayout_coupon.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_80), 0)
//
//
//        text_coupon_address.setSingleLine()
//        text_coupon_address.setOnClickListener {
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
//        text_main_coupon_sort.setOnClickListener {
//            val builder = AlertBuilder.Builder()
//            builder.setContents(getString(R.string.word_sort_distance), getString(R.string.word_sort_discount), getString(R.string.word_sort_recent))
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(object : OnAlertResultListener {
//
//                override fun onCancel() {
//
//                }
//
//                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                    when (event_alert.getValue()) {
//                        1 -> {
//                            mSort = "distance"
//                            text_main_coupon_sort.text = getString(R.string.word_sort_distance)
//                        }
////                        2 -> {
////                            mSort = "reward_luckybol,${EnumData.GoodsSort.desc}"
////                            text_main_coupon_sort.text = getString(R.string.word_sort_reward)
////                        }
//                        2 -> {
//                            mSort = "discount_ratio,${EnumData.GoodsSort.desc}"
//                            text_main_coupon_sort.text = getString(R.string.word_sort_discount)
//                        }
//                        3 -> {
//                            mSort = "seq_no,${EnumData.GoodsSort.desc}"
//                            text_main_coupon_sort.text = getString(R.string.word_sort_recent)
//                        }
//                    }
//
//                    for (fragment in childFragmentManager.fragments) {
//                        (fragment as CouponFragment).setData()
//                    }
//                }
//            }).builder().show(activity)
//        }
//
//        check_main_coupon_sort.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//                for (fragment in childFragmentManager.fragments) {
//                    (fragment as CouponFragment).setData()
//                }
//            }
//        })
//
//        text_main_coupon_like_count.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, CouponContainerActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        getGoodsLikeCount()
//        setData()
//    }
//
//    fun getGoodsLikeCount() {
//        if (!LoginInfoManager.getInstance().isMember) {
//            text_main_coupon_like_count.text = "0"
//            return
//        }
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
//                        text_main_coupon_like_count?.text = response.data.count.toString()
//                    }else{
//                        text_main_coupon_like_count?.text = "99+"
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
//        var locationData = LocationUtil.getSpecifyLocationData()
//        if (locationData != null) {
//            if (StringUtils.isEmpty(locationData.address)) {
//                PplusCommonUtil.callAddress(locationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                    override fun onResult(address: String) {
//                        if (!isAdded) {
//                            return
//                        }
//                        locationData = LocationUtil.getSpecifyLocationData()
//                        text_coupon_address?.text = locationData!!.address
//
//                    }
//                })
//            } else {
//                text_coupon_address?.text = locationData!!.address
//            }
//
//            getCategory()
//        }
//    }
//
//    private fun getCategory() {
//        CategoryInfoManager.getInstance().categoryListShop
//
//        val categoryList = CategoryInfoManager.getInstance().categoryListShop
//
//        val list = arrayListOf<Category>()
//
//        val category = Category()
//        category.no = -1L
//        category.name = getString(R.string.word_total)
//        list.add(category)
//
//        if (categoryList != null) {
//            list.addAll(categoryList)
//        }
//
//        mAdapter = PagerAdapter(childFragmentManager)
//        pager_coupon.adapter = mAdapter
//        mAdapter!!.setTitle(list)
//        tabLayout_coupon.setViewPager(pager_coupon)
//        pager_coupon.currentItem = currentPos
//    }
//
//    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//
//        internal var mCategoryList: MutableList<Category>
//        var fragMap: SparseArray<Fragment>
//            internal set
//
//        init {
//            fragMap = SparseArray()
//            mCategoryList = ArrayList<Category>()
//        }
//
//        fun setTitle(categoryList: MutableList<Category>) {
//
//            this.mCategoryList = categoryList
//            notifyDataSetChanged()
//        }
//
//        override fun getPageTitle(position: Int): String? {
//
//            if (position == 0) {
//                return getString(R.string.word_total)
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
//            val fragment = CouponFragment.newInstance(mCategoryList[position])
////            val fragment = CategoryGoodsFragment.newInstance(mCategoryList[position])
//            fragMap.put(position, fragment)
//            return fragment
//        }
//    }
//
//    var currentPos = 0
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_LOCATION_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    currentPos = pager_coupon.currentItem
//                    setData()
//                }
//            }
//            Const.REQ_DETAIL, Const.REQ_SIGN_IN->{
//                if (resultCode == Activity.RESULT_OK) {
//                    getGoodsLikeCount()
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
//                MainCouponFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(ARG_PARAM1, param1)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
