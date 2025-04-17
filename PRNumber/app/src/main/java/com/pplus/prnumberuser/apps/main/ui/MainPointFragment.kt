//package com.pplus.prnumberuser.apps.main.ui
//
//
//import android.app.Activity
//import android.content.Intent
//import android.graphics.Color
//import android.os.Bundle
//import android.view.View
//import androidx.activity.result.contract.ActivityResultContracts
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.bol.ui.BolConfigActivity
//import com.pplus.prnumberuser.apps.common.builder.PPlusPermission
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.common.ui.base.BaseFragment
//import com.pplus.prnumberuser.apps.event.ui.LottoActivity
//import com.pplus.prnumberuser.apps.event.ui.NumberEventActivity
//import com.pplus.prnumberuser.apps.event.ui.PlayActivity
//import com.pplus.prnumberuser.apps.my.ui.RankingActivity
//import com.pplus.prnumberuser.apps.post.ui.UserPostActivity
//import com.pplus.prnumberuser.apps.recommend.ui.InviteActivity
//import com.pplus.prnumberuser.apps.signin.ui.SnsLoginActivity
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.utils.part.apps.permission.Permission
//import com.pplus.utils.part.apps.permission.PermissionListener
//import com.pplus.utils.part.format.FormatUtil
//import kotlinx.android.synthetic.main.fragment_main_point.*
//import java.util.*
//
//class MainPointFragment : BaseFragment<BaseActivity>() {
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
//        return R.layout.fragment_main_point
//    }
//
//    override fun initializeView(container: View?) {
//
//
//    }
//
//    override fun init() {
//
//        image_main_point_play.setOnClickListener {
//            val intent = Intent(activity, PlayActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            signInLauncher.launch(intent)
//        }
//
//        layout_main_point_lotto.setOnClickListener {
////            if(!PplusCommonUtil.loginCheck(activity!!)){
////                return@setOnClickListener
////            }
//            val intent = Intent(activity, LottoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            signInLauncher.launch(intent)
//        }
//
//        layout_main_point_ranking.setOnClickListener {
//            val intent = Intent(activity, RankingActivity::class.java)
//            intent.putExtra(Const.TYPE, EnumData.RankType.recommend.name)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        layout_main_point_real_win_impression.setOnClickListener {
////            if(!PplusCommonUtil.loginCheck(activity!!)){
////                return@setOnClickListener
////            }
//            val intent = Intent(activity, UserPostActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
////        layout_main_point_share_event.setOnClickListener {
////            val intent = Intent(activity, ShareEventActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////            startActivity(intent)
////        }
//
//        layout_main_point_number_event.setOnClickListener {
//            val intent = Intent(activity, NumberEventActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            signInLauncher.launch(intent)
//        }
//
//        layout_main_point_invite.setOnClickListener {
//            if(!PplusCommonUtil.loginCheck(requireActivity(), signInLauncher)){
//                return@setOnClickListener
//            }
//            val intent = Intent(activity, InviteActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        text_main_point_retention_bol.setOnClickListener {
//            if (LoginInfoManager.getInstance().isMember) {
//                val intent = Intent(activity, BolConfigActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                startActivity(intent)
//            }else{
//                val intent = Intent(activity, SnsLoginActivity::class.java)
//                signInLauncher.launch(intent)
//            }
//        }
//        text_main_point_login.setOnClickListener {
//            val intent = Intent(activity, SnsLoginActivity::class.java)
//            signInLauncher.launch(intent)
//        }
//
//        if (LoginInfoManager.getInstance().isMember) {
//            text_main_point_retention_bol.visibility = View.VISIBLE
//            text_main_point_login.visibility = View.GONE
//            setRetentionBol()
//        }else{
//            text_main_point_retention_bol.visibility = View.GONE
//            text_main_point_login.visibility = View.VISIBLE
//        }
//    }
//
//    fun setRetentionBol() {
//        PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
//            override fun reload() {
//                text_main_point_retention_bol?.text = PplusCommonUtil.fromHtml(getString(R.string.html_cash_unit2, FormatUtil.getMoneyType(LoginInfoManager.getInstance().user.totalBol.toString())))
//            }
//        })
//    }
//
//    val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data = result.data
//            checkSignIn()
//        }
//    }
//
//    private fun checkSignIn(){
//        if (LoginInfoManager.getInstance().isMember) {
//            text_main_point_retention_bol.visibility = View.VISIBLE
//            text_main_point_login.visibility = View.GONE
//            setRetentionBol()
//        }else{
//            text_main_point_retention_bol.visibility = View.GONE
//            text_main_point_login.visibility = View.VISIBLE
//        }
//    }
//
//    override fun getPID(): String {
//        return "Main_point"
//    }
//
//    companion object {
//
//        @JvmStatic
//        fun newInstance() =
//                MainPointFragment().apply {
//                    arguments = Bundle().apply {
//                        //                        putString(Const.TAB, type)
////                        putString(ARG_PARAM2, param2)
//                    }
//                }
//    }
//}
