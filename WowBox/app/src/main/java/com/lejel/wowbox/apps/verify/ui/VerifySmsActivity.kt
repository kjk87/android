package com.lejel.wowbox.apps.verify.ui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.lejel.wowbox.Const
import com.lejel.wowbox.R
import com.lejel.wowbox.apps.common.builder.AlertBuilder
import com.lejel.wowbox.apps.common.builder.OnAlertResultListener
import com.lejel.wowbox.apps.common.builder.data.AlertData
import com.lejel.wowbox.apps.common.mgmt.LoginInfoManager
import com.lejel.wowbox.apps.common.toolbar.ImplToolbar
import com.lejel.wowbox.apps.common.toolbar.OnToolbarListener
import com.lejel.wowbox.apps.common.toolbar.ToolbarOption
import com.lejel.wowbox.apps.common.ui.base.BaseActivity
import com.lejel.wowbox.apps.join.JoinNicknameActivity
import com.lejel.wowbox.apps.login.WithdrawalCancelAuthActivity
import com.lejel.wowbox.apps.login.WithdrawalCancelCompleteActivity
import com.lejel.wowbox.apps.my.ui.WithdrawalCompleteActivity
import com.lejel.wowbox.core.network.ApiBuilder
import com.lejel.wowbox.core.network.model.dto.Member
import com.lejel.wowbox.core.network.model.response.NewResultResponse
import com.lejel.wowbox.core.util.PplusCommonUtil
import com.lejel.wowbox.databinding.ActivityVerifyWhatsAppBinding
import com.pplus.networks.common.PplusCallback
import com.pplus.utils.part.utils.time.DateFormatUtils
import retrofit2.Call

class VerifySmsActivity : BaseActivity(), ImplToolbar {

    private lateinit var binding: ActivityVerifyWhatsAppBinding

    override fun getLayoutView(): View {
        binding = ActivityVerifyWhatsAppBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun getPID(): String? {
        return ""
    }

    var mMobileNumber = ""
    var mType = ""
    var mWithdrawalReason = ""
    var mMember:Member? = null

    override fun initializeView(savedInstanceState: Bundle?) {
        mMobileNumber = intent.getStringExtra(Const.MOOBILE_NUMBER)!!
        mType = intent.getStringExtra(Const.TYPE)!!

        when(mType){
            "join", "verifyForWithdrawalCancel"->{
                mMember = PplusCommonUtil.getParcelableExtra(intent, Const.MEMBER, Member::class.java)
            }
            "withdrawal"->{
                mWithdrawalReason = intent.getStringExtra(Const.DATA)!!
            }
        }

        binding.textVerifyWhatsAppTitle.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_sms, 0, 0)
        binding.editVerifyWhatsAppMobileOtp.setSingleLine()
        binding.textVerifyWhatsAppMobileNumber.text = getString(R.string.format_sms_verify_code_desc1, mMobileNumber)


        val countTimer = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                val seconds = (millisUntilFinished / 1000).toInt() % 60
                val strS = DateFormatUtils.formatTime(seconds)

                binding.textVerifyWhatsAppResendDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_verify_code_desc2, strS))
            }

            override fun onFinish() {
                binding.textVerifyWhatsAppResendDesc.visibility = View.GONE
                binding.layoutVerifyWhatsAppResend.visibility = View.VISIBLE
            }
        }

        countTimer.start()

        binding.textVerifyWhatsAppResend.setOnClickListener {
            val params = HashMap<String, String>()
            params["mobileNumber"] = mMobileNumber
            params["type"] = mType
            showProgress("")
            ApiBuilder.create().sendSms(params).setCallback(object  : PplusCallback<NewResultResponse<Any>> {
                override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                    hideProgress()

                    binding.textVerifyWhatsAppResendDesc.visibility = View.VISIBLE
                    binding.layoutVerifyWhatsAppResend.visibility = View.GONE
                    binding.textVerifyWhatsAppResendDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_verify_code_desc2, "30"))
                    countTimer.start()
                }

                override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                    hideProgress()
                }
            }).build().call()
        }

        binding.editVerifyWhatsAppMobileOtp.addTextChangedListener {
            if(it!!.length == 6){
                val otp  = it.toString().trim()
                when(mType){
                    "login"->{
                        loginByMobile(mMobileNumber, otp)
                    }
                    "join"->{
                        verifyForJoin(mMember!!, otp)
                    }
                    "update_mobile"->{
                        updateVerifiedMobileNumber(mMobileNumber, otp)
                    }
                    "withdrawal"->{
                        verifyForWithdrawal(otp)
                    }
                    "verifyForWithdrawalCancel"->{
                        verifyForWithdrawalCancel(otp)
                    }

                }
            }
        }



        binding.textVerifyWhatsAppResendDesc.visibility = View.VISIBLE
        binding.layoutVerifyWhatsAppResend.visibility = View.GONE
        binding.textVerifyWhatsAppResendDesc.text = PplusCommonUtil.fromHtml(getString(R.string.html_verify_code_desc2, "30"))
    }

    private fun verifyForWithdrawal(otp:String){

        val params = HashMap<String, String>()
        params["reason"] = mWithdrawalReason
        params["verifyType"] = "sms"
        params["otp"] = otp
        showProgress("")
        ApiBuilder.create().withdrawal(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                setEvent("withdrawal")
                val intent = Intent(this@VerifySmsActivity, WithdrawalCompleteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.code == 506) {
                    binding.editVerifyWhatsAppMobileOtp.startAnimation(AnimationUtils.loadAnimation(this@VerifySmsActivity, R.anim.shake))
                    binding.editVerifyWhatsAppMobileOtp.setText("")
                }
            }
        }).build().call()
    }
    private fun verifyForWithdrawalCancel(otp:String){

        val params = HashMap<String, String>()
        params["userKey"] = mMember!!.userKey!!
        params["mobileNumber"] = mMember!!.mobileNumber!!
        params["verifyType"] = "sms"
        params["otp"] = otp
        showProgress("")
        ApiBuilder.create().withdrawalCancelWithVerify(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()

                val intent = Intent(this@VerifySmsActivity, WithdrawalCancelCompleteActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.code == 506) {
                    binding.editVerifyWhatsAppMobileOtp.startAnimation(AnimationUtils.loadAnimation(this@VerifySmsActivity, R.anim.shake))
                    binding.editVerifyWhatsAppMobileOtp.setText("")
                }
            }
        }).build().call()
    }

    private fun verifyForJoin(member:Member, otp:String){
        member.verifyType = "sms"

        val params = HashMap<String, String>()
        params["mobileNumber"] = mMobileNumber
        params["type"] = mType
        params["otp"] = otp
        showProgress("")
        ApiBuilder.create().verifySms(params).setCallback(object  : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                val intent = Intent(this@VerifySmsActivity, JoinNicknameActivity::class.java)
                intent.putExtra(Const.MEMBER, member)
                joinCompleteLauncher.launch(intent)
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.code == 506) {
                    binding.editVerifyWhatsAppMobileOtp.startAnimation(AnimationUtils.loadAnimation(this@VerifySmsActivity, R.anim.shake))
                    binding.editVerifyWhatsAppMobileOtp.setText("")
                } else {
                    showAlert(R.string.msg_failed_join)
                }
            }
        }).build().call()
    }

    private fun loginByMobile(mobileNumber:String, otp:String){
        val params = HashMap<String, String>()
        params["platformKey"] = mobileNumber
        params["device"] = PplusCommonUtil.getDeviceID()
        params["otp"] = otp
        params["verifyType"] = "sms"
        showProgress("")
        ApiBuilder.create().loginByMobile(params).setCallback(object : PplusCallback<NewResultResponse<Member>> {
            override fun onResponse(call: Call<NewResultResponse<Member>>?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.result != null) {
                    if(response.result!!.status == "active"){
                        LoginInfoManager.getInstance().member = response.result
                        LoginInfoManager.getInstance().save()

                        setResult(RESULT_OK)
                        finish()
                        //                        getNotSignedTermsList()


                    }else if(response.result!!.status == "waitingLeave"){
                        val builder = AlertBuilder.Builder()
                        builder.setTitle(getString(R.string.msg_alert_waiting_leave))
                        builder.addContents(AlertData.MessageData(getString(R.string.msg_alert_waiting_leave_desc), AlertBuilder.MESSAGE_TYPE.TEXT, 2))
                        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_withdrawal_cancel))
                        builder.setOnAlertResultListener(object : OnAlertResultListener {

                            override fun onCancel() {

                            }

                            override fun onResult(event_alert: AlertBuilder.EVENT_ALERT) {
                                when (event_alert) {
                                    AlertBuilder.EVENT_ALERT.RIGHT -> {
                                        val intent = Intent(this@VerifySmsActivity, WithdrawalCancelAuthActivity::class.java)
                                        intent.putExtra(Const.DATA, response.result)
                                        startActivity(intent)
                                    }

                                    else -> {

                                    }
                                }
                            }
                        }).builder().show(this@VerifySmsActivity)
                    }
                }
            }

            override fun onFailure(call: Call<NewResultResponse<Member>>?, t: Throwable?, response: NewResultResponse<Member>?) {
                hideProgress()
                if (response?.code == 506) {
                    binding.editVerifyWhatsAppMobileOtp.startAnimation(AnimationUtils.loadAnimation(this@VerifySmsActivity, R.anim.shake))
                    binding.editVerifyWhatsAppMobileOtp.setText("")
                }
            }
        }).build().call()
    }

    private fun updateVerifiedMobileNumber(mobileNumber:String, otp:String){
        val params = HashMap<String, String>()
        params["mobileNumber"] = mobileNumber
        params["verifyType"] = "sms"
        params["otp"] = otp
        showProgress("")
        ApiBuilder.create().updateVerifiedMobileNumber(params).setCallback(object : PplusCallback<NewResultResponse<Any>> {
            override fun onResponse(call: Call<NewResultResponse<Any>>?, response: NewResultResponse<Any>?) {
                hideProgress()
                setResult(RESULT_OK)
                finish()
            }

            override fun onFailure(call: Call<NewResultResponse<Any>>?, t: Throwable?, response: NewResultResponse<Any>?) {
                hideProgress()
                if (response?.code == 504) {
                    showAlert(R.string.msg_alread_joined_mobile_number)
                    setResult(RESULT_CANCELED)
                    finish()
                }else if (response?.code == 506) {
                    binding.editVerifyWhatsAppMobileOtp.startAnimation(AnimationUtils.loadAnimation(this@VerifySmsActivity, R.anim.shake))
                    binding.editVerifyWhatsAppMobileOtp.setText("")
                }
            }
        }).build().call()
    }

    val joinCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK){
            setResult(RESULT_OK)
            finish()
        }
    }

    override fun getToolbarOption(): ToolbarOption {

        val toolbarOption = ToolbarOption(this)
        toolbarOption.initializeDefaultToolbar("", ToolbarOption.ToolbarMenu.LEFT)
        return toolbarOption
    }

    override fun getOnToolbarClickListener(): OnToolbarListener {

        return object : OnToolbarListener {
            override fun onClick(v: View?, toolbarMenu: ToolbarOption.ToolbarMenu?, tag: Any?) {
                when (toolbarMenu) {
                    ToolbarOption.ToolbarMenu.LEFT -> if (tag == 1) {
                        onBackPressedDispatcher.onBackPressed()
                    }

                    else -> {}
                }
            }
        }
    }
}