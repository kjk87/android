package com.lejel.wowbox.apps.common.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.DialogManager
import com.lejel.wowbox.core.util.PplusCommonUtil
import java.io.Serializable

abstract class BaseFragment<T : BaseActivity?> : Fragment(), Serializable {
    @JvmField
    var LOG_TAG = this.javaClass.simpleName
    protected var mContainer: View? = null
    private var parentActivity: T? = null
    lateinit var firebaseAnalytics: FirebaseAnalytics

    abstract fun getPID(): String?

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    //    override fun onActivityCreated(savedInstanceState: Bundle?) {
    //        super.onActivityCreated(savedInstanceState)
    //        init()
    //    }

    abstract fun init()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setAnalytics(context, getPID())
    }

    fun setAnalytics(context: Context, id: String?) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        var pid = id
        if (StringUtils.isNotEmpty(pid)) {
            pid = LOG_TAG
        }
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, pid)
        bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
        firebaseAnalytics.logEvent(javaClass.simpleName, bundle)

//        val eventAttr = AdBrixRm.AttrModel()
//        eventAttr.setAttrs("screen_name", pid)
//        eventAttr.setAttrs("deviceId", PplusCommonUtil.getDeviceID())
//        AdBrixRm.event(javaClass.simpleName, eventAttr)

    }

    fun setEvent(context: Context, action: String) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        val bundle = Bundle()
        bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
        firebaseAnalytics.logEvent(action, bundle)

//        val eventAttr = AdBrixRm.AttrModel()
//        eventAttr.setAttrs("deviceId", PplusCommonUtil.getDeviceID())
//        AdBrixRm.event(action, eventAttr)

        //        try {
        //            bundle = Bundle()
        //            bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
        //            val logger = AppEventsLogger.newLogger(context)
        //            logger.logEvent(action, bundle)
        //        }catch (e: Exception){
        //
        //        }
    }

    fun getParentActivity(): T {
        if (super.getActivity() != null) {
            if (activity is BaseActivity) {
                parentActivity = activity as T
            }
        }
        return parentActivity!!
    }

    fun showAlert(message: String) {
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
        builder.setContents(message)
        builder.builder().show(requireActivity())
    }

    fun showAlert(@StringRes messageId: Int) {
        showAlert(getString(messageId))
    }

    fun showAlert(message: String, line: Int = 2) {
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(message, AlertBuilder.MESSAGE_TYPE.TEXT, line))
        builder.setRightText(getString(R.string.word_confirm)).builder().show(requireActivity())
    }

    fun showProgress(msg: String?) {
        LogUtil.e(LOG_TAG, "showProgress : {}", msg)
        DialogManager.instance.showLoadingDialog(requireActivity(), msg, false)
    }

    fun hideProgress() {
        LogUtil.e(LOG_TAG, "hideProgress")
        try {
            DialogManager.instance.hideLoadingDialog(requireActivity())
        } catch (e: Exception) {
        }
    }

}