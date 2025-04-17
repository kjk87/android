//package com.pplus.luckybol.apps.main.ui
//
//
//import android.app.Activity
//import android.content.Intent
//import android.os.Bundle
//import android.util.SparseArray
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentStatePagerAdapter
//import com.pplus.luckybol.Const
//import com.pplus.luckybol.R
//import com.pplus.luckybol.apps.bol.ui.CashExchangeActivity
//import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
//import com.pplus.luckybol.apps.common.ui.base.BaseActivity
//import com.pplus.luckybol.apps.common.ui.base.BaseFragment
//import com.pplus.luckybol.apps.event.ui.Event12Activity
//import com.pplus.luckybol.apps.event.ui.PlayActivity
//import com.pplus.luckybol.apps.mobilegift.ui.MobileGiftHistoryActivity
//import com.pplus.luckybol.apps.mobilegift.ui.MobileGiftListFragment
//import com.pplus.luckybol.apps.signin.ui.SnsLoginActivity
//import com.pplus.luckybol.core.network.ApiBuilder
//import com.pplus.luckybol.core.network.model.dto.MobileGiftCategory
//import com.pplus.luckybol.core.network.model.response.NewResultResponse
//import com.pplus.luckybol.core.util.PplusCommonUtil
//import com.pplus.networks.common.PplusCallback
//import kotlinx.android.synthetic.main.fragment_mobile_gift.*
//import retrofit2.Call
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [MobileGiftFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class MobileGiftFragment : BaseFragment<BaseActivity>() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
////            mParam1 = arguments!!.getString(ARG_PARAM1)
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_mobile_gift
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//    }
//
//    override fun onPause() {
//        super.onPause()
//    }
//
//    private var mFragmentAdapter: PagerAdapter? = null
//
//    override fun init() {
//
//        layout_mobile_gift_event.setOnClickListener {
//            val intent = Intent(activity, Event12Activity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        layout_mobile_gift_play.setOnClickListener {
//            val intent = Intent(activity, PlayActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        text_mobile_gift_purchase_history.setOnClickListener {
//            val intent = Intent(activity, MobileGiftHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        layout_mobile_gift_cash_exchange.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//
//            val intent = Intent(activity, CashExchangeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        tabLayout_mobile_gift_category.setSelectedIndicatorColors(ContextCompat.getColor(activity!!, R.color.color_232323))
//        tabLayout_mobile_gift_category.setCustomTabView(R.layout.item_mobile_gift_tab, R.id.text_mobile_gift_category)
//        tabLayout_mobile_gift_category.setBottomBorder(resources.getDimensionPixelSize(R.dimen.height_8))
//        tabLayout_mobile_gift_category.setDistributeEvenly(false)
//        tabLayout_mobile_gift_category.setDividerWidthHeight(resources.getDimensionPixelSize(R.dimen.width_50), 0)
//
////        text_mobile_gift_retention_bol.setOnClickListener {
////            val intent = Intent(activity, PointConfigActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
//
//        text_mobile_gift_login_join.setOnClickListener {
//            val intent = Intent(activity, SnsLoginActivity::class.java)
//            activity?.startActivityForResult(intent, Const.REQ_SIGN_IN)
//        }
//
//        image_mobile_gift_back.setOnClickListener {
//            activity?.onBackPressed()
//        }
//
//        if (LoginInfoManager.getInstance().isMember) {
//            text_mobile_gift_title.visibility = View.VISIBLE
//            text_mobile_gift_login_join.visibility = View.GONE
//            text_mobile_gift_purchase_history.visibility = View.VISIBLE
//            setRetentionBol()
//        } else {
//            text_mobile_gift_title.visibility = View.GONE
//            text_mobile_gift_login_join.visibility = View.VISIBLE
//            text_mobile_gift_purchase_history.visibility = View.GONE
//        }
//        getCategory()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_SIGN_IN -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    if (LoginInfoManager.getInstance().isMember) {
//                        text_mobile_gift_title.visibility = View.VISIBLE
//                        text_mobile_gift_login_join.visibility = View.GONE
//                        text_mobile_gift_purchase_history.visibility = View.VISIBLE
//                        setRetentionBol()
//                    } else {
//                        text_mobile_gift_title.visibility = View.GONE
//                        text_mobile_gift_login_join.visibility = View.VISIBLE
//                        text_mobile_gift_purchase_history.visibility = View.GONE
//                    }
//                }
//            }
//            Const.REQ_CASH_CHANGE -> {
//                if (LoginInfoManager.getInstance().isMember) {
//                    text_mobile_gift_title.visibility = View.VISIBLE
//                    text_mobile_gift_login_join.visibility = View.GONE
//                    setRetentionBol()
//                } else {
//                    text_mobile_gift_title.visibility = View.GONE
//                    text_mobile_gift_login_join.visibility = View.VISIBLE
//                }
//
//            }
//        }
//    }
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//            override fun reload() {
//                if (!isAdded) {
//                    return
//                }
////                text_mobile_gift_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_bol_unit, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
//    }
//
//    private fun getCategory() {
//
//        ApiBuilder.create().mobileGiftCategoryAll.setCallback(object : PplusCallback<NewResultResponse<MobileGiftCategory>> {
//
//            override fun onResponse(call: Call<NewResultResponse<MobileGiftCategory>>, response: NewResultResponse<MobileGiftCategory>) {
//                if (!isAdded) {
//                    return
//                }
//
//                mFragmentAdapter = PagerAdapter(childFragmentManager)
//
////                val categoryList = ArrayList<MobileGiftCategory>()
////                val category = MobileGiftCategory()
////                category.name = getString(R.string.word_play)
////                categoryList.add(category)
////                categoryList.addAll(response.datas)
//                mFragmentAdapter!!.setTitle(response.datas)
//                pager_mobile_gift?.adapter = mFragmentAdapter
//                tabLayout_mobile_gift_category.setViewPager(pager_mobile_gift)
//                pager_mobile_gift?.currentItem = 0
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<MobileGiftCategory>>, t: Throwable, response: NewResultResponse<MobileGiftCategory>) {
//
//            }
//        }).build().call()
//    }
//
//    inner class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
//
//        internal var mCategoryList: MutableList<MobileGiftCategory>
//        var fragMap: SparseArray<Fragment>
//            internal set
//
//        init {
//            fragMap = SparseArray()
//            mCategoryList = ArrayList()
//        }
//
//        fun setTitle(categoryList: MutableList<MobileGiftCategory>) {
//
//            this.mCategoryList = categoryList
//            notifyDataSetChanged()
//        }
//
//        override fun getPageTitle(position: Int): String? {
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
////            if (position == 0) {
////                val fragment = PlayFragment.newInstance()
////                fragMap.put(position, fragment)
////                return fragment
////            } else {
////                val fragment = MobileGiftListFragment.newInstance(mCategoryList[position], position)
////                fragMap.put(position, fragment)
////                return fragment
////            }
//            val fragment = MobileGiftListFragment.newInstance(mCategoryList[position], position)
//            fragMap.put(position, fragment)
//            return fragment
//
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_mypage_ Mobile Coupon"
//    }
//
//    companion object {
//
//        fun newInstance(): MobileGiftFragment {
//
//            val fragment = MobileGiftFragment()
//            val args = Bundle()
////            args.putString(ARG_PARAM1, param1)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
