package com.pplus.luckybol.apps.common.ui.base

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.igaworks.v2.core.AdBrixRm
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.data.AlertData.MessageData
import com.pplus.luckybol.apps.common.mgmt.DialogManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarGravity
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.luckybol.databinding.ActivityBaseBinding
import com.pplus.luckybol.databinding.ToolbarBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.apps.resource.ResourceUtil
import com.pplus.utils.part.info.OsUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call

/**
 * <pre>
 * Application base Activity..
 * <re>
 * **해당 activity 에서 처리 할 수 있는 작업**
 * <t>
 * 1. 타이틀을 처리 할 수 있도록 구현c
 * 2. 스크린 로그 기록 할 수 있도록
</t> *
</re> *
</pre> *
 */
abstract class BaseActivity : AppCompatActivity() {
    @JvmField
    var LOG_TAG = this.javaClass.simpleName
    private var toolbarOption: ToolbarOption? = null
    private var implToolbar: ImplToolbar? = null
    private var oldBackground: LayerDrawable? = null
    var toolbarBinding: ToolbarBinding? = null
        private set

    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        //initActivityOption();
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        if (OsUtil.isLollipop()) {
            val w = window // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        if (this is ImplToolbar) {
            initializeToolbar()
        } else {
            setContentView(getLayoutView())
            setActionbarColor(Color.BLACK)
        }
        if (StringUtils.isNotEmpty(getPID())) {
            setAnalytics(getPID()!!)
        }else{
            setAnalytics(LOG_TAG)
        }

//        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        initializeView(savedInstanceState)
    }

    fun setAnalytics(pid:String){
        var bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, pid)
        bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

        val eventAttr = AdBrixRm.AttrModel()
        eventAttr.setAttrs("screen_name", pid)
        eventAttr.setAttrs("deviceId", PplusCommonUtil.getDeviceID())
        AdBrixRm.event("screen_view", eventAttr)

//        try {
//            bundle = Bundle()
//            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getPID())
//            bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
//            val logger = AppEventsLogger.newLogger(this)
//            logger.logEvent("screen_view", bundle)
//        }catch (e: Exception){
//
//        }
    }

    fun setEvent(action:String){
        var bundle = Bundle()
        bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
        firebaseAnalytics.logEvent(action, bundle)

        val eventAttr = AdBrixRm.AttrModel()
        eventAttr.setAttrs("deviceId", PplusCommonUtil.getDeviceID())
        AdBrixRm.event(action, eventAttr)

//        try {
//            bundle = Bundle()
//            bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
//            val logger = AppEventsLogger.newLogger(this)
//            logger.logEvent(action, bundle)
//        }catch (e: Exception){
//
//        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }

    abstract fun getLayoutView(): View

    abstract fun getPID(): String?

    abstract fun initializeView(savedInstanceState: Bundle?)

    private fun initializeToolbar() {
        val binding = ActivityBaseBinding.inflate(layoutInflater)
        implToolbar = this as ImplToolbar
        setContentView(binding.root)
        binding.root.fitsSystemWindows = true
        toolbarBinding = ToolbarBinding.inflate(layoutInflater)
        binding.appbar.addView(toolbarBinding!!.root)
        val toolbar = toolbarBinding!!.toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
        setSupportActionBar(toolbar)
        toolbarOption = implToolbar!!.getToolbarOption()
        if (toolbarOption != null) {
            if (!toolbarOption!!.isScrollFlags) {
                val toolbarLayoutParams = toolbar.layoutParams as AppBarLayout.LayoutParams
                toolbarLayoutParams.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            }
            val textTitle = toolbarBinding!!.toolbarTitle
            textTitle.setSingleLine()
            if (StringUtils.isEmpty(toolbarOption!!.title)) {
                textTitle.text = ""
            } else {
                textTitle.text = toolbarOption!!.title
            }
            val titleParams = textTitle.layoutParams as RelativeLayout.LayoutParams
            when (toolbarOption!!.titleGravity) {
                ToolbarGravity.CENTER -> titleParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                ToolbarGravity.LEFT -> titleParams.addRule(RelativeLayout.RIGHT_OF, R.id.left_btn_area)
                ToolbarGravity.RIGHT -> titleParams.addRule(RelativeLayout.LEFT_OF, R.id.right_btn_area)
            }
            textTitle.layoutParams = titleParams
            val leftLinearLayout =
                toolbarBinding!!.leftBtnArea
            val rightLinearLayout =
                toolbarBinding!!.rightBtnArea
            val toolbarMenus = arrayOf(ToolbarMenu.LEFT, ToolbarMenu.RIGHT)
            var params: RelativeLayout.LayoutParams? = null
            for (menu in toolbarMenus) {
                val viewArrayList = toolbarOption!!.getToolbarMenuArrayList(menu)
                when (menu) {
                    ToolbarMenu.LEFT -> {
                        params = leftLinearLayout.layoutParams as RelativeLayout.LayoutParams
                        leftLinearLayout.layoutParams = params
                        if (viewArrayList != null && !viewArrayList.isEmpty()) {
                            var previousView: View? = null
                            for (v in viewArrayList) {
                                v.setOnClickListener(toolbarLeftClickListener)
                                ResourceUtil.setGenerateViewId(v)
                                val layoutParams = toolbarOption!!.viewParams
                                if (previousView != null) {
                                    layoutParams.addRule(RelativeLayout.RIGHT_OF, previousView.id)
                                }
                                leftLinearLayout.addView(v, layoutParams)
                                previousView = v
                            }
                            textTitle.setPadding(0, 0, 0, 0)
                        } else {
                            // 좌측 메뉴가 없다면 패딩을 주도록함!
                            //                            textTitle.setPadding(getResources().getDimensionPixelSize(R.dimen.width_72), 0, 0, 0);
                        }
                    }
                    ToolbarMenu.RIGHT -> {
                        params = rightLinearLayout.layoutParams as RelativeLayout.LayoutParams
                        rightLinearLayout.layoutParams = params
                        if (viewArrayList != null && !viewArrayList.isEmpty()) {
                            var previousView: View? = null
                            for (v in viewArrayList) {
                                v.setOnClickListener(toolbarRightClickListener)
                                ResourceUtil.setGenerateViewId(v)
                                val layoutParams = toolbarOption!!.viewParams
                                if (previousView != null) {
                                    layoutParams.addRule(RelativeLayout.RIGHT_OF, previousView.id)
                                }
                                rightLinearLayout.addView(v, layoutParams)
                                previousView = v
                            }
                        }
                    }
                }
            }
            setActionbarColor(toolbarOption!!.toolbarBackgroundColor)
        }
        val v = getLayoutView()
        binding.mainContent.addView(v)
    }

    fun setActionbarColor(@ColorInt color: Int) {
        var color = color
        val orginColor = color
        if (color == Color.WHITE) {
            color = Color.BLACK
        }
        val colorDrawable: Drawable = ColorDrawable(orginColor)
        //        Drawable bottomDrawable = new ColorDrawable(ResourceUtil.getColor(this, android.R.color.transparent));
        val ld = LayerDrawable(arrayOf(colorDrawable))
        if (oldBackground == null) {
            if (supportActionBar != null) supportActionBar!!.setBackgroundDrawable(ld)
        } else {
            if (supportActionBar != null) {
                val td = TransitionDrawable(arrayOf<Drawable>(oldBackground!!, ld))
                supportActionBar!!.setBackgroundDrawable(td)
                td.startTransition(200)
            }
        }
        oldBackground = ld
    }

    var toolbarLeftClickListener = View.OnClickListener { view ->
        implToolbar!!.getOnToolbarClickListener()?.onClick(view, ToolbarMenu.LEFT, view.tag)
    }
    var toolbarRightClickListener = View.OnClickListener { view ->
        implToolbar!!.getOnToolbarClickListener()?.onClick(view, ToolbarMenu.RIGHT, view.tag)
    }

    fun setTitle(title: String?) {
        toolbarBinding!!.toolbarTitle.text = title
    }

    val toolBarTitle: String
        get() = toolbarBinding!!.toolbarTitle.text.toString()

    @JvmOverloads
    fun showAlert(message: String?, line: Int = 2) {
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(MessageData(message, AlertBuilder.MESSAGE_TYPE.TEXT, line))
        builder.setRightText(getString(R.string.word_confirm)).builder().show(this)
    }

    fun showAlert(@StringRes messageId: Int) {
        showAlert(getString(messageId), 2)
    }

    fun showAlert(@StringRes messageId: Int, line: Int) {
        showAlert(getString(messageId), line)
    }

    fun showProgress(msg: String?) {
        LogUtil.e(LOG_TAG, "showProgress : {}", msg)
        DialogManager.getInstance().showLoadingDialog(this, msg, false)
    }

    fun hideProgress() {
        LogUtil.e(LOG_TAG, "hideProgress")
        DialogManager.getInstance().hideLoadingDialog(this)
    }

    interface LocationListener {
        fun onLocation(result : ActivityResult)
    }

    var mLocationListener : LocationListener? = null

    val locationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        mLocationListener?.onLocation(result)
    }

    val verificationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data

            if (data != null) {
                val verifiedData = data.getParcelableExtra<User>(Const.DATA)!!
                if (LoginInfoManager.getInstance().user.mobile?.replace(Const.APP_TYPE + "##", "") != verifiedData.mobile) {
                    showAlert(R.string.msg_incorrect_joined_mobile_number)
                }else{
                    val user = LoginInfoManager.getInstance().user

                    val prams = User()
                    prams.no = user.no
                    prams.name = verifiedData.name
                    prams.birthday = verifiedData.birthday
                    prams.mobile = verifiedData.mobile
                    prams.gender = verifiedData.gender
                    prams.verification = verifiedData.verification
                    prams.appType = user.appType
                    showProgress("")
                    ApiBuilder.create().updateExternal(prams).setCallback(object :
                        PplusCallback<NewResultResponse<User>> {

                        override fun onResponse(call: Call<NewResultResponse<User>>,
                                                response: NewResultResponse<User>
                        ) {
                            hideProgress()
                            showProgress("")
                            PplusCommonUtil.reloadSession(object : PplusCommonUtil.Companion.ReloadListener {
                                override fun reload() {
                                    hideProgress()
                                    showAlert(R.string.msg_verified)
                                }
                            })
                        }

                        override fun onFailure(call: Call<NewResultResponse<User>>,
                                               t: Throwable,
                                               response: NewResultResponse<User>
                        ) {
                            hideProgress()
                        }
                    }).build().call()
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (supportFragmentManager.fragments != null) {
            for (fragment in supportFragmentManager.fragments) {
                fragment?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        onWindowFocusChanged(true)
    }

    override fun onPause() {
        super.onPause()
    }
}