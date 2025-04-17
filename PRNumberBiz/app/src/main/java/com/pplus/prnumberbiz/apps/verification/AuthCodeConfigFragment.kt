package com.pplus.prnumberbiz.apps.verification


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const

import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment
import com.pplus.prnumberbiz.apps.signup.ui.VerificationMeActivity
import com.pplus.prnumberbiz.core.network.model.dto.Verification
import kotlinx.android.synthetic.main.fragment_auth_code_config.*


/**
 * A simple [Fragment] subclass.
 * Use the [AuthCodeConfigFragment.newInstance] factory method to
 * create an instance of this mapFragment.
 */
class AuthCodeConfigFragment : BaseFragment<AuthCodeConfigActivity>() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (arguments != null) {
//            mParam1 = arguments.getString(ARG_PARAM1)
//            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.fragment_auth_code_config
    }

    override fun initializeView(container: View?) {

    }

    override fun init() {

        val page = LoginInfoManager.getInstance().user.page!!

        if (StringUtils.isEmpty(page.authCode)) {
            text_find_auth_code.visibility = View.INVISIBLE
            text_change_auth_code.text = getString(R.string.msg_set_verification_number)
        }

        text_find_auth_code.setOnClickListener {
            val builder = AlertBuilder.Builder()
            builder.setTitle(getString(R.string.word_notice_alert))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_verification_number_find1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_verification_number_find2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_verification_number_find3), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
            builder.setOnAlertResultListener(object : OnAlertResultListener {

                override fun onCancel() {

                }

                override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                    when (event_alert) {
                        AlertBuilder.EVENT_ALERT.RIGHT -> {
                            val intent = Intent(activity, VerificationMeActivity::class.java)
                            intent.putExtra(Const.MOBILE_NUMBER, LoginInfoManager.getInstance().user.mobile)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            intent.putExtra(Const.KEY, Const.VERIFICATION)
                            activity!!.startActivityForResult(intent, Const.REQ_VERIFICATION)
                        }
                    }
                }
            }).builder().show(activity)

        }

        text_change_auth_code.setOnClickListener {
            if (StringUtils.isEmpty(page.authCode)) {
                parentActivity.inputNumber("")
            }else{
                parentActivity.confirmNumber()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                Const.REQ_VERIFICATION ->{
                    if(data != null){
                        var verification = data.getParcelableExtra<Verification>(Const.VERIFICATION);
                        parentActivity.inputNumber(verification)
                    }
                }
            }
        }
    }

    override fun getPID(): String {
        return ""
    }


    companion object {


        /**
         * Use this factory method to create a new instance of
         * this mapFragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         *
         * @return A new instance of mapFragment VerificationNumberConfigFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): AuthCodeConfigFragment {

            val fragment = AuthCodeConfigFragment()
            val args = Bundle()
//            args.putString(ARG_PARAM1, param1)
//            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }

}// Required empty public constructor
