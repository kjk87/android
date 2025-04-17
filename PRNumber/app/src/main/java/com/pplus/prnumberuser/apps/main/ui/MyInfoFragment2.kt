//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.view.View
//import androidx.fragment.app.Fragment
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.ui.LottoActivity
//import com.pplus.prnumberuser.apps.mobilegift.ui.MobileGiftActivity
//import com.pplus.prnumberuser.apps.my.ui.MyWinHistoryActivity
//import com.pplus.prnumberuser.apps.setting.ui.AlarmContainerActivity
//import com.pplus.prnumberuser.apps.setting.ui.AppSettingActivity
//import com.pplus.prnumberuser.apps.setting.ui.ProfileConfigActivity
//import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.format.FormatUtil
//import com.pplus.utils.part.utils.StringUtils
//import kotlinx.android.synthetic.main.fragment_my_info2.*
//import java.util.*
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [MyInfoFragment2.newInstance] factory method to
// * create an instance of this fragment.
// */
//class MyInfoFragment2 : BaseFragment<BaseActivity>() {
//
//    // TODO: Rename and change types of parameters
////    private var mParam1: String? = null
////    private var mParam2: String? = null
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
//        return R.layout.fragment_my_info2
//    }
//
//    override fun initializeView(container: View?) {
//
//    }
//
//    override fun onResume() {
//        super.onResume()
////        IgawCommon.startSession(activity)
//    }
//
//    override fun onPause() {
//        super.onPause()
////        IgawCommon.endSession()
//    }
//
//    override fun init() {
//
//        layout_my_info_lotto.setOnClickListener {
//
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, LottoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        layout_my_info_point_shop.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, MobileGiftActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        layout_my_info_win_history.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, MyWinHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//
//        layout_my_info_alarm.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, AlarmContainerActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_ALARM)
//        }
//
//        layout_my_info_setting.setOnClickListener {
//            val intent = Intent(activity, AppSettingActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//
//        text_my_info_retention_bol.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, BolConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        text_my_info_nickname.setOnClickListener {
//            if(!LoginInfoManager.getInstance().isMember){
//                val intent = Intent(activity, SnsLoginActivity::class.java)
//                activity?.startActivityForResult(intent, Const.REQ_SIGN_IN)
//            }
//        }
//
//        image_my_info.setOnClickListener {
//            if(!LoginInfoManager.getInstance().isMember){
//                val intent = Intent(activity, SnsLoginActivity::class.java)
//                activity?.startActivityForResult(intent, Const.REQ_SIGN_IN)
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, ProfileConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_DETAIL)
//        }
//
//        checkLogin()
//    }
//
//    private fun checkLogin(){
//        if (LoginInfoManager.getInstance().isMember) {
////            view_my_info_background1.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_850)
////            view_my_info_background2.visibility = View.VISIBLE
////            layout_my_info_point_bar.visibility = View.VISIBLE
//            layout_my_info_point.visibility = View.VISIBLE
//            text_my_info_nickname.setBackgroundColor(ResourceUtil.getColor(activity, android.R.color.transparent))
//            setData()
//            setCount()
//        } else {
////            view_my_info_background1.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_660)
////            view_my_info_background2.visibility = View.GONE
////            layout_my_info_point_bar.visibility = View.GONE
//            layout_my_info_point.visibility = View.GONE
//
//            text_my_info_nickname.setText(R.string.word_login_join2)
//            text_my_info_nickname.setBackgroundResource(R.drawable.underbar_ffffff_transparent)
//            image_my_info.setImageResource(R.drawable.ic_mypage_profile_default)
//        }
//    }
//
//
//    private fun setData() {
//        val user = LoginInfoManager.getInstance().user
//        if (user.profileImage != null && StringUtils.isNotEmpty(user.profileImage!!.url)) {
//            Glide.with(this).load(user.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_mypage_profile_default).error(R.drawable.ic_mypage_profile_default)).into(image_my_info)
//        }
//
//        text_my_info_nickname.text = user.nickname
//
//        setPoint()
//
//    }
//
//    private fun setCount() {
////        getWinCount()
////        getReviewCount()
////        getBuyCount()
////        getGoodsLikeCount()
////        text_my_info_event_ticket_count.text = LoginInfoManager.getInstance().user.eventTicketCount.toString()
////        getFriendsCount()
////        getMyPostCount()
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            Const.REQ_ALARM -> {
//                setPoint()
//            }
//            Const.REQ_SIGN_IN, Const.REQ_CASH_CHANGE, Const.REQ_DETAIL -> {
//                checkLogin()
//            }
//        }
//    }
//
//    private fun setPoint() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//            override fun reload() {
//                if (!isAdded) {
//                    return
//                }
//
//                text_my_info_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//
//            }
//        })
//    }
//
//    override fun getPID(): String {
//        return "Main_mypage"
//    }
//
//    companion object {
//
//        fun newInstance(): MyInfoFragment2 {
//
//            val fragment = MyInfoFragment2()
//            val args = Bundle()
////            args.putString(ARG_PARAM1, param1)
//            fragment.arguments = args
//            return fragment
//        }
//    }
//
//}// Required empty public constructor
