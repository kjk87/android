package com.pplus.luckybol.apps.signup.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pplus.utils.part.utils.StringUtils
import com.pplus.luckybol.Const
import com.pplus.luckybol.R
import com.pplus.luckybol.apps.common.builder.AlertBuilder
import com.pplus.luckybol.apps.common.builder.OnAlertResultListener
import com.pplus.luckybol.apps.common.builder.data.AlertData
import com.pplus.luckybol.apps.common.toolbar.ImplToolbar
import com.pplus.luckybol.apps.common.toolbar.OnToolbarListener
import com.pplus.luckybol.apps.common.toolbar.ToolbarOption
import com.pplus.luckybol.apps.common.ui.base.BaseActivity
import com.pplus.luckybol.apps.signin.ui.FindIdActivity
import com.pplus.luckybol.core.Crypt
import com.pplus.luckybol.core.code.common.SnsTypeCode
import com.pplus.luckybol.core.network.ApiBuilder
import com.pplus.luckybol.core.network.model.dto.User
import com.pplus.luckybol.core.network.model.response.NewResultResponse
import com.pplus.luckybol.databinding.ActivityJoinBinding
import com.pplus.networks.common.PplusCallback
import retrofit2.Call
import java.util.HashMap

/**
 * 회원가입
 */
class JoinActivity : BaseActivity(), ImplToolbar {

    override fun getPID(): String {

        return ""
    }

    private lateinit var binding: ActivityJoinBinding

    override fun getLayoutView(): View {
        binding = ActivityJoinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initializeView(savedInstanceState: Bundle?) {
        joinVerification()
    }

    private fun joinVerification() {

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.join_container, JoinVerificationFragment.newInstance(), JoinVerificationFragment::class.java.simpleName)
        ft.commit()
    }


    fun join(params: User) {

        val loginId = intent.getStringExtra(Const.ID)
        val password = intent.getStringExtra(Const.PASSWORD)
        val accountType = intent.getStringExtra(Const.ACCOUNT_TYPE)
        val nickname = intent.getStringExtra(Const.NICKNAME)

        if(StringUtils.isNotEmpty(loginId) && StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(accountType)){
            params.loginId = loginId
            params.password = Crypt.encrypt(password)
            params.encrypted = true
            params.accountType = accountType
            params.nickname = nickname
        }else{
            params.accountType = SnsTypeCode.pplus.name
        }

        val fragment = JoinFragment.newInstance(params)

        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.join_container, fragment, fragment.javaClass.simpleName)
        ft.commit()
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return OnToolbarListener { v, toolbarMenu, tag ->
            when (toolbarMenu) {
                ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                    close()
                }
                else -> {}
            }
        }
    }

    private fun checkExistUser(user: User) {
        val params = HashMap<String, String>()
        params["mobile"] = user.mobile!!
        params["appType"] = Const.APP_TYPE
        ApiBuilder.create().existsUser(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {

            override fun onResponse(call: Call<NewResultResponse<Any>>, response: NewResultResponse<Any>) {
                hideProgress()
                val builder = AlertBuilder.Builder()
                builder.setTitle(getString(R.string.word_notice_alert))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_exist_user_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.addContents(AlertData.MessageData(getString(R.string.msg_exist_user_alert2), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                builder.setBackgroundClickable(false).setAutoCancel(false)
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm))
                builder.setOnAlertResultListener(object : OnAlertResultListener {

                    override fun onCancel() {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }

                    override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {

                        when (event_alert) {
                            AlertBuilder.EVENT_ALERT.RIGHT -> {
                                val intent = Intent(this@JoinActivity, FindIdActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                                setResult(Activity.RESULT_CANCELED)
                                finish()
                            }
                            else->{
                                setResult(Activity.RESULT_CANCELED)
                                finish()
                            }
                        }
                    }
                }).builder().show(this@JoinActivity)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>, t: Throwable, response: NewResultResponse<Any>) {
                hideProgress()
                val paramsJoin = User()
                paramsJoin.verification = user.verification
                paramsJoin.mobile = user.mobile
                paramsJoin.name = user.name
                paramsJoin.birthday = user.birthday
                paramsJoin.gender = user.gender
                join(paramsJoin)

            }
        }).build().call()
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
                    else -> {}
                }
            }
        }).builder().show(this)
    }

    //    @Override
    //    public void onBackPressed(){
    //
    //        close();
    //    }
}
