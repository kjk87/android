package com.pplus.prnumberbiz.apps.signup.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.pple.pplus.utils.part.utils.StringUtils
import com.pplus.prnumberbiz.Const
import com.pplus.prnumberbiz.R
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity
import com.pplus.prnumberbiz.apps.signin.ui.FindIdActivity
import com.pplus.prnumberbiz.core.code.common.SnsTypeCode
import com.pplus.prnumberbiz.core.network.ApiBuilder
import com.pplus.prnumberbiz.core.network.model.dto.User
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse
import network.common.PplusCallback
import retrofit2.Call

class JoinActivity : BaseActivity(), ImplToolbar {
    override fun getPID(): String {
        return ""
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_join
    }

    var key: String? = null

    override fun initializeView(savedInstanceState: Bundle?) {

        key = intent.getStringExtra(Const.KEY)
//        val params = intent.getParcelableExtra<User>(Const.USER)
        if (StringUtils.isEmpty(key)) {
            key = Const.JOIN
        }

        val intent = Intent(this, VerificationMeActivity::class.java)
        intent.putExtra(Const.KEY, Const.JOIN)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivityForResult(intent, Const.REQ_VERIFICATION)

//        if (key.equals(Const.LEVEL_UP)) {
//            join(LoginInfoManager.getInstance().user)
//        } else {
//            joinVerification()
//        }
    }

    private fun joinVerification() {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.join_container, JoinVerificationFragment.newInstance(), JoinVerificationFragment::class.java.simpleName)
        ft.commit()
    }

//    private fun pending(){
//        val ft = supportFragmentManager.beginTransaction()
//        ft.replace(R.id.join_container, UpdatePendingUserFragment.newInstance(), UpdatePendingUserFragment::class.java.simpleName)
//        ft.commit()
//    }

    fun join(params: User) {

        if (key.equals(Const.JOIN)) {
            val loginId = intent.getStringExtra(Const.ID)
            val token = intent.getStringExtra(Const.PASSWORD)
            val accountType = intent.getStringExtra(Const.ACCOUNT_TYPE)

            if (StringUtils.isNotEmpty(loginId) && StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(accountType)) {
                params.loginId = loginId
                params.password = token
                params.accountType = accountType
            } else {
                params.accountType = SnsTypeCode.pplus.name
            }
        }

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.join_container, JoinFragment.newInstance(key!!, params), JoinFragment::class.java.simpleName)
        ft.commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.REQ_VERIFICATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val user = data.getParcelableExtra<User>(Const.USER)
                        if (key.equals(Const.LEVEL_UP)) {
                            LoginInfoManager.getInstance().user.verification = user.verification
                            LoginInfoManager.getInstance().user.mobile = user.mobile
                            LoginInfoManager.getInstance().user.name = user.name
                            LoginInfoManager.getInstance().user.birthday = user.birthday
                            LoginInfoManager.getInstance().user.gender = user.gender
                            join(LoginInfoManager.getInstance().user)
                        } else {
                            checkExistUser(user)
                        }

                    }else{
                        finish()
                    }
                }else{
                    finish()
                }

            }
        }
    }

    private fun checkExistUser(user: User) {
        val params = HashMap<String, String>()
        params["mobile"] = user.mobile!!
        ApiBuilder.create().existsUser(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                hideProgress()
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_exist_user_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_exist_user_alert2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {

                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val intent = Intent(this@JoinActivity, FindIdActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        }
                    }
                }).builder().show(this@JoinActivity)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                hideProgress()
                val params = User()
                params.verification = user.verification
                params.mobile = user.mobile
                params.name = user.name
                params.birthday = user.birthday
                params.gender = user.gender
                join(params)

            }
        }).build().call()
    }

    override fun getToolbarOption(): ToolbarOption {
        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.RIGHT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {
        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.RIGHT -> if (tag == 1) {
                    close()
                }
            }
        }
    }

    override fun onBackPressed() {
        close()
    }

    private fun close() {

        val builder = AlertBuilder.Builder()
        builder.setTitle(getString(R.string.word_notice_alert))
        builder.addContents(AlertData.MessageData(getString(R.string.msg_close_signUp), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
        builder.setOnAlertResultListener(object : OnAlertResultListener {

            override fun onCancel() {

            }

            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                when (event_alert) {
                    AlertBuilder.EVENT_ALERT.RIGHT -> finish()
                }
            }
        }).builder().show(this)
    }
}
