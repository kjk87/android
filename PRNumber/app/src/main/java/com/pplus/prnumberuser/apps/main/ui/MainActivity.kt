//package com.pplus.prnumberuser.apps.main.ui
//
//import android.content.Intent
//import android.graphics.PointF
//import android.graphics.Rect
//import android.os.Bundle
//import android.os.Handler
//import android.support.customtabs.CustomTabsClient
//import com.igaworks.IgawCommon
//import com.nightonke.boommenu.Animation.OrderEnum
//import com.nightonke.boommenu.BoomButtons.ButtonPlaceAlignmentEnum
//import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum
//import com.nightonke.boommenu.BoomButtons.HamButton
//import com.nightonke.boommenu.Piece.PiecePlaceEnum
//import com.pplus.utils.part.apps.resource.ResourceUtil
//import com.pplus.utils.part.logs.LogUtil
//import com.pplus.utils.part.pref.PreferenceUtil
//import com.pplus.utils.part.utils.StringUtils
//import com.pplus.prnumberuser.Const
//import com.pplus.prnumberuser.PRNumberApplication
//import com.pplus.prnumberuser.R
//import com.pplus.prnumberuser.apps.common.Foreground
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
//import com.pplus.prnumberuser.apps.common.mgmt.SchemaManager
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity
//import com.pplus.prnumberuser.apps.friend.ui.PlusFriendActivity
//import com.pplus.prnumberuser.apps.goods.ui.BuyHistoryActivity
//import com.pplus.prnumberuser.apps.my.ui.MyInfoActivity
//import com.pplus.prnumberuser.apps.plus.ui.PlusNewsActivity
//import com.pplus.prnumberuser.apps.setting.ui.ProfileConfigActivity
//import com.pplus.prnumberuser.core.chrome.customtab.CustomTabUtil
//import com.pplus.prnumberuser.core.chrome.shared.ServiceConnectionCallback
//import com.pplus.prnumberuser.core.code.common.EnumData
//import com.pplus.prnumberuser.core.code.common.PageTypeCode
//import com.pplus.prnumberuser.core.code.common.SnsTypeCode
//import com.pplus.prnumberuser.core.network.ApiBuilder
//import com.pplus.prnumberuser.core.util.PplusCommonUtil
//import com.pplus.prnumberuser.core.util.ToastUtil
//import com.pplus.prnumberuser.push.PushReceiveData
//import kotlinx.android.synthetic.main.activity_main.*
//import java.util.*
//
//class MainActivity : BaseActivity() {
//    override fun getPID(): String {
//        return ""
//    }
//
//    override fun getLayoutRes(): Int {
//        return R.layout.activity_main
//    }
//
//    private var mPushData: PushReceiveData? = null
//
//    override fun initializeView(savedInstanceState: Bundle?) {
//
//        if (PreferenceUtil.getDefaultPreference(this).get(Const.SIGN_UP, false)) {
//            PreferenceUtil.getDefaultPreference(this).put(Const.SIGN_UP, false)
//            val intent = Intent(this, ProfileConfigActivity::class.java)
//            intent.putExtra(Const.SIGN_UP, true)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_SIGN_UP_PROFILE)
//        } else {
//            setAdbrixData()
//        }
//
//        if (!LoginInfoManager.getInstance().user.accountType.equals(SnsTypeCode.pplus.name)) {
//            val intent = Intent(this, SnsAccountAlertActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        image_main_my.setOnClickListener {
//            val intent = Intent(this, MyInfoActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//        }
//
//        image_main_phonebook.setOnClickListener {
//            val intent = Intent(this, PlusFriendActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivityForResult(intent, Const.REQ_CASH_CHANGE)
//
//        }
//
//        image_main_dial.setOnClickListener {
//            val intent = Intent(this, PadActivity::class.java)
//            intent.putExtra(Const.KEY, Const.PAD)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//            startActivity(intent)
//        }
//
//        Foreground.get(this).addListener(object : Foreground.Listener {
//            override fun onBecameForeground() {
//                ApiBuilder.create().session.build().call()
//                CustomTabUtil.unbindCustomTabsService(this@MainActivity)
//                bindCustomTab()
//            }
//
//            override fun onBecameBackground() {
//
//            }
//        })
//
//        setBmb()
//        bindCustomTab()
//        setMainFragment()
//        onNewIntent(intent)
//    }
//
//    private fun setMainFragment(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.main_container, MainPRFragment.newInstance(), MainPRFragment::class.java.simpleName)
//        ft.commit()
//    }
//
//    private fun setBmb() {
//        bmb_main.piecePlaceEnum = PiecePlaceEnum.Custom
//        bmb_main.buttonPlaceEnum = ButtonPlaceEnum.Custom
//        bmb_main.orderEnum = OrderEnum.DEFAULT
//        bmb_main.buttonPlaceAlignmentEnum = ButtonPlaceAlignmentEnum.Center
//
//        bmb_main.clearBuilders()
//        val rect = Rect(resources.getDimensionPixelSize(R.dimen.width_170), 0, resources.getDimensionPixelSize(R.dimen.width_900), resources.getDimensionPixelSize(R.dimen.height_170))
//        if(LoginInfoManager.getInstance().user.page == null){
//            bmb_main.addBuilder(HamButton.Builder()
//                    .normalColor(ResourceUtil.getColor(this, R.color.white))
//                    .normalImageRes(R.drawable.ic_floating_prnumber)
//                    .normalTextRes(R.string.msg_make_biz)
//                    .normalTextColor(ResourceUtil.getColor(this, R.color.color_232323))
//                    .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                    .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                    .textRect(rect)
//                    .pieceColorRes(R.color.white)
//                    .containsSubText(false)
//                    .listener {
//                        if (LoginInfoManager.getInstance().user.page == null) {
//                            val intent = Intent(this, BizIntroduceActivity::class.java)
//                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                            startActivity(intent)
//                        } else {
//                            PplusCommonUtil.runOtherApp("com.pplus.prnumberbiz")
//                        }
//                    })
//        }
//
////        bmb_main.addBuilder(HamButton.Builder()
////                .normalColor(ResourceUtil.getColor(this, R.color.white))
////                .normalImageRes(R.drawable.ic_floating_event)
////                .normalTextRes(R.string.word_number_event)
////                .normalTextColor(ResourceUtil.getColor(this, R.color.color_232323))
////                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
////                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
////                .textRect(rect)
////                .pieceColorRes(R.color.white)
////                .containsSubText(false)
////                .listener {
////                    val intent = Intent(this, PRNumberEventActivity::class.java)
////                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
////                    startActivityForResult(intent, Const.REQ_CASH_CHANGE)
////                })
//
//        bmb_main.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(this, R.color.white))
//                .normalImageRes(R.drawable.ic_floating_hotdeal)
//                .normalTextRes(R.string.word_around_hotdeal)
//                .normalTextColor(ResourceUtil.getColor(this, R.color.color_232323))
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .textRect(rect)
//                .pieceColorRes(R.color.white)
//                .containsSubText(false)
//                .listener {
//                    val intent = Intent(this, HotDealActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//
//                })
//
//        bmb_main.addBuilder(HamButton.Builder()
//                .normalColor(ResourceUtil.getColor(this, R.color.white))
//                .normalImageRes(R.drawable.ic_floating_plus)
//                .normalTextRes(R.string.word_plus_news)
//                .normalTextColor(ResourceUtil.getColor(this, R.color.color_232323))
//                .buttonHeight(resources.getDimensionPixelSize(R.dimen.height_170))
//                .buttonWidth(resources.getDimensionPixelSize(R.dimen.width_900))
//                .textRect(rect)
//                .pieceColorRes(R.color.white)
//                .containsSubText(false)
//                .listener {
//                    val intent = Intent(this, PlusNewsActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//
//                })
//
//        setButtonPlace()
//        setPiecePlace()
//    }
//
//    private fun setButtonPlace() {
//        val builder = (bmb_main.builders[0] as HamButton.Builder)
//        val h = builder.buttonHeight
//        val w = builder.buttonWidth
//
//        val vm = bmb_main.buttonVerticalMargin
//        val vm_0_5 = vm / 2
//        val h_0_5 = h / 2
//
//        val halfButtonNumber = bmb_main.builders.size / 2
//
//        if (bmb_main.builders.size % 2 == 0) run {
//            for (i in halfButtonNumber - 1 downTo 0)
//                bmb_main.customButtonPlacePositions.add(PointF(0f, -h_0_5 - vm_0_5 - i * (h + vm)))
//            for (i in 0 until halfButtonNumber)
//                bmb_main.customButtonPlacePositions.add(PointF(0f, +h_0_5 + vm_0_5 + i * (h + vm)))
//        } else {
//            for (i in halfButtonNumber - 1 downTo 0)
//                bmb_main.customButtonPlacePositions.add(PointF(0f, -h - vm - i * (h + vm)))
//            bmb_main.customButtonPlacePositions.add(PointF(0f, 0f))
//            for (i in 0 until halfButtonNumber)
//                bmb_main.customButtonPlacePositions.add(PointF(0f, +h + vm + i * (h + vm)))
//        }
//    }
//
//    private fun setPiecePlace() {
//        val h = bmb_main.hamHeight
//        val w = bmb_main.hamWidth
//
//        val pn = bmb_main.builders.size
//        val pn_0_5 = pn / 2
//        val h_0_5 = h / 2
//        val vm = bmb_main.pieceVerticalMargin
//        val vm_0_5 = vm / 2
//
//        if (pn % 2 == 0) {
//            for (i in pn_0_5 - 1 downTo 0)
//                bmb_main.customPiecePlacePositions.add(PointF(0f, -h_0_5 - vm_0_5 - i * (h + vm)))
//            for (i in 0 until pn_0_5)
//                bmb_main.customPiecePlacePositions.add(PointF(0f, +h_0_5 + vm_0_5 + i * (h + vm)))
//        } else {
//            for (i in pn_0_5 - 1 downTo 0)
//                bmb_main.customPiecePlacePositions.add(PointF(0f, -h - vm - i * (h + vm)))
//            bmb_main.customPiecePlacePositions.add(PointF(0f, 0f))
//            for (i in 0 until pn_0_5)
//                bmb_main.customPiecePlacePositions.add(PointF(0f, +h + vm + i * (h + vm)))
//        }
//    }
//
//    override fun onNewIntent(intent: Intent) {
//
//        super.onNewIntent(intent)
//        mPushData = intent.getParcelableExtra(Const.PUSH_DATA)
//        setPushData()
//        setSchemeData()
//        setKeyMove(intent)
//    }
//
//    private fun setPushData() {
//
//        if (mPushData != null) {
//            PreferenceUtil.getDefaultPreference(this).put(Const.PUSH_ID, 0)
//            val schema = SchemaManager.getInstance(this).setSchemaData(mPushData)
//            SchemaManager.getInstance(this).moveToSchema(schema, true)
//        }
//    }
//
//    private fun setKeyMove(data: Intent) {
//        val key = data.getStringExtra(Const.KEY)
//        if (StringUtils.isNotEmpty(key)) {
//            when (key) {
//                Const.GOODS_HISTORY -> {
//                    val intent = Intent(this, BuyHistoryActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    startActivity(intent)
//                }
//            }
//        }
//    }
//
//    private fun setSchemeData() {
//
//        val scheme = PRNumberApplication.getSchemaData()
//
//        if (StringUtils.isNotEmpty(scheme)) {
//            if (scheme.contains(SchemaManager.SCHEMA_PRNUMBER)) {
//                SchemaManager.getInstance(this).moveToSchema(scheme, false)
//                PRNumberApplication.setSchemaData(null)
//            }
//        }
//    }
//
//
//    fun bindCustomTab() {
//        CustomTabUtil.bindCustomTabsService(this, object : ServiceConnectionCallback {
//            override fun onServiceConnected(client: CustomTabsClient?) {
//                LogUtil.e(LOG_TAG, "onServiceConnected")
//                CustomTabUtil.mClient = client
//                CustomTabUtil.mClient!!.warmup(0)
//            }
//
//            override fun onServiceDisconnected() {
//                LogUtil.e(LOG_TAG, "onServiceDisconnected")
//            }
//        })
//    }
//
//    fun setAdbrixData() {
//        IgawCommon.setUserId(this, LoginInfoManager.getInstance().user.no.toString())
//        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.gender)) {
//
//            if (LoginInfoManager.getInstance().user.gender.equals(EnumData.GenderType.male.name)) {
//                IgawCommon.setGender(IgawCommon.Gender.MALE)
//            } else {
//                IgawCommon.setGender(IgawCommon.Gender.FEMALE)
//            }
//        }
//
//        if (StringUtils.isNotEmpty(LoginInfoManager.getInstance().user.birthday)) {
//            val birth = LoginInfoManager.getInstance().user.birthday!!.substring(0, 4)
//            val year = Calendar.getInstance().get(Calendar.YEAR)
//            val age = year - birth.toInt()
//            IgawCommon.setAge(age)
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//
//    override fun onBackPressed() {
//
//        back()
//    }
//
//    internal var isExitBackPressed = false
//    private val mHandler = Handler()
//    private fun back() {
//        if (isExitBackPressed) {
//
//            for (activity in PRNumberApplication.getActivityList()) {
//                activity.finish()
//            }
//
//            if (!isFinishing) {
//                finish()
//            }
//
//        } else {
//            isExitBackPressed = true
//
//            ToastUtil.show(this, getString(R.string.msg_quit))
//
//            mHandler.postDelayed({
//                isExitBackPressed = false
//            }, 3000)
//        }
//    }
//}
