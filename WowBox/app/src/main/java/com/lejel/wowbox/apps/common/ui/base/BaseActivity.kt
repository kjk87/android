package com.lejel.wowbox.apps.common.ui.base

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
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.pplus.utils.part.resource.ResourceUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.data.AlertData.MessageData
import com.lejel.wowbox.apps.common.mgmt.DialogManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption.ToolbarGravity
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption.ToolbarMenu
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityBaseBinding
import com.lejel.wowbox.databinding.ToolbarBinding

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
//        if (OsUtil.isLollipop()) {
//            val w = window // in Activity's onCreate() for instance
//            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
//            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//        }
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
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, pid)
        bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
        firebaseAnalytics.logEvent(javaClass.simpleName, bundle)

//        val eventAttr = AdBrixRm.AttrModel()
//        eventAttr.setAttrs("screen_name", pid)
//        eventAttr.setAttrs("deviceId", PplusCommonUtil.getDeviceID())
//        AdBrixRm.event(javaClass.simpleName, eventAttr)

    }

    fun setEvent(action:String){
        val bundle = Bundle()
        bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
        firebaseAnalytics.logEvent(action, bundle)

//        val eventAttr = AdBrixRm.AttrModel()
//        eventAttr.setAttrs("deviceId", PplusCommonUtil.getDeviceID())
//        AdBrixRm.event(action, eventAttr)

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
        toolbarBinding = ToolbarBinding.inflate(layoutInflater)
        binding.appbar.addView(toolbarBinding!!.root)
        val toolbar = toolbarBinding!!.toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
        setSupportActionBar(toolbar)
        val toolbarOption = implToolbar!!.getToolbarOption()
        if (toolbarOption != null) {
            if (!toolbarOption.isScrollFlags) {
                val toolbarLayoutParams = toolbar.layoutParams as AppBarLayout.LayoutParams
                toolbarLayoutParams.scrollFlags =
                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
            }
            val textTitle = toolbarBinding!!.toolbarTitle
            textTitle.setSingleLine()
            if (StringUtils.isEmpty(toolbarOption.title)) {
                textTitle.text = ""
            } else {
                textTitle.text = toolbarOption.title
            }
            val titleParams = textTitle.layoutParams as RelativeLayout.LayoutParams
            when (toolbarOption.titleGravity) {
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
                val viewArrayList = toolbarOption.getToolbarMenuArrayList(menu)
                when (menu) {
                    ToolbarMenu.LEFT -> {
                        params = leftLinearLayout.layoutParams as RelativeLayout.LayoutParams
                        leftLinearLayout.layoutParams = params
                        if (viewArrayList != null && !viewArrayList.isEmpty()) {
                            var previousView: View? = null
                            for (v in viewArrayList) {
                                v.setOnClickListener(toolbarLeftClickListener)
                                ResourceUtil.setGenerateViewId(v)
                                val layoutParams = toolbarOption.getViewParams()
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
                                val layoutParams = toolbarOption.getViewParams()
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
            setActionbarColor(ResourceUtil.getColor(this, R.color.white))
        }
        val v = getLayoutView()
        binding.mainContent.addView(v)
    }

    private fun setActionbarColor(@ColorInt color: Int) {
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

    private var toolbarLeftClickListener = View.OnClickListener { view ->
        implToolbar!!.getOnToolbarClickListener()?.onClick(view, ToolbarMenu.LEFT, view.tag)
    }
    private var toolbarRightClickListener = View.OnClickListener { view ->
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
//        builder.setTitle(getString(R.string.word_notice_alert))
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
        DialogManager.instance.showLoadingDialog(this, msg, false)
    }

    fun hideProgress() {
        LogUtil.e(LOG_TAG, "hideProgress")
        DialogManager.instance.hideLoadingDialog(this)
    }

//    interface LocationListener {
//        fun onLocation(result : ActivityResult)
//    }

//    var mLocationListener : LocationListener? = null
//
//    val locationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        mLocationListener?.onLocation(result)
//    }


    override fun onResume() {
        super.onResume()
        onWindowFocusChanged(true)
    }

    override fun onPause() {
        super.onPause()
    }
}