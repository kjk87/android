//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.graphics.Color
//import android.os.Bundle
//import android.os.Handler
//import android.os.SystemClock
//import android.view.View
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.igaworks.IgawCommon
//import com.igaworks.adbrix.IgawAdbrix
//import com.igaworks.adpopcorn.IgawAdpopcorn
//import com.igaworks.adpopcorn.IgawAdpopcornExtension
//import com.igaworks.adpopcorn.interfaces.IAdPOPcornEventListener
//import com.igaworks.adpopcorn.style.ApStyleManager
//import com.pplus.utils.part.apps.permission.Permission
//import com.pplus.utils.part.apps.permission.PermissionListener
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.utils.part.utils.time.DateFormatUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.bol.ui.PointConfigActivity
//import com.pplus.prnumberuser.apps.common.builder.PPlusPermission
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.data.EventLoadingView
//import com.pplus.prnumberuser.apps.event.ui.*
//import com.pplus.prnumberuser.apps.post.ui.UserPostActivity
//import com.pplus.prnumberuser.apps.recommend.ui.RecommendActivity
//import com.pplus.prnumberuser.core.code.common.EventType
//import com.pplus.prnumberuser.core.location.LocationUtil
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.network.model.dto.Event
//import com.pplus.prnumberuser.core.network.model.dto.EventResult
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import kotlinx.android.synthetic.main.fragment_main_home2.*
//import com.pplus.networks.common.PplusCallback
//import retrofit2.Call
//import java.util.*
//
//class MainHomeFragment2 : BaseFragment<BaseActivity>() {
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
//        return R.layout.fragment_main_home2
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun init() {
//
//        text_home2_retention_bol.setOnClickListener {
//            val intent = Intent(activity, PointConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
////        image_home2_search.setOnClickListener {
////            val intent = Intent(activity, SearchActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////        }
//
//        image_home2_back.setOnClickListener {
//            activity?.onBackPressed()
//        }
//
//
////        if(Const.API_URL.startsWith("https://api")){
////            image_home2_shop.visibility = View.GONE
////            image_main_pad.visibility = View.GONE
////        }else{
////            image_home2_shop.visibility = View.VISIBLE
////            image_main_pad.visibility = View.VISIBLE
////            getLocation()
////        }
//
////        image_home2_shop.visibility = View.VISIBLE
////        getLocation()
//
//        image_home2_good_luck.setOnClickListener {
//            val intent = Intent(activity, EventActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
////        image_home2_shop.setOnClickListener {
////            val intent = Intent(activity, OrderPageActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
//
//        image_home2_number.setOnClickListener {
//            val intent = Intent(activity, NumberEventActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        image_home2_good_play.setOnClickListener {
//            val intent = Intent(activity, PlayActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        image_home2_real_impression.setOnClickListener {
//            val intent = Intent(activity, UserPostActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_home2_share.setOnClickListener {
//            val intent = Intent(activity, ShareEventActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_home2_invite.setOnClickListener {
//            val intent = Intent(activity, RecommendActivity::class.java)
////            val intent = Intent(activity, LottoEventDetailActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
////        image_main_pad.setOnClickListener {
////            val intent = Intent(activity, PadActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
////            startActivity(intent)
////        }
//
//        image_home2_adpopcorn.setOnClickListener {
//            val pPlusPermission = PPlusPermission(activity)
//            pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE)
//            pPlusPermission.addPermission(Permission.PERMISSION_KEY.PPLUS_PHONE)
//            pPlusPermission.setPermissionListener(object : PermissionListener {
//
//                override fun onPermissionGranted() {
//
//                    IgawCommon.setUserId(activity, LoginInfoManager.getInstance().user.no.toString())
//                    val optionMap = HashMap<String, Any>()
//                    optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_TITLE_TEXT, getString(R.string.word_bol_charge_station))
//                    optionMap.put(ApStyleManager.CustomStyle.OFFERWALL_THEME_COLOR, Color.parseColor("#579ffb"))
//                    optionMap.put(ApStyleManager.CustomStyle.TOP_BAR_BG_COLOR, Color.parseColor("#579ffb"))
//                    optionMap.put(ApStyleManager.CustomStyle.BOTTOM_BAR_BG_COLOR, Color.parseColor("#579ffb"))
//                    ApStyleManager.setCustomOfferwallStyle(optionMap)
//                    IgawAdpopcornExtension.setCashRewardAppFlag(activity, true)
//                    IgawAdpopcorn.openOfferWall(activity)
//                    IgawAdbrix.retention("Main_luckybol_a_charging")
//                    IgawAdbrix.firstTimeExperience("Main_luckybol_a_charging")
//                }
//
//                override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
//
//                }
//            })
//            pPlusPermission.checkPermission()
//        }
//
//        IgawAdpopcorn.setEventListener(activity, object : IAdPOPcornEventListener {
//            override fun OnClosedOfferWallPage() {
//                setRetentionBol()
//            }
//        })
//
//        setRetentionBol()
//        getEvent()
//    }
//
//    private val MIN_CLICK_INTERVAL: Long = 1000 //in millis
//    private var lastClickTime: Long = 0
//
//    private fun getEvent() {
//        val params = HashMap<String, String>()
//        params["no"] = "3"
//        params["pg"] = "1"
//
//        ApiBuilder.create().getEventListByGroup(params).setCallback(object : PplusCallback<NewResultResponse<Event>> {
//
//            override fun onResponse(call: Call<NewResultResponse<Event>>, response: NewResultResponse<Event>) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//                if (response.datas.size > 0) {
//                    val event = response.datas[0]
//
//                    Glide.with(activity!!).load(event.bannerImage?.url).apply(RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_home2_event)
//
//                    image_home2_event.setOnClickListener {
//                        val currentTime = SystemClock.elapsedRealtime()
//                        if (currentTime - lastClickTime > MIN_CLICK_INTERVAL) {
//                            lastClickTime = currentTime
//
//                            if (event.winAnnounceType.equals(EventType.WinAnnounceType.special.name)) {
//                                val currentMillis = System.currentTimeMillis()
//                                val winAnnounceDate = DateFormatUtils.PPLUS_DATE_FORMAT.parse(event.winAnnounceDate).time
//                                if (currentMillis > winAnnounceDate) {
//
//                                    val intent = Intent(activity, EventImpressionActivity::class.java)
//                                    intent.putExtra(Const.DATA, event)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    activity!!.startActivityForResult(intent, Const.REQ_EVENT_DETAIL)
//                                    return@setOnClickListener
//                                }
//                            }
//
//                            if (event.primaryType.equals(EventType.PrimaryType.move.name)) {
//                                val intent = Intent(activity, EventMoveDetailActivity::class.java)
//                                intent.putExtra(Const.DATA, event)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                activity!!.startActivityForResult(intent, Const.REQ_EVENT_DETAIL)
//                            } else {
//                                if (StringUtils.isNotEmpty(event.contents)) {
//                                    val intent = Intent(activity, EventDetailActivity::class.java)
//                                    intent.putExtra(Const.DATA, event)
//                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                    activity!!.startActivityForResult(intent, Const.REQ_EVENT_DETAIL)
//                                } else {
//                                    if (event.primaryType.equals(EventType.PrimaryType.insert.name)) {
//                                        val intent = Intent(activity, PadActivity::class.java)
////                                val intent = Intent(activity, AppMainActivity2::class.java)
//                                        intent.putExtra(Const.KEY, Const.PAD)
//                                        intent.putExtra(Const.NUMBER, event.virtualNumber)
//                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        startActivity(intent)
//                                        activity!!.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out)
//
//                                    } else if (event.primaryType.equals(EventType.PrimaryType.join.name)) {
//                                        joinEvent(event)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Event>>, t: Throwable, response: NewResultResponse<Event>) {
//
//                hideProgress()
//                if (!isAdded) {
//                    return
//                }
//
//            }
//        }).build().call()
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
//                            mLoading.show(fragmentManager, "")
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
//                            activity!!.startActivityForResult(intent, Const.REQ_RESULT)
//                        }, delayTime)
//
//                    } else {
//                        val intent = Intent(activity, EventResultActivity::class.java)
//                        intent.putExtra(Const.EVENT_RESULT, response.data)
//                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                        activity!!.startActivityForResult(intent, Const.REQ_RESULT)
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
//                    }
//                })
//
//            }
//        })
//    }
//
//    private fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//
//            override fun reload() {
//
//                text_home2_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            Const.REQ_LOCATION_CODE -> {
//                getLocation()
//            }
//            Const.REQ_CASH_CHANGE, Const.REQ_RESULT, Const.REQ_EVENT_DETAIL -> {
//                setRetentionBol()
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
//                MainHomeFragment2().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, tab.name)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
