package com.pplus.prnumberuser.apps.main.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
import com.pplus.prnumberuser.apps.card.ui.CardConfigActivity
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
import com.pplus.prnumberuser.apps.event.ui.PlayActivity
import com.pplus.prnumberuser.apps.my.ui.MyFavoriteActivity
import com.pplus.prnumberuser.apps.my.ui.MyPlusActivity
import com.pplus.prnumberuser.apps.product.ui.PurchaseHistoryActivity
import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
import com.pplus.prnumberuser.apps.setting.ui.AlarmContainerActivity
import com.pplus.prnumberuser.apps.setting.ui.AppSettingActivity
import com.pplus.prnumberuser.apps.setting.ui.ProfileConfigActivity
import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.prnumberuser.databinding.FragmentMyInfoBinding
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.format.FormatUtil
import com.pplus.utils.part.utils.StringUtils
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [MyInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyInfoFragment : BaseFragment<BaseActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mParam1 = arguments!!.getString(ARG_PARAM1)
        }
    }
    private var _binding: FragmentMyInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMyInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun init() {

//        layout_my_info_event.setOnClickListener {
//            val intent = Intent(activity, PRNumberEventActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        layout_my_info_play.setOnClickListener {
//            val intent = Intent(activity, PlayActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }

//        binding.layoutMyInfoPointShop.setOnClickListener {
////            if (!PplusCommonUtil.loginCheck(requireActivity(), object :
////                    PplusCommonUtil.Companion.SignInListener {
////                    override fun onSign() {
////                        checkLogin()
////                    }
////                })) {
////                return@setOnClickListener
////            }
//            val intent = Intent(activity, TuneEventActivity::class.java)
//            intent.putExtra(Const.TAB, 1)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            signInLauncher.launch(intent)
//        }

//        layout_my_info_win_history.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, MyWinHistoryActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }

//        layout_my_info_friend.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, UserFriendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_my_info_event_ticket_history.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, TicketConfigActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }

        binding.layoutMyInfoAlarm.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, AlarmContainerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutMyInfoAllianceInquiry.setOnClickListener {
            PplusCommonUtil.openChromeWebView(requireActivity(), "https://docs.google.com/forms/d/e/1FAIpQLSfnnA-mFy39jtcME9eDEJgHfr6rFIE9aN9-3bEC-zNHupNy5w/viewform")
        }

        binding.layoutMyInfoPlus.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MyPlusActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoBuyHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
//            val intent = Intent(activity, OrderPurchaseHistoryActivity::class.java)
            val intent = Intent(activity, PurchaseHistoryActivity::class.java)
            intent.putExtra(Const.KEY, Const.ORDER)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutMyInfoTicketHistory.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, PurchaseHistoryActivity::class.java)
            intent.putExtra(Const.KEY, Const.TICKET)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

//        binding.layoutMyInfoSubscription.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, MySubscriptionActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            signInLauncher.launch(intent)
//        }

//        binding.layoutMyInfoReview.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, MyReviewActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            signInLauncher.launch(intent)
//        }

        binding.layoutMyInfoSetting.setOnClickListener {
            val intent = Intent(activity, AppSettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

//        layout_my_info_goods_like.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, ProductLikeActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            activity?.startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }

        binding.layoutMyInfoMyPlus.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MyPlusActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoCardConfig.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, CardConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        layout_my_info_qr_pay.setOnClickListener {
//            if (!PplusCommonUtil.loginCheck(activity!!)) {
//                return@setOnClickListener
//            }
//
//            if(LoginInfoManager.getInstance().user.verification!!.media != "external"){
//                val builder = AlertBuilder.Builder()
//                builder.setTitle(getString(R.string.word_notice_alert))
//                builder.addContents(AlertData.MessageData(getString(R.string.msg_verification_me_for_service), AlertBuilder.MESSAGE_TYPE.TEXT, 3))
//                builder.addContents(AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
//                builder.setOnAlertResultListener(object : OnAlertResultListener {
//                    override fun onCancel() {}
//                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
//                        when (event_alert) {
//                            AlertBuilder.EVENT_ALERT.RIGHT -> {
//                                val intent = Intent(activity, VerificationMeActivity::class.java)
//                                intent.putExtra(Const.KEY, Const.VERIFICATION_ME)
//                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                activity?.startActivityForResult(intent, Const.REQ_VERIFICATION)
//                            }
//                        }
//                    }
//                }).builder().show(activity)
//            }else{
//                val intent = Intent(activity, QRPayActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                activity?.startActivityForResult(intent, Const.REQ_QR_PAY)
//            }
//
//
//        }

        binding.layoutMyInfoHashTag.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, MyFavoriteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoInvite.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, InviteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.textMyInfoRetentionBol.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, BolConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutMyInfoShareBiz.setOnClickListener {
            if (!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)) {
                return@setOnClickListener
            }
            val intent = Intent(activity, SharePRNumberActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

//        image_my_info_make_biz.setOnClickListener {
//            val intent = Intent(activity, BizIntroduceActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_my_info_my_page.setOnClickListener {
//            PplusCommonUtil.runOtherApp("com.pplus.prnumberbiz")
//        }

//        layout_my_info_ads_inquiry.setOnClickListener {
//            PplusCommonUtil.openChromeWebView(activity!!, getString(R.string.msg_ad_url))
//        }

        binding.textMyInfoNickname.setOnClickListener {
            if(!LoginInfoManager.getInstance().isMember){
                val intent = Intent(activity, SnsLoginActivity::class.java)
                signInLauncher.launch(intent)
            }
        }

        binding.imageMyInfo.setOnClickListener {
            if(!LoginInfoManager.getInstance().isMember){
                val intent = Intent(activity, SnsLoginActivity::class.java)
                signInLauncher.launch(intent)
                return@setOnClickListener
            }
            val intent = Intent(activity, ProfileConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }


        binding.imageMyPointPlay.setOnClickListener {
            val intent = Intent(activity, PlayActivity::class.java)
            intent.putExtra(Const.KEY, 2)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        binding.layoutMyInfoProfile.setOnClickListener {
            if(!LoginInfoManager.getInstance().isMember){
                val intent = Intent(activity, SnsLoginActivity::class.java)
                signInLauncher.launch(intent)
                return@setOnClickListener
            }
            val intent = Intent(activity, ProfileConfigActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            signInLauncher.launch(intent)
        }

        checkLogin()
    }

    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        checkLogin()
    }

    private fun checkLogin(){
        if (LoginInfoManager.getInstance().isMember) {
//            view_my_info_background1.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_850)
//            view_my_info_background2.visibility = View.VISIBLE
//            layout_my_info_point_bar.visibility = View.VISIBLE
//            layout_my_info_count.visibility = View.VISIBLE
            binding.layoutMyInfoPoint.visibility = View.VISIBLE
            binding.layoutMyInfoProfile.visibility = View.VISIBLE
            binding.textMyInfoNickname.setBackgroundColor(ResourceUtil.getColor(activity, android.R.color.transparent))
            setData()
            setCount()
        } else {
//            view_my_info_background1.layoutParams.height = resources.getDimensionPixelSize(R.dimen.height_660)
//            view_my_info_background2.visibility = View.GONE
//            layout_my_info_point_bar.visibility = View.GONE
//            layout_my_info_count.visibility = View.GONE
            binding.layoutMyInfoPoint.visibility = View.GONE
            binding.layoutMyInfoProfile.visibility = View.GONE

            binding.textMyInfoNickname.setText(R.string.word_login_join2)
            binding.textMyInfoNickname.setBackgroundResource(R.drawable.underbar_ffffff_transparent)
            binding.imageMyInfo.setImageResource(R.drawable.ic_mypage_profile_default)
        }
    }


    private fun setData() {
        val user = LoginInfoManager.getInstance().user
        if (user.profileImage != null && StringUtils.isNotEmpty(user.profileImage!!.url)) {
            Glide.with(this).load(user.profileImage!!.url).apply(RequestOptions().centerCrop().placeholder(R.drawable.ic_mypage_profile_default).error(R.drawable.ic_mypage_profile_default)).into(binding.imageMyInfo)
        }

        binding.textMyInfoNickname.text = user.nickname
        setPoint()

    }

    private fun setCount() {
//        getWinCount()
//        getReviewCount()
//        getPurchaseCount()
//        getProductLikeCount()
//        text_my_info_event_ticket_count.text = LoginInfoManager.getInstance().user.eventTicketCount.toString()
//        getFriendsCount()
//        getMyPostCount()

    }

//    fun getProductLikeCount() {
//        ApiBuilder.create().countProductLike.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                if (response != null && response.data != null) {
//                    text_my_info_goods_like_count?.text = response.data.toString()
//
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
//
//            }
//        }).build().call()
//    }
//
//    private fun getPurchaseCount() {
//
//        ApiBuilder.create().countPurchaseProductByMemberSeqNo.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?,
//                                    response: NewResultResponse<Int>?) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                if (response?.data != null) {
//                    text_my_info_buy_count?.text = FormatUtil.getMoneyType(response.data.toString())
//                } else {
//                    text_my_info_buy_count?.text = "0"
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?,
//                                   t: Throwable?,
//                                   response: NewResultResponse<Int>?) {
//            }
//        }).build().call()
//    }

//    private fun getReviewCount() {
//
//        ApiBuilder.create().countProductReviewByyMemberSeqNo.setCallback(object : PplusCallback<NewResultResponse<Int>> {
//            override fun onResponse(call: Call<NewResultResponse<Int>>?, response: NewResultResponse<Int>?) {
//
//                if (!isAdded) {
//                    return
//                }
//
//                if(response?.data != null){
//                    text_my_info_review_count?.text = FormatUtil.getMoneyType(response.data.toString())
//                }else{
//                    text_my_info_review_count?.text = "0"
//                }
//            }
//
//            override fun onFailure(call: Call<NewResultResponse<Int>>?, t: Throwable?, response: NewResultResponse<Int>?) {
//            }
//        }).build().call()
//    }

    private fun setPoint() {
        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
            override fun reload() {
                if (!isAdded) {
                    return
                }

                binding.textMyInfoRetentionBol.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))

            }
        })
    }

    override fun getPID(): String {
        return "Main_mypage"
    }

    companion object {

        fun newInstance(): MyInfoFragment {

            val fragment = MyInfoFragment()
            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
