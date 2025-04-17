package com.pplus.prnumberuser.apps.common.ui.base

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
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import com.igaworks.v2.core.AdBrixRm
import com.pplus.networks.common.PplusCallback
import com.pplus.prnumberuser.Const
import com.pplus.prnumberuser.R
import com.pplus.prnumberuser.apps.common.builder.AlertBuilder
import com.pplus.prnumberuser.apps.common.impl.ImplFragment
import com.pplus.prnumberuser.apps.common.mgmt.DialogManager
import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberuser.core.network.ApiBuilder
import com.pplus.prnumberuser.core.network.model.dto.User
import com.pplus.prnumberuser.core.network.model.response.NewResultResponse
import com.pplus.prnumberuser.core.util.PplusCommonUtil
import com.pplus.utils.part.logs.LogUtil
import com.pplus.utils.part.utils.StringUtils
import retrofit2.Call
import java.io.Serializable

abstract class BaseFragment<T : BaseActivity?> : Fragment(), ImplFragment, View.OnClickListener, Serializable {
    @JvmField
    var LOG_TAG = this.javaClass.simpleName
    protected var mContainer: View? = null
    private var parentActivity: T? = null
    lateinit var firebaseAnalytics: FirebaseAnalytics

    abstract fun getPID(): String?

    override fun onClick(v: View) {}

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
        if (StringUtils.isNotEmpty(id)) {
            var bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, id)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)

            val eventAttr = AdBrixRm.AttrModel()
            eventAttr.setAttrs("screen_name", id)
            AdBrixRm.event("screen_view", eventAttr)

            try {
                bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, id)
                val logger = AppEventsLogger.newLogger(context)
                logger.logEvent("screen_view", bundle)
            }catch (e: Exception){

            }
        }
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