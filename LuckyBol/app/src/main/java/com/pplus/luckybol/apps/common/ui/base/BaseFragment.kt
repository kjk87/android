package com.pplus.luckybol.apps.common.ui.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.igaworks.v2.core.AdBrixRm
import com.pplus.networks.common.PplusCallback
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.impl.ImplFragment
import com.pplus.luckybol.apps.common.mgmt.DialogManager
import com.pplus.luckybol.apps.common.mgmt.LoginInfoManager
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.core.util.PplusCommonUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.io.Serializable

abstract class BaseFragment<T : BaseActivity?> : Fragment(), ImplFragment, Serializable {
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setAnalytics(context, getPID())
    }

    fun setAnalytics(context: Context, id:String?){
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        var pid = id
        if (StringUtils.isNotEmpty(pid)) {
            pid = LOG_TAG
        }
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
//            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, pid)
//            bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
//            val logger = AppEventsLogger.newLogger(context)
//            logger.logEvent("screen_view", bundle)
//        }catch (e: Exception){
//
//        }
    }

    fun setEvent(context: Context, action:String){
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        var bundle = Bundle()
        bundle.putString("deviceId", PplusCommonUtil.getDeviceID())
        firebaseAnalytics.logEvent(action, bundle)

        val eventAttr = AdBrixRm.AttrModel()
        eventAttr.setAttrs("deviceId", PplusCommonUtil.getDeviceID())
        AdBrixRm.event(action, eventAttr)

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
            if (getActivity() is BaseActivity) {
                parentActivity = getActivity() as T?
            }
        }
        return parentActivity!!
    }

    fun showAlert(message: String?) {
        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE)
        builder.setContents(message)
        builder.builder().show(getActivity())
    }

    fun showAlert(@StringRes messageId: Int) {
        showAlert(getString(messageId))
    }

    fun showProgress(msg: String?) {
        LogUtil.e(LOG_TAG, "showProgress : {}", msg)
        DialogManager.getInstance().showLoadingDialog(getActivity(), msg, false)
    }

    fun hideProgress() {
        LogUtil.e(LOG_TAG, "hideProgress")
        try {
            DialogManager.getInstance().hideLoadingDialog(getActivity())
        } catch (e: Exception) {
        }
    }

    val verificationLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data

            if (data != null) {
                val verifiedData = data.getParcelableExtra<User>(Const.DATA)!!
                if (LoginInfoManager.getInstance().user.mobile != verifiedData.mobile) {
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
                                    if (!isAdded) {
                                        return
                                    }
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
        if (childFragmentManager.fragments != null) {
            for (fragment in childFragmentManager.fragments) {
                fragment?.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}