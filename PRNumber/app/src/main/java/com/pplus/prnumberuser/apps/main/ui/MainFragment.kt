//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.os.Handler
//import androidx.fragment.app.Fragment
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.data.EventLoadingView
//import com.pplus.prnumberuser.apps.event.ui.EventResultActivity
//import com.pplus.prnumberuser.apps.my.ui.MyFavoriteActivity
//import com.pplus.prnumberuser.apps.my.ui.MyInfoActivity
//import com.pplus.prnumberuser.apps.search.ui.LocationAroundPageActivity
//import com.pplus.prnumberuser.apps.search.ui.SearchActivity
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.dto.EventResult
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_main.*
//import kotlinx.android.synthetic.main.layout_pplus_info.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [MainFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class MainFragment : BaseFragment<AppMainActivity>() {
//
//    // TODO: Rename and change types of parameters
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        super.onCreate(savedInstanceState)
//        if (arguments != null) {
////            type = arguments.getSerializable(Const.TYPE) as EnumData.AdsType
//        }
//    }
//
//    override fun getLayoutResourceId(): Int {
//        return R.layout.fragment_main
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        val textList = listOf(getString(R.string.msg_main_prnumber1), getString(R.string.msg_main_prnumber2))
//        text_main_pr_event_random_number.setTexts(textList.toTypedArray())
//
//        pager_main_pr_event.visibility = View.GONE
////        val pagerAdapter = MainPagerAdapter(activity!!)
////        pager_main_pr_event.adapter = pagerAdapter
////        pager_main_pr_event.interval = 4000
////        pager_main_pr_event.startAutoScroll()
//
////        indicator_main_pr_event.setImageResId(R.drawable.indi_home)
////        indicator_main_pr_event.removeAllViews()
////        indicator_main_pr_event.build(LinearLayout.HORIZONTAL, pagerAdapter.count)
////        pager_main_pr_event.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
////
////            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
////
////            }
////
////            override fun onPageSelected(position: Int) {
////
////                indicator_main_pr_event?.setCurrentItem(position)
////            }
////
////            override fun onPageScrollStateChanged(state: Int) {
////
////            }
////        })
//
////        pagerAdapter.setOnItemClickListener(object : MainPagerAdapter.OnItemClickListener {
////            override fun onItemClick(position: Int) {
////                when (position) {
////                    0 -> {//prnumber 이벤트
////                        val intent = Intent(activity, PRNumberEventActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                        activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////                    }
////                    1 -> {//굿럭
////                        val intent = Intent(activity, EventActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                        activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////                    }
////                    2 -> {//로또
////                        val intent = Intent(activity, LottoActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                        activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////                    }
////                    3 -> {//복불복
////                        val intent = Intent(activity, PlayActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                        activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////                    }
////                    4 -> {//공유왕
////                        val intent = Intent(activity, ShareEventActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                        startActivity(intent)
////                    }
////                    5 -> {//랭킹왕
////                        val intent = Intent(activity, RecommendActivity::class.java)
////                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                        startActivity(intent)
////                    }
////                }
////            }
////        })
//
//
//        text_main_pr_event_retention_bol.setOnClickListener {
////            val intent = Intent(activity, MyPointActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        image_main_pr_event_my.setOnClickListener {
//            val intent = Intent(activity, MyInfoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            cashChangeLauncher.launch(intent)
//        }
//
//        image_main_pr_event_search.setOnClickListener {
//            val intent = Intent(activity, SearchActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//            startActivity(intent)
//        }
//
//        layout_main_pr_event_random_number.setOnClickListener {
//            //            val intent = Intent(activity, PadActivity::class.java)
////            intent.putExtra(Const.KEY, Const.PAD)
//////            intent.putExtra(Const.NUMBER, item.virtualNumber)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////            activity!!.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)
//
//            val intent = Intent(activity, SearchActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_main_hotdeal.setOnClickListener {
////            parentActivity.setHotdealFragment()
////            val intent = Intent(activity, HotDealActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//////            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////            startActivity(intent)
//        }
//
//        layout_main_plus.setOnClickListener {
//            val intent = Intent(activity, LocationAroundPageActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_main_tag.setOnClickListener {
//            val intent = Intent(activity, MyFavoriteActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
////        recycler_event.addOnScrollListener(RecyclerScaleScrollListener(parentActivity.image_event_pad))
//
//        layout_pplus_info_btn.setOnClickListener {
//            if (layout_pplus_info.visibility == View.GONE) {
//                text_pplus_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_up, 0)
//                layout_pplus_info.visibility = View.VISIBLE
//            } else if (layout_pplus_info.visibility == View.VISIBLE) {
//                text_pplus_info_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_footer_arrow_down, 0)
//                layout_pplus_info.visibility = View.GONE
//            }
//        }
//
//
//        getLocation()
//
//        setRetentionBol()
//    }
//
//    private fun getLocation() {
//        PplusCommonUtil.alertLocation(getParentActivity(), false, object : PplusCommonUtil.Companion.SuccessLocationListener {
//            override fun onSuccess() {
//                if (!isAdded) {
//                    return
//                }
//
//                PplusCommonUtil.callAddress(LocationUtil.specifyLocationData, object : PplusCommonUtil.Companion.OnAddressCallListener {
//
//                    override fun onResult(address: String) {
//
//                        if (!isAdded) {
//                            return
//                        }
//                    }
//                })
//
//            }
//        })
//    }
//
//    fun joinEvent(event: Event) {
//        val params = HashMap<String, String>()
//        params["no"] = event.no.toString()
//        showProgress("")
//
//        ApiBuilder.create().joinEvent(params).setCallback(object : PplusCallback<NewResultResponse<EventResult>> {
//            override fun onResponse(call: Call<NewResultResponse<EventResult>>?, response: NewResultResponse<EventResult>?) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                if (response!!.data != null) {
//                    if (event.winAnnounceType.equals(EventType.WinAnnounceType.immediately.name)) {
//                        val mLoading = EventLoadingView()
//                        mLoading.isCancelable = false
//                        mLoading.setText(getString(R.string.msg_checking_event_result))
//                        var delayTime = 2000L
//                        mLoading.isCancelable = false
//                        try {
//                            mLoading.show(parentFragmentManager, "")
//                        } catch (e: Exception) {
//
//                        }
//
//                        val handler = Handler()
//                        handler.postDelayed({
//
//                            try {
//                                mLoading.dismiss()
//                            } catch (e: Exception) {
//
//                            }
//
//                            val intent = Intent(activity, EventResultActivity::class.java)
//                            intent.putExtra(Const.EVENT_RESULT, response.data)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            cashChangeLauncher.launch(intent)
//                        }, delayTime)
//
//                    } else {
//                        val intent = Intent(activity, EventResultActivity::class.java)
//                        intent.putExtra(Const.EVENT_RESULT, response.data)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        cashChangeLauncher.launch(intent)
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<EventResult>>?, t: Throwable?, response: NewResultResponse<EventResult>?) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                if (response != null) {
//                    PplusCommonUtil.showEventAlert(activity!!, response.resultCode, event)
//                }
//
//            }
//        }).build().call()
//    }
//
//    val cashChangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        setRetentionBol()
//    }
//
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//
//                text_main_pr_event_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
//    }
//
//    override fun getPID(): String {
//        return "Main_pr_eventlist"
//    }
//
//    companion object {
//
//        fun newInstance(): MainFragment {
//
//            val fragment = MainFragment()
//            val args = Bundle()
////            args.putSerializable(Const.TYPE, type)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
